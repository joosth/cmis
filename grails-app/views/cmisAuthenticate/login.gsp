<html>
	<head>
		<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>
		<meta name="layout" content="main" />
		<title>Login</title>
        <r:script>
          $(function() {
          	$('#login').modal('show');

          	$("#login").on('shown', function () {
  				$('input:text:visible:first', this).focus();	
			});
          	 
          });
      	</r:script>
	</head>
	<body>	
		<g:form class="form-horizontal" name="loginform" id="loginform" action="loginsubmit" >				
			<div id="login" class="modal hide fade" >			
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h3><g:message code="cmisAuthenticate.login.title" /></h3>
				</div>			
				<div class="modal-body">									
					<div class="control-group">
						<label class="control-label" for="username"><g:message code="cmisAuthenticate.login.username.label" /></label>
						<div class="controls"><g:textField name="username" /></div>
					</div>
					<div class="control-group">
						<label class="control-label" for="password"><g:message code="cmisAuthenticate.login.password.label" /></label>
					  	<div class="controls" >
					  		<g:passwordField name="password"/>
					  	</div>
					</div>						
				</div>
				<div class="modal-footer">
					<input class="btn btn-primary" type="submit" value="${message(code:'cmisAuthenticate.login.submit.label')} &raquo;" />
				</div>			
			</div>
		</g:form>
	</body>
</html>


