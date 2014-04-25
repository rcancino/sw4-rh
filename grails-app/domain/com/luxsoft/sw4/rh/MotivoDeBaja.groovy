package com.luxsoft.sw4.rh

/**
 * Motivo de baja para efectos del seguro social
 * 
 * @author rcancino
 *
 */
class MotivoDeBaja {
	
	String clave
	String descripcion
	
	MotivoDeBaja() {}
	MotivoDeBaja(String cla,String desc){
		this.clave=cla
		this.descripcion=desc
		
	}

    static constraints = {
		descripcion minSize:1,nullable:true
    }
	
	static cargaInicial() {
		new MotivoDeBaja('1','Termino de contrato').save(flush:true)
		new MotivoDeBaja('2','Renuncia voluntaria').save(flush:true)
		new MotivoDeBaja('3','Abandono de empleo').save(flush:true)
		new MotivoDeBaja('4','Defunción').save(flush:true)
		new MotivoDeBaja('5','Clausura').save(flush:true)
		new MotivoDeBaja('6','Otros').save(flush:true)
		new MotivoDeBaja('7','Ausentismo').save(flush:true)
		new MotivoDeBaja('8','Rescision de contrato').save(flush:true)
		new MotivoDeBaja('9','Jubilación').save(flush:true)
		new MotivoDeBaja('A','Pensión').save(flush:true)
		
	}
}
