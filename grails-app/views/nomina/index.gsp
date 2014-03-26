
<%@ page import="com.luxsoft.sw4.rh.Nomina" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="catalogos">
		<g:set var="entityName" value="${message(code: 'nomina.label', default: 'Nomina')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-nomina" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-nomina" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="tipo" title="${message(code: 'nomina.tipo.label', default: 'Tipo')}" />
					
						<g:sortableColumn property="diaDePago" title="${message(code: 'nomina.diaDePago.label', default: 'Dia De Pago')}" />
					
						<g:sortableColumn property="periodicidad" title="${message(code: 'nomina.periodicidad.label', default: 'Periodicidad')}" />
					
						<g:sortableColumn property="formaDePago" title="${message(code: 'nomina.formaDePago.label', default: 'Forma De Pago')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'nomina.dateCreated.label', default: 'Date Created')}" />
					
						<th><g:message code="nomina.empresa.label" default="Empresa" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${nominaInstanceList}" status="i" var="nominaInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${nominaInstance.id}">${fieldValue(bean: nominaInstance, field: "tipo")}</g:link></td>
					
						<td>${fieldValue(bean: nominaInstance, field: "diaDePago")}</td>
					
						<td>${fieldValue(bean: nominaInstance, field: "periodicidad")}</td>
					
						<td>${fieldValue(bean: nominaInstance, field: "formaDePago")}</td>
					
						<td><g:formatDate date="${nominaInstance.dateCreated}" /></td>
					
						<td>${fieldValue(bean: nominaInstance, field: "empresa")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${nominaInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
