<%@page expressionCodec="none" %>

<div class="form-group ${invalid ? 'has-error has-feedback' : ' has-feedback'}">
	<label class="col-sm-2 control-label" for="${property}">${label}</label>
	<div class="col-sm-8">
		 ${widget}
		
	</div>
</div>