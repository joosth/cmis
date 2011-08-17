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

/*
 * This class represents a CMIS Entry
 * It uses propertyMissing methods to enable easy reading/writing of any CMIS property
 */
class CmisEntry {
	def xml
	def prop
	def link
	def cmisService	
	
	/*
	 * Construct the entry using the entry XML
	 */
	CmisEntry(def theXml) {
		xml=theXml
		prop=new Property(this)
		link=new Link(this)
		cmisService=SpringUtil.getBean("cmisService")
	}
	
	/*
	 * Get the title
	 */
	def getTitle() {
		xml.title.text()
	}
	
	/*
	 * Set the title
	 */
	def setTitle(def title) {
		xml.title=title
	}
	
	/*
	 * Get the summary
	 */
	def getSummary() {
		xml.summary.text()
	}
	
	/*
	 * Set the summary
	 */
	def setSummary(def summary) {
		xml.summary=summary
	}
	
	/*
	 * Get the uuid (full object Id)
	 */
	
	def getUuid() {
		prop.objectId
	}
	
	/*
	 * Get a map of properties
	 * Key is property name, value is value
	 */
	
	def getProperties() {
		def props=[:]
		xml.object.properties.'*'.each { property ->
			String key=property.@queryName
			props.put(key, property.value.text())
		}
		return props
	}
	
	/*
	 * Get the entry links 
	 */
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
	
	/*
	 * Get the URL to this entry's icon
	 */	
	def getIconUrl() {
		xml.icon.text()
	}
	
	/*
	 * Get the URL to this entry's thumbnail rendition server up through the document controller.
	 * If the repository doesn't provide one whe revert to a generic icon
	 */
	def getThumbnailUrl() {		
		if (link.alternate_thumbnail) {
			return "${cmisService.contextPath}/cmisDocument/thumbnail?objectId=${prop.objectId}"
		} else {
			return "${cmisService.contextPath}/css/theme/images/generic-file-32.png"
		}
	}
	
	/*
	 * Get the enclosure
	 */
	def getEnclosure() {		
		return links['enclosure']
	}
	
	/*
	 * Get the object type
	 */
	def getType() {
		return properties['objectTypeId']
		
	}
	
	/*
	 * Get a property or link by it's short name by using it as a property
	 */
	
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
	
	/*
	 * Set a property by addressing it as an assignment 
	 */
	def propertyMissing(String name,value) { 
		if(properties["cmis:${name}"]) {			
			xml.object.properties.'*'.find { property -> property.@queryName == "cmis:${name}" }.'value'=value		
		} else {
			throw new MissingMethodException(name, delegate, args)
			
		}
		                                       
	}
	
	/*
	 * Get the CSS-safe type name of this entry (slashes (/) and dots (.)replaced by minus signs (-) 
	 */
	def getCssClassName() {
		if (prop.baseTypeId=='cmis:folder') {
			return "folder"
		} else {
			prop.contentStreamMimeType.replace("/","-").replace(".","-")
		}
	}
	
	/*
	 * Returns true if this entry is a folder or folder subtype
	 */
	def isFolder() {
		return prop.baseTypeId=='cmis:folder'
	}
	
	/*
	* Returns true if this entry is a document or document subtype
	*/
	def isDocument() {
		return prop.baseTypeId=='cmis:document'
	}
	
	/*
	 * Returns true if this entry has a version history
	 */
	def hasHistory() {
		String historyLink=link.'version-history'
		return historyLink.length()>0
	}
	
	/*
	 * Returns true is this entry is checked out (ie it has a working copy)
	 */
	def isCheckedOut() {
		String pwc=link.'working-copy'
		return pwc.length()>0
	}
	
	/*
	 * Returns true if this entry is a working copy
	 */
	def isPwc() {
		String pwc=link.'via'
		return pwc.length()>0
	}
	
}
