 <div id="dialog" title="${message(code:'cmis.dialog.updatedocument.title')}">
	<g:form name="ajaxdialogform" controller="cmisDocument" action="updatedocumentsubmit" >			 							
		<g:hiddenField name="objectId" value="${entry.prop.objectId}"/>						
		<table style="border:none;">
		    <tbody>                        
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="document.name.title" default="Document name" />:</td>                                
		            <td valign="top" class="value">${entry.title}</td>
		            <td><span tabindex="9999" class="help action" title="${message(code:'document.name.help',default:'Document name|The name of the document')}" href="" >&nbsp;</span></td>		                               
		        </tr>
		        	                                                               
		    </tbody>
		</table>
		<div id="file-uploader">      
    <noscript>          
        <p>Please enable JavaScript to use file uploader.</p>
        <!-- or put a simple form for upload here -->
    </noscript>         
</div>
	</g:form>     
</div> 
               