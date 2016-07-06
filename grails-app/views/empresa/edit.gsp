<html>
<head>
	<meta charset="UTF-8">
	<title>${empresaInstance.clave}</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="alert alert-info">
					<h2> 
						${empresaInstance.nombre} 
					</h2>
					<g:if test="${flash.message}">
						<span class="label label-warning">${flash.message}</span>
					</g:if> 
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<g:hasErrors bean="${empresaInstance}">
				            <div class="alert alert-danger">
				                <g:renderErrors bean="${empresaInstance}" as="list" />
				            </div>
				        </g:hasErrors>
						
						<g:form class="form-horizontal" action="update" method="PUT" >
							<g:hiddenField name="id" value="${empresaInstance.id}"/>
							<g:hiddenField name="version" value="${empresaInstance.version}"/>
							
							<f:with bean="${empresaInstance }">
								<f:field property="nombre" widget-class="form-control uppercase-field " widget-autofocus="on"/>
								<f:field property="rfc" widget-class="form-control uppercase-field"/>
								<f:field property="clave" widget-class="form-control uppercase-field"/>
							%{-- 	<g:render template="/common/direccion" bean="${empresaInstance}"/>  --}%
								<legend>Timbrado</legend>
								%{-- <f:field property="usuarioPac" widget-class="form-control" cols="col-sm-4"/>
								<f:field property="passwordPac" widget-class="form-control" cols="col-sm-4"/> --}%
								<f:field property="passwordPfx" widget-class="form-control" cols="col-sm-4"/>
								%{-- <f:field property="timbradoDePrueba" widget-class="form-control" cols="col-sm-4"/> --}%
							</f:with>
							
							<div class="form-group">
						    	<div class="col-sm-offset-9 col-sm-2">
						      		<button type="submit" class="btn btn-default">
						      			<span class="glyphicon glyphicon-floppy-save"></span> Actualizar
						      		</button>
						    	</div>
						  	</div>
						  	
						</g:form>
			</div>
      <div class="col-md-12">
        <legend>Criptografía  </legend>

        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
          <div class="panel panel-default">
            <div class="panel-heading" role="tab" id="headingOne">
                <h4 class="panel-title">
                  <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                  Llave privada
                  </a>
                </h4>
            </div>
          <div id="collapseOne" class="panel-collapse collapse " aria-labelledby="headingOne">
              <div class="panel-body">
            <div class="form-group">
              <textarea name="llavePrivada" rows="8" cols="50" class="form-control" disabled>
                ${empresaInstance.getPrivateKey()}

              </textarea>
            </div>
                <g:uploadForm class="form-inline" action="registrarLlavePrivada" id="${empresaInstance.id}">
                  <div class="form-group">
                      <label for="inputFile">Actualizar archivo</label>
                      <input type="file" name="file" autocomplete="off" class="form-control ">
                   </div> 
                  <g:submitButton class="btn btn-primary" name="aceptar"value="Aceptar" />
                </g:uploadForm>
            </div>
          </div>
          
          </div>
          <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingTwo">
                    <h4 class="panel-title">
                      <a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                          Certificado digital
                      </a>
                    </h4>
                </div>
                <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
                    <div class="panel-body">
                      <div class="form-group">
                        <label for="inputFile">Número de certificado</label>
                        <input type="text" name="text" autocomplete="off" class="form-control " disabled 
                          value="${empresaInstance.numeroDeCertificado}">
                      </div>
                      <div class="form-group">
                        <textarea name="certificadoDigital" rows="8" cols="50" class="form-control" disabled>
                          ${empresaInstance.getCertificado()}
                        </textarea>
                      </div>
                          <g:uploadForm class="form-inline" action="registrarCertificado" id="${empresaInstance.id}">
                            <div class="form-group">
                                <label for="inputFile">Actualizar archivo</label>
                                <input type="file" name="file" autocomplete="off" class="form-control ">
                             </div> 
                            <g:submitButton class="btn btn-primary" name="aceptar"value="Aceptar" />
                          </g:uploadForm>
                    </div>
                </div>
          </div>
          </div>

        
      </div>

		</div>
	</div> 
	
</body>
</html>