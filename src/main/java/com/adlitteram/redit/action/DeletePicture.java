package com.adlitteram.redit.action;

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
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.jasmin.gui.explorer.ExplorerModel;
import com.adlitteram.jasmin.gui.explorer.ExplorerPane;
import org.slf4j.Logger;
import com.adlitteram.redit.gui.ArticlePane;
import java.awt.event.ActionEvent;
import org.slf4j.LoggerFactory;

public class DeletePicture extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(DeletePicture.class);
   //
   private final AppManager appManager;

   public DeletePicture(AppManager appManager) {
      super("DeletePicture");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager.getArticle());
   }

   public static void action(Article article) {
      logger.info("DeletePicture");

      ArticlePane articlePane = article.getArticlePane();
      if (articlePane != null) {
         ExplorerModel model = article.getExplorerModel();
         ExplorerPane pictureList = articlePane.getExplorerPane();
         int[] indices = pictureList.getSelectedIndices();
         for (int i = indices.length - 1; i >= 0; i--) {
            model.remove(indices[i]);
         }
      }
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null
              && appManager.getArticlePane().getExplorerPane().getSelectedIndices().length > 0
              && appManager.getArticlePane().isPicturePaneVisible());
   }
}
