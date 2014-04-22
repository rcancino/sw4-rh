%{-- nav.navbar.navbar-default.navbar.fixed-top[role="navigation"]>.container>.navbar-header>button.navbar-toggle[type="button" data-toggle="collapse" data-target="#mainMenu"]>span.icon-bar*3^a.navbar-brand[href="#"]{Luxor} --}%
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
				<li><a href="#">Reportes</a></li>
				<g:render template="/_menu/configuracion"/>
			</ul>
				%{-- <nav:primary class="nav navbar-nav"/> --}%
				%{-- <ul class="nav navbar">
					<li class="dropdown"><a href="#" data-toggle="dropdown">Catálogos<b class="caret"></b></a></li>
				</ul> --}%
			<ul class="nav navbar-nav navbar-right">
				<g:render template="/_menu/user"/>
			</ul>
		</div>
		</sec:ifLoggedIn>
	</div>
	
</nav>