package com.journaldev.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import com.journaldev.spring.model.Invoice1;
import com.journaldev.spring.model.Person;

@Repository
public class PersonDAOImpl implements PersonDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(PersonDAOImpl.class);

	private SessionFactory sessionFactory;
	private JdbcTemplate jdbcTemplate;
	
	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}

	public void addPerson(Person p) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(p);
		
		logger.info("Person saved successfully, Person Details="+p);
	}

	public void updatePerson(Person p) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(p); 	
		logger.info("Person updated successfully, Person Details="+p);
	}

	@SuppressWarnings("unchecked")
	public List<Person> listPersons() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Person> personsList = session.createQuery("from Person").list();
		for(Person p : personsList){
			logger.info("Person List::"+p);
		}
		return personsList;
	}

	public Person getPersonById(int id) {
		Session session = this.sessionFactory.getCurrentSession();		
		Person p = (Person) session.load(Person.class, new Integer(id));
		logger.info("Person loaded successfully, Person details="+p);
		return p;
	}

	public void removePerson(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Person p = (Person) session.load(Person.class, new Integer(id));
		if(null != p){
			session.delete(p);
		}
		logger.info("Person deleted successfully, person details="+p);
	}

	public void saveInvoice(Invoice1 invoice,boolean updateExisting) {
		Session session = this.sessionFactory.getCurrentSession();	
		session.saveOrUpdate(invoice);
		/*for (Invoice1 invoice1 : invoice) {
			session.saveOrUpdate(invoice1);
			Invoice1 iii=(Invoice1) session.get(Invoice1.class, invoice1.getInvoiceNumber());
			//session.clear();session.flush();
			if(null!=iii )
			{
				if(updateExisting)
					session.update(invoice1);
			}else
				session.persist(invoice1);
		}*/
	}

	@Override
	public List<Invoice1> listInvoices() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Invoice1> invoiceList = session.createQuery("from Invoice1").list();
		for(Invoice1 p : invoiceList){
			logger.info("Invoice List:"+p);
		}
		return invoiceList;
	}

	@Override
	public Date getMaxInvoiceDate() {
		Session session=this.sessionFactory.getCurrentSession();
		Criteria criteria = session
			    .createCriteria(Invoice1.class)
			    .setProjection(Projections.max("insertDate"));
			Date maxDate = (Date)criteria.uniqueResult();
		return maxDate;
	}

	public List<Invoice1> listInvoices(String date, String salesPerson) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery("select * from Invoice1 where sales_person=:salesPerson and  DATE_FORMAT(INVOICE_DATE,'%Y-%c') = :date order by INVOICE_DATE desc limit 10")
				.addEntity(Invoice1.class)
				.setParameter("salesPerson", salesPerson)
				.setParameter("date", date);
		monthTotalOFindividual(date,salesPerson);
		return (List<Invoice1>) query.list();
	}
	public List<Map<String,Object>> listInvoicess(String date,String salesPerson){
		return this.getJdbcTemplate().queryForList("select INVOICE_NUMBER as invoiceNumber, cost , CUSTOM_COMMENT as customComment,"
				+ " customer , INSERT_DATE as insertDate, INVOICE_AMOUNT as invoiceAmount, "+
				" INVOICE_DATE as invoiceDate, profit , PROFIT_PER as profitPer, " +
				" SALES_PERSON as salesPerson, datee , " +
				" CASE WHEN TIMESTAMPDIFF(MINUTE,insert_Date,NOW()) <1 THEN '1' ELSE '0' END as isGreen " + 
				" from Invoice1 where sales_person='"+salesPerson+"' and  DATE_FORMAT(INVOICE_DATE,'%Y-%c') = '"+date+"'  order by insert_date desc limit 10");
	}
	/*public Map<String,Object> getSPMonthData(String date,String salesPerson){
		Map<String,Object> result=new HashMap<>();
	List<Invoice1> invoices1=listInvoices();
		result.put("data",listInvoices());
		result.put("monthTotal", this.getJdbcTemplate().queryForMap("select sum(INVOICE_AMOUNT) as amountTotal,sum(cost) as costTotal,sum(profit) as profitTotal, "+
				" (sum(profit)/sum(INVOICE_AMOUNT) *100) as profitPer "+
				"  from Invoice1 where sales_person='"+salesPerson+"' and  "+ 
				" DATE_FORMAT(INVOICE_DATE,'%Y-%c') = '"+date+"'"));
		result.put("total", invoices1.size());
		return result;
	}*/

	@Override
	public Object monthTotalOFindividual(final String date, final String salesPerson) {
		// TODO Auto-generated method stub
		Map<String,Object> resultMap= this.getJdbcTemplate().queryForMap("select ifnull(sum(cost),0) as costTotal,sum(invoice_amount) as amountTotal,"
				+ "sum(profit) as profitTotal, "
				+ "(sum(profit)/sum(INVOICE_AMOUNT) *100) as profitPer "
				+ "from Invoice1 where sales_person='"+salesPerson+"' "+
				" and DATE_FORMAT(INVOICE_DATE,'%Y-%c') = '"+date+"'");
		return resultMap;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Map<String, Object>> getAllSalesPerson() {
		try {
			if(this.getJdbcTemplate().getDataSource().getConnection().isClosed()){
				//this.getJdbcTemplate().getDataSource().getConnection().
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.getJdbcTemplate().queryForList("select distinct(SALES_PERSON) from Invoice1  where sales_person is not null");
	}
	
	public Map<String,Object> getYearReport(String date){
		Map<String,Object> result=new HashMap<>();
		List<Map<String,Object>> data1= this.getJdbcTemplate().queryForList("select ifnull(sales_person,'Others') as sales_person,sum(INVOICE_AMOUNT) as amountTotal,sum(profit) as profitTotal, "+
				" (sum(profit)/sum(INVOICE_AMOUNT) *100) as profitPer "+
				" from Invoice1 "+
				" where DATE_FORMAT(INVOICE_DATE,'%Y-%c') = '"+date+"' "+
				" group by sales_person");
		result.put("data",data1);
		result.put("monthTotal", this.getJdbcTemplate().queryForMap("select sum(INVOICE_AMOUNT) as amountTotal,sum(profit) as profitTotal, "+
																" (sum(profit)/sum(INVOICE_AMOUNT) *100) as profitPer "+
																" from Invoice1 "+
																" where DATE_FORMAT(INVOICE_DATE,'%Y-%c') = '"+date+"'"));
		result.put("yearTotal", this.getJdbcTemplate().queryForMap("select sum(INVOICE_AMOUNT) as amountTotal,sum(profit) as profitTotal, "+
																" (sum(profit)/sum(INVOICE_AMOUNT) *100) as profitPer "+
																" from Invoice1 "+
																" where DATE_FORMAT(INVOICE_DATE,'%Y') = '2016'"));
		result.put("total", data1.size());
		return result;
	}

	@Override
	public Map<String, Object> monthlySales(String year) {
		Map<String,Object> result=new HashMap<>();
		List l= this.getJdbcTemplate().queryForList("select date_format(INVOICE_DATE,'%M') as month,sum(INVOICE_AMOUNT) as amountTotal,sum(profit) as profitTotal, "
				+" (sum(profit)/sum(INVOICE_AMOUNT) *100) as profitPer"
				+"  from Invoice1 where "
				+" DATE_FORMAT(INVOICE_DATE,'%Y') = '"+year+"'" 
				+" group by date_format(INVOICE_DATE,'%M') order by monthname(INVOICE_DATE) desc");
		result.put("invoices",l);
		result.put("total",l.size());
		result.put("totalss",this.getJdbcTemplate().queryForMap("select sum(INVOICE_AMOUNT) as amountTotal,sum(profit) as profitTotal, "
					+" (sum(profit)/sum(INVOICE_AMOUNT) *100) as profitPer "
					+" from Invoice1"
					+" where DATE_FORMAT(INVOICE_DATE,'%Y') = '"+year+"'"));
			return result;
	}
	
	
}
