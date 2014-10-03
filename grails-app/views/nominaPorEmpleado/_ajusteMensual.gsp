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
								<th class="text-center">Variable</th>
								<th class="text-center">Valor</th>
							</tr>
						</thead>
						<tbody >
							<tr>
								<td> Base gravable</td>
								<td ><g:formatNumber number="${ajuste.baseGravable}" type="currency"/></td>
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