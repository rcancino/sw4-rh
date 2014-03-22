<%@page expressionCodec="none" %>

<div class="form-group ${invalid ? 'has-error has-feedback' : value? 'has-success has-feedback':''}">
	<label class="col-sm-2 control-label" for="${property}">${label}</label>
	<div class="col-sm-8">
		 ${widget}
		<g:if test="${invalid}">
			<span class="help-block">${errors.join('<br>')}</span>
		</g:if>
		<span class="glyphicon ${invalid ? 'glyphicon-remove' :  value? 'glyphicon-ok':''} form-control-feedback"></span>
	</div>
</div>