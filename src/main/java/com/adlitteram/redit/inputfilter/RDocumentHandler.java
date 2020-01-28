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
import com.adlitteram.redit.StyleManager;
import javax.swing.text.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class RDocumentHandler extends DefaultHandler {

   private static final Logger logger = LoggerFactory.getLogger(RDocumentHandler.class);

   private final StyledDocument document;
   private Style logicalStyle;
   private Style oldLogicalStyle;
   private SimpleAttributeSet charStyle;
   private SimpleAttributeSet oldCharStyle;
   private StringBuilder buffer;

   public RDocumentHandler(StyledDocument document) {
      this.document = document;
      charStyle = new SimpleAttributeSet();
      oldCharStyle = new SimpleAttributeSet();
   }

   @Override
   public void startElement(String uri, String local, String raw, Attributes attrs) {
      Style s = document.getStyle(raw);
      if (s != null) {
         if (s.isDefined(StyleManager.GROUP)) {
            oldLogicalStyle = logicalStyle;
            logicalStyle = s;
         }
         else {
            addCharacters();
            oldCharStyle = charStyle;
            charStyle = new SimpleAttributeSet(s);
         }
      }
      else if ("p".equals(raw)) {
         buffer = new StringBuilder();
      }
      else if ("b".equals(raw)) {
         addCharacters();
         StyleConstants.setBold(charStyle, true);
      }
      else if ("i".equals(raw)) {
         addCharacters();
         StyleConstants.setItalic(charStyle, true);
      }
      else if ("u".equals(raw)) {
         addCharacters();
         StyleConstants.setUnderline(charStyle, true);
      }
   }

   @Override
   public void endElement(String uri, String local, String raw) {
      Style s = document.getStyle(raw);
      if (s != null) {
         if (s.isDefined(StyleManager.GROUP)) {
            logicalStyle = oldLogicalStyle;
         }
         else {
            addCharacters();
            charStyle = oldCharStyle;
         }
      }
      else if ("p".equals(raw)) {
         addCharacters();
         document.setLogicalStyle(document.getLength() - 1, logicalStyle);
      }
      else if ("b".equals(raw)) {
         addCharacters();
         StyleConstants.setBold(charStyle, false);
      }
      else if ("i".equals(raw)) {
         addCharacters();
         StyleConstants.setItalic(charStyle, false);
      }
      else if ("u".equals(raw)) {
         addCharacters();
         StyleConstants.setUnderline(charStyle, false);
      }
   }

   private void addCharacters() {
      try {
         if (buffer != null && buffer.length() >= 0) {
            document.insertString(document.getLength(), buffer.toString(), charStyle);
            charStyle = new SimpleAttributeSet(charStyle);
            buffer = new StringBuilder();
         }
      }
      catch (BadLocationException ex) {
         logger.warn("", ex);
      }
   }

   @Override
   public void characters(char ch[], int start, int length) {
      if (buffer != null) {
         buffer.append(ch, start, length);
      }
   }

   @Override
   public void ignorableWhitespace(char ch[], int start, int length) {
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
