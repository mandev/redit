package com.adlitteram.redit.inputfilter;

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
