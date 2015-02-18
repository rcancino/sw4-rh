<html>
<head>
	<meta charset="UTF-8">
	%{-- <meta name="layout" content="operacionesForm"/> --}%
	<title>INFONAVIT ${infonavitInstance.id }</title>
	<r:require module="forms"/>
</head>
<body>
	<div class="container">
		
		<div class="row">
			<div class="col-md-12">
				<div class="page-header">
					<h3>Crédito INFONAVIT: ${infonavitInstance.empleado} (${infonavitInstance.empleado.clave } ID:${infonavitInstance.empleado.id })</h3>
					<g:if test="${flash.message}">
						<p>${flash.message}</p>
					</g:if>
				</div>
			</div>
		</div><!-- end .row 1 Headeer-->

		<div class="row">
			<div class="col-md-8">
				<g:form class="form-horizontal numeric-form" action="save">

					<div class="form-group">
					    <label class="col-sm-3 control-label">Número de crédito</label>
					    <div class="col-sm-3">
					      <p class="form-control-static">${infonavitInstance.numeroDeCredito}</p>
					    </div>
					    <label class="col-sm-3 control-label">Alta</label>
					     <div class="col-sm-3">
					       <p class="form-control-static">${g.formatDate(date:infonavitInstance.alta)}</p>
					     </div>
					 </div>
					 
					 <div class="form-group">
					    <label class="col-sm-3 control-label">Tipo</label>
					    <div class="col-sm-3">
					    	<p class="form-control-static">${infonavitInstance.tipo}</p>
					    </div>
					    <label class="col-sm-3 control-label">Descuento</label>
					     <div class="col-sm-3">
					     	<p class="form-control-static">${g.formatNumber(number:infonavitInstance.cuotaFija,format:"##.####")}</p>
					     </div>
					</div>
					
					<g:if test="${infonavitInstance.partidas}">
						<div class="form group">
							<label class="col-sm-3 control-label">SMG</label>
							<div class="col-sm-3">
								<p class="form-control-static">${infonavitInstance.salarioMinimoGeneral}</p>
							</div>
						</div>
						<div class="form group">
							<label class="col-sm-3 control-label">SDI</label>
							<div class="col-sm-3">
								<p class="form-control-static">${infonavitInstance.salarioDiarioIntegrado}</p>
							</div>
						</div>
					</g:if>
					
					<div class="form group">
						<label class="col-sm-3 control-label">Cuota diaria</label>
						<div class="col-sm-3">
							<p class="form-control-static">${infonavitInstance.cuotaDiaria}</p>
						</div>
						<label class="col-sm-3 control-label">Seguro</label>
						 <div class="col-sm-3">
						 	<g:if test="${infonavitInstance.partidas}">
						 		<p class="form-control-static">${infonavitInstance.seguroDeVivienda}</p>
						 	</g:if>
						 </div>
					</div>
					
					<div class="form group">
						<label class="col-sm-3 control-label">Bimestre actual</label>
						<div class="col-sm-3">
							<p class="form-control-static">${infonavitInstance.bimestreActual}</p>
						</div>
						<label class="col-sm-3 control-label">Abonos acumulados</label>
						 <div class="col-sm-3">
						 	<p class="form-control-static">${abonos}</p>
						 </div>
					</div>
				</g:form>
			</div>
		</div><!-- end .row Forma -->

		<div class="row">
			<div class="col-md-12">
				<div class="page-header">
					<h4>Bimestres</h4>
				</div>
				<g:render template="bimestresGrid"/>
				
				
			</div>
		</div>

		<div class="row">
			<ul class="nav nav-pills ">
				<li>
					<g:link action="index" class="btn btn-default"> <span class="glyphicon glyphicon-list-alt"></span> Catálogo</g:link>
				</li>
				<li>
					<g:link action="create" class="btn btn-default"> <span class="glyphicon glyphicon-floppy-saved"></span> Nuevo</g:link>
				</li>
				
				<li>
					<g:link action="edit" class="btn btn-default" id="${infonavitInstance.id}"> Editar</g:link>
				</li>
				<li>
					<g:link action="editarBitacora" class="list-group-item" id="${infonavitInstance.id}"> Bitacora</g:link>
				</li>
				
				<li>
					<g:link action="calcularCuotaBimestral" id="${infonavitInstance.id}" 
						class="btn btn-default"
						onclick="return confirm('Calcular cuota para  bimestre actual?')"> 
						<span class="glyphicon glyphicon-cog"></span> Re calcular
					</g:link>
				</li>

				<li>
					<g:link action="delete" id="${infonavitInstance.id}" 
						class="btn btn-danger"
						onclick="return confirm('Eliminar prestamo?')"> 
						<span class="glyphicon glyphicon-trash"></span> Eliminar
					</g:link>
				</li>
				
			</ul>
		</div> <!-- end .row 2 Toolbar-->

	</div>

	<content tag="header">
		<h3>Crédito INFONAVIT: ${infonavitInstance.empleado} (${infonavitInstance.empleado.clave })</h3>
	</content>
	
	<content tag="formTitle">
		Crédito INFONAVIT ${infonavitInstance.id}
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-pills nav-stacked">
			<li>
				<g:link action="index" class="list-group-item"> <span class="glyphicon glyphicon-list-alt"></span> Catálogo</g:link>
			</li>
			
			
			<li>
				<g:link action="edit" class="list-group-item" id="${infonavitInstance.id}"> Editar</g:link>
			</li>
			
			
			
			<li>
				<g:link action="delete" id="${infonavitInstance.id}" 
					class="list-group-item"
					onclick="return confirm('Eliminar prestamo?')"> 
					<span class="glyphicon glyphicon-trash"></span> Eliminar
				</g:link>
			</li>
			
			
		</ul>
		
	</content>
	
	<content tag="form">
		<g:form class="form-horizontal numeric-form" action="save">
		<fieldset disabled="disabled">
		<f:with bean="${infonavitInstance}">
			<f:field property="alta" input-class="form-control"/>
			<f:field property="numeroDeCredito" input-class="form-control"/>
			<f:field property="tipo" input-class="form-control"/>
			<f:field property="cuotaFija" input-type="text" input-class="form-control " label="Descuento"/>
			<f:field property="cuotaDiaria" input-type="text" input-class="form-control " />
			<f:field property="comentario" input-class="form-control"/>
		</f:with>
		</fieldset>
		<div class="form-group">
		    
		</div>
		
		</g:form>
	</content>
	
</body>
</html>