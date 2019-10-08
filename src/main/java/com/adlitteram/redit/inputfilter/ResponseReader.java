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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class ResponseReader {

   protected ResponseHandler handler;

   public ResponseReader() {
      this(new ResponseHandler());
   }

   public ResponseReader(ResponseHandler handler) {
      this.handler = handler;
   }

   public Response read(InputStream is) throws IOException {
      return read(new InputSource(is));
   }

   public Response read(InputSource is) throws IOException {
      try {
         XMLReader parser = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
         parser.setContentHandler(handler);
         parser.setErrorHandler(handler);
         parser.setFeature("http://xml.org/sax/features/validation", false);
         parser.setFeature("http://xml.org/sax/features/namespaces", false);
         parser.parse(is);
      }
      catch (ParserConfigurationException | SAXException ex) {
         throw new IOException(ex);
      }
      return handler.getResponse();
   }

   public Response read(String path) throws IOException {
      return read(new InputSource(new File(path).toURI().toString()));
   }
}
