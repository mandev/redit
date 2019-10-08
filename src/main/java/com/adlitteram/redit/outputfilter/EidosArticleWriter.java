/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit.outputfilter;

import com.adlitteram.redit.Article;
import com.adlitteram.redit.RDocument;
import java.io.IOException;
import java.io.Writer;
import javax.swing.text.BadLocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EidosArticleWriter extends ArticleWriter {

   private static final Logger logger = LoggerFactory.getLogger(EidosArticleWriter.class);
   //

   public EidosArticleWriter(Article article) {
      super(article);
   }

   @Override
   protected void writeDocument(Writer writer, RDocument document) throws IOException {
      try {
         EidosDocWriter docWriter = new EidosDocWriter(writer, document, encoding);
         docWriter.write();
      }
      catch (BadLocationException ex) {
         logger.warn("", ex);
         throw new IOException(ex.getMessage());
      }
   }
}
