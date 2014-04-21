<html>
<head>
	<meta charset="UTF-8">
	<title>Empleado: ${empleadoInstance.perfil.numeroDeTrabajador}</title>
</head>
<body>

	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="alert alert-info">
					<h3>${empleadoInstance.nombre}</h3>
				</div>
			</div>
		</div> <!-- End .row 1-->
		
		<div class="row">
			<col-md-12></col-md-12>
		</div> <!-- end .row 2 -->
		
		<div class="row">
			<div class="col-md-6">
				<div class="media">
					<a href="#" class="pull-left"><img src="..." alt="" class="media-object"></a>
				</div>
				<div class="media-body">
					
				</div>
				<form class="form-horizontal" action="">
					<fieldset disabled="">
					<f:with bean="empleadoInstance">
						<f:field property="curp" input-class="form-control" />	
						<f:field property="rfc" input-class="form-control" />	
						<f:field property="clave" input-class="form-control" />	
						<f:field property="alta" input-class="form-control" />	
					</f:with>
					</fieldset>
					
				</form>
			</div>
			<div class="col-md-6">
				<div class="panel panel-default">
					<div class="panel-heading">Datos personales</div>
					<div class="panel-body">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Esse, eos, voluptatibus, incidunt cum veritatis aperiam aliquid fuga sunt magni facilis in corrupti unde! Vero, fugiat, perspiciatis doloremque laborum minima fugit!</div>
				</div>
			</div>
		</div><!-- End .row 3 -->

		<div class="row">
			<col-md-12>
				<div class="panel panel-default">
					<div class="panel-heading">Estadistica</div>
					<div class="panel-body">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium magni sint nobis unde iure illum ullam. Consectetur, quam, deserunt sed tempora laudantium inventore. Impedit, reiciendis labore modi itaque accusamus beatae!</div>
				</div>
			</col-md-12>
		</div>

	</div>
	%{-- <div class="container">
		<div class="row">
			<div class="col-md-2">
				<div class="list-group">
					<a href=" link_1" class="list-group-item active">Operaciones</a>
					<g:link action="index" class="list-group-item">
						<span class="glyphicon glyphicon-list"></span> Catálogo
					</g:link>
					<a href=" link_3" class="list-group-item">Buscar</a>
				</div>
			</div>
			<div class="col-md-10">
				<g:render template="empleadoCreateForm"/>
			</div>
		</div>
	</div> --}%
	
</body>
</html>