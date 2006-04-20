/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999-2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.schema.*;
import org.exolab.castor.util.Configuration;
import org.xml.sax.*;

import java.net.URL;
import java.util.Vector;

public class IncludeUnmarshaller extends SaxUnmarshaller
{

    public IncludeUnmarshaller
        (Schema schema, AttributeList atts, Resolver resolver, Locator locator)
        throws SAXException
    {
      this(schema, atts, resolver, locator, null);
    }

    public IncludeUnmarshaller
        (Schema schema, AttributeList atts, Resolver resolver, Locator locator, SchemaUnmarshallerState state)
		throws SAXException
    {
        super();
        setResolver(resolver);

		//-- Get schemaLocation
		String include = atts.getValue("schemaLocation");
		if (include == null)
			throw new SAXException("'schemaLocation' attribute missing on 'include'");

		// note: URI not supported (just system path), so remove any file://
        String absolute = include;
		if (absolute.startsWith("file://")){
            absolute = absolute.substring(7);
            if (java.io.File.separatorChar =='\\')
                absolute = absolute.substring(1);
        }

        // if the path is relative Xerces append the "user.Dir"
        // we need to keep the base directory of the document
        if (!new java.io.File(absolute).isAbsolute()) {

             String temp = locator.getSystemId();
             if (absolute.startsWith("./"))
                absolute = absolute.substring(2);
             if (absolute.startsWith("/"))
                absolute = include.substring(1);
             if (temp != null) {
                //remove 'file://'
                temp = temp.substring(7);
                if (java.io.File.separatorChar =='\\')
                    temp = temp.substring(1);
                temp = temp.substring(0,temp.lastIndexOf('/')+1);
                //resolve the previous directory
                if (absolute.startsWith("../")) {
                    absolute = absolute.substring(3);
                    temp = temp.substring(0,temp.lastIndexOf('/'));
                    temp = temp.substring(0,temp.lastIndexOf('/')+1);
                }
                include = temp + absolute;
                temp = null;
            }
        }
        //-- Has this schema location been processed?
        if (state.processed(include)) {
            return;
        }
		state.markAsProcessed(include, schema);


        //just keep track of the schemaLocation
        schema.addInclude(include);

		Parser parser = null;
		try {
		    parser = Configuration.getParser();
		}
		catch(RuntimeException rte) {}
		if (parser == null) {
		    throw new SAXException("Error failed to create parser for include");
		}
		else
		{
			SchemaUnmarshaller schemaUnmarshaller = new SchemaUnmarshaller(true, state);
			schemaUnmarshaller.setSchema(schema);
			parser.setDocumentHandler(schemaUnmarshaller);
			parser.setErrorHandler(schemaUnmarshaller);
		}

		try {
            parser.parse(new InputSource(include));
		}
		catch(java.io.IOException ioe) {
		    throw new SAXException("Error reading include file '"+include+"'");
		}
	}


    /**
     * Sets the name of the element that this UnknownUnmarshaller handles
     * @param name the name of the element that this unmarshaller handles
    **/
    public String elementName() {
        return SchemaNames.INCLUDE;
    } //-- elementName

    /**
     * Returns the Object created by this SaxUnmarshaller
     * @return the Object created by this SaxUnmarshaller
    **/
    public Object getObject() {
        return null;
    } //-- getObject

}
