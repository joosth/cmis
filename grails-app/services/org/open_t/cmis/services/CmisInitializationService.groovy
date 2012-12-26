package org.open_t.cmis.services
import org.apache.chemistry.opencmis.client.api.CmisObject
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId
import org.apache.chemistry.opencmis.client.api.Rendition
import org.open_t.cmis.*


class CmisInitializationService implements org.springframework.beans.factory.InitializingBean {
	
	

	void afterPropertiesSet() {
		CmisObject.metaClass.getCssClassName = { ->
			if (delegate.baseTypeId==BaseTypeId.CMIS_FOLDER) {
				return "folder"
			} else {
				delegate.getProperty("cmis:contentStreamMimeType")?.getValue().replace("/","-")?.replace(".","-")
			}
		}
		
		CmisObject.metaClass.getProp = { ->
			return new Prop(delegate)			
		}
		
		CmisObject.metaClass.getIsDocument = { ->
			return delegate.baseTypeId== BaseTypeId.CMIS_DOCUMENT		
		}
		
		CmisObject.metaClass.getIsFolder = { ->
			return delegate.baseTypeId== BaseTypeId.CMIS_FOLDER
		}
		
		CmisObject.metaClass.getProps = { ->
			def propmap=[:]
			delegate.getProperties().each { property ->
				propmap.put(property.queryName,property.value)
			}
			return propmap
		}
		
		CmisObject.metaClass.getPropertiesMap = { ->
			def propmap=[:]
			delegate.getProperties().each { property ->
				propmap.put(property.queryName,property)
			}
			return propmap
		}
		
		CmisObject.metaClass.getIsPwc = { ->			
			return delegate.prop.versionLabel=='pwc'
		}
		
		CmisObject.metaClass.getIsCheckedOut = { ->
			return (delegate.baseTypeId== BaseTypeId.CMIS_DOCUMENT) && delegate.isVersionSeriesCheckedOut()
		}
			
		CmisObject.metaClass.getRendition = { kind ->
			def rendition=null
			delegate.getRenditions().each { theRendition ->
				if (theRendition.kind==kind) {
					rendition=theRendition
				}
			}
			return rendition			
		}

		
		
		
	}
    
}
