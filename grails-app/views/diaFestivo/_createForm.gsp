<%@page expressionCodec="none" %>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Día festivo</h3>
	</div>
	<div class="panel-body">	
	
		<g:form action="save" role="form" class="form-horizontal" >
			<div class="row">
				
				<div class="col-md-8">
					<fieldset>
						<f:with bean="diaFestivoInstance">
							<f:field property="ejercicio" input-class="form-control" />
							<f:field property="fecha" input-class="form-control" />
							<f:field property="descripcion" input-class="form-control" />
							<f:field property="parcial" input-class="form-control" />
							<f:field property="salida" input-class="form-control" >
								<g:datePicker name="salida" precision="minute" class="form-control"/>
							</f:field>
						</f:with>
					</fieldset>
					<div class="form-group">
		    			<div class="col-sm-offset-9 col-sm-2">
		      				<button type="submit" class="btn btn-default">
		      					<span class="glyphicon glyphicon-floppy-save"></span> Salvar
		      				</button>
		    			</div>
		  			</div>
		  		</div><!-- End .col-md-8 -->
		  		
		  		
		  	</div><!-- End .row -->
		</g:form>
	</div>
	<div class="panel-footer"></div>
</div>
<r:script>
    /** JavaScrip para date picker **/
   $("#datetime").datepicker({
        changeMonth: true,
        changeYear: true,
        appendText: "",
        showAnim: "fold",
        showButtonPanel: true,
        dateFormat:"dd/mm/yy" 
    });
    
    
</r:script>