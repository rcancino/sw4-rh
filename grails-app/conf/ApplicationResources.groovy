modules = {
    application {
        resource url:'js/application.js'
    }
    luxor {
    	dependsOn 'bootstrap,application'
    	resource url:'css/luxor.css'

    }
    forms {
        dependsOn 'jquery'
        resource url: 'js/autoNumeric.js'
        resource url: 'js/forms.js'
    }  
    datepicker{
        dependsOn 'jquery-ui'
        resource url:'js/jquery.ui.datepicker-es.js'  
        // resource url:'css/jquery-ui.css'
    }
	mask{
		dependsOn 'jquery'
		resource url: 'js/jquery.mask.min.js'
	}
    datatables{
        dependsOn 'jquery'
        resource url:'js/datatables/jquery.dataTables.min.js'
    }
	/*
    overrides {
        'bootstrap-css' {
            resource id: 'bootstrap-css', url:'/css/cerulean.css'
        }
    }
    */
}