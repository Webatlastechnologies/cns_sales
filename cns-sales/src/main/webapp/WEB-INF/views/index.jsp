<!DOCTYPE html>
<html>
<head>
    <title>Invoices</title>
    <link rel="stylesheet" href="resources/styles/kendo.common.min.css" />
    <link rel="stylesheet" href="resources/styles/kendo.default.min.css" />
 <!--    <link rel="stylesheet" href="resources/styles/AllViewAllPage.css" /> -->

    <script src="resources/js/jquery.min.js"></script>
    <script src="resources/js/kendo.all.min.js"></script>
    <style type="text/css">
    .k-grid-header .k-with-icon .k-link {
    color: BLACK;
    font-weight: bold;
    margin-right: 1.3em;
}
#grid {
	max-height: 80%;
}
</style>
</head>
<body>
        <div id="example">
            <div id="grid"></div>

            <script>
                $(document).ready(function () {
                    var crudServiceBaseUrl = "",
                        dataSource = new kendo.data.DataSource({
                            transport: {
                                read:  {
                                    url: crudServiceBaseUrl + "invoice",
                                    dataType: "json",
                                    type: "POST"
                                },
                                update: {
                                    url: crudServiceBaseUrl + "invoice/update",
                                    dataType: "json"
                                },
                                destroy: {
                                    url: crudServiceBaseUrl + "/Products/Destroy",
                                    dataType: "json"
                                },
                                create: {
                                    url: crudServiceBaseUrl + "/Products/Create",
                                    dataType: "json"
                                },
                                parameterMap: function(options, operation) {
                                    if (operation !== "read" && options.models) {
                                        return {models: kendo.stringify(options.models)};
                                    }
                                }
                            },
                            batch: true,
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
                                    	invoiceDate : {editable:true ,type:"datetime"},
                                    	invoiceAmount: {editable: true ,type:"number",  validation: { required: false } },
                                    	customer :{type:"string",editable:false},
                                    	salesPerson :{type:"string",editable:true},
                                    	customComment:{ editable: true, type: "string", validation: { required: false} },
                                    	cost: { editable: true ,type:"number"},
                                    	profit: { editable: false ,type:"number", validation: { required: false } }, 
                                    	profitPer: { editable: false ,type:"number", validation: { required: false } }, 
                                    },
                                }
                            }
                        });

                    $("#grid").kendoGrid({
                        dataSource:	 dataSource,
                        pageable: true,
                         pageable: {
				           pageSizes:[5,10,20,50],
				           buttonCount: 5
				        },
				        columnMenu : {
				    		columns : true
				    	},
				        sortable: true, 
				        columnMenu: true,
				        filterable: true,
				        scrollable: false,
                        columns: [
                            { field: "invoiceNumber", title:"Invoice Number"},
                            { title:"Invoice Date",field : "invoiceDate",template:"#=kendo.toString(invoiceDate, 'D')#",}, 
                            { field: "customer", title:"Customer"},
                            {field:"salesPerson",title:"Sales Person"},
                            { field: "invoiceAmount", title:"Invoice Value",format: '{0:n2}' },
                            { field:"cost", title:"Cost",format: '{0:n2}' },
                            { field:"profit", title:"Profit",format: '{0:n2}' },
                            {title:"Profit %",field:"profitPer",format: '{0:n2} %'},
                            {title:"Status",field:"customComment"},
                            { command: ["edit"], title: "&nbsp;"}],
                        editable: "inline"
                    });
                });
            </script>
        </div>


</body>