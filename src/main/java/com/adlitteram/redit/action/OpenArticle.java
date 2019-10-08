package com.adlitteram.redit.action;

import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.XProp;
import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.jasmin.gui.widget.FileChooser;
import com.adlitteram.jasmin.utils.ExtFilter;
import com.adlitteram.jasmin.utils.GuiUtils;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.Main;
import com.adlitteram.redit.ReditApplication;
import com.adlitteram.redit.inputfilter.RdzReader;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenArticle extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(OpenArticle.class);
   //
   private final AppManager appManager;

   public OpenArticle(AppManager appManager) {
      super("OpenArticle");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager);

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(AppManager appManager) {
      logger.info("OpenArticle");

      Article article = appManager.getArticle();

      if (article.isDirty()) {
         switch (JOptionPane.showOptionDialog(Main.getMainFrame(), Message.get("SaveArticle.Confirm") + " \"" + article.getName() + " \"  ?",
                 Message.get("SaveArticle.Title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null)) {

            case JOptionPane.OK_OPTION:
               SaveArticle.action(article);
               if (article.isDirty()) {
                  return;
               }
               break;
            case JOptionPane.CLOSED_OPTION:
               break;
            case JOptionPane.CANCEL_OPTION:
               return;
         }
      }

      FileChooser fc = new FileChooser(Main.getMainFrame(), Message.get("OpenArticle.Title"));
      fc.setDirectory(XProp.get("OpenArticle.Dir", ReditApplication.HOME_DIR));
      fc.addFileFilter(new ExtFilter("rdz", "Redit files"));

      File file = null;
      while (fc.showOpenDialog() == FileChooser.APPROVE_OPTION) {
         file = fc.getSelectedFile();
         if (file != null) {
            XProp.put("OpenArticle.Dir", file.getParent());
            break;
         }
      }

      if (file != null) {
         try {
            action(appManager, file, false);
         }
         catch (IOException ex) {
            logger.warn("", ex);
            GuiUtils.showError(Message.get("OpenArticle.Error") + "\n" + ex.getLocalizedMessage());
         }
      }
   }

   public static void action(AppManager appManager, File file, boolean checkDirty) throws IOException {
      Main.getMainFrame().setBusy(true);

      Article newArticle = new Article(appManager);
      RdzReader reader = new RdzReader(newArticle);

      try {
         reader.read(file.getPath());

         String bckDir = FilenameUtils.normalizeNoEndSeparator(ReditApplication.USER_BCK_DIR);
         String fileDir = FilenameUtils.normalizeNoEndSeparator(file.getParent());
         if (FilenameUtils.equalsOnSystem(bckDir, fileDir) && file.getName().startsWith("backup_")) {
            newArticle.setPath(null);
         }

         newArticle.getUndoManager().discardAllEdits();

         Main.getMainFrame().setBusy(false);

         Article article = appManager.getArticle();
         if (checkDirty && article != null && article.isDirty()) {
            switch (JOptionPane.showOptionDialog(Main.getMainFrame(), Message.get("SaveArticle.Confirm") + " \"" + article.getName() + " \" ?",
                    Message.get("SaveArticle.Title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null)) {

               case JOptionPane.OK_OPTION:
                  SaveArticle.action(article);
                  if (article.isDirty()) {
                     return;
                  }
                  break;
               case JOptionPane.CLOSED_OPTION:
                  break;
               case JOptionPane.CANCEL_OPTION:
                  return;
            }
         }

         appManager.addArticle(newArticle);
         newArticle.getArticlePane().setPicturePaneVisible(newArticle.getExplorerModel().size() > 0);
         newArticle.setClean();
      }
      finally {
         Main.getMainFrame().setBusy(false);
      }
   }
}
