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
import java.util.Date;
import java.text.*;

import org.open_t.cmis.*;
import org.open_t.base64.*;
import net.sf.jmimemagic.*;
import groovy.xml.StreamingMarkupBuilder

class CmisService {
    
    static transactional = true
	static scope="session"
	def restService
    Repositories repositories=null
    def contextPath=""
    def url=""
    def initialized=false
    def enabled=false
	def pluginManager
    /*
     * Initialize this CmisService bean, remember url, username,password
     */
    def init(theUrl,username,password) {
		//println "Initilaizing with url: ${theUrl}, username: ${username}, password: ${password}"
    	url=theUrl
    	restService.credentials(username,password)    	
    	 if (!repositories) {    	
			  repositories = new Repositories(this)
    	 }
    	initialized=true
    	enabled=true
		
		contextPath=org.open_t.util.SpringUtil.applicationContext.id+pluginManager.getPluginPath("cmis")
		
    }
	
	/*
	 * Get the URL of a CMIS entry by ID
	 */
    
    def objectUrlById(id){    	
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
	
	/*
	 * Get the URL of a CMIS entry by path
	 */
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
    
    /*
     * List all descendants of the given Entry
     */
    
    def listDescendants(def cmisEntry) {
    	
    	def response=restService.get("${cmisEntry.link.down}?renditionFilter=cmis:thumbnail&orderBy=cmis:name%20ASC")    	
		
		def descendantList=response.entry.collect { entry -> 
			new CmisEntry(entry)
		}
		return descendantList		
	}
    
	/*
	 * List all checked out entries
	 */
    def listCheckedOut() {    	
    	def response=restService.get("${repositories.collection.checkedout}?renditionFilter=cmis:thumbnail&orderBy=cmis:name%20ASC")    	
		
		def checkedOutList=response.entry.collect { entry -> 
			new CmisEntry(entry)
		}
		return checkedOutList	
    }
    
	/*
	 * List the version history of this entry
	 */
    
    def listHistory(def cmisEntry) {
    	
		def response=restService.get("${cmisEntry.link.'version-history'}?renditionFilter=cmis:thumbnail&orderBy=cmis:creationDate%20DESC")    	
		
		def historyList=response.entry.collect { entry -> 
			new CmisEntry(entry)
		}
		return historyList
	}
	
	/*
	 * Get entry by object ID
	 * The special object ID "checkedout" returns the checked out documents collection
	 */
    
    
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
	
	/*
	 * Get CMIS Entry by path
	 */
    
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
	
	/*
	 * Create a folder
	 */
    
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
    		println "Hey, we have a mimetype match: ${mimetype}"
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
		
		
		restService.writeBase64WrappedFile ("POST",downUrl,new File(filename),requestHeader,requestFooter);
    	
    }
    /*
     * Update a CMIS entry
     */
    def update (cmisEntry) {
    	def entryXml=cmisEntry.xml
	
    	def xml = new StreamingMarkupBuilder().bind{ mkp.yield entryXml }
		def xmlText=xml.toString()
		restService.put(cmisEntry.link.edit,xmlText)
	}
	
	/*
	 * Check out a CMIS entry
	 */
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
    /*
     * Get the working copy of a CMIS entry (from the entry ID)
     */
    def workingCopy(objectId) {
		def entry=cmisService.getEntry(objectId)
		def pwcxml=restService.get (entry.link.'working-copy')
		return new CmisEntry(pwcxml)	
    }
    
	/*
	 * Check in a CMIS Entry
	 */
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
	
	def getFile(cmisEntry,file) {
		def url=cmisEntry.link.enclosure
		restService.getFile(url,file)
		def lastModified=cmisEntry.prop.lastModificationDate
		def lmDate=parseDate(lastModified)
		file.setLastModified(lmDate.getTime())
	}
	
	Date parseDate(String dateString) {
		dateString=dateString.trim()
		
		int lastColon=dateString.lastIndexOf(":")
		int len=dateString.length()
		if ((dateString[len-6]=="+" || dateString[len-6]=="-") && (lastColon == len-3)){
			dateString=dateString.substring(0,len-3)+dateString.substring(len-2)
			//println "converting ${dateString}"
			
		} else {
			//println " not converting"
		}
		
		
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ").parse(dateString)
	}
	
	String formatDate(Date date) {
		String DATE_FORMAT_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
		return DateFormatUtils.format (date,DATE_FORMAT_8601);
	}
	
	def createPath(cmisPath) {
		
		def pathElements=cmisPath.split("/")
	
		def parent=getEntryByPath("/")
		
		def path=""
		pathElements.each { pathElement ->
			if (pathElement.length()>0) {
	
				path+="/"+pathElement
				def entry=getEntryByPath(path)
				if(!entry) {
					createFolder(parent.objectId,pathElement,"Created by CMIS service")
					parent=getEntryByPath(path)
				} else {
					parent=entry
				}
			}
		}
		return getEntryByPath(cmisPath)
	}
	
	
}
