package com.adlitteram.redit.action;

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
