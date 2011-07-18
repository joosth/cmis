// configuration for plugin testing - will not be included in the plugin zip
 
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

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
}

cmis {
	enabled=true
	
	url="http://localhost:8080/alfresco/service/cmis"
//	url="http://cmis.alfresco.com:80/service/cmis"
	
	username="admin"
	password="admin"
	// Base path for SPP on-line editing
	sppBasePath="http://10.0.1.140:7070/alfresco"
	// Base path for WebDAV online editing
	webdavBasePath="http://10.0.1.140:8080/alfresco/webdav"
}
