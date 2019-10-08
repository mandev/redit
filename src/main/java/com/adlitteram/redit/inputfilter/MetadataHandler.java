package com.adlitteram.redit.inputfilter;

import com.adlitteram.jasmin.utils.NumUtils;
import com.adlitteram.redit.Metadata;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class MetadataHandler extends DefaultHandler {

   private static final Logger logger = LoggerFactory.getLogger(MetadataHandler.class);

   private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

   private final ZipFile zipFile;
   private Metadata metadata;

   public MetadataHandler(ZipFile zipFile) {
      this.zipFile = zipFile;
   }

   @Override
   public void startElement(String uri, String local, String raw, Attributes attrs) throws SAXException {
      if ("article".equals(raw)) {

         metadata = new Metadata(attrs.getValue("release"), attrs.getValue("version"),
                 attrs.getValue("filename"), attrs.getValue("name"), convertToDate(attrs.getValue("date")),
                 NumUtils.longValue(attrs.getValue("length"), -1));

         // This is a hack to stop parsing eagerly  
         throw new SAXStopException("Stop Parsing");
      }
   }

   private Date convertToDate(String value) {
      try {
         return DATE_FORMATTER.parse(value);
      }
      catch (ParseException ex) {
         logger.warn(null, ex);
      }
      return new Date(new File(zipFile.getName()).lastModified());
   }

   public Metadata getMetadata() {
      return metadata;
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
