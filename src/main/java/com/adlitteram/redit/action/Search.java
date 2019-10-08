package com.adlitteram.redit.action;

import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.gui.dialog.SearchDialog;
import java.awt.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Search extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(Search.class);
   //
   public static SearchDialog searchDialog;
   //
   private final AppManager appManager;

   public Search(AppManager appManager) {
      super("Search");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager);

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(AppManager appManager) {
      logger.info("Search");
      if (searchDialog == null) {
         searchDialog = new SearchDialog(appManager);
      }
      searchDialog.setVisible(true);
   }

   public static boolean isDialogVisible() {
      return (searchDialog != null && searchDialog.isVisible());
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null
              && appManager.getArticlePane().getTextPane().getDocument().getLength() > 0
              && !Search.isDialogVisible() && !SearchAndReplace.isDialogVisible());
   }
}
