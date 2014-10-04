<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operacionesForm"/>
	<title>Aguinaldo ${aguinaldoInstance.id}</title>
</head>
<body>

	<content tag="header">
		<h3>Aguinaldo de ${aguinaldoInstance.empleado} ${aguinaldoInstance.ejercicio}
			<small>
				( <g:formatDate date="${aguinaldoInstance.fechaInicial}" format="dd/MM/yyyy"/> al
				 <g:formatDate date="${aguinaldoInstance.fechaFinal}" format="dd/MM/yyyy"/> )
			</small>
		</h3>
		
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-pills nav-stacked">
  			<li><g:link action="index">
  					<span class="glyphicon glyphicon-arrow-left"></span> Lista de aguinaldos
  			    </g:link>
  			</li>
  			<li><g:link action="create">
  					<span class="glyphicon glyphicon-plus"></span> Nuevo
  			    </g:link>
  			</li>
  			<li><g:link action="recalcular">
  					<span class="glyphicon glyphicon-cog"></span> Recalcular
  			    </g:link>
  			</li>
		</ul>
	</content>
	
	<content tag="formTitle">${aguinaldoInstance.empleado}</content>
	
	<content tag="form">
		
		<g:hasErrors bean="${aguinaldoInstance}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${aguinaldoInstance}" as="list" />
            </div>
        </g:hasErrors>
		
		<g:form class="form-horizontal" action="update">
			
			<f:with bean="${aguinaldoInstance }">
				<div class="col-md-6">
					<legend> Periodo (Días)</legend>
					<f:field property="diasDelEjercicio"  
						input-class="form-control " 
						label="Ejercicio" 
						input-type="text"
						input-readonly="readonly" />

					<f:field property="diasDeAguinaldo"  
						input-class="form-control " 
						label="Aguinaldo" 
						input-type="text"
						input-readonly="readonly" />

					<f:field property="diasDeBono"  
						input-class="form-control " 
						label="Bono" 
						input-type="text"
						input-readonly="readonly" />
					

					<f:field property="faltas"  
						input-class="form-control " 
						label="Faltas" 
						input-type="text"/>

					<f:field property="incapacidades"  
						input-class="form-control " 
						label="Incap" 
						input-type="text" />

					<f:field property="diasParaAguinaldo"  
						input-class="form-control " 
						label="Para Aguinaldo" 
						input-type="text"
						input-readonly="readonly" />
						
					<f:field property="diasParaBono"  
						input-class="form-control " 
						label="Para Bono" 
						input-type="text"
						input-readonly="readonly" />
					
				</div>
			</f:with>
			<div class="form-group">
		    	<div class="col-sm-offset-9 col-sm-2">
		      		<button type="submit" class="btn btn-default">
		      			<span class="glyphicon glyphicon-floppy-save"></span> Actualizar
		      		</button>
		    	</div>
		  	</div>
		</g:form>
		
		<r:script>
			
		</r:script>

	</content>
	
</body>
</html>