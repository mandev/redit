package com.adlitteram.redit.action;

import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.gui.dialog.SearchReplaceDialog;
import java.awt.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchAndReplace extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(SearchAndReplace.class);
   //
   public static SearchReplaceDialog searchReplaceDialog;
   //
   private final AppManager appManager;

   public SearchAndReplace(AppManager appManager) {
      super("SearchAndReplace");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager);

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(AppManager appManager) {
      logger.info("SearchAndReplace");
      if (searchReplaceDialog == null) {
         searchReplaceDialog = new SearchReplaceDialog(appManager);
      }
      searchReplaceDialog.setVisible(true);
   }

   public static boolean isDialogVisible() {
      return (searchReplaceDialog != null && searchReplaceDialog.isVisible());
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null
              && appManager.getArticlePane().getTextPane().getDocument().getLength() > 0
              && !Search.isDialogVisible() && !SearchAndReplace.isDialogVisible());
   }
}
