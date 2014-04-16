package com.luxsoft.sw4.rh

import org.springframework.security.access.annotation.Secured

//@Secured(['ROLE_USER'])
class HomeController {

	@Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def index() { 
    	if(!isLoggedIn()){
			redirect (controller:'login')
		}
    }
}
