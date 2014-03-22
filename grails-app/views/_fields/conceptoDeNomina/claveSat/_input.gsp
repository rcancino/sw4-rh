<%@page expressionCodec="none" %>
<%--<g:select name="${property}" from="${bean.tipo=='PERCEPTION'?SatPercepcion.findAll():SatDeduccion.findAll()}"/>--%>
<g:select class="form-control"  
	name="${property}" 
	value="${value}"
	from="${com.luxsoft.sw4.rh.sat.SatPercepcion.findAll()}" 
	optionKey="clave" optionValue="descripcion"
	noSelection="['':'Seleccione una clave']"/>




