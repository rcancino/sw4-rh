package com.luxsoft.sw4



import grails.validation.Validateable;

import java.text.DateFormat
import java.text.SimpleDateFormat

import org.grails.databinding.BindingFormat;

@Validateable
class Periodo {
	
	@BindingFormat('dd/MM/yyyy')
	Date fechaInicial
	
	@BindingFormat('dd/MM/yyyy')
	Date fechaFinal
	
	static String defaultFormat='dd/MM/yyyy'
	
	static constraints = {
		fechaInicial()
		fechaFinal(nullable:false,validator:{val,object->
			if(val<object.fechaInicial)
				return 'fechaFinal.anteriorAFechaInicial'
			else
				return true
		})
	}
	
	Periodo(){
		fechaInicial=new Date()
		fechaFinal=new Date()
	}
	
	Periodo(String f1,String f2){
		fechaInicial=Date.parse(defaultFormat, f1).clearTime()
		fechaFinal=Date.parse(defaultFormat,f2).clearTime()
	}
	
	String toString(){
		"${fechaInicial.format(defaultFormat)} - ${fechaFinal.format(defaultFormat)}"
	}
	
	def int dias(){
		return fechaFinal-fechaInicial
	}
	
	public static Periodo getPeriodoEnUnMes(int mes){
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.MONTH,mes);
		cal.set(Calendar.DATE,1);
		Date start=cal.getTime();
		int last=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DATE,last);
		Date end=cal.getTime();
		Periodo p=new Periodo(fechaInicial:start,fechaFinal:end);
		return p;
	}
	public static Periodo getPeriodoEnUnMes(int mes,int ano){
		Calendar cal=Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.YEAR,ano);
		cal.set(Calendar.MONTH,mes);
		cal.set(Calendar.DATE,1);
		
		Date start=cal.getTime();
		int last=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DATE,last);
		
		Date end=cal.getTime();
		Periodo p=new Periodo(fechaInicial:start,fechaFinal:end);
		return p;
	}
	
	public static Periodo getPeriodoAnual(int year){
		Periodo p1=getPeriodoEnUnMes(0,year);
		Periodo p2=getPeriodoEnUnMes(11,year);
		Periodo p=new Periodo(fechaInicial:p1.fechaInicial,fechaFinal:p2.fechaFinal);
		return p;
	}
	
	
	

}
