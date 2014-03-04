<%@ page import="com.luxsoft.sw4.rh.Puesto" %>



<div class="fieldcontain ${hasErrors(bean: puestoInstance, field: 'clave', 'error')} required">
	<label for="clave">
		<g:message code="puesto.clave.label" default="Clave" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="clave" maxlength="20" required="" value="${puestoInstance?.clave}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: puestoInstance, field: 'descripcion', 'error')} ">
	<label for="descripcion">
		<g:message code="puesto.descripcion.label" default="Descripcion" />
		
	</label>
	<g:textArea name="descripcion" cols="40" rows="5" maxlength="300" value="${puestoInstance?.descripcion}"/>

</div>

