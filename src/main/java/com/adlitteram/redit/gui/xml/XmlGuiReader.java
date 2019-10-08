package com.adlitteram.redit.gui.xml;

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
