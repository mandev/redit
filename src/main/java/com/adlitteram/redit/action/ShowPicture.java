package com.adlitteram.redit.action;

import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.jasmin.gui.explorer.ExplorerPane;
import com.adlitteram.jasmin.gui.explorer.ImageFile;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.gui.ArticlePane;
import com.adlitteram.redit.gui.PictureViewer;
import java.awt.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowPicture extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(ShowPicture.class);
   //
   private final AppManager appManager;

   public ShowPicture(AppManager appManager) {
      super("ShowPicture");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager.getArticle());
   }

   public static void action(Article article) {
      logger.info("ShowPicture");

      ArticlePane articlePane = article.getArticlePane();
      if (articlePane != null) {
         ExplorerPane pictureList = articlePane.getExplorerPane();
         ImageFile[] files = pictureList.getSelectedImageFiles();
         ImageFile file = pictureList.getSelectedImageFile();
         if (files.length > 0) {
            new PictureViewer(files, file, article);
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
