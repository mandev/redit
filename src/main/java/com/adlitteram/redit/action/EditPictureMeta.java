package com.adlitteram.redit.action;

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
