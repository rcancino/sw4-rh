<html>
<head>
	<meta charset="UTF-8">
	<r:require module="forms"/>
</head>
<body>

<content tag="contentTitle">
	Perfil de ${empleadoInstance.nombre}
</content>
<content tag="actions">
	
	<g:if test="${!edit}">
		<g:link class="btn btn-default" action="perfil" id="${empleadoInstance.id}" params="[edit:'true']">Modificar</g:link>
	</g:if>
	
</content>

<content tag="content">
	
	<g:hasErrors bean="${empleadoInstance?.perfil}">
		<div class="alert alert-danger alert-dismissable">
			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
			<ul>
				<g:eachError var="err" bean="${empleadoInstance.perfil}">
					<li><g:message error="${err}"/></li>
				</g:eachError>
			</ul>
		</div>
	</g:hasErrors>
	
	<form class="form-horizontal" method="post">
		<g:hiddenField name="id" value="${empleadoInstance.id}"/>
		<g:hiddenField name="version" value="${empleadoInstance?.version}" />
		<g:hiddenField name="view" value="perfil" />
		<div class="col-md-6">
		
			<fieldset ${!edit?'disabled=""':''}>
				<f:with bean="empleadoInstance">
					<f:field property="perfil.tipo" input-class="form-control" />	
					<f:field property="perfil.numeroDeTrabajador" input-class="form-control" />	
					<f:field property="perfil.puesto" input-class="form-control" />	
					<f:field property="perfil.departamento" input-class="form-control" />	
					<f:field property="perfil.ubicacion" input-class="form-control" />	
					<f:field property="perfil.tipoDeContrato" input-class="form-control" />	
					<f:field property="perfil.jornada" input-class="form-control" />	
					
					<f:field property="perfil.regimenContratacion" input-class="form-control" 
						value="${value?:com.luxsoft.sw4.rh.sat.SatRegimenContratacion.findByClave(2) }"/>
					<f:field property="perfil.riesgoPuesto" input-class="form-control"
						value="${value?:com.luxsoft.sw4.rh.sat.SatRiesgoPuesto.findByClave(3) }" />
					<f:field property="perfil.turno" input-class="form-control" />
				</f:with>
			</fieldset>
		
		</div>
		<div class="col-md-6">
		
			<fieldset ${!edit?'disabled=""':''}>
				<f:with bean="empleadoInstance">
					
					
				</f:with>
			</fieldset>
		
	</div>
	
	<g:if test="${edit}">
	<div class="form-group">
    	<div class="col-sm-offset-8 col-sm-12">
      		<g:actionSubmit class="btn btn-primary"  value="Actualizar" action="update"/>
      		<g:link class="btn btn-default" action="perfil" id="${empleadoInstance.id}" >Cancelar</g:link>
    	</div>
	</div>
	</g:if>

	

	</form>
</content>
		
	
</body>
</html>