modules = {
    application {
        resource url:'js/application.js'
    }
    luxor {
    	dependsOn 'bootstrap'
    	resource url:'css/luxor.css'

    }
}