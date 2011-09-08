<html>
	<head>
		<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>
		<meta name='layout' content='main' />
		<title><g:message code="login" /></title>
<style type='text/css' media='screen'>

#login {
	display: block; z-index: 1001; outline: 0px none; position: absolute; height: auto; width: 300px; top: 100px; left: 679px;
}

#login  input[type='text'|type='password']{ 
	width: 120px;	
}

#login .login_message {color:red;}
#login .text_ {width:120px;}
#login .chk {height:12px;}
.layout-table {border:none;}
</style>
        <g:javascript>
        
		jQuery.fn.center = function () {
    		this.css("position","absolute");
    		this.css("top", (($(window).height() - this.outerHeight()) / 2) + $(window).scrollTop() + "px");
    		this.css("left", (($(window).width() - this.outerWidth()) / 2) + $(window).scrollLeft() + "px");
    		return this;
		}
		
		$(function() {					
       		 $("#login").center();
        	$( "#login" ).show( "slide", function() {
        	$("#username").focus();
        }
        );
		});
		                
        </g:javascript>


	</head>
	<body onload="document.loginform.username.focus();">
	
		<div id='login' class="ui-dialog ui-widget ui-widget-content ui-corner-all  ui-draggable ui-resizable">

			<g:if test='${flash.message}'>
				<div class='login_message'>${flash.message}</div>
			</g:if>
			<div class='faheader ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix'>
			<span id="ui-dialog-title-dialog" class="ui-dialog-title" ><g:message code="login.title" />:</span></div>
			<g:form name="loginform" id="loginform" action="loginsubmit" >
			<table class="layout-table">
				<tr>
					<td><g:message code="login.username" /></td>
					<td><g:textField name="username" /></td>
					
				</tr>
				<tr>
					<td><g:message code="login.password" /></td>
					<td><g:passwordField name="password" /></td>
					
				</tr>
				
				<tr><td>
					<input class='button submit' type='submit' value='${message(code:"login.submit")} &raquo;' />
					</td>
				</tr>
				</table>
			</g:form>
	
		</div>

	</body>
</html>
