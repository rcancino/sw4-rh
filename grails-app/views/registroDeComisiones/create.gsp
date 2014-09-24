<%@ page import="com.luxsoft.sw4.rh.CalendarioDet" %>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operacionesForm"/>
	<title>Comisiones</title>
</head>
<body>

	<content tag="header">
		<h3>Alta de comisión</h3>
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-pills nav-stacked">
  			<li><g:link action="index">
  					<span class="glyphicon glyphicon-arrow-left"></span> Registro de Comisiones
  			    </g:link>
  			</li>
  			<li><g:link action="create">
  					<span class="glyphicon glyphicon-arrow-"></span> Nuevo
  			    </g:link>
  			</li>
		</ul>
	</content>
	
	<content tag="formTitle">Registro de comisión</content>
	
	<content tag="form">
		
		<g:hasErrors bean="${registroDeComisionesInstance}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${registroDeComisionesInstance}" as="list" />
            </div>
        </g:hasErrors>
		
		<g:form class="form-horizontal" action="save">
			
			<f:with bean="${registroDeComisionesInstance}">
				
				<f:field property="empleado" input-class="form-control"/>
				<div class="form-group">
    					<label for="calendarioField" class="col-sm-2 control-label">Calendario</label>
    					<div class="col-sm-10">
    						<g:select id="calendarioField" class="form-control"  
								name="calendarioDet" 
								from="${CalendarioDet
									.findAll('from CalendarioDet d where d not in (select n.calendarioDet from RegistroDeComisiones n) order by d.calendario.tipo,d.folio')}" 
								optionKey="id" 
								optionValue="${{it.calendario.tipo+' '+it.folio+' ( '+it.inicio.format('MMM-dd')+' al '+it.fin.format('MMM-dd')+ ' )'}}"
							/>
    					</div>
  				</div>
				<f:field property="importe" input-class="form-control" input-type='text'/>
				<f:field property="comentario" input-class="form-control"/>
				
			</f:with>
			<div class="form-group">
		    	<div class="col-sm-offset-9 col-sm-2">
		      		<button type="submit" class="btn btn-default">
		      			<span class="glyphicon glyphicon-floppy-save"></span> Guardar
		      		</button>
		    	</div>
		  	</div>
		</g:form>
		
		<r:script>
			
		</r:script>

	</content>
	
</body>
</html>