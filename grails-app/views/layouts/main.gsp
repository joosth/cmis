<html>
    <head>
        <title><g:layoutTitle default="CMIS" /></title>
        
        <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css',contextPath:'')}" />
        <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'jquery.cluetip.css',contextPath:'')}" />
        <link rel="stylesheet" type="text/css" href="${resource(dir:'css/theme',file:'datatables.css',contextPath:'')}" />
        
        <link rel="stylesheet" type="text/css" href="${resource(dir:'css/theme',file:'mimetypes.css',contextPath:'')}" />
        <link rel="stylesheet" type="text/css" href="${resource(dir:'css/theme',file:'roller-theme.css',contextPath:'')}" />
        <link rel="stylesheet" type="text/css" href="${resource(dir:'css/theme',file:'theme.css',contextPath:'')}" />
       
        <link rel="shortcut icon" href="${createLinkTo(dir:'images',file:'favicon.ico',contextPath:'')}" type="image/x-icon" />

        
        <g:javascript src="jquery/jquery.js" />
                 
        <g:javascript src="jquery/jquery-ui-1.8.custom.min.js" contextPath="" />
        
        <g:javascript src="jquery/jquery.timers-1.2.js"  contextPath="" />
        
        <g:javascript src="jquery/jquery.cluetip-patched.js"  contextPath="" />
        
        <g:javascript src="jquery/jquery.dataTables.js"  contextPath="" />
     
        <g:layoutHead />
        
        
    </head>
    <body>
    <div id="main-toolbar" >
    	<span id="menu-toolbar" >
    		<g:link title="Home|Go to the home folder" class="action home help" controller="browse" action="browse">&nbsp;</g:link>
    	</span>
    	
    	<span id="user-toolbar" style="float:right;">    		
    		<g:if test="${session.user}" >${session.user} <g:link title="Logout|Log out ${session.user}" class="logout action help" controller="authenticate" action="logout">&nbsp;</g:link></g:if>
    		
    	</span>
    
    </div>
           
	  <div class="page-body" >
            <g:if test="${flash.message}">
            <div class="message">
            <g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}"/>
            </div>
            </g:if>
            <div id="status" style="z-index:10000;display:none;position:absolute;left:50px;top:50px;width:100px;border:1px solid #AAA;background-color:#EEE;text-align:center;padding:10px;" ></div>
            
        <g:layoutBody />
        </div>
    </body>
</html>
