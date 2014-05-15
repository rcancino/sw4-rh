<%@page expressionCodec="none"%>
<r:require module="datepicker"/>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-hidden="true">&times;</button>
	<h4 class="modal-title" id="myModalLabel">Agregar Periodo</h4>
</div>
<g:form action="agregarPeriodo" id="${calendarioInstance.id}" class="form-horizontal" >
	<div class="modal-body">
		
		<div class="form-group">
    		<label for="inicio" class="col-sm-2 control-label">Folio</label>
    		<div class="col-sm-10">
      			<g:field type="text" class="form-control " id="folio" name="folio"/>
    		</div>
  		</div>
		
  		<div class="form-group">
    		<label for="inicio" class="col-sm-2 control-label">Inicio</label>
    		<div class="col-sm-10">
      			<g:field type="text" class="form-control dateField" id="inicio" name="inicio"/>
    		</div>
  		</div>
  		
  		<div class="form-group">
    		<label for="fin" class="col-sm-2 control-label">Fin</label>
    		<div class="col-sm-10">
      			<g:field type="text" class="form-control dateField" id="fin" name="fin"/>
    		</div>
  		</div>
  		
  		<div class="form-group">
    		<label for="pago" class="col-sm-2 control-label">Pago</label>
    		<div class="col-sm-10">
      			<g:field type="text" class="form-control dateField" id="pago" name="fechaDePago"/>
    		</div>
  		</div>
  		
  		<fieldset>
  		<legend>Periodo de asistencia:</legend>
  		<div class="form-group">
    		<label for="asistenciaInicial" class="col-sm-2 control-label">Inicial</label>
    		<div class="col-sm-10">
      			<g:field type="text" class="form-control dateField" id="asistenciaInicial" name="asistencia.fechaInicial"/>
    		</div>
  		</div>
  		
  		<div class="form-group">
    		<label for="asistenciaFinal" class="col-sm-2 control-label">Final</label>
    		<div class="col-sm-10">
      			<g:field type="text" class="form-control dateField" id="asistenciaFinal" name="asistencia.fechaFinal"/>
    		</div>
  		</div>
  		
  		</fieldset>
  		
  		
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
		<g:submitButton class="btn btn-primary" name="update" value="Salvar"/>
	</div>
</g:form>

<r:script>

</r:script>