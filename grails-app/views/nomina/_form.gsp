<%@ page import="com.luxsoft.sw4.rh.Nomina" %>



<div class="fieldcontain ${hasErrors(bean: nominaInstance, field: 'tipo', 'error')} ">
	<label for="tipo">
		<g:message code="nomina.tipo.label" default="Tipo" />
		
	</label>
	<g:select name="tipo" from="${nominaInstance.constraints.tipo.inList}" value="${nominaInstance?.tipo}" valueMessagePrefix="nomina.tipo" noSelection="['': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: nominaInstance, field: 'diaDePago', 'error')} ">
	<label for="diaDePago">
		<g:message code="nomina.diaDePago.label" default="Dia De Pago" />
		
	</label>
	<g:select name="diaDePago" from="${nominaInstance.constraints.diaDePago.inList}" value="${nominaInstance?.diaDePago}" valueMessagePrefix="nomina.diaDePago" noSelection="['': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: nominaInstance, field: 'periodicidad', 'error')} ">
	<label for="periodicidad">
		<g:message code="nomina.periodicidad.label" default="Periodicidad" />
		
	</label>
	<g:select name="periodicidad" from="${nominaInstance.constraints.periodicidad.inList}" value="${nominaInstance?.periodicidad}" valueMessagePrefix="nomina.periodicidad" noSelection="['': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: nominaInstance, field: 'formaDePago', 'error')} ">
	<label for="formaDePago">
		<g:message code="nomina.formaDePago.label" default="Forma De Pago" />
		
	</label>
	<g:select name="formaDePago" from="${nominaInstance.constraints.formaDePago.inList}" value="${nominaInstance?.formaDePago}" valueMessagePrefix="nomina.formaDePago" noSelection="['': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: nominaInstance, field: 'empresa', 'error')} required">
	<label for="empresa">
		<g:message code="nomina.empresa.label" default="Empresa" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="empresa" name="empresa.id" from="${com.luxsoft.sw4.Empresa.list()}" optionKey="id" required="" value="${nominaInstance?.empresa?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: nominaInstance, field: 'folio', 'error')} required">
	<label for="folio">
		<g:message code="nomina.folio.label" default="Folio" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="folio" type="number" value="${nominaInstance.folio}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: nominaInstance, field: 'pago', 'error')} required">
	<label for="pago">
		<g:message code="nomina.pago.label" default="Pago" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="pago" precision="day"  value="${nominaInstance?.pago}"  />

</div>

<div class="fieldcontain ${hasErrors(bean: nominaInstance, field: 'partidas', 'error')} ">
	<label for="partidas">
		<g:message code="nomina.partidas.label" default="Partidas" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${nominaInstance?.partidas?}" var="p">
    <li><g:link controller="nominaPorEmpleado" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="nominaPorEmpleado" action="create" params="['nomina.id': nominaInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'nominaPorEmpleado.label', default: 'NominaPorEmpleado')])}</g:link>
</li>
</ul>


</div>
<fieldset class="embedded"><legend><g:message code="nomina.periodo.label" default="Periodo" /></legend>
<div class="fieldcontain ${hasErrors(bean: nominaInstance, field: 'periodo.fechaFinal', 'error')} required">
	<label for="periodo.fechaFinal">
		<g:message code="nomina.periodo.fechaFinal.label" default="Fecha Final" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="fechaFinal" precision="day"  value="${periodoInstance?.fechaFinal}"  />

</div>

<div class="fieldcontain ${hasErrors(bean: nominaInstance, field: 'periodo.fechaInicial', 'error')} required">
	<label for="periodo.fechaInicial">
		<g:message code="nomina.periodo.fechaInicial.label" default="Fecha Inicial" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="fechaInicial" precision="day"  value="${periodoInstance?.fechaInicial}"  />

</div>
</fieldset>
<div class="fieldcontain ${hasErrors(bean: nominaInstance, field: 'total', 'error')} required">
	<label for="total">
		<g:message code="nomina.total.label" default="Total" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="total" value="${fieldValue(bean: nominaInstance, field: 'total')}" required=""/>

</div>

