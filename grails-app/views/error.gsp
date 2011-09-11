<%@ page import="grails.util.Environment" %>
<html>
  <head>
	  <title>Woops</title>
	  <meta name="layout" content="main" />
	  <style type="text/css">
	  		.message {
	  			border: 1px solid black;
	  			padding: 5px;
	  			background-color:#E9E9E9;
	  		}
	  		.stack {
	  			border: 1px solid black;
	  			padding: 5px;
	  			overflow:auto;
	  			height: 300px;
	  		}
	  		.snippet {
	  			padding: 5px;
	  			background-color:white;
	  			border:1px solid black;
	  			margin:3px;
	  			font-family:courier;
	  		}
	  </style>
  </head>

  <body>
    
	<div class="body">
		<div class="nav" > <span class="menuButton"></span></div>
		<h1>Woops</h1>
		
    	
    	<div class="dialog">
    	<p>Processing your request did not work out as expected. We apologize for the inconvenience.</p>
    	</div>
    	
    	<g:if test="${Environment.current!=Environment.PRODUCTION}">
    	<h1>Error Details</h1>
 		<div class="dialog">
  		
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
	  		<div class="snippet">
	  			<g:each var="cs" in="${exception.codeSnippet}">
	  				${cs?.encodeAsHTML()}<br />
	  			</g:each>
	  		</div>
		</g:if>
		</p>
		</div>
  		<div class="dialog">
	<g:if test="${exception}">
	    <h1>Stack Trace</h1>
	    <div class="dialog">
	      <pree><g:each in="${exception.stackTraceLines}">${it.encodeAsHTML()}<br/></g:each></pree>
	    </div>
	</g:if>
	</div>
	</g:if>
	
	</div>
	
  </body>
</html>