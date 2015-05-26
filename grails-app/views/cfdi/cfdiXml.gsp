<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>Cfdi ${cfdiInstance.folio}</title>
</head>
<body>
	<div class="container">
	
		<div class="row">
			<div class="col-md-12">
				<div class="panel panel-primary">
					<!-- Default panel contents -->
  					<div class="panel-heading">
  						Cfdi: ${cfdiInstance.serie}- ${cfdiInstance?.folio}
  					</div>
  					<div class="panel-body">
						<div class="alert alert-info">
							${xml}
						</div>
  					</div>
  					<div class="panel-footer">
  						<div class="btn-group">
  						
  							<g:link class="btn btn-default btn-sm" action="show" resource="${cfdiInstance}">Cfdi</g:link>
  							
							<g:link class="btn btn-default btn-sm" action="descargarXml" resource="${cfdiInstance}">
								<span class="glyphicon glyphicon-cloud-download"> Descargar XML</span>
							</g:link>
							
							<g:link  action="cancelar" class="btn btn-default btn-sm"  
									onclick="return confirm('Cancelar CFDI?');" id="${cfdiInstance.id }">
								<span class="glyphicon glyphicon-remove-circle"></span> Cancelar
							</g:link>
							
  						</div>
						
					</div>
				</div>

			</div>

		</div><!-- end .row -->
		


		
	</div>
	
	
</body>
</html>

