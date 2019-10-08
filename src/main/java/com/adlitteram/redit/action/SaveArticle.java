package com.adlitteram.redit.action;

import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.jasmin.utils.GuiUtils;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.Main;
import com.adlitteram.redit.outputfilter.RdzArticleWriter;
import java.awt.event.ActionEvent;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveArticle extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(SaveArticle.class);
   //
   private final AppManager appManager;

   public SaveArticle(AppManager appManager) {
      super("SaveArticle");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager);
   }

   public static void action(AppManager appManager) {
      logger.info("SaveArticle");
      action(appManager.getArticle());

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(Article article) {
      logger.info("SaveArticle");

      String path = article.getPath();
      if (path == null || path.length() == 0) {
         SaveAsArticle.action(article);
      }
      else {
         try {
            Main.getMainFrame().setBusy(true);
            article.setPath(FilenameUtils.removeExtension(path) + ".rdz");
            RdzArticleWriter articleWriter = new RdzArticleWriter(article);
            articleWriter.write();
            article.setClean();
            article.getAppManager().getRecentFilesManager().addFilename(article.getPath());
         }
         catch (IOException ex) {
            logger.warn("", ex);
            Main.getMainFrame().setBusy(false);
            GuiUtils.showError(Message.get("SaveArticle.Error") + "\n" + ex.getLocalizedMessage());
         }
         finally {
            Main.getMainFrame().setBusy(false);
         }
      }
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null
              && appManager.getArticle().isDirty());
   }
}
