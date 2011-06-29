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

package org.open_t.cmis.services

import java.io.FileOutputStream;

import groovy.xml.*
import java.net.*;
import org.open_t.cmis.*;
import org.open_t.base64.*;
import net.sf.jmimemagic.*;
import groovy.xml.StreamingMarkupBuilder

class CmisService {
    
    static transactional = true
	//static scope="session"
	

    def restService
    Repositories repositories
    def contextPath=""
    def url=""
    def initialized=false
    	
    def init(theUrl,username,password) {
    	url=theUrl
    	restService.login(username,password)
    	restService.authenticate()
    	 if (!repositories) {    		 
			  repositories = new Repositories(this)
    	 }
    	initialized=true
    }
    
    def objectUrlById(id){
    	/*if (!id.startsWith("workspace://SpacesStore/") && !id.startsWith("checkedout")) {
    		id="workspace://SpacesStore/"+id;
    	}
    	*/
    	if (repositories) {
	    	String template=repositories.templates.objectbyid

	    	template=template.replace("{id}", id)    	
	    	template=template.replace("{filter}", "")
	    	template=template.replace("{includeAllowableActions}", "false")
	    	template=template.replace("{includePolicyIds}", "false")
	    	template=template.replace("{includeRelationships}", "false")
	    	template=template.replace("{includeACL}", "false")
	    	template=template.replace("{renditionFilter}", "cmis:thumbnail")
    	return template
    	}
    	else {
    		return ""
    	}
    }
    
    def objectUrlByPath(path){
    	if (repositories) {
	    	String template=repositories.templates.objectbypath
	    	template=template.replace("{path}", path)    	
	    	template=template.replace("{filter}", "")
	    	template=template.replace("{includeAllowableActions}", "false")
	    	template=template.replace("{includePolicyIds}", "false")
	    	template=template.replace("{includeRelationships}", "false")
	    	template=template.replace("{includeACL}", "false")
	    	template=template.replace("{renditionFilter}", "cmis:thumbnail")
    	return template
    	}
    	else {
    		return ""
    	}
    }
    
    
    
    def listDescendants(def cmisEntry) {
    	
    	def response=restService.get("${cmisEntry.link.down}?renditionFilter=cmis:thumbnail&orderBy=cmis:name%20ASC")    	
		
		def descendantList=response.entry.collect { entry -> 
			new CmisEntry(entry)
		}
		return descendantList		
	}
    
    def listCheckedOut() {    	
    	def response=restService.get("${repositories.collection.checkedout}?renditionFilter=cmis:thumbnail&orderBy=cmis:name%20ASC")    	
		
		def checkedOutList=response.entry.collect { entry -> 
			new CmisEntry(entry)
		}
		return checkedOutList	
    }
    
    
    def listHistory(def cmisEntry) {
    	
		def response=restService.get("${cmisEntry.link.'version-history'}?renditionFilter=cmis:thumbnail&orderBy=cmis:creationDate%20DESC")    	
		
		def historyList=response.entry.collect { entry -> 
			new CmisEntry(entry)
		}
		return historyList
	}
    
    
    def getEntry(def objectId) {
    	if (objectId=="checkedout") {

			def checkedoutUrl=repositories.collection.checkedout
			def checkedOutXML="""<?xml version="1.0" encoding="UTF-8"?>
<entry xmlns="http://www.w3.org/2005/Atom" xmlns:app="http://www.w3.org/2007/app" xmlns:cmisra="http://docs.oasis-open.org/ns/cmis/restatom/200908/" xmlns:cmis="http://docs.oasis-open.org/ns/cmis/core/200908/" xmlns:alf="http://www.alfresco.org">
<author><name>System</name></author>
<content src=""/><id>checkedout</id>
<link rel="down" href="${checkedoutUrl}" type="application/atom+xml;type=feed"/>
<link rel="down" href="${checkedoutUrl}" type="application/cmistree+xml"/>


<published></published>
<summary>Checked out documents</summary>
<title>Checked out documents</title>
<cmisra:object>
<cmis:properties>
<cmis:propertyString propertyDefinitionId="cmis:name" displayName="Name" queryName="cmis:name"><cmis:value>Checked out documents</cmis:value></cmis:propertyString>
<cmis:propertyString propertyDefinitionId="cm:title" displayName="Title" queryName="cm:title"><cmis:value>Checked out documents</cmis:value></cmis:propertyString>
<cmis:propertyId propertyDefinitionId="cmis:baseTypeId" displayName="Base Type Id" queryName="cmis:baseTypeId"><cmis:value>cmis:folder</cmis:value></cmis:propertyId>
<cmis:propertyId propertyDefinitionId="cmis:objectId" displayName="Object Id" queryName="cmis:objectId"><cmis:value>checkedout</cmis:value></cmis:propertyId>

</cmis:properties>

</cmisra:object>

</entry>
			"""
    
			CmisEntry checkedOutEntry=new CmisEntry(new XmlSlurper().parseText(checkedOutXML))
			return checkedOutEntry
    	} else {
    		def url=objectUrlById(objectId)    	
        	def entry=restService.get(url)    	
    		entry.declareNamespace(atom:'http://www.w3.org/2005/Atom',cmis:'http://docs.oasis-open.org/ns/cmis/core/200908/',cmisra:'http://docs.oasis-open.org/ns/cmis/restatom/200908/')
    		return new CmisEntry(entry)
    	}
    	
		
		
	}
    
