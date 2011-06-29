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
import java.io.File;

import org.springframework.web.multipart.* 

import java.io.OutputStreamWriter;

import groovy.xml.*
import java.net.*;
import org.open_t.cmis.*;
import net.sf.jmimemagic.*;

 class MyAuthenticator extends Authenticator {
	
	def username
	def password
	 
    public PasswordAuthentication getPasswordAuthentication() {
        System.err.println("Feeding username ${username} and password ${password} for " + getRequestingScheme());
        return (new PasswordAuthentication(username, password.toCharArray()));
    }
 }


class RestService {
	//static scope="session"
    static transactional = true

	def maxBufSize=1000000
	def cmisUserName=""
	def cmisUserPassword=""		
	MyAuthenticator authenticator=null
	
	
	def authenticate() {
		
		if (!authenticator) {
			MyAuthenticator.setDefault(null);
			authenticator= new MyAuthenticator();			
		}
		authenticator.username=cmisUserName
		authenticator.password=cmisUserPassword
		MyAuthenticator.setDefault(authenticator);				
	}
	
	def login(username,password) {
		cmisUserName=username
		cmisUserPassword=password		
	}
	
	def getAuthenticated() {
		return authenticator!=null
	}
		

    def read(def method,def url) {
    	println "READ"
    	authenticate()
    	println "GETTING CONN to ${url}"
    	def conn
    	try {
    		conn = new URL(url).openConnection()
    	
    	println "GOT CONN"
    	conn.requestMethod = method
       	
   		conn.connect()
   		def theContent= conn.content.text
   		
    	conn.disconnect()
    	
    	println "content: ${theContent}"
    	
     	def slurper = new XmlSlurper()    	
        def response=slurper.parseText(theContent)
    		return response    	
    	} catch(Exception e) {
    		println "Hm. That's not good: ${e}"
    		return null
    	}
    }
    
    def delete(def url) {
    	
    	authenticate()
    	
		def conn = new URL(url).openConnection()
    	
    	conn.requestMethod = "DELETE"
       	
   		conn.connect()
   		
    	conn.disconnect()
    	
    	
    	
     	
    	return conn.responseCode    	
    	
    }
    
    
    
    
    def write (def method,def url,def xmlText,def mimeType="application/atom+xml") {
		//println "The mimetype is ${mimeType}"
		def response=""
		try {
			authenticate()
			URLConnection conn = new URL(url).openConnection()
	    	
	    	conn.requestMethod = method
	    	conn.setRequestProperty("Content-Type",mimeType)
	       	
	      	conn.doOutput = true
	
	      	Writer writer=new OutputStreamWriter(conn.outputStream)
	
			writer.write(xmlText)
	    	writer.flush()
	    	writer.close()	    	

	    	
	        //println "The response code is ${conn.responseCode}"
	    	
	    	def theResponse= conn.content.text
	    	//println "The response content is: ${theResponse}"
	    	
	    	
	    	def slurper = new XmlSlurper()    	
	        response=slurper.parseText(theResponse)
	        conn.disconnect()    	
    	} catch (Exception e) {
    		println "HTTP ${method} error ${e}"
       	}
            
        return response
	}
    
