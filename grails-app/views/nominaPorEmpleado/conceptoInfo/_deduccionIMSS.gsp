<!--
	log.debug 'Salario minimo: '+salarioMinimo
	model.salarioMinimo=salarioMinimo

	log.debug "Faltas $nominaPorEmpleado.faltas  FactorDescanso: $factorDescanso"
	model.faltas=nominaPorEmpleado.faltas 
	model.factorDescanso=factorDescanso
	
	log.debug 'Dias trabajados: '+diasTrabajados+ ' Vacaciones: '+nominaPorEmpleado.vacaciones
	log.debug 'Dias del periodo: '+diasDelPeriodo
	model.diasTrabajados=diasTrabajados
	model.diasDelPeriodo=diasDelPeriodo
	
	log.debug 'EyM sobre dif. entre SBC y 3 SMGDF: '+emd
	model.eym=emd
	
	
	log.debug 'Prestaciones en dinero EyM sobre SBC: '+pd
	model.prestacionesEnDinero=pd
	
	log.debug 'Gastos mdicos pensionado sobre SBC: '+gmp
	model.gastosMedicos=gmp
	
	log.debug 'Invalidez y Vida sobre SBC: '+iv
	model.sbc=iv
	
	log.debug 'Seguro de Retiro: '+sr
	model.retiro=sr
	
	
	log.debug 'Cesanta edad avanzada y vejez sobre SBC: '+cv
	model.cesantia=cv
	
	
	log.debug 'Seguro de Guarderas sobre SBC: '+sg
	model.seguroGuarderias=sg
	
	log.debug 'Riesgos de trabajo: '+rt
	model.riesgo=rt
	
	
	log.debug 'Infonavit: '+inf
	model.infonavit=inf
-->
<div class="">
	<table class="table table-striped table-bordered table-small-font">
		<thead >
			<tr >
				<th class="text-center">Variable</th>
				<th class="text-center">Valor</th>
			</tr>
		</thead>
		<tbody class="text-right">
			
			<tr>
				<td class="text-right">Salario mínimo</td>
				<td><g:formatNumber number="${salarioMinimo}" type="currency"/></td>
			</tr>
			<tr>
				<td class="text-right">Faltas</td>
				<td>${faltas}</td>
			</tr>
			<tr>
				<td class="text-right">Factor Desc</td>
				<td>${factorDescanso}</td>
			</tr>
			<tr>
				<td class="text-right">Dias Trabajados</td>
				<td><g:formatNumber number="${diasTrabajados}" format="###.##"/></td>
			</tr>
			<tr>
				<td class="text-right">Dias del periodo</td>
				<td><g:formatNumber number="${diasDelPeriodo}" format="###.##"/></td>
				
			</tr>
			<tr>
				<td class="text-right">E y M</td>
				<td><g:formatNumber number="${eym}" type="currency"/></td>
			</tr>
			<tr>
				<td class="text-right">Prestaciones en dinero EyM sobre SBC</td>
				<td><g:formatNumber number="${prestacionesEnDinero}" type="currency"/></td>
			</tr>
			<tr>
				<td class="text-right">Gastos mdicos pensionado sobre SBC</td>
				<td><g:formatNumber number="${gastosMedicos}" type="currency"/></td>
			</tr>
			<tr>
				<td class="text-right">Invalidez y Vida </td>
				<td><g:formatNumber number="${sbc}" type="currency"/></td>
			</tr>
			<tr>
				<td class="text-right">Seguro de Retiro </td>
				<td><g:formatNumber number="${sr}" type="currency"/></td>
			</tr>

			<tr>
				<td class="text-right">Cesanta edad avanzada</td>
				<td><g:formatNumber number="${cesantia}" type="currency"/></td>
			</tr>
			<tr>
				<td class="text-right">Seguro de Guarderias </td>
				<td><g:formatNumber number="${sr}" type="currency"/></td>
			</tr>
			<tr>
				<td class="text-right">Riesgos de trabajo </td>
				<td><g:formatNumber number="${seguroGuarderias}" type="currency"/></td>
			</tr>
			<tr>
				<td class="text-right">Infonavit </td>
				<td><g:formatNumber number="${infonavit}" type="currency"/></td>
			</tr>
			
			
		</tbody>
		<tfoot>
			<tr>
				<th>Total Excento</th>
				<th class="text-right"><g:formatNumber number="${importeExcento}" type="currency"/></th>
			</tr>
			<tr>
				<th>Total Gravado</th>
				<th class="text-right"><g:formatNumber number="${importeGravado}" type="currency"/></th>
			</tr>
		</tfoot>
	</table>
</div>





