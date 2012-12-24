/*
 * Grails CMIS Plugin
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
package org.open_t.cmis;
import org.apache.chemistry.opencmis.client.api.CmisObject;

/*
 * CMIS property class
 */
class Prop {
	CmisObject cmisObject
	
	def Prop(CmisObject cmisObject) {
		this.cmisObject=cmisObject
	}
	
	/*
	* Get a CMIS property by using it's name as a property
	*/
	def propertyMissing(String name) {
		if (!name.contains(":")) {
			name="cmis:${name}"
		}
		return cmisObject?.getProperty(name)?.getValue()		
	}
	
	/*
	* Set a CMIS property by using it's name in an assignment
	*/
	def propertyMissing(String name,value) { 
		if (!name.contains(":")) {
			name="cmis:${name}"
		}
		
		
		def property=cmisObject.getProperty(name)
		
		if (property) {			
			property.setValue(value)
			
		} else {
			throw new MissingMethodException(name, delegate, args)			
		}		                                       
	}	
}
