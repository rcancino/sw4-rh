import com.luxsoft.sw4.rh.*
import com.luxsoft.sw4.Empresa
import com.luxsoft.sw4.Direccion

class BootStrap {

    def init = { servletContext ->
		/*
		def empresa=Empresa.findWhere(clave:'PAPEL')
		if(!empresa){
			empresa=new Empresa(clave:'PAPEL',nombre:'PAPEL SA DE CV',rfc:'PAP830101CR3',regimen:'REGIMEN GENERAL DE LEY PERSONAS MORALE'
				,registroPatronal:'01070619109')
			empresa.direccion=new Direccion(
				calle:'BIOLOGO MAXIMINO MARTINEZ',
				numeroExterior:'3902',
				colonia:'SAN SALVADOR XOCHIMANCA',
				municipio:'AZCAPOTZALCO',
				codigoPostal:'02870',
				estado:'DISTRITO FEDERAL',
				pais:'MEXICO')
			empresa.save(failOnError:true)
		}
		//Alta de departamentos basicos
		def deptos=['COMPRAS','VENTAS','CONTABILIDAD','TESORERIA','DIRECCION','ALMACEN','EMBARQUES']
		deptos.each{
			Departamento.findOrSaveWhere(clave:it,descripcion:it)
		}
		def puestos=[
			[clave:'VEND01',descripcion:'Vendedor sucursal nivel 1'],
			[clave:'AUXCOM1',descripcion:'Auxiliar compras nivel 1'],
			[clave:'AUXCONTA1',descripcion:'Auxiliar contable nivel 1'],
			[clave:'GERVENT01',descripcion:'Gerente de ventas sucursal']
		]
		puestos.each{
			Puesto.findOrSaveWhere(clave:it.clave,descripcion:it.descripcion)
		}
		['OFICINAS','TACUBA','BOLIVAR','ANDRADE','CALLE4','5FEBRERO'].each{
			Ubicacion.findOrSaveWhere(clave:it,descripcion:it,empresa:empresa)
		}*/
		
    }
    def destroy = {
    }
}
