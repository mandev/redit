package com.adlitteram.redit.inputfilter;

import com.adlitteram.jasmin.utils.NumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ResponseHandler extends DefaultHandler {

   private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);
   //
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
      //characters(ch,start,length) ;
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
