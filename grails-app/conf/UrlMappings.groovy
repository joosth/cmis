class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(controller:'cmisBrowse',action:"documentlist")
		"500"(view:'/error')
	}
}
