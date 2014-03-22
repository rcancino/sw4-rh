<%@page expressionCodec="none" %>
 
<g:select class="form-control"  
	name="${property}" 
	value="${value?.id}"
	from="${com.luxsoft.sw4.rh.Puesto.findAll()}" 
	optionKey="id" optionValue="clave"
	noSelection="[null:'Seleccione una puesto']"/>




