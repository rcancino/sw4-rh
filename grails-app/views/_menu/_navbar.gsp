<nav class="navbar navbar-default navbar-fixed-top navbar-inverse" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#mainMenu">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<g:link controller="home" action="index" class="navbar-brand"><span class="glyphicon glyphicon-home"></span> Luxor</g:link>
		</div>
		<sec:ifLoggedIn>
		<div class="collapse navbar-collapse" id="mainMenu">
			<ul class="nav navbar-nav">
				<g:render template="/_menu/catalogos"/>
				<g:render template="/_menu/operaciones"/>
				<g:render template="/_menu/procesos"/>
				<li><g:link controller="reporte" action="index" >Reportes</g:link></li>
				<g:render template="/_menu/configuracion"/>
			</ul>
				
			<ul class="nav navbar-nav navbar-right">
				<g:render template="/_menu/user"/>
			</ul>
		</div>
		</sec:ifLoggedIn>
		<g:if test="${grailsApplication.config.grails.plugin.springsecurity.active == false}">
		<div class="collapse navbar-collapse" id="mainMenu">
			<ul class="nav navbar-nav">
				<g:render template="/_menu/catalogos"/>
				<g:render template="/_menu/operaciones"/>
				<g:render template="/_menu/procesos"/>
				<li><g:link controller="reporte" action="index" >Reportes</g:link></li>
				<li><g:link controller="contratacion" action="index" >Contratación</g:link></li>
				<g:render template="/_menu/configuracion"/>
			</ul>
				
			<ul class="nav navbar-nav navbar-right">
				<g:render template="/_menu/user"/>
				<li><a>Semana: ${session?.calendarioSemana?.folio}</a></li>
				<li><a>Quincena: ${session?.calendarioQuincena?.folio}</a></li>
				<li><a>${session.ejercicio}</a></li>
			</ul>
		</div>
		</g:if>
	</div>
	
</nav>