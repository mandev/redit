package com.adlitteram.redit.action;

import com.adlitteram.jasmin.XProp;
import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.gui.StatusBar;
import java.awt.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowStatusBar extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(ShowStatusBar.class);
   //
   private final AppManager appManager;

   public ShowStatusBar(AppManager appManager) {
      super("ShowStatusBar");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      logger.info("ShowStatusBar");

      StatusBar statusBar = appManager.getMainFrame().getStatusBar();
      statusBar.setVisible(!statusBar.isVisible());
      XProp.put("StatusBar.IsVisible", statusBar.isVisible());
      appManager.updateWidget("ShowStatusBar", statusBar.isVisible());

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null);
   }
}
