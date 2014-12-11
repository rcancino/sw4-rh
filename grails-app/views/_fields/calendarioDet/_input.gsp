<%@page expressionCodec="none" %>
<g:hiddenField id="calendarioDetId" name="${property}.id" value="${value?.id}" />
<input type="text" id="${property}" name="${property}Nombre"  class="form-control calendarioDetField" 
	value="${value}"></input>
<r:require module="jquery-ui"/>
<r:script>
$(function(){
	$(".calendarioDetField").autocomplete({
			source:'<g:createLink controller="calendarioDet" action="getCalendariosAsJSON"/>',
			minLength:3,
			select:function(e,ui){
				console.log('Valor seleccionado: '+ui.item.id);
				$("#calendarioDetId").val(ui.item.id);
			},
			appendTo: "#calendarioDetPanel"
	});
});
</r:script>



