/**
* JavaScript para ayudar al procesamiento de formas
*
**/
if (typeof jQuery !== 'undefined') {
	
}
/**
* Inicializa  campos para procesamiento de cantidades numericas
*/
$(function(){
	$(".moneda-field").attr("type",'text');
	
	//$(".moneda-field").autoNumeric({vMin:'0.00',wEmpty:'zero',mRound:'B',aSign: '$',mDec:'2'});
	$(".moneda-field").autoNumeric({wEmpty:'zero',mRound:'B',aSign: '$'});
	//$(".numeric-field").attr("type",'text');
	//$(".numeric-field").autoNumeric({vMin:'0.00',wEmpty:'zero',mDec:'2'});
	//$(".numeric-field-4").attr("type",'text');
	//$(".numeric-field-4").autoNumeric({vMin:'0.00',wEmpty:'zero',mDec:'4'});
	
});

/**
* Function handler para quitar el signo de pesos a los campos numericos de la forma
*/
$(function(){
	$(".numeric-form").submit(function(event){

		$(".moneda-field",this).each(function(index,element){
			var val=$(element).val();
			var name=$(this).attr('name');
			var newVal=$(this).autoNumeric('get');
			$(this).val(newVal);
			//console.log('Enviando elemento numerico con valor:'+name+" : "+val+ " new val:"+newVal);
		});
		
	});
	
});
