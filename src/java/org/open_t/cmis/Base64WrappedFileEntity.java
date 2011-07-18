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

package org.open_t.cmis;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.SequenceInputStream;
import java.io.Writer;

import org.apache.http.entity.FileEntity;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;

import org.open_t.base64.*;

/*
 * Entity for streaming a Base64 encoded file with a header and footer to be used with HttpClient
 * This allows for efficient streaming of files in a CMIS CreateDocument call.
 */

public class Base64WrappedFileEntity extends FileEntity {
	String header;
	String footer;
	
	/*
	 * Constructor, remember the header and footer
	 */

	public Base64WrappedFileEntity(File file, String contentType,String header,String footer) {
		super(file, contentType);
		this.header=header;
		this.footer=footer;
	}

	public long getContentLength() {
            return -1;
    }
	
	/*
	 * Get the content stream, consisting of header, base64 encoded file and footer
	 * 
	 */
	 public InputStream getContent() throws IOException {
		 	//Set up the file stream
		    org.open_t.base64.Base64.InputStream is = new Base64.InputStream(new FileInputStream(file),Base64.ENCODE |Base64.DO_BREAK_LINES);		    
		    InputStream headerStream = new ByteArrayInputStream(header.getBytes());
		    InputStream footerStream = new ByteArrayInputStream(footer.getBytes());
		    Vector<InputStream> inputStreams = new Vector<InputStream>();
		    inputStreams.add(headerStream);
		    inputStreams.add(is);
		    inputStreams.add(footerStream);
		    Enumeration<InputStream> enu = inputStreams.elements();
		    return new SequenceInputStream(enu);
	    }

	/*
	 * Write the stream to an outputstream
	 * @see org.apache.http.entity.FileEntity#writeTo(java.io.OutputStream)
	 */
	public void writeTo(final OutputStream outstream) throws IOException {
        if (outstream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        
        org.open_t.base64.Base64.InputStream is = new Base64.InputStream(new FileInputStream(file),Base64.ENCODE |Base64.DO_BREAK_LINES);
        
        
        InputStream instream = new FileInputStream(this.file);
        try {
        	Writer writer=new OutputStreamWriter(outstream);
        	
			writer.write(header);
	    	writer.flush();
	    	
            byte[] tmp = new byte[4096];
            int l;
            while ((l = is.read(tmp)) != -1) {
                outstream.write(tmp, 0, l);
            }
            outstream.flush();
            writer.write(footer);
            writer.close();
        } finally {
            instream.close();
        }
    }
}
