
<li class="dropdown">
	<sec:ifLoggedIn>
		<a class="dropdown-toggle" data-toggle="dropdown" href="#">
    		<i class="glyphicon glyphicon-user"></i>
    		<sec:loggedInUserInfo field="username"/><b class="caret"></b>
		</a>
		<ul class="dropdown-menu" role="menu">
			<li>
				<g:link controller="logout" ><img src='${fam.icon(name: 'door_out')}'/> Salir</g:link>
			</li>
		</ul>
	</sec:ifLoggedIn>
</li>



