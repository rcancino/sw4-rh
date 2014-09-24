<%@ page import="com.luxsoft.sw4.rh.CalendarioDet" %>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operacionesForm"/>
	<title>Comisiones</title>
</head>
<body>

	<content tag="header">
		<h3>Registro ${registroDeComisionesInstance.id}</h3>
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
		
		<g:form class="form-horizontal" action="update">
			
			<f:with bean="${registroDeComisionesInstance}">
				
				<div class="form-group">
   					<label class="col-sm-2 control-label">Empleado</label>
    				<div class="col-sm-10">
      					<p class="form-control-static">
      						${registroDeComisionesInstance.empleado} 
      					</p>
    				</div>
  				</div>
  				<div class="form-group">
					<label class="col-sm-2 control-label">Ejercicio</label>
    				<div class="col-sm-10">
      					<p class="form-control-static">
      						${registroDeComisionesInstance.calendarioDet.calendario.ejercicio} 
      					</p>
    				</div>
  				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">Calendario</label>
    				<div class="col-sm-10">
      					<p class="form-control-static">
      						${registroDeComisionesInstance.calendarioDet.calendario.tipo} 
      					</p>
    				</div>
  				</div>
  				<div class="form-group">
   					<label class="col-sm-2 control-label">Folio</label>
    				<div class="col-sm-10">
      					<p class="form-control-static">
      						 ${registroDeComisionesInstance.calendarioDet.folio}
      					</p>
    				</div>
  				</div>
				<div class="form-group">
   					<label class="col-sm-2 control-label">Importe</label>
    				<div class="col-sm-10">
      					<p class="form-control-static">
      						<g:formatNumber number="${registroDeComisionesInstance.importe}" type="currency"/> 
      					</p>
    				</div>
  				</div>
  				<div class="form-group">
   					<label class="col-sm-2 control-label">Nómina</label>
    				<div class="col-sm-10">
      					<p class="form-control-static">
      						<g:if test="${registroDeComisionesInstance.nominaPorEmpleadoDet}">
      							<g:link controll="nominaPorEmpleado" action="edit" 
      								id="${registroDeComisionesInstance.nominaPorEmpleadoDet.nomina.id}">
      								${registroDeComisionesInstance.nominaPorEmpleadoDet.nomina.id}
      							</g:link>
      						</g:if>
      						<g:else>	
      							PENDIENTE DE PAGO
      						</g:else>
      					</p>
    				</div>
  				</div>
				<div class="form-group">
   					<label class="col-sm-2 control-label">comentario</label>
    				<div class="col-sm-10">
      					<p class="form-control-static">
      						${registroDeComisionesInstance.comentario} 
      					</p>
    				</div>
  				</div>
			</f:with>
			
		</g:form>
		
		

	</content>
	
</body>
</html>