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
import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.jasmin.gui.explorer.ExplorerPane;
import com.adlitteram.jasmin.gui.explorer.ImageFile;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.gui.ArticlePane;
import com.adlitteram.redit.gui.dialog.ArticlePictureDialog;
import java.awt.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditPictureMeta extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(EditPictureMeta.class);
   //
   private final AppManager appManager;

   public EditPictureMeta(AppManager appManager) {
      super("EditPictureMeta");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager.getArticle());
   }

   public void action(Article article) {
      ArticlePane articlePane = article.getArticlePane();
      if (articlePane != null) {
         ExplorerPane pictureList = articlePane.getExplorerPane();
         ImageFile[] files = pictureList.getSelectedImageFiles();
         ArticlePictureDialog.create(appManager.getMainFrame(), article, files);
      }
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null
              && appManager.getArticlePane().getExplorerPane().getSelectedIndices().length > 0
              && appManager.getArticlePane().isPicturePaneVisible());
   }
}
