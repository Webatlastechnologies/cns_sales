package com.journaldev.spring.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectifier.xeroclient.models.Invoice;
import com.journaldev.spring.dao.PersonDAO;
import com.journaldev.spring.model.Invoice1;
import com.journaldev.spring.model.Person;

@Service
public class PersonServiceImpl implements PersonService {
	
	private PersonDAO personDAO;

	public void setPersonDAO(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	@Transactional
	public void addPerson(Person p) {
		this.personDAO.addPerson(p);
	}

	@Transactional
	public void updatePerson(Person p) {
		this.personDAO.updatePerson(p);
	}

	@Transactional
	public List<Person> listPersons() {
		return this.personDAO.listPersons();
	}
	
	@Transactional
	public Person getPersonById(int id) {
		return this.personDAO.getPersonById(id);
	}

	@Transactional
	public void removePerson(int id) {
		this.personDAO.removePerson(id);
	}

	@Transactional
	public void saveInvoice(Invoice1 invoice,boolean updateExisting) {
		this.personDAO.saveInvoice(invoice,updateExisting);
	}

	@Transactional
	public List<Invoice1> listInvoices() {
		return this.personDAO.listInvoices();
	}

	@Transactional
	public Date getMaxInvoiceDate() {
		return this.personDAO.getMaxInvoiceDate();
	}

	@Transactional
	public List<Invoice1> listInvoices(String date, String salesPerson) {
		return this.personDAO.listInvoices(date, salesPerson);
	}

	@Transactional
	public Object monthTotalOFindividual(String date, String salesPerson) {
		return this.personDAO.monthTotalOFindividual(date, salesPerson);
	}

	@Transactional
	public List<Map<String, Object>> getAllSalesPerson() {
		return this.personDAO.getAllSalesPerson();
	}

	@Transactional
	public Map<String, Object> getYearReport(String date) {
		return this.personDAO.getYearReport(date);
	}

	@Transactional
	public List<Map<String, Object>> listInvoicess(String date,
			String salesPerson) {
		return this.personDAO.listInvoicess(date, salesPerson);
	}

	@Transactional
	public Map<String, Object> monthlySales(String year) {
		return this.personDAO.monthlySales(year);
	}

}
