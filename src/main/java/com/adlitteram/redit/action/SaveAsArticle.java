package com.adlitteram.redit.action;

import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.XProp;
import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.jasmin.gui.widget.FileChooser;
import com.adlitteram.jasmin.utils.ExtFilter;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.Main;
import com.adlitteram.redit.ReditApplication;
import java.awt.event.ActionEvent;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveAsArticle extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(SaveAsArticle.class);
   //
   private final AppManager appManager;

   public SaveAsArticle(AppManager appManager) {
      super("SaveAsArticle");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager);

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(AppManager appManager) {
      logger.info("SaveAsArticle");
      action(appManager.getArticle());
   }

   public static void action(Article article) {
      logger.info("SaveAsArticle");

      FileChooser fc = new FileChooser(Main.getApplication().getMainFrame(), Message.get("SaveAsArticle.Title"));
      fc.setDirectory(XProp.get("OpenArticle.Dir", ReditApplication.HOME_DIR));
      fc.addFileFilter(new ExtFilter("rdz", "Redit files"));

      while (fc.showSaveDialog() == FileChooser.APPROVE_OPTION) {
         File file = fc.getSelectedFile();
         if (file != null) {
            XProp.put("OpenArticle.Dir", file.getParent());
            article.setPath(file.getPath());
            SaveArticle.action(article);
            return;
         }
      }
   }
}
