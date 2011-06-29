<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />       
        <title>Documents</title>
    </head>
    <body>

   <table>
    
    
    <tr>
    	<td class="thumbnail"><g:link action="list" id="${entry.uuid}">  <img src="${entry.thumbnailUrl}" /></g:link></td>
    	<td class="details" >
    		<h1>${entry.title}</h1>
    		<div>Creation date: ${entry.'Creation Date'}<br />
    		Version: ${entry.'Version Label'}
    		</div>
   		</td>
    	<td class="actions">
    		
    	</td></tr>
    	</table>
    <br />
    <g:form method="post" >
    <table>
    <tr class="prop">
		<td valign="top" class="name">
        	<label for="title"><g:message code="document.title" default="Title" /></label>
        </td>
		<td valign="top" class="value ${hasErrors(bean: documentInstance, field: 'documentStatus', 'errors')}">
			<g:textField name="title" value="${entry.title}" />
            <a tabindex="9999" class="awesome small blue help button" title="${message(code:'document.documentStatus.help',default:'x')}" href="" >?</a>                                    
		</td>
	</tr>
    
    </table>
    
        <g:hiddenField name="objectId" value="${params.objectId}"/>
    
     <div class="buttons">
		<span class="button"><g:actionSubmit class="awesome small blue button" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>                    
	</div>
	</g:form>    
	    
    
    
    
   
    </body>
</html>