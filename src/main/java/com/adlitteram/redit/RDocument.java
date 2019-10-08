package com.adlitteram.redit;

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
import javax.swing.text.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RDocument extends DefaultStyledDocument {

   private static final Logger logger = LoggerFactory.getLogger(RDocument.class);
   //
   private final Article article;

   public RDocument(Article article) {
      super(article.getAppManager().getStyleManager().getStyleContext());
      this.article = article;
      setDocumentFilter(new RDocumentFilter());
      setLogicalStyle(0, getStyle(StyleManager.BODY_STYLE));
   }

   public Article getArticle() {
      return article;
   }

   class RDocumentFilter extends DocumentFilter {

      @Override
      public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
         StyledDocument doc = (StyledDocument) fb.getDocument();
         Element paragraph = doc.getParagraphElement(offset);
         if (offset == paragraph.getStartOffset() && (offset + length) < doc.getLength()) {
            doc.setLogicalStyle(offset, doc.getLogicalStyle(offset + length));
         }
         fb.remove(offset, length);
      }

//        @Override
//        public void insertString(DocumentFilter.FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
//        }
      @Override
      public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String str, AttributeSet attr) throws BadLocationException {
         StyledDocument doc = (StyledDocument) fb.getDocument();

         if ("\"".equals(str)) {

            int paraOffset = doc.getParagraphElement(offset).getStartOffset();
            String text = getText(paraOffset, offset - paraOffset);
            str = "«";

            for (int i = text.length() - 1; i >= 0; i--) {
               char c = text.charAt(i);
               if (c == '«') {
                  str = "»";
                  break;
               }
               if (c == '»') {
                  str = "«";
                  break;
               }
            }
         }

         super.replace(fb, offset, length, str, attr);
      }
   }

   public boolean canChangeFontSize(int n) {
      ElementIterator it = new ElementIterator(getDefaultRootElement());
      Element element;
      while ((element = it.next()) != null) {
         if (AbstractDocument.ContentElementName.equals(element.getName())) {
            AttributeSet attr = element.getAttributes();
            if (attr instanceof MutableAttributeSet && !attr.isDefined(StyleManager.GROUP)
                    && attr.isDefined(StyleConstants.Size)) {

               int fontSize = StyleConstants.getFontSize(attr);
               int fs = (n > 0) ? StyleManager.nextFontSize(fontSize) : StyleManager.prevFontSize(fontSize);
               if (fs == fontSize) {
                  return false;
               }
            }
         }
      }
      return true;
   }

   public void changeFontSize(int n) {
      writeLock();
      try {
         ElementIterator it = new ElementIterator(getDefaultRootElement());
         Element element;
         while ((element = it.next()) != null) {
            if (AbstractDocument.ContentElementName.equals(element.getName())) {
               AttributeSet attr = element.getAttributes();
               if (attr instanceof MutableAttributeSet && !attr.isDefined(StyleManager.GROUP)
                       && attr.isDefined(StyleConstants.Size)) {

                  int fontSize = StyleConstants.getFontSize(attr);
                  fontSize = (n > 0) ? StyleManager.nextFontSize(fontSize) : StyleManager.prevFontSize(fontSize);
                  StyleConstants.setFontSize((MutableAttributeSet) attr, fontSize);
               }
//                    else {
//                        logger.info("Cannot change fontSize : " + attr.getAttribute(StyleConstants.NameAttribute));
//                    }
            }
         }
      }
      finally {
         writeUnlock();
      }
   }
}
