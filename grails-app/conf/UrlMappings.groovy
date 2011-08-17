class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(controller:'cmisBrowse',action:"browse")
		"500"(view:'/error')
	}
}
