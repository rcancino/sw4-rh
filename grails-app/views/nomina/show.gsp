<%@ page import="com.luxsoft.sw4.rh.Nomina" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="dashboard_1"/>
	<title>Nomina</title>
</head>
<body>
	
	<content tag="header">
		<g:link action="index" id="${nominaInstance.id}" params="[periodicidad:nominaInstance.periodicidad]">
			<h4>Nómina: ${nominaInstance.folio} ${nominaInstance.periodicidad}  ${nominaInstance.formaDePago } (${nominaInstance.periodo}) </h4>
		</g:link>
	</content>
	
	<content tag="buttonBar">
	
		<div class="button-panel">
			<div class="btn-group">
			<g:link action="index" class="btn btn-default">
				<span class="glyphicon glyphicon-repeat"></span> Refrescar
			</g:link>
			
			
			</div>
			
			<div class="btn-group">
				<button type="button" name="reportes" class="btn btn-default dropdown-toggle" data-toggle="dropdown" role="menu">Reportes <span class="caret"></span></button>
				<ul class="dropdown-menu">
					<li>
						<g:jasperReport
          						jasper="NominaCaratula"
          						format="PDF"
          						name="Carátula">
    							<g:hiddenField name="NOMINA" value="${nominaInstance.id}"/>
 						</g:jasperReport>
					</li>
					
					<g:if test="${['QUINCENAL','SEMANAL'].contains(nominaInstance.periodicidad) }">
						
						
						<li>
							<g:jasperReport
          						jasper="${nominaInstance.periodicidad=='QUINCENAL'?'NominaCaratulaQ':'NominaCaratulaS' }"
          						format="PDF"
          						name="Resumen">
    							<g:hiddenField name="NOMINA" value="${nominaInstance.id}"/>
 							 </g:jasperReport>
						</li>
						
					</g:if>
					<li>
							<button class="btn btn-default" data-toggle="modal" data-target="#detalleDeNominaForm"> Detalle</button>
					</li>
				</ul>
			</div> <!-- Fin .btn-group -->
			
			<div class="btn-group">
				<button type="button" name="reportes" class="btn btn-default dropdown-toggle" data-toggle="dropdown" >Operaciones <span class="caret"></span></button>
				<ul class="dropdown-menu">
					
					<li>
						<g:link  controller="nominaPorEmpleado" action="create" id="${nominaInstance.id}"> 
							<span class="glyphicon glyphicon-plus"></span> Agregar
						</g:link> 
					</li>
					
					<li>
						<g:link  action="actualizarPartidas" id="${nominaInstance.id}"> 
							<span class="glyphicon glyphicon-cog"></span> Actualizar
						</g:link> 
					</li>
					<li>
						<g:link action="delete" id="${nominaInstance.id}" onclick="return confirm('Eliminar toda la nomina?');"> 
							<span class="glyphicon glyphicon-trash"></span> Eliminar
						</g:link> 
					</li>
					<li>
						<g:link action="timbrar" id="${nominaInstance.id}" onclick="return confirm('Timbrar toda la nomina?');"> 
							 Timbrar
						</g:link> 
					</li>
					<li>
						<g:link action="depurar" 
							id="${nominaInstance.id}" 
							onclick="return confirm('Depurar toda la nomina?');"> Depurar
						</g:link> 
					</li>
					<li>
						<g:link  action="ajusteMensualIsr" 
							onClick="return confirm('Aplicar ajuste mensual ISR?');"
							id="${nominaInstance.id}" >
							 Ajuste mensual ISR 
						</g:link>
					</li>
					<li>
						<g:link  action="aplicarCalculoAnual" 
							onClick="return confirm('Aplicar calculo anual ISR?');"
							id="${nominaInstance.id}" >
							 Calculo anual
						</g:link>
					</li>
					<li>
						<g:link  controller="reciboDeNomina"
							action="imprimirCfdis" 
							onClick="return confirm('Imprimir todos los recibos de nómina?');"
							id="${nominaInstance.id}" >
							<span class="glyphicon glyphicon-print"></span>
							 Imprimir recibos
						</g:link>
					</li>
					
					
				</ul>
			</div> <!-- Fin .btn-group -->
			
		</div>
		<g:render template="reporteDetalleDeNominaDialog"/>
	</content>
	<content tag="grid">
		
		<ul class="nav nav-tabs" role="tablist">
		  <li class="${nominaInstance.periodicidad=='SEMANAL'?'active':''}"><a href="#andrade" role="tab" data-toggle="tab">Andrade</a></li>
		  <li><a href="#bolivar" role="tab" data-toggle="tab">Bolivar</a></li>
		  <li><a href="#calle4" role="tab" data-toggle="tab">Calle 4</a></li>
		  <li><a href="#cf5febrero" role="tab" data-toggle="tab">5 de Febrero</a></li>
		  <li><a href="#tacuba" role="tab" data-toggle="tab">Tacuba</a></li>
		  <li class="${nominaInstance.periodicidad=='QUINCENAL'?'active':''}"><a href="#oficinas" role="tab" data-toggle="tab">Oficinas</a></li>
		  <li><a href="#ventas" role="tab" data-toggle="tab">Ventas</a></li>
		</ul>

		<div class="tab-content">
	  		<div class="tab-pane ${nominaInstance.periodicidad=='SEMANAL'?'active':''}" id="andrade">
				<g:render template="nominaDetGridPanel" model="['partidasList':partidasMap['ANDRADE']]"/>
	  		</div>
	  		<div class="tab-pane" id="bolivar">
	  			<g:render template="nominaDetGridPanel" model="['partidasList':partidasMap['BOLIVAR']]"/>
	  		</div>
	  		<div class="tab-pane" id="calle4">
	  			<g:render template="nominaDetGridPanel" model="['partidasList':partidasMap['CALLE4']]"/>
	  		</div>
	  		<div class="tab-pane" id="cf5febrero">
	  			<g:render template="nominaDetGridPanel" model="['partidasList':partidasMap['CF5FEBRERO']]"/>
	  		</div>
	  		<div class="tab-pane" id="tacuba">
	  			<g:render template="nominaDetGridPanel" model="['partidasList':partidasMap['TACUBA']]"/>
	  		</div>
	  		<div class="tab-pane ${nominaInstance.periodicidad=='QUINCENAL'?'active':''}" id="oficinas">
	  			<g:render template="nominaDetGridPanel" model="['partidasList':partidasMap['OFICINAS']]"/>
	  		</div>
	  		<div class="tab-pane" id="ventas">
	  			<g:render template="nominaDetGridPanel" model="['partidasList':partidasMap['VENTAS']]"/>
	  		</div>
		</div>
	</content>



</body>
</html>
