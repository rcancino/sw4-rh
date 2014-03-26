<%@page expressionCodec="none" %>
<r:require module="datepicker"/>
<div class="input-group">
	<input type="text" id="${property}" name="${property}" class="form-control" value="${g.formatDate(date:value,format:'dd/MM/yyyy') }"></input>
	<span class="input-group-btn">
        <button id="${property}Btn" class="btn btn-default" type="button"><span class="glyphicon glyphicon-calendar"></span></button>
     </span>
</div>


<r:script>
    /** JavaScrip para date picker **/
   $("#${property}").datepicker({
        //showOn: "both",
        changeMonth: true,
        changeYear: true,
        appendText: "",
        showAnim: "fold",
        showButtonPanel: true,
        dateFormat:"dd/mm/yy" 
    });
    $("#${property}Btn").click(function(){
    	$("#${property}").datepicker('show');
    	console.log("Date picker fired..");
    });
    //console.log('Tipo: '+item)
</r:script>

<%-- <jqueryPicker:date id="${id}" name="${property}" value="${value}" class="form-control"/> --%>
