 <div id="dialog" title="New document">
	<g:form name="ajaxdialogform" controller="document" action="newdocumentsubmit" >			 							
		<g:hiddenField name="parentId" value="${parentId}"/>						
		<table style="border:none;">
		    <tbody> 
		    <%--                       
		        <tr class="prop">
		            <td valign="top" class="name"><g:message code="folder.name" default="Document name" />:</td>                                
		            <td valign="top" class="value"><g:textField name="name" /></td>
		            <td><a tabindex="9999" class="help button" title="${message(code:'document.name.help',default:'Document name|Enter the name of the document here')}" href="" >?</a></td>		                               
		        </tr>
		        <tr class="prop null">
		            <td valign="top" class="name"><g:message code="document.summary" default="Document summary" /></td>
		            <td valign="top" ><g:textArea name="summary" cols="40" rows="5" /></td>
		            <td><a tabindex="9999" class="help button" title="${message(code:'document.summary.help',default:'Summary|Enter a document summary here')}" href="" >?</a></td>
	            </tr>
	            
	            <tr class="prop">
		            <td valign="top" class="name"><g:message code="file.name" default="File name" />:</td>                                
		            <td valign="top" class="value"><input type="file" name="uploadFile" /></td>
		            <td><a tabindex="9999" class="help button" title="${message(code:'file.name.help',default:'File name|Enter the name of the file here')}" href="" >?</a></td>		                               
		        </tr>
		       <input type="submit" value="Submit" />
		       --%>
		        
	         
	            
	            
	            
	                                                               
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
               