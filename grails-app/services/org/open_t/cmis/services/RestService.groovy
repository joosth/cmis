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

import org.open_t.cmis.*;
import net.sf.jmimemagic.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.auth.*
import org.apache.http.client.methods.*
import org.apache.http.entity.*
import org.apache.http.protocol.HttpContext
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder



class RestService {

    static transactional = false
	static scope="session"

	def maxBufSize=1000000
	
	def username
	def password
	
	
	
	def authenticated=false

	/* set authentication for client using the service-wide credentials */ 
	def authenticate(def client) {
		// If an external proxy user is defined, replace the username with that name
		if (ConfigurationHolder.config.cmis.proxyUser) {
			client.getCredentialsProvider().setCredentials(
				// TODO limit scope
				new AuthScope(null, -1),
				new UsernamePasswordCredentials(ConfigurationHolder.config.cmis.proxyUser, ""));
		} else {
			client.getCredentialsProvider().setCredentials(
			// TODO limit scope
				new AuthScope(null, -1),
				new UsernamePasswordCredentials(username, password));
		}
    	// If an remote user header is defined, add the header
		if (ConfigurationHolder.config.cmis.remoteUserHeader) {
			client.addRequestInterceptor(new HttpRequestInterceptor()
				{
					public void process(final HttpRequest request,
										final HttpContext context) throws HttpException, IOException
					{
						request.addHeader(ConfigurationHolder.config.cmis.remoteUserHeader, username);
					}
				});
		}			
		authenticated=true
	}
	
	/* Set credentials to be used */
	def credentials(theUsername,thePassword) {		
		username=theUsername
		password=thePassword		
	}
	
	/* Read from url, return slurped XML */
	
    def read(def method,def url) {
		def client=new DefaultHttpClient();
    	try {
			
			HttpGet httpget = new HttpGet(url);
			authenticate(client)
			log.debug "executing request" + httpget.getRequestLine()
			
			HttpResponse response = client.execute(httpget);
			HttpEntity entity = response.getEntity();

			log.debug "----------------------------------------";
			log.debug (response.getStatusLine());
			if (entity != null) {
				log.debug("Response content length: " + entity.getContentLength());
			}
        
			if (response.statusLine.statusCode==200) {    	
				def theContent= entity.content.text

				def slurper = new XmlSlurper()    	
				def parsedResponse=slurper.parseText(theContent)
				return parsedResponse
			} else {
				return null
			}
		    	
		} catch(Exception e) {
    		log.debug "Hm. That's not good: ${e}"
    		return null
    	} finally {
			client.getConnectionManager().shutdown();
    	}
    }
    
	/* Delete to url, return status code */
    def delete(def url) {		
		def client=new DefaultHttpClient();
		try {
			authenticate(client)    	
			HttpDelete httpdelete = new HttpDelete(url);
			HttpResponse response = client.execute(httpdelete);
		} finally {
			client.getConnectionManager().shutdown();
		}    	    	
		return response.statusLine.statusCode==200 ? null : response.statusLine.statusCode
    }
	/* write to URL, return slurped XML */
    def write (def method,def url,def xmlText,def mimeType="application/atom+xml") {
		def rsp
		def client=new DefaultHttpClient();
		try {
			authenticate(client)
			
			def entity= new StringEntity(xmlText,mimeType,null)
			
			def httpwrite
			if (method=="PUT") {
				httpwrite = new HttpPut(url);
			} else {
				httpwrite = new HttpPost(url);
			}
			httpwrite.setEntity(entity)
			
			HttpResponse response = client.execute(httpwrite);
			
			HttpEntity resEntity = response.getEntity();
			def theResponse=resEntity.content.text
	    	
	    	def slurper = new XmlSlurper()    	
	        rsp=slurper.parseText(theResponse)
    	} catch (Exception e) {
    		log.debug "HTTP ${method} error ${e}"
       	}  finally {
			client.getConnectionManager().shutdown();
		} 
            
        return rsp
	}
    	def writeBase64WrappedFile (def method,def url,def file,def header,def footer) {
		
		def client=new DefaultHttpClient();
		authenticate(client)
		
		def rsp
		try {			
			def httpwrite
			if (method=="PUT") {		
				httpwrite = new HttpPut(url);
			} else {		
				httpwrite = new HttpPost(url);
			}

			FileEntity reqEntity = new Base64WrappedFileEntity(file, "application/atom+xml",header,footer);
			
			
			httpwrite.setEntity(reqEntity)

			HttpResponse response = client.execute(httpwrite);
			HttpEntity resEntity = response.getEntity();
			def theResponse=resEntity.content.text
			
			def slurper = new XmlSlurper()
			rsp=slurper.parseText(theResponse)
		} catch (Exception e) {
			log.debug "HTTP ${method} error ${e}"			
  	    } finally {
		  client.getConnectionManager().shutdown();
		  }
			
		return rsp

	}
	    
    
    