    def writeStream (def method,def url,def stream,def header,def footer,def mimeType="application/atom+xml") {
		//println "The mimetype is ${mimeType}"
		def response=""
		try {
			authenticate()
			def conn = new URL(url).openConnection()
	    	
	    	conn.requestMethod = method
	    	conn.setRequestProperty("Content-Type",mimeType)
	       	
	      	conn.doOutput = true
	
	      	Writer writer=new OutputStreamWriter(conn.outputStream)
	
			writer.write(header)
	    	writer.flush()
	    	
    		def bufsize=100000
			//println "The buffer size is ${bufsize}"		
			byte[] bytes=new byte[(int)bufsize]

            def offset=0
            def len=1
            while (len>0) {
            	//print "."
            	len=stream.read(bytes, 0, bufsize)
            	//println "len=${len}"
            	if (len>0)
            		conn.outputStream.write(bytes,0,len)            
            		offset+=bufsize
            }    
	    	
			writer.write(footer)	    	
	    	writer.close()	    	
	    		    	
	        //println "The response code is ${conn.responseCode}"
	    	
	    	def theResponse= conn.content.text
	    	//println "The response content is: ${theResponse}"
	    	
	    	
	    	def slurper = new XmlSlurper()    	
	        response=slurper.parseText(theResponse)
	        conn.disconnect()    	
    	} catch (Exception e) {
    		println "HTTP ${method} error ${e}"
    		return null
       	}            
        return new CmisEntry(response)
	}
    
    
    
    
	def get(def url) { return read("GET",url) }
	//def delete(def url) { return read("DELETE",url) }
	def post(def url,def xmlText) { return write("POST",url,xmlText) }
	def post(def url,def xmlText,def mimeType) { return write("POST",url,xmlText,mimeType) }
	def put(def url,def xmlText) { return write("PUT",url,xmlText) }
	def put(def url,def xmlText,mimeType) { return write("PUT",url,xmlText,mimeType) }
	
	
	def writeFile (def method,def url, MultipartFile file) {

		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		def mimeType = fileNameMap.getContentTypeFor(file.originalFilename)

		//println "The mimetype is ${mimeType}"
		
		
		
		
//		try {
		authenticate()
	
		def conn = new URL(url).openConnection()
    	conn.doOutput = true
      	
    	conn.requestMethod = method

    	conn.setRequestProperty("Content-Type",mimeType)
    	
    	InputStream is= file.getInputStream();
		int length=file.getSize()
		
		//println "The file length is ${length} AAAA"
		
		def bufsize=length<maxBufSize ? length : maxBufSize
		//println "The buffer size is ${bufsize}"		
		byte[] bytes=new byte[(int)bufsize]

		def offset=0
		while (offset<length) {
			//print "."
            def len=is.read(bytes, 0, bufsize)            
            conn.outputStream.write(bytes,0,len)            
            offset+=bufsize
		}    			
    	
    	conn.connect()
        //println "The response code is ${conn.responseCode}"
    	//println "The response content is:"
    //	theResponse= conn.content.text
    //	println theResponse

        conn.disconnect()
        /*
            
        	} catch (Exception e) {
        		println "HTTP PUT error ${e}"
        	}
          */  
        return ""
	}
	
	def writeNormalFile (def method,def url, File file) {

		Magic parser = new Magic() ;
    	// getMagicMatch accepts Files or byte[],
    	// which is nice if you want to test streams
    	def mimetype="application/octet-stream"
    	try {
    		// The false means no extension hints.
    		MagicMatch match = parser.getMagicMatch(file,false);
    		mimetype=match.getMimeType();
    		//println "Hey, we have a mimetype match: ${mimetype}"
		} catch (Exception e) {
    		// Quietly stay at the default if we can't find it ...
    	}
		
		//println "The mimetype is ${mimetype}"
		
		
		
		
//		try {
		authenticate()
	
		def conn = new URL(url).openConnection()
    	conn.doOutput = true
      	
    	conn.requestMethod = method

    	conn.setRequestProperty("Content-Type",mimetype)
    	
		
    	InputStream is= new FileInputStream(file)
		int length=file.size()
		
		//println "The file length is ${length} AAAA"
		
		def bufsize=length<maxBufSize ? length : maxBufSize
		//println "The buffer size is ${bufsize}"		
		byte[] bytes=new byte[(int)bufsize]

		def offset=0
		while (offset<length) {
			//print "."
            def len=is.read(bytes, 0, bufsize)            
            conn.outputStream.write(bytes,0,len)            
            offset+=bufsize
		}    			
    	
    	conn.connect()
        //println "The response code is ${conn.responseCode}"
    	//println "The response content is:"
    //	theResponse= conn.content.text
    //	println theResponse

        conn.disconnect()
        /*
            
        	} catch (Exception e) {
        		println "HTTP PUT error ${e}"
        	}
          */  
        return ""
	}
	
	
	
	def streamFile(url,fileName,response) {
		
		authenticate()
    	
		def conn = new URL(url).openConnection()
    	
    	conn.requestMethod = "GET"
       	conn.setDoInput (true)
   		conn.connect()
   		
		
		//println conn.getHeaderFields()
		
		response.setHeader("Content-disposition", "attachment; filename=\"" +fileName+"\"")
			
		
		def bufsize=100000
		//println "The buffer size is ${bufsize}"		
		byte[] bytes=new byte[(int)bufsize]

		def offset=0
		def len=1
		while (len>0) {
			//print "."
            len=conn.inputStream.read(bytes, 0, bufsize)
            //println "len=${len}"
            if (len>0)
            response.outputStream.write(bytes,0,len)            
            offset+=bufsize
		}
   		
   		conn.disconnect()
    	conn.contentType

		response.outputStream.flush()
	}
	
}
