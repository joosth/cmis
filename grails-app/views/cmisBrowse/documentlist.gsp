<html>
    <head>
    	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />       
        <title><g:message code="cmisBrowse.documentlist.title" default="Document list" /></title>
		<cmis:head path="/Sites/digitaal-archief/documentLibrary" readOnly="false" cico="true" restore="true"/>
		<%--<cmis:head path="/" readOnly="false" cico="true" restore="true"/>--%>
    </head>    
    <body>
		<div class="row">
			<div class="span12" >    
				<cmis:list />
			</div>
		</div>        
    </body>
</html>
