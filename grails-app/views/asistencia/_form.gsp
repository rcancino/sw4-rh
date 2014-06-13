<%@page expressionCodec="none" %>
<div class="row">
	<div class="col-md-4">
		<g:form class="form-horizontal">
			<div class="form-group">
   				<label class="col-sm-4 control-label">Departamento: </label>
    			<div class="col-sm-8">
      				<p class="form-control-static">
      					${asistenciaInstance.empleado.perfil.departamento.clave}
      				</p>
    			</div>
  			</div>
  			<div class="form-group">
   				<label class="col-sm-4 control-label">Tipo</label>
    			<div class="col-sm-8">
      				<p class="form-control-static">
      					${asistenciaInstance.tipo} 
      				</p>
    			</div>
  			</div>
  			<div class="form-group">
   				<label class="col-sm-4 control-label">Folio</label>
    			<div class="col-sm-8">
      				<p class="form-control-static">
      					${asistenciaInstance?.calendarioDet?.folio} 
      				</p>
    			</div>
  			</div>
  			<div class="form-group">
   				<label class="col-sm-4 control-label">Periodo</label>
    			<div class="col-sm-8">
      				<p class="form-control-static">
      					${asistenciaInstance.periodo} 
      				</p>
    			</div>
  			</div>
  			
  			
  			
  			
		</g:form>
	</div>
	
	<div class="col-md-4">
		<g:form class="form-horizontal">
			<div class="form-group">
   				<label class="col-sm-6 control-label">Retardo menor</label>
    			<div class="col-sm-6">
      				<p class="form-control-static">
      					${asistenciaInstance.retardoMenor}
      				</p>
    			</div>
  			</div>
  			<div class="form-group">
   				<label class="col-sm-6 control-label">Retardo mayor</label>
    			<div class="col-sm-6">
      				<p class="form-control-static">
      					${asistenciaInstance.retardoMayor}
      				</p>
    			</div>
  			</div>
  			<div class="form-group">
   				<label class="col-sm-6 control-label">Retardo comida</label>
    			<div class="col-sm-6">
      				<p class="form-control-static">
      					${asistenciaInstance.retardoComida}
      				</p>
    			</div>
  			</div>
  			
		</g:form>
	</div>
	
	<div class="col-md-4">
		<g:form class="form-horizontal">
		
			<div class="form-group">
   				<label class="col-sm-4 control-label">Faltas</label>
    			<div class="col-sm-8">
      				<p class="form-control-static">
      					${asistenciaInstance.faltas} 
      				</p>
    			</div>
  			</div>
			
			<div class="form-group">
   				<label class="col-sm-4 control-label">Asistencias</label>
    			<div class="col-sm-8">
      				<p class="form-control-static">
      					${asistenciaInstance.asistencias} 
      				</p>
    			</div>
  			</div>
  			
  			<div class="form-group">
   				<label class="col-sm-4 control-label">Vacaciones</label>
    			<div class="col-sm-8">
      				<p class="form-control-static">
      					${asistenciaInstance.vacaciones} 
      				</p>
    			</div>
  			</div>
  			
  			<div class="form-group">
   				<label class="col-sm-4 control-label">Incapacidades</label>
    			<div class="col-sm-8">
      				<p class="form-control-static">
      					${asistenciaInstance.incapacidades} 
      				</p>
    			</div>
  			</div>
  			
		</g:form>
	</div>
</div>
