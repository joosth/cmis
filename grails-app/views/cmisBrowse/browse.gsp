<html>
    <head>
    	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />       
        <title>Browse</title>
        <link rel="stylesheet" href="${resource(dir:'js/uploader',file:'fileuploader.css')}" />
        <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'jquery.jstree.css')}" />
         
        <g:javascript src="uploader/fileuploader.js" />
    
        <g:javascript src="jquery/jquery.cookie.js" contextPath="${pluginContextPath}" />
        
		<g:javascript src="jquery/jquery.hotkeys.js" contextPath="${pluginContextPath}" />
		
		<g:javascript src="jquery/jquery.jstree.js" contextPath="${pluginContextPath}" />
		
		<cmis:head path="/" />
    </head>
    
    <body>
	    <div class="body">
	    	<div id="status" style="display:none;position:absolute;left:50px;top:50px;width:100px;border:1px solid #AAA;background-color:#EEE;text-align:center;padding:10px;" ></div>
	    	    <%-- File tree --%>
		        <table><tr>
		        <td>
		        <cmis:tree />
				</td><td>
				<%-- Show/edit pane --%>
				<cmis:pane />
				</td>
				</tr></table>
		</div>        
    </body>

</html>
