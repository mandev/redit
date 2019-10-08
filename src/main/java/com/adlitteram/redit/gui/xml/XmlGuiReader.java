/**
 * Copyright (C) 1999-2002 Emmanuel Deviller
 *
 * @version 1.0
 * @author Emmanuel Deviller
 */
package com.adlitteram.redit.gui.xml;

import com.adlitteram.redit.gui.MainFrame;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import javax.swing.JComponent;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.XMLReader;

public class XmlGuiReader {

   private static final Logger logger = LoggerFactory.getLogger(XmlGuiReader.class);
   //
   private final MainFrame mainFrame;

   public XmlGuiReader(MainFrame mainFrame) {
      this.mainFrame = mainFrame;
   }

   public JComponent read(String filename) {
      return read(new File(filename).toURI());
   }

   public JComponent read(URI uri) {
      XmlGuiHandler xmh;

      try {
         XMLReader parser = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
         xmh = new XmlGuiHandler(mainFrame);
         parser.setContentHandler(xmh);
         parser.setErrorHandler(xmh);
         parser.setFeature("http://xml.org/sax/features/validation", false);
         parser.setFeature("http://xml.org/sax/features/namespaces", false);
         parser.parse(uri.toString());
      }
      catch (org.xml.sax.SAXParseException spe) {
         logger.warn("", spe);
         return null;
      }
      catch (org.xml.sax.SAXException | IOException | ParserConfigurationException se) {
         logger.warn("", se);
         return null;
      }
      return xmh.getComponent();
   }
}
