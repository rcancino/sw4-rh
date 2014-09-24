<%@page expressionCodec="none" %>
<g:hiddenField id="empleadoId" name="${property}.id" value="${value?.id}" />
<input type="text" id="${property}" name="${property}Nombre"  class="form-control empleadoField" value="${value}"></input>
<r:require module="jquery-ui"/>
<r:script>
$(function(){
	$(".empleadoField").autocomplete({
			source:'<g:createLink  action="getEmpleados"/>',
			minLength:3,
			select:function(e,ui){
				console.log('Valor seleccionado: '+ui.item.id);
				$("#empleadoId").val(ui.item.id);
			}
	});
});
</r:script>



