package org.open_t.cmis
import org.open_t.cmis.*;
class AuthenticateController {

	def cmisService
	def restService
	
    def index = { }
    
    def login = {
			println "We're in the login controller"
			render(view:'login')
	}
    
    def loginsubmit = {
		restService.authenticator=null
    	//println "Here we are logging in..."
    	restService.login(params.username,params.password)    	
    	//println "Username=${params.username},Password=${params.password}"
    	cmisService.repositories = new Repositories(restService)
		//println "Checkout collection: ${cmisService.repositories.collection.checkedout}"
		session["user"]=params.username
		flash.message="User ${session["user"]} logged in."
		
		redirect(controller:'browse',action:'browse')
    }
	
	
	def logout = {
			session["user"]=null
			restService.authenticator=null
			redirect(action:'login')
	}
    
    
    
}
