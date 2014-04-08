%{-- nav.navbar.navbar-default.navbar.fixed-top[role="navigation"]>.container>.navbar-header>button.navbar-toggle[type="button" data-toggle="collapse" data-target="#mainMenu"]>span.icon-bar*3^a.navbar-brand[href="#"]{Luxor} --}%
<nav class="navbar navbar-default navbar-fixed-top navbar-inverse" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#mainMenu">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button><a href="#" class="navbar-brand">Luxor</a>
		</div>
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
		</div>
	</div>
	
</nav>