    def getEntryByPath(path) {
    	def url=objectUrlByPath(path)    	
    	def entry=restService.get(url)
    	if(entry) {
    		entry.declareNamespace(atom:'http://www.w3.org/2005/Atom',cmis:'http://docs.oasis-open.org/ns/cmis/core/200908/',cmisra:'http://docs.oasis-open.org/ns/cmis/restatom/200908/')
    		return new CmisEntry(entry)
    	} else {
    		return null
    	}
    	
    }
    
    def createFolder(parentId,name,summary) {
    	
    	def parentEntry=getEntry(parentId)
    	def downUrl=parentEntry.link.down
    	
    	
   		def request="""<?xml version="1.0" encoding="UTF-8"?> 
    	    <entry xmlns="http://www.w3.org/2005/Atom" xmlns:app="http://www.w3.org/2007/app" 
    	    	xmlns:cmisra="http://docs.oasis-open.org/ns/cmis/restatom/200908/"
    	    	xmlns:cmis="http://docs.oasis-open.org/ns/cmis/core/200908/" > 
    	    <!--<author><name>cmis</name></author>--> 
    	    <summary>${summary}</summary> 
    	    <title>${name}</title> 
    	    <cmisra:object>
    	    <cmis:properties>
   				<cmis:propertyId propertyDefinitionId="cmis:objectTypeId" displayName="Object Type Id" queryName="cmis:objectTypeId"><cmis:value>cmis:folder</cmis:value></cmis:propertyId>    			
   				<cmis:propertyString propertyDefinitionId="cmis:name" displayName="Name" queryName="cmis:name"><cmis:value>${name}</cmis:value></cmis:propertyString>
    	    </cmis:properties>
    	    </cmisra:object>
    	    </entry>
    	    </entry>
    	    """	
	
    		
    		
    	def response=restService.post(downUrl,request)
    	
    }
    
    def createDocument(String parentId,String filename,String name,String summary) {    	    	
    	Magic parser = new Magic() ;
    	// getMagicMatch accepts Files or byte[],
    	// which is nice if you want to test streams
    	def mimetype="application/octet-stream"
    	try {
    		// The false means no extension hints.
    		MagicMatch match = parser.getMagicMatch(new File(filename),false);
    		mimetype=match.getMimeType();
    		//println "Hey, we have a mimetype match: ${mimetype}"
		} catch (Exception e) {
    		// Quietly stay at the default if we can't find it ...
    	}

    	def requestHeader="""<?xml version="1.0" encoding="UTF-8"?>
    				<entry xmlns="http://www.w3.org/2005/Atom" xmlns:app="http://www.w3.org/2007/app" xmlns:cmis="http://docs.oasis-open.org/ns/cmis/core/200908/" xmlns:alf="http://www.alfresco.org" xmlns:opensearch="http://a9.com/-/spec/opensearch/1.1/" xmlns:cmisra="http://docs.oasis-open.org/ns/cmis/restatom/200908/">
    			<author><name></name></author>
    			<summary>${summary}</summary>
    			<title>${name}</title>
    			<content type="${mimetype}">"""

    	def requestFooter="""
    			</content>
    			<cmisra:object>
    			<cmis:properties>
    			<cmis:propertyId propertyDefinitionId="cmis:objectTypeId"><cmis:value>cmis:document</cmis:value></cmis:propertyId>
    			</cmis:properties>
    			</cmisra:object></entry>"""
		def parentEntry=getEntry(parentId)
		def downUrl=parentEntry.link.down

		org.open_t.base64.Base64.InputStream is = new Base64.InputStream(new FileInputStream(filename),Base64.ENCODE |Base64.DO_BREAK_LINES);


    	restService.writeStream('POST',downUrl,is,requestHeader,requestFooter)
    	
    }
    
    def update (cmisEntry) {
    	def entryXml=cmisEntry.xml
	
    	def xml = new StreamingMarkupBuilder().bind{ mkp.yield entryXml }
		def xmlText=xml.toString()
		restService.put(cmisEntry.link.edit,xmlText)
	}
    
    def checkout(objectId) {
    	def request="""<?xml version="1.0" encoding="utf-8"?>
		<entry xmlns="http://www.w3.org/2005/Atom"
		xmlns:cmisra="http://docs.oasis-open.org/ns/cmis/restatom/200908/"
		xmlns:cmis="http://docs.oasis-open.org/ns/cmis/core/200908/">
		<cmisra:object>
		<cmis:properties>
		<cmis:propertyId propertyDefinitionId="cmis:objectId"><cmis:value>${objectId}</cmis:value></cmis:propertyId>
		</cmis:properties>
		</cmisra:object>
		</entry>"""
    	return restService.post(repositories.collection.checkedout,request,"application/atom+xml;type=entry");

    }
    
    def workingCopy(objectId) {
		def entry=cmisService.getEntry(objectId)
		def pwcxml=restService.get (entry.link.'working-copy')
		return new CmisEntry(pwcxml)	
    }
    
    def checkin(objectId,comment,major=false) {
    	def entry=getEntry(objectId)
		def request="""<?xml version="1.0" encoding="utf-8"?>
					<entry xmlns="http://www.w3.org/2005/Atom"
					xmlns:cmisra="http://docs.oasis-open.org/ns/cmis/restatom/200908/"
					xmlns:cmis="http://docs.oasis-open.org/ns/cmis/core/200908/">					
					</entry>"""
		
		String checkinComment=URLEncoder.encode(comment)		
		def url="${entry.link.self}?checkin=true&major=${major}&checkinComment=${checkinComment}"
		return restService.put(url,request)		
    }
    
}