<%--
<%@ page import="com.luxsoft.cfdi.Cfdi" %>
<!DOCTYPE html>
<html>
	<head>
		<title>CFDI ${cfdiInstance.serie} - ${cfdiInstance.folio}</title>
	</head>
	<body>
		<a href="#show-cfdi" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				
			</ul>
		</div>
		<div id="show-cfdi" class="content scaffold-show" role="main">
			<h1>Comprobante fiscal digital CFDI id: ${cfdiInstance.id }</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list cfdi">
			
				<g:if test="${cfdiInstance?.serie}">
				<li class="fieldcontain">
					<span id="serie-label" class="property-label"><g:message code="cfdi.serie.label" default="Serie" /></span>
					
						<span class="property-value" aria-labelledby="serie-label"><g:fieldValue bean="${cfdiInstance}" field="serie"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cfdiInstance?.tipo}">
				<li class="fieldcontain">
					<span id="tipo-label" class="property-label"><g:message code="cfdi.tipo.label" default="Tipo" /></span>
					
						<span class="property-value" aria-labelledby="tipo-label"><g:fieldValue bean="${cfdiInstance}" field="tipo"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cfdiInstance?.fecha}">
				<li class="fieldcontain">
					<span id="fecha-label" class="property-label"><g:message code="cfdi.fecha.label" default="Fecha" /></span>
					
						<span class="property-value" aria-labelledby="fecha-label"><g:formatDate date="${cfdiInstance?.fecha}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${cfdiInstance?.folio}">
				<li class="fieldcontain">
					<span id="folio-label" class="property-label"><g:message code="cfdi.folio.label" default="Folio" /></span>
					
						<span class="property-value" aria-labelledby="folio-label"><g:fieldValue bean="${cfdiInstance}" field="folio"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cfdiInstance?.uuid}">
				<li class="fieldcontain">
					<span id="uuid-label" class="property-label"><g:message code="cfdi.uuid.label" default="Uuid" /></span>
					
						<span class="property-value" aria-labelledby="uuid-label"><g:fieldValue bean="${cfdiInstance}" field="uuid"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cfdiInstance?.timbrado}">
				<li class="fieldcontain">
					<span id="timbrado-label" class="property-label"><g:message code="cfdi.timbrado.label" default="Timbrado" /></span>
					
						<span class="property-value" aria-labelledby="timbrado-label"><g:formatDate date="${cfdiInstance?.timbrado}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${cfdiInstance?.emisor}">
				<li class="fieldcontain">
					<span id="emisor-label" class="property-label"><g:message code="cfdi.emisor.label" default="Emisor" /></span>
					
						<span class="property-value" aria-labelledby="emisor-label"><g:fieldValue bean="${cfdiInstance}" field="emisor"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cfdiInstance?.receptor}">
				<li class="fieldcontain">
					<span id="receptor-label" class="property-label"><g:message code="cfdi.receptor.label" default="Receptor" /></span>
					
						<span class="property-value" aria-labelledby="receptor-label"><g:fieldValue bean="${cfdiInstance}" field="receptor"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cfdiInstance?.receptorRfc}">
				<li class="fieldcontain">
					<span id="rfc-label" class="property-label"><g:message code="cfdi.rfc.label" default="Rfc" /></span>
					
						<span class="property-value" aria-labelledby="rfc-label"><g:fieldValue bean="${cfdiInstance}" field="receptorRfc"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cfdiInstance?.xmlName}">
				<li class="fieldcontain">
					<span id="xmlName-label" class="property-label"><g:message code="cfdi.xmlName.label" default="Xml Name" /></span>
					
						<span class="property-value" aria-labelledby="xmlName-label"><g:fieldValue bean="${cfdiInstance}" field="xmlName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cfdiInstance?.xml}">
				<li class="fieldcontain">
					<span id="xml-label" class="property-label"><g:message code="cfdi.xml.label" default="Xml" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${cfdiInstance?.cadenaOriginal}">
				<li class="fieldcontain">
					<span id="cadenaOriginal-label" class="property-label"><g:message code="cfdi.cadenaOriginal.label" default="Cadena Original" /></span>
					
						<span class="property-value" aria-labelledby="cadenaOriginal-label"><g:fieldValue bean="${cfdiInstance}" field="cadenaOriginal"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cfdiInstance?.origen}">
				<li class="fieldcontain">
					<span id="origen-label" class="property-label"><g:message code="cfdi.origen.label" default="Origen" /></span>
					
						<span class="property-value" aria-labelledby="origen-label"><g:fieldValue bean="${cfdiInstance}" field="origen"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cfdiInstance?.tipo}">
				<li class="fieldcontain">
					<span id="tipoDeCfdi-label" class="property-label"><g:message code="cfdi.tipoDeCfdi.label" default="Tipo De Cfdi" /></span>
					
						<span class="property-value" aria-labelledby="tipoDeCfdi-label"><g:fieldValue bean="${cfdiInstance}" field="tipo"/></span>
					
				</li>
				</g:if>
			
				
			
				
			
				<g:if test="${cfdiInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="cfdi.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${cfdiInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				
			
				
			
				<g:if test="${cfdiInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="cfdi.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${cfdiInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				
			
				<g:if test="${cfdiInstance?.total}">
				<li class="fieldcontain">
					<span id="total-label" class="property-label"><g:message code="cfdi.total.label" default="Total" /></span>
					
						<span class="property-value" aria-labelledby="total-label"><g:fieldValue bean="${cfdiInstance}" field="total"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<fieldset class="buttons">
				<g:jasperReport
					controller="cfdi"
					action="imprimirCfdi"
					jasper="CFDI" 
					format="PDF" 
					name="Imprimir CFDI">
							<g:hiddenField name="id" value="${cfdiInstance.id}"/>
				</g:jasperReport>
			</fieldset>
			<g:form url="[resource:cfdiInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="timbrar" resource="${cfdiInstance}">Timbrar</g:link>
					<g:link class="edit" action="mostrarXml" resource="${cfdiInstance}">XML</g:link>
					<g:link class="create" action="descargarXml" resource="${cfdiInstance}">Descargar XML</g:link>
					<g:if test="${cfdiInstance.uuid }">
						<g:link  action="cancelar"  
							onclick="return confirm('Cancelar CFDI?');" id="${cfdiInstance.id }">Cancelar</g:link>
					</g:if>
					<g:else>
						<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					</g:else>
					
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
 --%>