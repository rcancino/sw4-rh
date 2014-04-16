<%@page expressionCodec="none" %>
<form action="${postUrl}" 
	method='POST' id='loginForm' 
	class="form-horizontal" 
	role="form" 	
	autocomplete='off'>
	
	<div class="form-group">
		<label for="username" class="col-sm-2 control-label">
			<g:message code="springSecurity.login.username.label"/>
		</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" id="usename" name="j_username" placeholder="usuario">
		</div>
	</div>

	<div class="form-group">
		<label for="password" class="col-sm-2 control-label">
			<g:message code="springSecurity.login.password.label"/>
		</label>
		<div class="col-sm-10">
			<input type="password" class="form-control" id="password" name="j_password" placeholder="password">
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-10">
			<div class="checkbox">
				<label >
					<input type='checkbox' name='${rememberMeParameter}' id='remember_me'
					 <g:if test='${hasCookie}'>checked='checked'</g:if> />
					 <g:message code="springSecurity.login.remember.me.label"/>
				</label>
			</div>
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-10">
			<input 
				class="btn btn-primary"
				type='submit' 
				id="submit" 
				value='${message(code: "springSecurity.login.button")}'/>
		</div>
	</div>

</form>