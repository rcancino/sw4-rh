package com.luxsoft.sw4.rh



import grails.test.mixin.*
import spock.lang.*

@TestFor(AsistenciaImssController)
@Mock(AsistenciaImss)
class AsistenciaImssControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.asistenciaImssInstanceList
            model.asistenciaImssInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.asistenciaImssInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            def asistenciaImss = new AsistenciaImss()
            asistenciaImss.validate()
            controller.save(asistenciaImss)

        then:"The create view is rendered again with the correct model"
            model.asistenciaImssInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            asistenciaImss = new AsistenciaImss(params)

            controller.save(asistenciaImss)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/asistenciaImss/show/1'
            controller.flash.message != null
            AsistenciaImss.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def asistenciaImss = new AsistenciaImss(params)
            controller.show(asistenciaImss)

        then:"A model is populated containing the domain instance"
            model.asistenciaImssInstance == asistenciaImss
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def asistenciaImss = new AsistenciaImss(params)
            controller.edit(asistenciaImss)

        then:"A model is populated containing the domain instance"
            model.asistenciaImssInstance == asistenciaImss
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/asistenciaImss/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def asistenciaImss = new AsistenciaImss()
            asistenciaImss.validate()
            controller.update(asistenciaImss)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.asistenciaImssInstance == asistenciaImss

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            asistenciaImss = new AsistenciaImss(params).save(flush: true)
            controller.update(asistenciaImss)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/asistenciaImss/show/$asistenciaImss.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/asistenciaImss/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def asistenciaImss = new AsistenciaImss(params).save(flush: true)

        then:"It exists"
            AsistenciaImss.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(asistenciaImss)

        then:"The instance is deleted"
            AsistenciaImss.count() == 0
            response.redirectedUrl == '/asistenciaImss/index'
            flash.message != null
    }
}
