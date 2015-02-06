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
								<td> Base gravable</td>
								<td ><g:formatNumber number="${ajuste.baseGravable}" type="currency"/></td>
							</tr>
							<tr>
								<td> Retardo/Permisodata</td>
								<td ><g:formatNumber number="${ajuste.permisoRetardoAcu}" type="currency"/></td>
							</tr>
							<tr>
								<td> Limite Inferior</td>
								<td ><g:formatNumber number="${ajuste.limiteInferior}" format="#,###.##"/></td>
							</tr>
							<tr>
								<td> Excedente del L.I.</td>
								<td ><g:formatNumber number="${ajuste.baseGravable-ajuste.limiteInferior}" format="#,###.##"/></td>
							</tr>
							<tr>
								<td> Tarifa</td>
								<td ><g:formatNumber number="${ajuste.tarifa}" format="#,###.##"/></td>
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
								<td> Impuesto Acumulado</td>
								<td ><g:formatNumber number="${ajuste.impuestoAcumulado}" type="currency"/></td>
							</tr>
							<tr>
								<td> Subsidio Acumulado</td>
								<td ><g:formatNumber number="${ajuste.subsidioAcumulado}" format="#,###.##"/></td>
							</tr>
							<tr>
								<td> Impuesto Final</td>
								<td ><g:formatNumber number="${ajuste.impuestoFinal}" format="#,###.##"/></td>
							</tr>
							<tr>
								<td> Subsidio Final</td>
								<td ><g:formatNumber number="${ajuste.subsidioFinal*-1.0}" format="#,###.##"/></td>
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
								<td> Subsidio Empleo Aplicado</td>
								<td ><g:formatNumber number="${ajuste.resultadoSubsidioAplicado}" format="#,###.##"/></td>
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