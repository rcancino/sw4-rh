modules = {
    application {
        resource url:'js/application.js'
    }
    luxor {
    	dependsOn 'bootstrap'
    	resource url:'css/luxor.css'

    }
    datepicker{
        dependsOn 'jquery-ui'
        resource url:'js/jquery.ui.datepicker-es.js'
    }
}