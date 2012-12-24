package org.open_t.cmis
import org.open_t.cmis.*;
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.open_t.cmis.authentication.*

class CmisAuthenticateController {

	def cmisService
	def restService
	
    def index = { }
    
    def login = {
			render(view:'login')
	}
    
    def loginsubmit = {
		cmisService.init(ConfigurationHolder.config.cmis.url,params.username,params.password)
		session["user"]=params.username
		flash.message=message(code:'cmisAuthenticate.loginsubmit.message',default:"User {0} logged in.",args:[session["user"]])
		redirect(controller:'cmisBrowse',action:'documentlist')
    }
	
	
	def logout = {
			session["user"]=null
			restService.authenticated=false
			redirect(action:'login')
	}
    
}
