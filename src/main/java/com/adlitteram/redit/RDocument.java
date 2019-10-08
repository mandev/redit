/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit;

import javax.swing.text.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author manu
 */
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
