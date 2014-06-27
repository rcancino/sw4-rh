import org.bouncycastle.jce.provider.BouncyCastleProvider

import com.luxsoft.sec.Role
import com.luxsoft.sec.User
import com.luxsoft.sec.UserRole
import com.luxsoft.sw4.rh.tablas.ZonaEconomica;

class BootStrap {

    def init = { servletContext ->
		def adminRole=Role.findOrSaveWhere(authority:'ROLE_ADMIN')
		def userRole=Role.findOrSaveWhere(authority:'ROLE_USER')
		def rhUserRole=Role.findOrSaveWhere(authority:'RH_USER')
		def rhManagerRole=Role.findOrSaveWhere(authority:'RH_MANAGER')
		
		def admin=User.findByUsername('admin')
		if(!admin){
			admin=new User(username:'admin',password:'admin').save(flush:true)
			UserRole.create(admin,userRole,true)
			UserRole.create(admin,adminRole,true)
			UserRole.create(admin,rhUserRole,true)
			UserRole.create(admin,rhManagerRole,true)
		}
		def guest=User.findByUsername('guest')
		if(!guest){
			guest=new User(username:'guest',password:'password').save(flush:true)
			UserRole.create(guest,userRole,true)
		}
		def rh=User.findByUsername('rh')
		if(!rh){
			rh=new User(username:'rh',password:'test').save(flush:true)
			UserRole.create(rh,userRole,true)
			UserRole.create(rh,rhUserRole,true)
			UserRole.create(rh,rhManagerRole,true)
		}
		java.security.Security.addProvider(new BouncyCastleProvider())
		
		//Zonas economicas por defecto
		def zonaA=ZonaEconomica.findOrSaveWhere(clave:'A')
		def zonaB=ZonaEconomica.findOrSaveWhere(clave:'B')
		def zonaC=ZonaEconomica.findOrSaveWhere(clave:'C')
		def zonaD=ZonaEconomica.findOrSaveWhere(clave:'D')
		
    }
    def destroy = {
    }
}
