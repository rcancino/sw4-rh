<%@page expressionCodec="none"%>
<g:if test="${flash.message}">
	<div role="status">
		<strong>
			${flash.message}
		</strong>
	</div>
</g:if>
