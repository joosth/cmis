<html>
    <head>
    	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />       
        <title><g:message code="cmisBrowse.browse.title" default="Browse" /></title>
		<cmis:head path="/" cico="true" restore="true"/>
    </head>    
    <body>
    	<div class="row">
    		<div class="span4">        			    		
				<%-- File tree --%>
				<cmis:tree />
			</div>
			<div class="span8">
				<%-- Show/edit pane --%>
				<cmis:pane />
			</div>
		</div>
    </body>
</html>
