package com.adlitteram.redit.action;

import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.jasmin.gui.explorer.ExplorerPane;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.gui.ArticlePane;
import com.adlitteram.redit.gui.dialog.IptcDialog;
import java.awt.event.ActionEvent;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditPicture extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(EditPicture.class);
   //
   private final AppManager appManager;

   public EditPicture(AppManager appManager) {
      super("EditPicture");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager.getArticle());
   }

   public void action(Article article) {
      logger.info("EditPicture");

      ArticlePane articlePane = article.getArticlePane();
      if (articlePane != null) {
         ExplorerPane pictureList = articlePane.getExplorerPane();
         File[] files = pictureList.getSelectedFiles();
         IptcDialog.create(appManager.getMainFrame(), article, files);
      }
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null
              && appManager.getArticlePane().getExplorerPane().getSelectedIndices().length > 0
              && appManager.getArticlePane().isPicturePaneVisible());
   }
}
