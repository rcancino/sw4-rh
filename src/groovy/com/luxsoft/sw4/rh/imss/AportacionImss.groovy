package com.luxsoft.sw4.rh.imss

class AportacionImss {
	
	String clave
	String nombre
	BigDecimal porcentajePatron
	BigDecimal porcentajeAsegurado
	BigDecimal limite
	
	static def valores=[
		 
		new AportacionImss(clave:'EM',nombre:'EyM sobre 1 SMGDF',porcentajePatron:20.4,porcentajeAsegurado:0.0,limite:1),
		new AportacionImss(clave:'EMD',nombre:'EyM sobre dif. entre SBC y 3 SMGDF',porcentajePatron:1.1,porcentajeAsegurado:0.4,limite:25),
		new AportacionImss(clave:'PD',nombre:'Prestaciones en dinero EyM sobre SBC',porcentajePatron:0.7,porcentajeAsegurado:0.25,limite:25),
		new AportacionImss(clave:'GMP',nombre:'Gastos mdicos pensionado sobre SBC',porcentajePatron:1.05,porcentajeAsegurado:0.375,limite:25),
		new AportacionImss(clave:'IV',nombre:'Invalidez y Vida sobre SBC',porcentajePatron:1.75,porcentajeAsegurado:0.625,limite:25),
		new AportacionImss(clave:'SR',nombre:'Seguro de Retiro',porcentajePatron:2,porcentajeAsegurado:0,limite:25),
		new AportacionImss(clave:'CV',nombre:'Cesanta edad avanzada y vejez sobre SBC',porcentajePatron:3.15,porcentajeAsegurado:1.125,limite:25),
		new AportacionImss(clave:'SG',nombre:'Seguro de Guarderas sobre SBC',porcentajePatron:1,porcentajeAsegurado:0,limite:25),
		new AportacionImss(clave:'RT',nombre:'Riesgos de trabajo',porcentajePatron:0.5,porcentajeAsegurado:0,limite:25),
		new AportacionImss(clave:'INF',nombre:'Infonavit',porcentajePatron:5,porcentajeAsegurado:0,limite:25),
		
		 
	]
	
	String toString(){
		"$clave $nombre $limite"
	}

}

class ZonaEconomica{
	String clave
	BigDecimal salario
	
	static def valores=[
		new ZonaEconomica(clave:'A',salario:67.29),
		new ZonaEconomica(clave:'B',salario:63.77),
		new ZonaEconomica(clave:'C',salario:64.76),
		new ZonaEconomica(clave:'D',salario:61.38),
	]
	
	String toString(){
		return "$clave: $salario"
	}
}
