/**
 * Copyright (C) 1999-2002 Emmanuel Deviller
 *
 * @version 1.0
 * @author Emmanuel Deviller
 */
package com.adlitteram.redit.inputfilter;

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
