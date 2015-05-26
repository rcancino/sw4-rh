<%@page expressionCodec="none"%>

<div class="modal fade" id="calendarioForm" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Recibo de PTU </h4>

			</div>	
			<div>
				<g:form action="recibosDePTU" class="form-horizontal">
				<fieldset>
				<legend> Parámetros</legend>
		
				<div class="form-group">
					 <label class="col-sm-2 control-label">Nómina</label>
					  	<div class="col-sm-10">
					    	<g:select class="form-control"  
					    		name="nomina"
					    		from="${com.luxsoft.sw4.rh.Nomina.findAllByTipoAndEjercicio("PTU","${ptuInstance.ejercicio+1}").sort{it.periodicidad}}" 
					    		optionKey="id" 
					    		optionValue="${{it.ejercicio+' '+it.periodicidad+' '+it.folio+' '+it.tipo+' '+it.formaDePago}}"
					    			
					    	/>	
					  	</div>
				</div>
				<div class="form-group">
		    	<div class="col-sm-offset-2 col-sm-3">
		      		<button type="submit" class="btn btn-default">
		      			<span class="glyphicon glyphicon-cog"></span> Ejecutar
		      		</button>
		    	</div>
		  	</div>

			</fieldset>
 				</g:form>
			</div>
			
		</div>	
	</div>	

</div>
