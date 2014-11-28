<%@page expressionCodec="none" %>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Modificación salarial</h3>
	</div>
	<div class="panel-body">
	
		<g:form action="save" role="form" class="form-horizontal" >
			<div class="well">

				<div class="form-group">
					<label class="col-sm-3 control-label">Empleado</label>
				    <div class="col-sm-9">
				      <p class="form-control-static">${modificacionInstance.empleado}</p>
				    </div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">Tipo</label>
				    <div class="col-sm-9">
				      <p class="form-control-static">${modificacionInstance.tipo}</p>
				    </div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">Fecha</label>
				    <div class="col-sm-9">
				      <p class="form-control-static">
				      	<g:formatDate date="${modificacionInstance.fecha}" format="dd/MM/yyyy"/>
				      </p>
				    </div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">Salario Anterio</label>
				    <div class="col-sm-9">
				      <p class="form-control-static">${modificacionInstance.salarioAnterior}</p>
				    </div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">Salario Nuevo</label>
				    <div class="col-sm-9">
				      <p class="form-control-static">${modificacionInstance.salarioNuevo}</p>
				    </div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">SDI Anterior</label>
				    <div class="col-sm-9">
				      <p class="form-control-static">${modificacionInstance.sdiAnterior}</p>
				    </div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">SDI Nuevo</label>
				    <div class="col-sm-9">
				      <p class="form-control-static">${modificacionInstance.sdiNuevo}</p>
				    </div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">Comentario</label>
				    <div class="col-sm-9">
				      <p class="form-control-static">${modificacionInstance.comentario}</p>
				    </div>
				</div>

				<g:if test="${modificacionInstance?.calculoSdi?.status=='APLICADO' }">
					<div class="col-sm-9">
				      <div class="aler alert-danger"> APLICADO </div>
				    </div>
				</g:if>
				
<%--				<g:if test="${!modificacionInstance.calculoSdi}">--%>
<%--					<div class="col-sm-9">--%>
<%--				      <div class="aler alert-danger"> APLICADO </div>--%>
<%--				    </div>--%>
<%--				</g:if>--%>
				
				
		
			</div>
			
			
		</g:form>
	</div>
	
</div>

