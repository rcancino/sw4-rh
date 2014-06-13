if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
			consol.log('Cargando con ajax...');
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}
