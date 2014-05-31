<%@ page import="com.luxsoft.sw4.rh.Nomina" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="dashboard_1"/>
	<title>Nomina</title>
</head>
<body>
	
	<content tag="header">
		<h4>Importación de nómina </h4>
	</content>
	
	<content tag="buttonBar">
		
	</content>
	
	<content tag="grid">
	
		 <g:hasErrors bean="${importacionCmd}">
			<div class="alert alert-danger alert-dismissable">
				<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
				<ul>
					<g:eachError var="err" bean="${importacionCmd}">
						<li><g:message error="${err}"/></li>
					</g:eachError>
				</ul>
			</div>
		</g:hasErrors>
	
		<div class="row">
			<div class="col-md-offset-2 col-md-8">
			
		<g:uploadForm action="importar" class="form-horizontal">
			
			<f:with bean="${importacionCmd}">
			
				<legend> Archivo</legend>
				
				<f:field property="archivo" input-class="form-control"/>
				
				<f:field property="folio" input-class="form-control"/>
				<f:field property="tipo" input-class="form-control"/>
				<legend> Periodo</legend>
				<f:field property="fechaInicial" input-class="form-control"/>
				<f:field property="fechaFinal" input-class="form-control"/>
				<legend> Pago</legend>
				<f:field property="fechaDePago" input-class="form-control"/>
				<f:field property="diaDePago" input-class="form-control"/>
			</f:with>
			
			<%--
			
			<legend> Archivo</legend>
			<div class="form-group">
				<label class="col-sm-2 control-label">Archivo</label>
				<div class="col-sm-6 ">
      				
    			</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label">Folio</label>
				<div class="col-sm-6">
      				<input name="folio" type="number" class="form-control"/>
    			</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label">Tipo</label>
				<div class="col-sm-6">
      				<g:select name="periodicidad" from="${['QUINCENAL','SEMANAL'] }" class="form-control" noSelection="[null:'Seleccione el tipo de nómina']"/>
    			</div>
			</div>
			
			<legend> Periodo</legend>
			<div class="form-group">
				<label class="col-sm-2 control-label">Fecha inicial</label>
				<div class="col-sm-6 ">
      				<input name="fechaInicial" type="date" class="form-control"/>
    			</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label">Fecha final</label>
				<div class="col-sm-6 ">
      				<input name="fechaFinal" type="date" class="form-control"/>
    			</div>
			</div>
			
			<legend> Pago</legend>
			<div class="form-group">
				<label class="col-sm-2 control-label">Fecha</label>
				<div class="col-sm-6 ">
      				<input name="fechaPago" type="date" class="form-control"/>
    			</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Dia</label>
				<div class="col-sm-6 ">
      				<input name="diaDePago" type="date" class="form-control"/>
    			</div>
			</div>
			 --%>
			
    		<div class="form-group">
		    	<div class="col-sm-offset-6 col-sm-2">
		      		<button type="submit" class="btn btn-default" name="upload" value="Upload">
		      			<span class="glyphicon glyphicon-floppy-save"></span> Aceptar
		      		</button>
		    	</div>
		    </div>
		</g:uploadForm>
		</div>
		</div>
	</content>

	

</body>
</html>
