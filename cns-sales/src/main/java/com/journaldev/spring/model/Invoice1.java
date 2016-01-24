package com.journaldev.spring.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
* The persistent class for the INVOICE database table.
*
*/
@Entity
public class Invoice1 implements Serializable {
private static final long serialVersionUID = 1L;

private BigDecimal cost;

@Column(name="CUSTOM_COMMENT")
private String customComment;

private String customer;

@Column(name="INVOICE_AMOUNT")
private BigDecimal invoiceAmount;

@Temporal(TemporalType.TIMESTAMP)
@Column(name="INVOICE_DATE")
private Date invoiceDate;


@Temporal(TemporalType.TIMESTAMP)
@Column(name="INSERT_DATE")
private Date insertDate;

@Id
@Column(name="INVOICE_NUMBER")
private String invoiceNumber;

private BigDecimal profit;

@Column(name="PROFIT_PER")
private BigDecimal profitPer;

@Column(name="SALES_PERSON")
private String salesPerson;

public Invoice1() {
}

public BigDecimal getCost() {
return this.cost;
}

public void setCost(BigDecimal cost) {
this.cost = cost;
}

public String getCustomComment() {
return this.customComment;
}

public void setCustomComment(String customComment) {
this.customComment = customComment;
}

public String getCustomer() {
return this.customer;
}

public void setCustomer(String customer) {
this.customer = customer;
}

public BigDecimal getInvoiceAmount() {
return this.invoiceAmount;
}

public void setInvoiceAmount(BigDecimal invoiceAmount) {
this.invoiceAmount = invoiceAmount;
}



public String getInvoiceNumber() {
return this.invoiceNumber;
}

public void setInvoiceNumber(String invoiceNumber) {
this.invoiceNumber = invoiceNumber;
}

public BigDecimal getProfit() {
return this.profit;
}

public void setProfit(BigDecimal profit) {
this.profit = profit;
}

public BigDecimal getProfitPer() {
return this.profitPer;
}

public void setProfitPer(BigDecimal profitPer) {
this.profitPer = profitPer;
}

public String getSalesPerson() {
return this.salesPerson;
}

public void setSalesPerson(String salesPerson) {
this.salesPerson = salesPerson;
}

public Date getInvoiceDate() {
	return invoiceDate;
}
private String datee;
public void setInvoiceDate(Date invoiceDate) {
	this.invoiceDate = invoiceDate;
	if(null!=invoiceDate)
		setDatee(new SimpleDateFormat("dd MMM yyyy").format(invoiceDate));
	
}

public String toString(){
	SimpleDateFormat dateFormat=new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
	return this.getInvoiceNumber()+" "+dateFormat.format(this.getInvoiceDate())+" "+this.getInvoiceAmount();
}

public Date getInsertDate() {
	return insertDate;
}

public void setInsertDate(Date insertDate) {
	this.insertDate = insertDate;
}

public String getDatee() {
	return datee;
}

public void setDatee(String datee) {
	this.datee = datee;
}


}