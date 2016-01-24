package com.journaldev.spring.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.journaldev.spring.model.Invoice1;
import com.journaldev.spring.model.Person;

public interface PersonDAO {

	public void addPerson(Person p);
	public void updatePerson(Person p);
	public List<Person> listPersons();
	public Person getPersonById(int id);
	public void removePerson(int id);
	public void saveInvoice(Invoice1 invoice,boolean updateExisting);
	public List<Invoice1> listInvoices();
	public List<Invoice1> listInvoices(String date,String salesPerson);
	public Object monthTotalOFindividual(String date,String salesPerson);
	public Date getMaxInvoiceDate();
	public List<Map<String, Object>> getAllSalesPerson();
	public Map<String,Object> getYearReport(String date);
	public List<Map<String,Object>> listInvoicess(String date,String salesPerson);
	public Map<String,Object> monthlySales(String year);
}
