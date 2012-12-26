/*
 * Proxy bean which helps singletons to access the CmisService
 */
beans = {
	cmisServiceProxy(org.springframework.aop.scope.ScopedProxyFactoryBean) {
		targetBeanName = 'cmisService'
		proxyTargetClass = true
   }
}

