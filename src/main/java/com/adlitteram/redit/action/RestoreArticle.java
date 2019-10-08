package com.adlitteram.redit.action;

import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.gui.dialog.RestoreDialog;
import java.awt.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestoreArticle extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(RestoreArticle.class);
   //
   private final AppManager appManager;

   public RestoreArticle(AppManager appManager) {
      super("RestoreArticle");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      RestoreDialog.create(appManager);

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }
}
