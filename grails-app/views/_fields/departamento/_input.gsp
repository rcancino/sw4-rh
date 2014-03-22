<%@page expressionCodec="none" %>

<g:select class="form-control"  
	name="${property}" 
	value="${value?.id}"
	from="${com.luxsoft.sw4.rh.Departamento.findAll()}" 
	optionKey="id" optionValue="clave"
	/>