	def get(def url) { return read("GET",url) }
	def post(def url,def xmlText) { return write("POST",url,xmlText) }
	def post(def url,def xmlText,def mimeType) { return write("POST",url,xmlText,mimeType) }
	def put(def url,def xmlText) { return write("PUT",url,xmlText) }
	def put(def url,def xmlText,mimeType) { return write("PUT",url,xmlText,mimeType) }
	
	def writeFile (def method,def url, File file) {
		
		log.debug "Write normal file to url ${url}"
		Magic parser = new Magic() ;
    	// getMagicMatch accepts Files or byte[],
    	// which is nice if you want to test streams
    	def mimetype="application/octet-stream"
    	try {
    		// The false means no extension hints.
    		MagicMatch match = parser.getMagicMatch(file,false);
    		mimetype=match.getMimeType();
		} catch (Exception e) {
    		// Quietly stay at the default if we can't find it ...
    	}
		
		log.debug "The mimetype is ${mimetype}"

		def client=new DefaultHttpClient();
		authenticate(client)
		
		def rsp=null
		try {
			
			def httpwrite
			if (method=="PUT") {
				httpwrite = new HttpPut(url);
			} else {
				httpwrite = new HttpPost(url);
			}
			FileEntity reqEntity = new FileEntity(file, mimetype);
			httpwrite.setEntity(reqEntity)
			HttpResponse response = client.execute(httpwrite);			
			rsp = response.statusLine.statusCode==200 ? null : response.statusLine.statusCode  
						
		} catch (Exception e) {
			log.debug "HTTP ${method} error ${e}"
  	    } finally {
		  client.getConnectionManager().shutdown();
		}
			
		return rsp		
	}
	
	
	/*
	 * Stream file from CMIS repository to client
	 */
	
	def streamFile(url,fileName,response) {
		
		
		
		def client=new DefaultHttpClient();
		
		HttpGet httpget = new HttpGet(url);
		authenticate(client)
		log.debug "executing request" + httpget.getRequestLine()
		HttpResponse rsp = client.execute(httpget);
		HttpEntity entity = rsp.getEntity();
		def inputStream=entity.getContent()
		
		response.setHeader("Content-disposition", "attachment; filename=\"" +fileName+"\"")
			
		
		def bufsize=100000
		byte[] bytes=new byte[(int)bufsize]

		def offset=0
		def len=1
		while (len>0) {

            len=inputStream.read(bytes, 0, bufsize)

            if (len>0)
            response.outputStream.write(bytes,0,len)            
            offset+=bufsize
		}
   		
		client.getConnectionManager().shutdown();
   		
		response.outputStream.flush()
	}
	
	def getFile(String url,File file) {
		
		
		
    	
		def client=new DefaultHttpClient();
		
		HttpGet httpget = new HttpGet(url);
		authenticate(client)
		log.debug "executing request" + httpget.getRequestLine()
		HttpResponse rsp = client.execute(httpget);
		HttpEntity entity = rsp.getEntity();
		def inputStream=entity.getContent()
			
		
		def bufsize=100000
		
		byte[] bytes=new byte[(int)bufsize]
		FileOutputStream out = new FileOutputStream(file)

		def offset=0
		def len=1
		while (len>0) {

            len=inputStream.read(bytes, 0, bufsize)
            
            
            
           // log.debug "len=${len}"
            if (len>0)
            	out.write(bytes,0,len)
            	
            offset+=bufsize
		}
		client.getConnectionManager().shutdown();

	}
	

	
}
