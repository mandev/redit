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
import com.adlitteram.jasmin.Message;
import com.adlitteram.redit.Metadata;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class RdzMetadataReader {

   private static Metadata parseStructure(InputStream is, ZipFile zipFile) throws IOException {
      MetadataHandler xmh = new MetadataHandler(zipFile);

      try {
         XMLReader parser = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
         parser.setContentHandler(xmh);
         parser.setErrorHandler(xmh);
         parser.setFeature("http://xml.org/sax/features/validation", false);
         parser.setFeature("http://xml.org/sax/features/namespaces", false);
         parser.parse(new InputSource(is));
      }
      catch (ParserConfigurationException | SAXException ex) {
         throw new IOException(ex);
      }
      catch (SAXStopException ex) {
         // This exception is "normal"
      }
      return xmh.getMetadata();
   }

   public static Metadata read(String path) throws IOException {
      return read(new File(path));
   }

   public static Metadata read(File file) throws IOException {

      ZipFile zipFile = null;
      Metadata metadata = null;

      try {
         zipFile = new ZipFile(file);
         ZipEntry entry = zipFile.getEntry("structure.xml");
         if (entry == null) {
            throw new IOException(Message.get("ZipReader.StructureNotFound"));
         }
         metadata = parseStructure(zipFile.getInputStream(entry), zipFile);
      }
      finally {
         if (zipFile != null) {
            zipFile.close();
         }
      }

      return metadata;
   }
}
