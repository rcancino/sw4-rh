import groovy.sql.Sql
import com.luxsoft.sw4.rh.sat.*

  def importarBancos(){
	def sql=new Sql(ctx.dataSource)
	sql.eachRow("select * from sw4rh.sat_banco"){ row->
	  def cat=SatBanco.findOrSaveWhere(clave:row.clave
									 ,nombre:row.nombre
									 ,nombreCorto:row.nombre_corto)
	}
	println 'Bancos registrados: '+SatBanco.findAll().size()
  }


 def importarDeducciones(){
	def sql=new Sql(ctx.dataSource)
	sql.eachRow("select * from sw4rh.sat_deduccion"){ row->
	  def cat=SatDeduccion.findOrSaveWhere(clave:row.clave
									 ,descripcion:row.descripcion
									 )
	}
	println 'Deducciones registradas: '+SatDeduccion.findAll().size()
  }

 def importarIncapacidades(){
	def sql=new Sql(ctx.dataSource)
	sql.eachRow("select * from sw4rh.sat_incapacidad"){ row->
	  def cat=SatIncapacidad.findOrSaveWhere(clave:row.clave
									 ,descripcion:row.descripcion
									 )
	}
	println 'Incapacidades registradas: '+SatIncapacidad.findAll().size()
  }

 def importarPercepciones(){
	def sql=new Sql(ctx.dataSource)
	sql.eachRow("select * from sw4rh.sat_percepcion"){ row->
	  def cat=SatPercepcion.findOrSaveWhere(clave:row.clave
									 ,descripcion:row.descripcion
									 )
	}
	println 'Percepciones registradas: '+SatPercepcion.findAll().size()
  }

 def importarRegimenContratacion(){
	def sql=new Sql(ctx.dataSource)
	sql.eachRow("select * from sw4rh.sat_regimen_contratacion"){ row->
	  def cat=SatRegimenContratacion.findOrSaveWhere(clave:row.clave
									 ,descripcion:row.descripcion
									 )
	}
	println 'Regimenes registrados: '+SatRegimenContratacion.findAll().size()
  }

 def importarRiesgoPuesto(){
	def sql=new Sql(ctx.dataSource)
	sql.eachRow("select * from sw4rh.sat_riesgo_puesto"){ row->
	  def cat=SatRiesgoPuesto.findOrSaveWhere(clave:row.clave
									 ,descripcion:row.descripcion
									 )
	}
	println 'Regimenes registrados: '+SatRiesgoPuesto.findAll().size()
  }

//importarBancos()
importarDeducciones()
importarIncapacidades()
importarPercepciones()
importarRegimenContratacion()
importarRiesgoPuesto()