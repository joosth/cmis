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
/*
 * CMIS property class
 */
class Property {
	def cmisEntry
	
	def Property(CmisEntry cmisEntry) {
		this.cmisEntry=cmisEntry
	}
	
	/*
	* Get a CMIS property by using it's name as a property
	*/
	def propertyMissing(String name) { 
		if(cmisEntry.properties["cmis:${name}"]) {
			return cmisEntry.properties["cmis:${name}"]
		} else {			
			return ""
		}
	}
	
	/*
	* Set a CMIS property by using it's name in an assignment
	*/
	def propertyMissing(String name,value) { 
		if(cmisEntry.properties["cmis:${name}"]) {			
			cmisEntry.xml.object.properties.'*'.find { property -> property.@queryName == "cmis:${name}" }.'value'=value		
		} else {
			throw new MissingMethodException(name, delegate, args)
			
		}
		                                       
	}
	
	
	
}
