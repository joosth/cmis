/*
 * Grails CMIS Plugin
 * Copyright 2010-2011, Open-T B.V., and individual contributors as indicated
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
package org.open_t.cmis;
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.groovy.grails.commons.ApplicationHolder;

import org.springframework.web.context.support.WebApplicationContextUtils;
import org.codehaus.groovy.grails.web.context.ServletContextHolder;
import org.springframework.context.ApplicationContext


/*
* Class representing a CMIS repository
*/
class Repositories {
	def restService
	
	def xml	
	def link
	def collection
	def baseUrl
	def ApplicationContext ctx
	
	def templates=[:]
	
	Repositories(def theCmisService) {
		restService=theCmisService.restService
		baseUrl=theCmisService.url
		xml=restService.get(baseUrl)
		link=new Link(this)
		collection=new Collection(this)
		// TODO add Collections, RepositoryInfo(s) and UriTemplates
		
		// TODO check for mediatype, according to the standard there could be more than 1 template per type
		xml?.workspace?.uritemplate.each { uritemplate ->			
			// Looks silly, but without the quotes the key isn't a string and can't be looked up as expected
			templates["${uritemplate.type}"]="${uritemplate.template.text()}"			
		}

	}
	
	def getTitle() {
		xml?.workspace?.title?.text()
	}
	
	
	def getLinks() {
		def links=[:]
		xml.link.each { link ->
			String key=link.@rel
			if (key=="down") {
				if (link.@type=="application/cmistree+xml") {
					key="down_cmistree"
				}
					
			}
			if (key=="alternate") {
				String renditionKind=link.@'cmisra:renditionKind'
				renditionKind=renditionKind.replace("cmis:","")				
					key="alternate_${renditionKind}"
			}
			
			links.put(key,link.@href.text())
		}
		return links
	}
	
	def getCollections() {
		def collections=[:]
		xml?.workspace?.collection?.each { collection ->
			String key=collection.collectionType.text()		
			collections.put(key,collection.@href.text())
		}
		return collections
	}
	
	
	def getRootFolderId() {
		return xml?.workspace?.repositoryInfo?.rootFolderId?.text()
	}
	
	
}
