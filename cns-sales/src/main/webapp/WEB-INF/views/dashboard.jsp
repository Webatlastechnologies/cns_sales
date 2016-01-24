	<!DOCTYPE html>
	<html>
	<head>
	    <title>CNS Trading Limited - Sales</title>
	    <link rel="stylesheet" href="resources/styles/kendo.common.min.css" />
	    <link rel="stylesheet" href="resources/styles/kendo.default.min.css" />
	 <!--    <link rel="stylesheet" href="resources/styles/AllViewAllPage.css" /> -->
	
	    <script src="resources/js/jquery.min.js"></script>
	    <script src="resources/js/kendo.all.min.js"></script>
	    
	     <script>
	            var salesPerson;
	            kendo.culture("en-US");
	            var salesPersons=jQuery.parseJSON('${salesPersons}');
	            var crudServiceBaseUrl = "",
	            dataSource = new kendo.data.DataSource({
	                transport: {
	                    read:  {
	                        url: crudServiceBaseUrl + "invoiceCustom",
	                        dataType: "json",
	                        type: 'POST',
	                        complete :function(data){
	                        	
	                        	var d=jQuery.parseJSON(data.responseText);
	                        	/* var profit=d.totalss.amountTotal - d.totalss.costTotal;
	                        	var profitPer=profit/d.totalss.amountTotal*100; */
	                        	$("#fiprofit").html(kendo.toString(d.totalss.profitTotal, "n"));
	                        	$("#fiprofitPer").html(kendo.toString(d.totalss.profitPer, "n"));
	                        	$("#ficost").html(kendo.toString(d.totalss.costTotal, "n"));
	                        	$("#fiamount").html(kendo.toString(d.totalss.amountTotal, "n"));
	                        	/* 
	                        	$("#profit").html(kendo.toString(profit, "n"));
	                        	$("#profitPer").html(kendo.toString(profitPer, "n"));
	                        	 */
	                        	$("#salesPerson").html(d.totalss.salesPerson);
	                        	$("#date").html(kendo.toString(new Date(), "y"));
	                        	 
	                        	$("#monthDiv").show();
	                        	$("#yearDiv").hide();
	                     		$("#monthDivYear").hide();
	                     		
	                        }
	                    },
	                    parameterMap: function(options, operation) {
	                            return {
	                            	models: kendo.stringify(options.models),
	                            	salesPerson : function(){
	                            		return salesPerson;
	                            	},
	                            	date : function(){
	                            		var d = new Date();
	                            		var r=d.getFullYear()+'-'+(d.getMonth()+1);
	                            		return r;
	                            	}
	                            };
	                    }
	                },
	               // batch: true,
	                pageSize: 20,
	    	        serverPaging: false,
	    	        serverSorting: false,
	    	        serverFiltering : false,
	                schema: {
	                	data: "invoices",	
	    	            total: "total",
	                     model: {
	                        id: "invoiceNumber",
	                         fields: {
	                        	invoiceNumber: { editable: false ,type:"string", nullable: true },
	                        	invoiceDate : {editable:false /* ,type:"datetime" */},
	                        	invoiceAmount: {editable: true ,type:"number",  validation: { required: false } },
	                        	customer :{type:"string",editable:false},
	                        	salesPerson :{type:"string",editable:false},
	                        	customComment:{ editable: true, type: "string", validation: { required: false} },
	                        	cost: { editable: true ,type:"number"},
	                        	profit: { editable: false ,type:"number", validation: { required: false } }, 
	                        	profitPer: { editable: false ,type:"number", validation: { required: false } }, 
	                        },
	                       
	                        /* invoiceDateCustom: function(dd){
	                        	var date=this.get("invoiceDate");
	                        	date=date.toString().replaceAll('12:00:00 AM','');
	                        	return date;
	                        } */
	                    } 
	                }
	            });
	            
	            
	            
	            var monthDataSource = new kendo.data.DataSource({
	                transport: {
	                    read:  {
	                        url: crudServiceBaseUrl + "yearTotal",
	                        dataType: "json",
	                        type: 'POST',
	                        complete :function(data){
	                        	kendo.culture("en-US");
	                        	var d=jQuery.parseJSON(data.responseText);
	                        	$("#month").html(kendo.toString(new Date(), "y"));
	                        	$("#year").html(new Date().getFullYear());
	                        	
	                        	$("#famount").html(kendo.toString(d.monthTotal.amountTotal, "n"));
	                        	$("#fprofit").html(kendo.toString(d.monthTotal.profitTotal, "n"));
	                        	$("#fprofitPer").html(kendo.toString(d.monthTotal.profitPer, "n"));
	                        	
	                        	/* $("#profitMonth").html(kendo.toString(d.monthTotal.profitTotal, "n"));
	                        	$("#profitPerMonth").html(kendo.toString(d.monthTotal.profitPer, "n")); */
	                        	
	                        	$("#profitYear").html(kendo.toString(d.yearTotal.profitTotal, "n"));
	                        	$("#profitPerYear").html(kendo.toString(d.yearTotal.profitPer, "n"));
	                        	$("#monthDiv").hide();
	                        	$("#yearDiv").show();
	                     		$("#monthDivYear").hide();
	                        }
	                    },
	                    parameterMap: function(options, operation) {
	                            return {
	                            	models: kendo.stringify(options.models),
	                            	date : function(){
	                            		var d = new Date();
	                            		var r=d.getFullYear()+'-'+(d.getMonth()+1);
	                            		return r;
	                            	}
	                            };
	                    }
	                },
	               // batch: true,
	                pageSize: 20,
	    	        serverPaging: false,
	    	        serverSorting: false,
	    	        serverFiltering : false,
	                schema: {
	                	data: "data",	
	    	            total: "total",
	                     model: {
	                         fields: {
	                        	 amountTotal: {editable: true ,type:"number",  validation: { required: false } },
	                        	 sales_person :{type:"string",editable:false},
	                        	 profitTotal: { editable: false ,type:"number", validation: { required: false } }, 
	                        	profitPer: { editable: false ,type:"number", validation: { required: false } }, 
	                        },
	                    } 
	                }
	            });
	            
	            var yearDataSource = new kendo.data.DataSource({
	                transport: {
	                    read:  {
	                        url: crudServiceBaseUrl + "monthlySales",
	                        dataType: "json",
	                        type: 'POST',
	                        complete :function(data){
	                        	kendo.culture("en-US");
	                        	$("#monthDiv").hide();
	                        	$("#yearDiv").hide();
	                     		$("#monthDivYear").show();
	                     		var d=jQuery.parseJSON(data.responseText);
	                        	$("#yamount").html(kendo.toString(d.totalss.amountTotal, "n"));
	                        	$("#yprofit").html(kendo.toString(d.totalss.profitTotal, "n"));
	                        	$("#yprofitPer").html(kendo.toString(d.totalss.profitPer, "n"));
	                     		
	                        }
	                    },
	                    parameterMap: function(options, operation) {
	                            return {
	                            	models: kendo.stringify(options.models),
	                            	year : function(){
	                            		var d = new Date();
	                            		var r=d.getFullYear();
	                            		return r;
	                            	}
	                            };
	                    }
	                },
	                pageSize: 20,
	    	        serverPaging: false,
	    	        serverSorting: false,
	    	        serverFiltering : false,
	                schema: {
	                	data: "invoices",	
	    	            total: "total",
	                     model: {
	                         fields: {
	                        	 amountTotal: {editable: true ,type:"number",  validation: { required: false } },
	                        	 month: {editable: true },
	                        	 profitTotal: { editable: false ,type:"number", validation: { required: false } }, 
	                        	profitPer: { editable: false ,type:"number", validation: { required: false } }, 
	                        },
	                    } 
	                },
	            });
	            var grid;
	            
	            var gridMonth;
	            var gridYear;
	            
	 function createGridMonth(){
	            	gridMonth=$("#gridMonth").kendoGrid({
	                    dataSource:	 monthDataSource,
	                    //pageable: true,
	                    /*  pageable: {
				           pageSizes:[5,10,20,50],
				           buttonCount: 5
				        }, */
				        columnMenu : {
				    		columns : true
				    	},
				        sortable: true, 
				        columnMenu: false,
				        filterable: false,
				        scrollable: false,
				        columns: [
		                            { field: "sales_person", title:"<span style='color:black;font-weight:bold;'>Sales Person</span>",footerTemplate: '<span>Totals</span>'},
		                            { field: "amountTotal", title:"<span style='color:black;font-weight:bold;'>Invoice Value</span>",footerTemplate: '<div id="famount" style="text-align : right"></div>' ,attributes: {style: "text-align : right"}  },
		                            { field:"profitTotal", title:"<span style='color:black;font-weight:bold;'>Profit</span>",format: '{0:n2}',footerTemplate: '<div id="fprofit" style="text-align : right"></div>',attributes: {style: "text-align : right"}  },
		                            {title:"<span style='color:black;font-weight:bold;'>Profit %</span>",field:"profitPer",format: '{0:n2} %',footerTemplate: '<div id="fprofitPer" style="text-align : right"></div>',attributes: {style: "text-align : right"}  },
		                        ],
		                    }).data("kendoGrid");
	            }
	 
	 function createGridYear(){
     	gridYear=$("#gridYear").kendoGrid({
             dataSource: yearDataSource,
		        columnMenu : {
		    		columns : true
		    	},
		        sortable: true, 
		        columnMenu: false,
		        filterable: false,
		        scrollable: false,
		        columns: [
                         { field: "month", title:"<span style='color:black;font-weight:bold;'>Month</span>",footerTemplate: '<span>Totals</span>'},
                         { field: "amountTotal", title:"<span style='color:black;font-weight:bold;'>Invoice Value</span>",footerTemplate: '<div id="yamount" style="text-align : right"></div>' ,attributes: {style: "text-align : right"}  },
                         { field:"profitTotal", title:"<span style='color:black;font-weight:bold;'>Profit</span>",format: '{0:n2}',footerTemplate: '<div id="yprofit" style="text-align : right"></div>',attributes: {style: "text-align : right"}  },
                         {title:"<span style='color:black;font-weight:bold;'>Profit %</span>",field:"profitPer",format: '{0:n2} %',footerTemplate: '<div id="yprofitPer" style="text-align : right"></div>',attributes: {style: "text-align : right"}  },
                     ],
                 }).data("kendoGrid");
     }
	            
	            function createGrid(){
	            	grid=$("#grid").kendoGrid({
	                    dataSource:	 dataSource,
				        columnMenu : {
				    		columns : true
				    	},
				        sortable: true, 
				        columnMenu: true,
				        filterable: true,
				        scrollable: false,
				        columns: [
		                            { field: "invoiceNumber", title:"Invoice Number" ,
		                            	template:
		                            		//"<span style='font-weight:bold; background-color: #\"red\"==\"red\"? \"red\" : \"green\" # ;'>#=invoiceNumber#</span>"
		                            		"<span style=' background-color : #if( isGreen ==1){# green #} else{# inherit #}# ' >#:invoiceNumber#</span>"
											,width: 200
		                            	},
		                            { title:"Invoice Date",template: "#= kendo.toString(kendo.parseDate(invoiceDate, 'MMM dd, yyyy H:mm:ss'), 'dd/MM/yyyy') #",field:"invoiceDate",width: 200}, 
		                            { field: "customer", title:"Customer",width: 350	},
		                          
		                            { field: "invoiceAmount", title:"Invoice Value",format: '{0:n2}',footerTemplate: '<div id="fiamount" style="text-align : right"></div>' ,attributes: {style: "text-align : right"} ,width: 200},
		                          
		                            { field:"profit", title:"Profit",format: '{0:n2}',footerTemplate: '<div id="fiprofit" style="text-align : right"></div>'   ,attributes: {style: "text-align : right"},width: 200 },
		                            {title:"Profit %",field:"profitPer",format: '{0:n2} %',footerTemplate: '<div id="fiprofitPer" style="text-align : right"></div>' ,attributes: {style: "text-align : right"} ,width: 200 },
		                            {title:"Status",field:"customComment",width: 250,
										template : "<div style='background-color :  #if( customComment =='Delivered'){# lightgreen #} else if (customComment =='Shipped'){# yellow #}else if (customComment =='Ordered'){# \\\\#1a75ff #}else if (customComment =='Problem'){# red #}else if (customComment =='Cancelled'){# inherit #} else if (customComment == ''  || customComment=='null'){# orange #}		else{# inherit #}#' > #if( customComment == null){# <div style=\'background-color:orange\'>&nbsp;</div> #} else{# #=customComment# #}#</div>"
									
									},
		                        ],
		                    }).data("kendoGrid");
	            }
	            
	            function refreshGrid(){
	            	 grid.dataSource.filter([]);
	          		 grid.dataSource.read();
	          		 grid.refresh();
	          		
	            }
	            function refreshGridMonth(){
	            	if(gridMonth==undefined){
	            		createGridMonth();
	            	}else{
	            		gridMonth.dataSource.filter([]);
	               		gridMonth.dataSource.read();
	               		gridMonth.refresh();
	            	}
	            	stage=3;
	            }
	            function refreshGridYear(){
	            	if(gridYear==undefined){
	            		createGridYear();
	            	}else{
	            		gridYear.dataSource.filter([]);
	               		gridYear.dataSource.read();
	               		gridYear.refresh();
	            	}
	            	stage=1;
	            }
	            var currentIndex=0;
	            var refreshTimer=1000*${time};
	            var stage=1;
	                $(document).ready(function () {
	                	
	                	$("#yearDiv").hide();
                		$("#monthDivYear").hide();
                		
                		 salesPerson=salesPersons[currentIndex++].SALES_PERSON;
 	                    createGrid();
 	                    var listSize=salesPersons.length;
 	                  
 	                    window.setInterval(function(){
	                    	
                		 if(stage==1){
                			 if(currentIndex<listSize){
                				 salesPerson=salesPersons[currentIndex++].SALES_PERSON;
                				 refreshGrid();
 	                    	 }else{
 	                    		 stage=2;
 	                    		 currentIndex=0;
 	                    		refreshGridMonth();
 	                    	 }
                		 }
                		  else if(stage==2){
                			 refreshGridMonth();
                		 }else if(stage==3){
                			 refreshGridYear();
                		 }
 	                  }, refreshTimer);  
                		 
                		 
	                	
	                /*     window.setInterval(function(){
	                    	
	                    	if(currentIndex==listSize){
	                    		currentIndex=0;
	                    	}else{
	                    		salesPerson=salesPersons[currentIndex++].SALES_PERSON;
	                    	}
	                    	
	                    	if(currentIndex==listSize){
	                    		refreshGridMonth();
	                    	}else{
	                    		refreshGrid();
	                    	}
	                    }, refreshTimer);  */
	                });
	            </script>
	            
	    <style type="text/css">
	    .k-grid-header .k-with-icon .k-link {
	    color: BLACK;
	    font-weight: bold;
	    margin-right: 1.3em;
	}
	
	#example { 
		margin : 0 5%;
	}
	.header{
		text-align: left;
	}
	.footer{
		text-align: center;
		margin: 10px 0 0 0;
	}
	h1{
		text-align: center;
		font-size: 43px;
	}
	h2{
		/* font-family: "Arial Black" */
		font-family: helvetica-w01-light, helvetica-w02-light, sans-serif;
	}
	#grid {
		height: 80%;
	}
	.k-link{
	font-size: 20px;
	text-align: center;
	}
	#grid td {
	    font-size: 18px;
	}
	</style>
	</head>
	<body>
	        <div id="example">
	       		<h1>
	       			<span style="font-size:43px;"><span style="text-shadow:#c8c8c8 1px 1px 0px, #b4b4b4 0px 2px 0px, #a0a0a0 0px 3px 0px, rgba(140, 140, 140, 0.498039) 0px 4px 0px, #787878 0px 0px 0px, rgba(0, 0, 0, 0.498039) 0px 5px 10px;">CNS Trading Limited - Sales&nbsp;</span></span>
	       			</h1>
	       		<div id="monthDiv">
	       		<div class="header">
	       			<h2><span id="salesPerson"></span> - <span id="date"></span></h2>
		       		</div>
		            <div id="grid"></div>
		           	<div class="footer">
	           		<!-- <h2>Total Profit : <span id="profit"></span> (<span id="profitPer"></span> %) </h2> -->
	           	</div>
	       		</div>
	       		<div id="yearDiv">
	       			<div class="header">
	       			<h2>Totals <span id="month"></span></h2>
		       		</div>
		            <div id="gridMonth"></div>
		           	<div class="footer">
	           		<!-- <div class="header">
	           		<h2>Totals - Year To Date <span id="year"></span></h2>
	           		</div>
	           		<h2>Totals        Profit : <span id="profitYear"></span> (<span id="profitPerYear"></span> %) </h2> -->
		       		</div>
	       		</div>
	       		
	       		
	       		<div id="monthDivYear">
	       			<div class="header">
	       			<h2>Monthly Sales - Year to Date 2016</h2>
		       		</div>
		            <div id="gridYear"></div>
		           	<div class="footer">
	           		<!-- <div class="header">
	           		<h2>Totals - Year To Date <span id="year"></span></h2>
	           		</div>
	           		<h2>Totals        Profit : <span id="profitYear"></span> (<span id="profitPerYear"></span> %) </h2> -->
		       		</div>
	       		</div>
	       		
	       		
	       		
	        </div>
	
	
	</body>
