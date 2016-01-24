package com.journaldev.spring;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.connectifier.xeroclient.models.Invoice;
import com.connectifier.xeroclient.models.LineItem;
import com.connectifier.xeroclient.models.TrackingCategory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.journaldev.spring.model.Invoice1;
import com.journaldev.spring.model.Person;
import com.journaldev.spring.service.PersonService;

@Controller
@EnableScheduling
public class PersonController {

	private PersonService personService;
	private SimpleDateFormat dateFormat=new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

	@Scheduled(fixedDelay=1000*2*60)
	public void getData() {
		try {

			Reader reader = null;
			try {
				ClassLoader classLoader = getClass().getClassLoader();
				reader = new FileReader(new File(classLoader.getResource("private.pem").getFile()));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*com.connectifier.xeroclient.XeroClient client = new com.connectifier.xeroclient.XeroClient(
					reader, "MUD8RGBN5Y0PZJNS0YUDBBTJ5QLAYQ",
					"JO2W7X3KQDIEFDBXFTV38OJFLPXNQO");*/
			com.journaldev.spring.XeroClient client = new com.journaldev.spring.XeroClient(
					reader, "YXX4DVWOVNFX19NQ4ZDGFQXGOL8IQI",
					"JDUA8WKMEJSTAIMR53WHSM9OXLAQKA");
			
			Calendar calendar=Calendar.getInstance();
			calendar.set(2016, 0, 1,0, 0, 0);
			Date startDate =null;
			startDate=this.personService.getMaxInvoiceDate();
			if(startDate==null)
				startDate = calendar.getTime();
			// select max date from invoices
			int pageNo = 1;
			
			List<Invoice1> invoices = new ArrayList<Invoice1>();
			List<Invoice1> invoicesTemp = new ArrayList<Invoice1>();
			do {
				invoicesTemp = client.getInvoices(startDate, "Type==\"ACCREC\"", null,pageNo++);
				invoices.addAll(invoicesTemp);
				//System.out.println(invoicesTemp.get(0).getInvoiceNumber());
			} while (invoicesTemp.size() == 100);
			/*List<Invoice1> invoices2 = new ArrayList<Invoice1>();
			for (Invoice i : invoices) {
				Invoice1 in = new Invoice1();
				in.setInvoiceNumber(i.getInvoiceNumber());
				if(null!=i.getContact())
					in.setCustomer(i.getContact().getName());
				in.setInvoiceAmount(i.getSubTotal());
				in.setInvoiceDate(i.getDate());
				in.setInsertDate(Calendar.getInstance().getTime());
				for (LineItem li : i.getLineItems())
					for (TrackingCategory tc : li.getTracking())
						if (!StringUtils.isEmpty(tc.getOption()))
							if (StringUtils.isEmpty(in.getSalesPerson()))
								in.setSalesPerson(tc.getOption());
				
				in.setCost(new BigDecimal(0));
				in.setProfit(new BigDecimal(0));
				in.setProfitPer(new BigDecimal(0));
				System.out.println(in.getInvoiceNumber()+" : "+in.getInvoiceDate());
				invoices2.add(in);*/
				for(Invoice1 invoice1:invoices )
				this.personService.saveInvoice(invoice1, false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Autowired(required = true)
	@Qualifier(value = "personService")
	public void setPersonService(PersonService ps) {
		this.personService = ps;
	}

	public ModelAndView login(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("login");
	}
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView doLogin(HttpServletRequest request,HttpServletResponse response){
		try{
		if(request.getParameter("userName").equals("cnssales") && request.getParameter("password").equals("xcns121")){
			request.getSession().setAttribute("isLoggedIn", "true");
			return new ModelAndView(new RedirectView(""));
		}else
		return new ModelAndView("login");
		}catch(Exception e)
		{
			return new ModelAndView("login");	
		}
	}
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView doLogout(HttpServletRequest request,HttpServletResponse response){
		request.getSession().invalidate();
			return new ModelAndView("login");	
		
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView salesDashboard(HttpServletRequest request,HttpServletResponse response) {
		try {
			if(request.getSession().getAttribute("isLoggedIn").toString().equals("false")){
				return login(request, response);
			}
		} catch (Exception e) {
			return login(request, response);
		}
		GsonBuilder gsonB = new GsonBuilder();
		gsonB.serializeNulls();
		Gson gson = gsonB.create();
		int time=7;
		if(null!=request.getParameter("time")){
			try {
				int i=Integer.parseInt(request.getParameter("time"));
				time=i;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return new ModelAndView("dashboard").addObject("salesPersons",gson.toJson(this.personService.getAllSalesPerson())).addObject("time", time);
	}

	@RequestMapping(value = "/persons", method = RequestMethod.GET)
	public String listPersons(Model model) {
		model.addAttribute("person", new Person());
		model.addAttribute("listPersons", this.personService.listPersons());
		return "person";
	}

	@RequestMapping(value = "/backend", method = RequestMethod.GET)
	public ModelAndView invoicepage(HttpServletRequest request,HttpServletResponse  response) {
		try {
			if(request.getSession().getAttribute("isLoggedIn").toString().equals("false")){
				return login(request, response);
			}
		} catch (Exception e) {
			return login(request, response);
		}
		return new ModelAndView("index");
	}

	@RequestMapping(value = "/monthlySales", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String monthlySales(final HttpServletRequest request,final HttpServletResponse response) {
		GsonBuilder gsonB = new GsonBuilder();
		gsonB.serializeNulls();
		Gson gson = gsonB.create();
		Map<String, Object> invoices = this.personService.monthlySales(request.getParameter("year").toString());
		String json = gson.toJson(invoices);
		return json;
	}
	
	@RequestMapping(value = "/invoice", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String invoice(final HttpServletRequest request,
			final HttpServletResponse response) {
		GsonBuilder gsonB = new GsonBuilder();
		gsonB.serializeNulls();
		Gson gson = gsonB.create();
		List<Invoice1> invoices = this.personService.listInvoices();
		String json = gson.toJson(invoices);
		String finalString = "{\"total\":\"" + invoices.size()
				+ "\",\"invoices\":" + json + "}";
		return finalString;
	}
	@RequestMapping(value = "/invoiceCustom", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String invoiceCustom(final HttpServletRequest request,final HttpServletResponse response) {
		GsonBuilder gsonB = new GsonBuilder();
		String salesPerson=request.getParameter("salesPerson");
		SimpleDateFormat idf=new SimpleDateFormat("yyyy-M");
		String dateStr=request.getParameter("date");
		gsonB.serializeNulls();
		Gson gson = gsonB.create();
		List<Invoice1> invoices;// = this.personService.listInvoices(dateStr,salesPerson);
		List<Map<String,Object>> invoiceList=(List<Map<String, Object>>) this.personService.listInvoicess(dateStr, salesPerson);
		Map<String,Object> totalss=(Map<String, Object>) this.personService.monthTotalOFindividual(dateStr, salesPerson);
		totalss.put("salesPerson", salesPerson);
		totalss.put("dateStr",dateStr);
		String invoiceStr = gson.toJson(invoiceList);
		String totalStr=gson.toJson(totalss);
		String finalString = "{\"total\":\"" + invoiceList.size()+ "\",\"invoices\":" + invoiceStr + " , \"totalss\" : "+totalStr+" }";
		return finalString;
	}
	@RequestMapping(value = "/yearTotal", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String yearTotals(final HttpServletRequest request,final HttpServletResponse response) {
		GsonBuilder gsonB = new GsonBuilder();
		SimpleDateFormat idf=new SimpleDateFormat("yyyy-M");
		String dateStr=request.getParameter("date");
		gsonB.serializeNulls();
		Gson gson = gsonB.create();
		String dataString = gson.toJson(this.personService.getYearReport(dateStr));
		return dataString;
	}

	public class CalendarSerializer implements JsonSerializer<Calendar>,
			JsonDeserializer<Calendar> {
		final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

		// final SimpleDateFormat getFormat=new
		// SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		@Override
		public JsonElement serialize(Calendar src, Type typeOfSrc,
				JsonSerializationContext context) {
			return new JsonPrimitive(format.format(src.getTime()));
		}

		@Override
		public Calendar deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			Calendar cal = Calendar.getInstance();
			// cal.setTimeInMillis(json.getAsJsonPrimitive().getAsLong());
			try {
				cal.setTime(format.parse(json.getAsJsonPrimitive()
						.getAsString()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return cal;
		}
	}

	@RequestMapping(value = "/invoice/update", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String updateInvoices(HttpServletRequest request,HttpServletResponse response) {
		Gson gson = new Gson();
		Type listType = new TypeToken<List<Invoice1>>() {
		}.getType();
		List<Invoice1> invoiceList = (List<Invoice1>) gson.fromJson(request.getParameter("models"), listType);
		List<Invoice1> updatedInvoiceList = new ArrayList<Invoice1>();
		for (Invoice1 invoice : invoiceList) {
			double profit,profitPer;
			if (invoice.getCost().equals(new BigDecimal(0))){
				profit = invoice.getCost().doubleValue();
				profitPer=0;
			}else{
				profit = invoice.getInvoiceAmount().doubleValue()-invoice.getCost().doubleValue();
				profitPer=profit/invoice.getInvoiceAmount().doubleValue()*100;
			}
			invoice.setProfit(new BigDecimal(profit));
			invoice.setProfitPer(new BigDecimal(profitPer));
			updatedInvoiceList.add(invoice);
			
		}
		for(Invoice1 invoice1:invoiceList)
		this.personService.saveInvoice(invoice1, true);
		String json = gson.toJson(updatedInvoiceList);
		return json;
	}

	/*
	 * @RequestMapping(value = "/invoices", method = RequestMethod.GET) public
	 * String invoices(Model model) { XeroClient xeroClient = new XeroClient();
	 * // Retrieve a list of Invoices try {
	 * 
	 * int page=1; ArrayOfInvoice arrayOfExistingInvoices=null; do{
	 * arrayOfExistingInvoices =
	 * xeroClient.getInvoices(page,"2016-01-02T00:00:00");
	 * if(arrayOfExistingInvoices.getInvoice().size()==100)page++;
	 * saveArrayOfInvoices(xeroClient, arrayOfExistingInvoices);
	 * }while(arrayOfExistingInvoices.getInvoice().size()==100); } catch
	 * (Exception ex) { System.out.println("Exception "+ex.getMessage());
	 * ex.printStackTrace(); }
	 * 
	 * model.addAttribute("person", new Person());
	 * model.addAttribute("listPersons", this.personService.listPersons());
	 * return "person"; }
	 */

	/*
	 * private void saveArrayOfInvoices(XeroClient xeroClient, ArrayOfInvoice
	 * arrayOfExistingInvoices) throws XeroClientException,
	 * XeroClientUnexpectedException { if (arrayOfExistingInvoices != null &&
	 * arrayOfExistingInvoices.getInvoice() != null) {
	 * this.personService.saveInvoice(arrayOfExistingInvoices); } }
	 */

	// For add and update person both
	@RequestMapping(value = "/person/add", method = RequestMethod.POST)
	public String addPerson(@ModelAttribute("person") Person p) {

		if (p.getId() == 0) {
			// new person, add it
			this.personService.addPerson(p);
		} else {
			// existing person, call update
			this.personService.updatePerson(p);
		}

		return "redirect:/persons";

	}

	@RequestMapping("/remove/{id}")
	public String removePerson(@PathVariable("id") int id) {

		this.personService.removePerson(id);
		return "redirect:/persons";
	}

	@RequestMapping("/edit/{id}")
	public String editPerson(@PathVariable("id") int id, Model model) {
		model.addAttribute("person", this.personService.getPersonById(id));
		model.addAttribute("listPersons", this.personService.listPersons());
		return "person";
	}

}
