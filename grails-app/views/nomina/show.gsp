
<%@ page import="com.luxsoft.sw4.rh.Nomina" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nomina.label', default: 'Nomina')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-nomina" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-nomina" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list nomina">
			
				<g:if test="${nominaInstance?.tipo}">
				<li class="fieldcontain">
					<span id="tipo-label" class="property-label"><g:message code="nomina.tipo.label" default="Tipo" /></span>
					
						<span class="property-value" aria-labelledby="tipo-label"><g:fieldValue bean="${nominaInstance}" field="tipo"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${nominaInstance?.diaDePago}">
				<li class="fieldcontain">
					<span id="diaDePago-label" class="property-label"><g:message code="nomina.diaDePago.label" default="Dia De Pago" /></span>
					
						<span class="property-value" aria-labelledby="diaDePago-label"><g:fieldValue bean="${nominaInstance}" field="diaDePago"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${nominaInstance?.periodicidad}">
				<li class="fieldcontain">
					<span id="periodicidad-label" class="property-label"><g:message code="nomina.periodicidad.label" default="Periodicidad" /></span>
					
						<span class="property-value" aria-labelledby="periodicidad-label"><g:fieldValue bean="${nominaInstance}" field="periodicidad"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${nominaInstance?.formaDePago}">
				<li class="fieldcontain">
					<span id="formaDePago-label" class="property-label"><g:message code="nomina.formaDePago.label" default="Forma De Pago" /></span>
					
						<span class="property-value" aria-labelledby="formaDePago-label"><g:fieldValue bean="${nominaInstance}" field="formaDePago"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${nominaInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="nomina.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${nominaInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${nominaInstance?.empresa}">
				<li class="fieldcontain">
					<span id="empresa-label" class="property-label"><g:message code="nomina.empresa.label" default="Empresa" /></span>
					
						<span class="property-value" aria-labelledby="empresa-label"><g:link controller="empresa" action="show" id="${nominaInstance?.empresa?.id}">${nominaInstance?.empresa?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${nominaInstance?.folio}">
				<li class="fieldcontain">
					<span id="folio-label" class="property-label"><g:message code="nomina.folio.label" default="Folio" /></span>
					
						<span class="property-value" aria-labelledby="folio-label"><g:fieldValue bean="${nominaInstance}" field="folio"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${nominaInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="nomina.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${nominaInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${nominaInstance?.pago}">
				<li class="fieldcontain">
					<span id="pago-label" class="property-label"><g:message code="nomina.pago.label" default="Pago" /></span>
					
						<span class="property-value" aria-labelledby="pago-label"><g:formatDate date="${nominaInstance?.pago}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${nominaInstance?.partidas}">
				<li class="fieldcontain">
					<span id="partidas-label" class="property-label"><g:message code="nomina.partidas.label" default="Partidas" /></span>
					
						<g:each in="${nominaInstance.partidas}" var="p">
						<span class="property-value" aria-labelledby="partidas-label"><g:link controller="nominaPorEmpleado" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${nominaInstance?.periodo}">
				<li class="fieldcontain">
					<span id="periodo-label" class="property-label"><g:message code="nomina.periodo.label" default="Periodo" /></span>
					
						<span class="property-value" aria-labelledby="periodo-label"><g:fieldValue bean="${nominaInstance}" field="periodo"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${nominaInstance?.total}">
				<li class="fieldcontain">
					<span id="total-label" class="property-label"><g:message code="nomina.total.label" default="Total" /></span>
					
						<span class="property-value" aria-labelledby="total-label"><g:fieldValue bean="${nominaInstance}" field="total"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:nominaInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${nominaInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
