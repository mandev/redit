package com.adlitteram.redit.xmlproperty;

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
import java.net.URI;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.XMLReader;

//static final String DEFAULT_PARSER_NAME = "javax.xml.parsers.SAXParser";
//XMLReader parser = (XMLReader)Class.forName(DEFAULT_PARSER_NAME).newInstance();
//static final String DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";
//parser.setFeature("http://apache.org/xml/features/continue-after-fatal-error", false);
public class XPropertiesReader {

   private static final Logger logger = LoggerFactory.getLogger(XPropertiesReader.class);

   public static boolean read(Properties props, String filename) {
      return read(props, new File(filename).toURI());
   }

   public static boolean read(Properties props, URI uri) {
      try {
         XMLReader parser = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
         XPropertiesHandler xh = new XPropertiesHandler(props);
         parser.setContentHandler(xh);
         parser.setErrorHandler(xh);
         parser.setFeature("http://xml.org/sax/features/validation", false);
         parser.setFeature("http://xml.org/sax/features/namespaces", false);
         //parser.setFeature( "http://apache.org/xml/features/validation/schema", false );
         parser.parse(uri.toString());
      }
      catch (org.xml.sax.SAXParseException spe) {
         logger.warn("", spe);
         return false;
      }
      catch (org.xml.sax.SAXException | IOException | ParserConfigurationException se) {
         logger.warn("", se);
         return false;
      }
      return true;
   }
}
