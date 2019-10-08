package com.adlitteram.redit.outputfilter;

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
