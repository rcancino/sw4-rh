

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="layout" content="catalogos"/>
		<title>Recibo </title>
	</head>
	<body>
	
		
				
		<g:if test="${nominaPorEmpladoInstance?.cfdi?.uuid}">
			<g:jasperReport
				controller="reciboDeNomina"
				action="imprimirCfdi"
				jasper="CFDI" 
				format="PDF" 
				name="">
				<g:hiddenField name="id" value="${nominaPorEmpladoInstance.id}"/>
			</g:jasperReport>
		</g:if>
		<%-- 
		<g:form action="cancelar" id="${nominaPorEmpladoInstance.id }" method="POST">
			<fieldset>
			<div class="">
				
				<g:link class="btn btn-info" action="mostrarXml" id="${nominaPorEmpladoInstance?.cfdi?.id}">Ver XML</g:link>
				<g:if test="${nominaPorEmpladoInstance?.cfdi?.uuid }">
					
					<g:link class="btn btn-success" action="descargarXml" resource="${cfdiInstance}"><i class="icon-download icon-white"></i> Descargar XML</g:link>
					<g:actionSubmit class="btn btn-danger" action="cancelar" value="${message(code: 'default.button.cancel.label', default: 'Cancelar')}" 
						onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Seguro que desa cancelar?')}');" />
				</g:if>
				<g:else>
					<g:link class="btn btn-primary" action="timbrar" id="${cfdiInstance.id}"><i class="icon-qrcode icon-white"></i> Timbrar</g:link>
					<g:actionSubmit class="btn btn-danger" action="delete" value="Eliminar" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</g:else>
			</div>
			</fieldset>
		</g:form>				
		--%>		
				
			
		
		
		
		
	</body>
</html>
