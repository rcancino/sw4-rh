modules = {
    application {
        resource url:'js/application.js'
    }
    luxor {
    	dependsOn 'bootstrap'
    	resource url:'css/luxor.css'

    }
    autoNumeric{
        dependsOn 'jquery' 
        resource url: 'js/autoNumeric.js'
    }
    datepicker{
        dependsOn 'jquery-ui'
        resource url:'js/jquery.ui.datepicker-es.js'
    }
}