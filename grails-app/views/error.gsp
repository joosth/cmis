<%@ page import="grails.util.Environment" %>
<html>
  <head>
	  <title><g:message code="exception.title" /></title>
	  <meta name="layout" content="main" />
  </head>

  <body>  
	<div class="alert alert-error">
		<button type="button" class="close" data-dismiss="alert">&times;</button>		
		<h4><g:message code="cmis.exception.title" /></h4>
  		<p><g:message code="cmis.exception.message" /></p>
	</div>
	<g:if test="${Environment.current!=Environment.PRODUCTION}">
		<div class="row">
    		<div class="span12">
    			<h4>Error Details</h4>
 				<p>
					<strong>Error ${request.'javax.servlet.error.status_code'}:</strong> ${request.'javax.servlet.error.message'.encodeAsHTML()}<br/>
					<strong>Servlet:</strong> ${request.'javax.servlet.error.servlet_name'}<br/>
					<strong>URI:</strong> ${request.'javax.servlet.error.request_uri'}<br/>
					<g:if test="${exception}">
	  					<strong>Exception Message:</strong> ${exception.message?.encodeAsHTML()} <br />
	  					<strong>Caused by:</strong> ${exception.cause?.message?.encodeAsHTML()} <br />
	  					<strong>Class:</strong> ${exception.className} <br />
	  					<strong>At Line:</strong> [${exception.lineNumber}] <br />
	  					<strong>Code Snippet:</strong><br />
	  					<div>
	  						<g:each var="cs" in="${exception.codeSnippet}">
	  							${cs?.encodeAsHTML()}<br />
	  						</g:each>
	  					</div>
					</g:if>
				</p>
		
  		
				<g:if test="${exception}">
	    			<h4>Stack Trace</h4>
	      			<pre><g:each in="${exception.stackTraceLines}">${it.encodeAsHTML()}</g:each></pre>
				</g:if>
			</div>
		</div>
	</g:if>
  </body>
</html>