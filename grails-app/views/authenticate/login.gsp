<html>
	<head>
		<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>
		<meta name='layout' content='main' />
		<title>Login</title>
<style type='text/css' media='screen'>


#login {
	width:300px;	
	margin: auto;	
}



#login .inner {
	//width:300px;
	
	text-align:left;
	padding:10px;
	border:1px solid #68A;
	background-color:#EEF;
	
}
#login .inner .fheader {
	padding:0px;margin:6px 0px 6px 0;color:#2e3741;font-size:14px;font-weight:bold;
}
#login .inner .cssform p{
	clear: left;
	margin: 0px auto;
	padding: 0px 0 0px 0;
	padding-left: 105px;	
	margin-bottom: 10px;	
}
#login .inner .cssform input[type='text'|type='password']{ 
	width: 120px;
}


#login .inner .cssform label{
	font-weight: bold;
	float: left;
	margin-left: -105px; 
	width: 100px;
}
#login .inner .login_message {color:red;}
#login .inner .text_ {width:120px;}
#login .inner .chk {height:12px;}

</style>
<script type='text/javascript'>

(function(){
//	document.loginform.username.focus();
})();

</script>


	</head>
	<body onload="document.loginform.username.focus();">
	
		<div id='login' class="dialog">
			<div class='inner'>
			<g:if test='${flash.message}'>
				<div class='login_message'>${flash.message}</div>
			</g:if>
			<div class='fheader'>Please Login:</div>
			<g:form name="loginform" id="loginform" action="loginsubmit" class="cssform">
			
				<p>
					<label for='username'>Username</label>
					<g:textField name="username" />
					
				</p>
				<p>
					<label for='password'>Password</label>
					<g:passwordField name="password" />
					
				</p>
				
				<p>
					<input class='button submit' type='submit' value='Login &raquo;' />
				</p>
			</g:form>
	
		</div>

</div>
	</body>
</html>
