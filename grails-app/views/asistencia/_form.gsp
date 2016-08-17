<%@page expressionCodec="none" %>
<div class="row">
	<div class="col-md-3">
		<g:form class="form-horizontal">
			<div class="form-group">
   				<label class="col-sm-4 control-label">Puesto: </label>
    			<div class="col-sm-8">
      				<p class="form-control-static">
      					${asistenciaInstance.empleado.perfil.puesto.clave}
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
  			<div class="form-group">
   				<label class="col-sm-4 control-label">Turno</label>
    			<div class="col-sm-8">
      				<p class="form-control-static">
      				
      					<g:link controller="turno" action="show" id="${asistenciaInstance.empleado.perfil.turno.id}"> 
      						${asistenciaInstance.empleado.perfil.turno.descripcion}
      					</g:link> 
      				</p>
    			</div>
  			</div>
  			
  			
		</g:form>
	</div>
	
	<div class="col-md-6">
		<g:form class="form-horizontal">
			<div class="form-group">
   				<label class="col-sm-3 control-label">Ret Menor</label>
    			<div class="col-sm-3">
      				<p class="form-control-static ">
      					<strong>${asistenciaInstance.retardoMenor}</strong>
      					<%--<span class="label ${asistenciaInstance.retardoComida>0?'label-warning':''}">
      					</span>
      				--%></p>
    			</div>
    			<label class="col-sm-3 control-label">Retardo</label>
    			<div class="col-sm-3">
      				<p class="form-control-static">
      					<strong>${asistenciaInstance.retardoMayor}</strong>
<%--      					<span class="label ${asistenciaInstance.retardoComida>0?'label-danger':''}">${asistenciaInstance.retardoMayor}</span>--%>
      				</p>
    			</div>
  			</div>
  			<div class="form-group">
   				
  			</div>
  			<div class="form-group">
   				<label class="col-sm-3 control-label">Ret men com</label>
    			<div class="col-sm-3">
    				<p class="form-control-static">
    					<strong>${asistenciaInstance.retardoMenorComida}</strong>
      					<%--<span class="label label-warning"></span>
      				--%></p>
    			</div>
    			<label class="col-sm-3 control-label">Ret Comida</label>
    			<div class="col-sm-3">
    				<p class="form-control-static">
    					<strong>${asistenciaInstance.retardoComida}</strong>
<%--      					<span class="label ${asistenciaInstance.retardoComida>0?'label-danger':''}">${asistenciaInstance.retardoComida}</span>--%>
      				</p>
      				
    			</div>
  			</div>
  			<div class="form-group">
   				<label class="col-sm-3 control-label">Minutos no laborados</label>
    			<div class="col-sm-3">
      				<p class="form-control-static">
      					${asistenciaInstance.minutosNoLaborados}
      				</p>
    			</div>
    			<label class="col-sm-3 control-label">Horas laboradas</label>
    			<div class="col-sm-3">
      				<p class="form-control-static">
      					${asistenciaInstance.horasTrabajadas}
      				</p>
    			</div>
  			</div>
  			<div class="form-group">
   				
  			</div>
		</g:form>
	</div>
	
	<div class="col-md-3">
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
   				<label class="col-sm-4 control-label">Incapacidad</label>
    			<div class="col-sm-8">
      				<p class="form-control-static">
      					${asistenciaInstance.incapacidades} 
      				</p>
    			</div>
  			</div>
  			
		</g:form>
	</div>
</div>
