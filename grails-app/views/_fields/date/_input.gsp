<%@page expressionCodec="none" %>
<r:require module="datepicker"/>
<input type="text" id="${id}" class="form-control"></input>

<r:script>
    /** JavaScrip para date picker **/
    var item=$("#${id}").datepicker({
        showOn: "both",
        changeMonth: true,
        changeYear: true,
        appendText: "",
        showAnim: "fold",
        showButtonPanel: true 
    });
    //console.log('Tipo: '+item)
</r:script>

<%-- <jqueryPicker:date id="${id}" name="${property}" value="${value}" class="form-control"/> --%>
