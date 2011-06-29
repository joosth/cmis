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
import org.open_t.util.*;

class CmisEntry {
	def xml
	def prop
	def link
	def cmisService
	
	CmisEntry(def theXml) {
		xml=theXml
		prop=new Property(this)
		link=new Link(this)
		cmisService=SpringUtil.getBean("cmisService")
	}
	
	def getTitle() {
		xml.title.text()
	}
	
	def setTitle(def title) {
		xml.title=title
	}
	
	def getSummary() {
		xml.summary.text()
	}
	
	def setSummary(def summary) {
		xml.summary=summary
	}
	
	
	def getUuid() {
		//prop.objectId.replace("workspace://SpacesStore/","")
		prop.objectId
	}
	
	def getProperties() {
		def props=[:]
		xml.object.properties.'*'.each { property ->
			String key=property.@queryName
			props.put(key, property.value.text())
		}
		return props
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
	
	def getIconUrl() {
		xml.icon.text()
	}
	
	def getThumbnailUrl() {
		if (link.alternate_thumbnail) {
			return "${cmisService.contextPath}/document/thumbnail?objectId=${prop.objectId}"
		} else {
			return "${cmisService.contextPath}/css/theme/images/generic-file-32.png"
		}
	}
	
	def getEnclosure() {
		//println "The encosure is ${links['enclosure']}"
		return links['enclosure']
	}
	
	def getType() {
		return properties['objectTypeId']
		
	}
	
	def propertyMissing(String name) { 
		if(properties["cmis:${name}"]) {
			return properties["cmis:${name}"]
		}
		else if (links[name]) {
			return links[name]
		} else {
			//throw new MissingMethodException(name, delegate, args)
			return null
		}
		                                       
	}
	
	def propertyMissing(String name,value) { 
		if(properties["cmis:${name}"]) {			
			xml.object.properties.'*'.find { property -> property.@queryName == "cmis:${name}" }.'value'=value		
		} else {
			throw new MissingMethodException(name, delegate, args)
			
		}
		                                       
	}
	
	def getCssClassName() {
		if (prop.baseTypeId=='cmis:folder') {
			return "folder"
		} else {
			prop.contentStreamMimeType.replace("/","-").replace(".","-")
		}
	}
	
	def isFolder() {
		return prop.baseTypeId=='cmis:folder'
	}
	
	def isDocument() {
		return prop.baseTypeId=='cmis:document'
	}
	
	def hasHistory() {
		String historyLink=link.'version-history'
		return historyLink.length()>0
	}
	
	def isCheckedOut() {
		String pwc=link.'working-copy'
		return pwc.length()>0
	}
	
	def isPwc() {
		String pwc=link.'via'
		return pwc.length()>0
	}
	
}
