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
import javax.swing.text.StyledDocument;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class RDocumentReader {

   private static final Logger logger = LoggerFactory.getLogger(RDocumentReader.class);

   private final StyledDocument document;

   public RDocumentReader(StyledDocument document) {
      this.document = document;
   }

   public StyledDocument read(InputStream in) throws IOException {
      return read(new InputSource(in));
   }

   public StyledDocument read(String filename) throws IOException {
      File file = new File(filename);
      return read(new InputSource(file.toURI().toString()));
   }

   public StyledDocument read(InputSource input) throws IOException {
      try {
         XMLReader parser = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
         RDocumentHandler xmh = new RDocumentHandler(document);
         parser.setContentHandler(xmh);
         parser.setErrorHandler(xmh);
         parser.setFeature("http://xml.org/sax/features/validation", false);
         parser.setFeature("http://xml.org/sax/features/namespaces", false);
         parser.parse(input);
         return document;
      }
      catch (ParserConfigurationException | SAXException ex) {
         logger.warn("", ex);
         throw new IOException(ex);
      }
   }

   public StyledDocument getDocument() {
      return document;
   }
}
