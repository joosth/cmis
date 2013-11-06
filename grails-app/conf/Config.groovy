// configuration for plugin testing - will not be included in the plugin zip
 
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    appenders {
        console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    }

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'		   

    warn   'org.mortbay.log'
	warn   'grails.app'
	debug  'org.open_t'
	
	
	root {
		info 'infoLog','warnLog','errorLog','custom', stdout
		error()
		additivity = true
	}
}

cmis {
	enabled=true
	
	url="http://localhost:8080/alfresco/service/cmis"
	
	username="admin"
	password="admin"
	
	// Base path for SPP on-line editing
	sppBasePath="http://localhost:7070/alfresco"

		// Base path for WebDAV online editing
	webdavBasePath="http://localhost:8080/alfresco/webdav"
	
	// For external authentication, use settings in comments below (remove url setting above)
	//url="http://localhost:8080/alfresco/wcservice/cmis"
	//authenticationClass="org.open_t.cmis.authentication.ExternalAuthenticationProvider"
	//authenticationParameters = ['remote-username-header':"X-Alfresco-Remote-User",'shared-secret-name':"X-Alfresco-Shared-Secret",'shared-secret-value':"CHANGETHIS"]
}
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"

dialog {
	bootstrap=true
}

