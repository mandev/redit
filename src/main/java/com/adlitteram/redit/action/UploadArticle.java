package com.adlitteram.redit.action;

import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.*;
import com.adlitteram.redit.gui.dialog.ConnectDialog;
import com.adlitteram.redit.gui.dialog.UploadDialog;
import com.adlitteram.redit.inputfilter.ResponseUser;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadArticle extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(UploadArticle.class);
   //
   private final AppManager appManager;

   public UploadArticle(AppManager appManager) {
      super("UploadArticle");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager);

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(final AppManager appManager) {
      logger.info("UploadArticle");

      Article article = appManager.getArticle();
      if (!StyleManager.checkTitleStyle(article.getDocument())) {
         int option = JOptionPane.showOptionDialog(Main.getMainFrame(), Message.get("StructureDialog.Continue"),
                 Message.get("StructureDialog.BadTitleStyle"), JOptionPane.YES_NO_OPTION,
                 JOptionPane.ERROR_MESSAGE, null, null, null);

         if (option == JOptionPane.CLOSED_OPTION || option == JOptionPane.NO_OPTION) {
            return;
         }
      }

      final User user = appManager.getUser();
      if (user != null && user.getUserName() != null && user.getUserName().length() > 0 && user.getPassword() != null) {

         Main.getMainFrame().setBusy(true);

         new SwingWorker<ResponseUser, Object>() {
            @Override
            public ResponseUser doInBackground() {
               UploadManager remoteManager = appManager.getRemoteManager();
               try {
                  return remoteManager.getUserInfo(user.getUserName(), user.getPassword());
               }
               catch (IOException ex) {
                  logger.warn("", ex);
               }
               return null;
            }

            @Override
            protected void done() {
               try {
                  Main.getMainFrame().setBusy(false);
                  ResponseUser response = get();
                  if (response != null && response.getError() == 0) {
                     User tmpUser = response.getUser();
                     tmpUser.setPassword(user.getPassword());
                     tmpUser.setColumnName(user.getColumnName());
                     appManager.setUser(tmpUser);
                     UploadDialog.create(appManager);
                  }
                  else {
                     ConnectDialog.create(appManager);
                  }
               }
               catch (InterruptedException | ExecutionException ex) {
                  logger.warn("", ex);
               }
            }
         }.execute();
      }
      else {
         ConnectDialog.create(appManager);
      }
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null
              && (appManager.getArticle().getDocument().getLength() > 0
              || appManager.getArticle().getExplorerModel().size() > 0));
   }
}
