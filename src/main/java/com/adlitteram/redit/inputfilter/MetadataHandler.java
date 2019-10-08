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
