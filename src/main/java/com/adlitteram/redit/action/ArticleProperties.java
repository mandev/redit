package com.adlitteram.redit.action;

import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.gui.ArticlePane;
import com.adlitteram.redit.gui.dialog.ArticlePropertiesDialog;
import java.awt.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleProperties extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(ArticleProperties.class);
   //
   private final AppManager appManager;

   public ArticleProperties(AppManager appManager) {
      super("ArticleProperties");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager.getArticle());
   }

   public void action(Article article) {
      logger.info("ArticleProperties");

      ArticlePane articlePane = article.getArticlePane();
      if (articlePane != null) {
         ArticlePropertiesDialog.create(appManager.getMainFrame(), article);
         appManager.getMainFrame().requestTextFocus();
         appManager.getActionManager().enableActions();
      }
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null);
   }
}
