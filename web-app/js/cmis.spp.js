/*
 * Grails CMIS Plugin
 * Copyright 2010-2011, Open-T B.V., and individual contributors as indicated
 * by the @author tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License
 * version 3 published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses
 */

/*
 * CMIS plugin Javascript SPP links
 */

/*
 * Handles on-line editing through Sharepoint protocol
 */
jQuery.fn.sppLink = function () {
	$(this.selector).live("click",function(e) {
		e.stopPropagation();
		var controlProgID = "SharePoint.OpenDocuments"
		var sppAppProgId=$(this).attr("sppAppProgId");
		var url=$(this).attr("href");
	
		 try
         {			
            activeXControl = new ActiveXObject(controlProgID + ".3");
            
            return activeXControl.EditDocument3(window, url, true, sppAppProgId);
         }
         catch(e)
         {
            try
            {
               activeXControl = new ActiveXObject(controlProgID + ".2");
               
               return activeXControl.EditDocument2(window, url, sppAppProgId);
            }
            catch(e1)
            {
               try
               {
                  activeXControl = new ActiveXObject(controlProgID + ".1");
               
                  return activeXControl.EditDocument(url, sppAppProgId);
               }
               catch(e2)
               {
                   return window.open(url, "_blank");
               }
            }
         }
		
		return false
		
	});
	
}
        
	
$(function() {
	$("span.spp-link").sppLink();		
});
