<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
    	<!-- This is main.gsp in CMIS -->
        <title><g:layoutTitle default="CMIS" /></title>
        <r:require modules="cmis,cmis-tree"/>
        <g:layoutHead />
		<r:layoutResources/>
		<dialog:head />
    </head>
    <body>
		<div class="navbar navbar-inverse navbar-fixed-top">
			<div class="navbar-inner">
        		<div class="container-fluid">
          			<a href="/cmis/cmisBrowse/documentlist" title="${message(code:'brand')}|${message(code:'brand')}" class="brand" >&nbsp;</a>
          			<div class="nav-collapse collapse">
	            		<ul class="nav">
		              		<li class="">
		                		<a href="/cmis/cmisBrowse/documentlist" class="brand" >CMIS</a>
		              		</li>
			   		        <g:if test="${session.user}">
		              			<dialog:dropdown code="view">
									<dialog:menuitem controller="cmisBrowse" action="documentlist" icon="icon-list"/>
		              				<dialog:menuitem controller="cmisBrowse" action="browse" icon="icon-folder-close"/>
								</dialog:dropdown>
							</g:if>
			        	</ul>
			    		<ul class="nav pull-right">
		       				<li><g:if test="${session.user}">
		       					<g:link controller="cmisAuthenticate" action="logout">${session.user}</g:link>
		       				</g:if>
		       				</li>
		       			</ul>
					</div>
				</div>
			</div>
		</div>
        <div class="container-fluid" id="page">
        	<div class="row-fluid">
        		<div class="span12" style="margin-top:45px;">
	        		<div id="statusmessage">
                        <g:if test="${flash.message}">
                            <div class="alert alert-success">
                                <button type="button" class="close" data-dismiss="alert">×</button>
                                ${flash.message}
                            </div>
                        </g:if>
	   				</div>
	        		<g:if test="${flash.errorMessage}">
	        	    	<div class="alert alert-error">
	    					<button type="button" class="close" data-dismiss="alert">×</button>
	    					${flash.errorMessage}
	    				</div>
	   				</g:if>
   				</div>
 			</div>
   			<g:layoutBody />
   			<r:layoutResources />
            <dialog:last />
		</div>
	</body>
</html>
