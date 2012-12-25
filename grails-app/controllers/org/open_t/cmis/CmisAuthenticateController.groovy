/*
 * CMIS Plugin for Grails
 * Copyright 2010-2013, Open-T B.V., and individual contributors as indicated
 * by the @author tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License
 * version 3 published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses
 */

/**
 * Authentication controller, uses the CMIS repository as the authentication service
 * 
 * @author Joost Horward
 */
 
package org.open_t.cmis
import org.open_t.cmis.services.*;
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.open_t.cmis.authentication.*

class CmisAuthenticateController {

	def cmisService
	def restService
	
    // Show login dialog    
    def login = {
			render(view:'login')
	}
    
	// Process login dialog, verify against CMIS repository
    def loginsubmit = {
		cmisService.init(ConfigurationHolder.config.cmis.url,params.username,params.password)
		session["user"]=params.username
		flash.message=message(code:'cmisAuthenticate.loginsubmit.message',default:"User {0} logged in.",args:[session["user"]])
		redirect(controller:'cmisBrowse',action:'documentlist')
    }
	
	// Log out by clearing the session
	def logout = {
			session["user"]=null
			redirect(action:'login')
	}    
}
