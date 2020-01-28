package com.adlitteram.redit.inputfilter;

/*-
 * #%L
 * rEdit
 * %%
 * Copyright (C) 2009 - 2019 mandev
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
import com.adlitteram.jasmin.utils.NumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ResponseHandler extends DefaultHandler {

   private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

   protected Response response;
   protected StringBuilder buffer;

   public ResponseHandler() {
      this(new Response());
   }

   public ResponseHandler(Response response) {
      this.response = response;
      buffer = new StringBuilder();
   }

   public Response getResponse() {
      return response;
   }

   @Override
   public void startElement(String uri, String local, String raw, Attributes attrs) {
      buffer.setLength(0);
   }

   @Override
   public void endElement(String uri, String local, String raw) {

      if ("error".equals(raw)) {
         response.setError(NumUtils.intValue(buffer.toString()));
      }
      else if ("comment".equals(raw)) {
         response.setComment(buffer.toString());
      }
   }

   @Override
   public void characters(char ch[], int start, int length) {
      buffer.append(ch, start, length);
   }

   @Override
   public void ignorableWhitespace(char ch[], int start, int length) {
   }

   // ErrorHandler methods
   @Override
   public void warning(SAXParseException ex) {
      logger.warn(getLocationString(ex), ex);
   }

   @Override
   public void error(SAXParseException ex) {
      logger.warn(getLocationString(ex), ex);
   }

   @Override
   public void fatalError(SAXParseException ex) throws SAXException {
      logger.warn(getLocationString(ex), ex);
   }

   // Returns a string of the location.
   private String getLocationString(SAXParseException ex) {
      StringBuilder str = new StringBuilder();

      String systemId = ex.getSystemId();
      if (systemId != null) {
         int index = systemId.lastIndexOf('/');
         if (index != -1) {
            systemId = systemId.substring(index + 1);
         }
         str.append(systemId);
      }
      str.append(':').append(ex.getLineNumber());
      str.append(':').append(ex.getColumnNumber());
      return str.toString();
   }
}
