<%@page expressionCodec="none" %>

<g:select class="form-control"  
	name="${property}" 
	value="${value?.id}"
	from="${com.luxsoft.sw4.rh.MotivoDeSeparacion.findAll()}" 
	optionKey="id" optionValue="descripcion"
	noSelection="[null:'Seleccione una causa']"/>




