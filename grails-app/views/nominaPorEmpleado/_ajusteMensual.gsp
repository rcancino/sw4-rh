<%@page expressionCodec="none"%>
<div class="panel-group " id="ajustePanel">
	<div class="panel panel-warning">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a data-toggle="collapse" data-parent="#ajustePanel"
					href="#ajusteCollapseOne"> Ajuste mensual </a>
			</h4>
		</div>

		<div id="ajusteCollapseOne" class="panel-collapse collapse ">
			<div class="row">
				<div class="col-md-6">
					<table class="table table-striped table-bordered ">
						<thead >
							<tr >
								<th class="text-center">Descripción</th>
								<th class="text-center">Valor</th>
							</tr>
						</thead>
						<tbody >
							<tr>
								<td> Base gravable mensual</td>
								<td ><g:formatNumber number="${ajuste.baseGravable}" type="currency"/></td>
							</tr>
							
							<tr>
								<td> Retardo/Permiso</td>
								<td ><g:formatNumber number="${ajuste.permisoRetardoAcu}" type="currency"/></td>
							</tr>
							<tr>
								<td> Base gravable neta</td>
								<td ><g:formatNumber number="${ajuste.baseGravable-ajuste.permisoRetardoAcu}" type="currency"/></td>
							</tr>
							
							<tr>
								<td> Limite Inferior</td>
								<td ><g:formatNumber number="${ajuste.limiteInferior}" format="#,###.##"/></td>
							</tr>
							<tr>
								<td> Excedente del L.I.</td>
								<td ><g:formatNumber number="${ajuste.baseGravable-ajuste.permisoRetardoAcu-ajuste.limiteInferior}" format="#,###.##"/></td>
							</tr>
							<tr>
								<td> % Sobre excedente del L.I.</td>
								<td ><g:formatNumber number="${ajuste.tarifa}" format="#,###.##"/></td>
							</tr>
							<tr>
								<td> Impuesto marginal</td>
								<td ><g:formatNumber
								 number="${((ajuste.baseGravable-ajuste.permisoRetardoAcu -ajuste.limiteInferior)*ajuste.tarifa)/100}" 
								format="#,###.##"/></td>
							</tr>
							<tr>
								<td> Cuota Fija</td>
								<td ><g:formatNumber number="${ajuste.cuotaFija}" format="#,###.##"/></td>
							</tr>
							<tr>
								<td> Impuesto Determinado</td>
								<td ><g:formatNumber number="${ajuste.impuestoMensual}" format="#,###.##"/></td>
							</tr>
							<tr>
								<td> Subsidio al Empleo</td>
								<td ><g:formatNumber number="${ajuste.subsidioMensual}" format="#,###.##"/></td>
							</tr>
							
						</tbody>
						<tfoot>
							<tr>
								
							</tr>
						</tfoot>
					</table>
				</div>
				
				<div class="col-md-6">
					<table class="table table-striped table-bordered ">
						<thead >
							<tr >
								<th class="text-center">Descripción</th>
								<th class="text-center">Valor</th>
							</tr>
						</thead>
						<tbody >
							<tr>
								<td> Impuesto del periodo</td>
								<td ><g:formatNumber number="${ajuste.impuestoFinal}" format="#,###.##"/></td>
							</tr>
							<tr>
								<td> Subsidio al empleo del periodo</td>
								<td ><g:formatNumber number="${ajuste.subsidioFinal*-1.0}" format="#,###.##"/></td>
							</tr>
							<tr>
								<td> Impuesto Acumulado</td>
								<td ><g:formatNumber number="${ajuste.impuestoAcumulado}" type="currency"/></td>
							</tr>
							<tr>
								<td> Subsidio Acumulado</td>
								<td ><g:formatNumber number="${ajuste.subsidioAcumulado}" format="#,###.##"/></td>
							</tr>
							<tr>
								<td> Resultado Impuesto</td>
								<td ><g:formatNumber number="${ajuste.resultadoImpuesto}" format="#,###.##"/></td>
							</tr>
							<tr>
								<td> Resultado Subsidio</td>
								<td ><g:formatNumber number="${ajuste.resultadoSubsidio*-1.0}" format="#,###.##"/></td>
							</tr>
							<tr>
								<td>Impuesto a cargo 
								</td>
								<td >
									<g:if test="${ajuste.resultadoSubsidio>0.0 && ajuste.impuestoAcumulado>0.0}">
										<g:formatNumber 
											number="${ (ajuste.resultadoSubsidio.abs()-ajuste.impuestoAcumulado) >0.0 ? (ajuste.resultadoSubsidio.abs()-ajuste.impuestoAcumulado):0.0}" 
											format="#,###.##"/>
									</g:if>
									<g:else>0.0</g:else>
									
								</td>
							</tr>
							<tr>
								<td>Subsidio a favor
								</td>
								<td >
									<g:if test="${ajuste.resultadoSubsidio>0.0 && ajuste.impuestoAcumulado>0.0}">
										<g:formatNumber 
											number="${ (ajuste.resultadoSubsidio.abs()-ajuste.impuestoAcumulado) <0.0 ? (ajuste.resultadoSubsidio.abs()-ajuste.impuestoAcumulado):0.0}" 
											format="#,###.##"/>
									</g:if>
									<g:else>0.0</g:else>
									
								</td>
							</tr>
							<tr>
								<td> Subsidio Empleo Aplicado</td>
								<td ><g:formatNumber number="${ajuste.resultadoSubsidioAplicado}" format="#,###.##"/></td>
							</tr>
							<tr>
								<td> Subsidio aplicado acumulado (mes)</td>
								<td ><g:formatNumber number="${ajuste.subsidioAplicado+ajuste.resultadoSubsidioAplicado}" format="#,###.##"/></td>
							</tr>
							
						</tbody>
						<tfoot>
							<tr>
								
							</tr>
						</tfoot>
					</table>
				</div>
				
			</div>
		</div>
	</div>


</div>