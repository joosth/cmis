package org.open_t.cmis
import org.open_t.cmis.*;
import org.codehaus.groovy.grails.commons.ConfigurationHolder

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
		flash.message=message(code:'cmisauthenticate.loginsubmit.message',default:"User {0} logged in.",args:[session["user"]])
		redirect(controller:'cmisBrowse',action:'browse')
    }
	
	
	def logout = {
			session["user"]=null
			restService.authenticated=false
			redirect(action:'login')
	}
    
}
