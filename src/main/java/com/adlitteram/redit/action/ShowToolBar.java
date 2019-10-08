package com.adlitteram.redit.action;

import com.adlitteram.jasmin.XProp;
import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.StyleManager;
import java.awt.event.ActionEvent;
import javax.swing.JToolBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowToolBar extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(ShowToolBar.class);
   //
   private final AppManager appManager;

   public ShowToolBar(AppManager appManager) {
      super("ShowToolBar");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      logger.info("ShowToolBar");

      StyleManager styleManager = appManager.getStyleManager();
      styleManager.increaseFontSize(appManager.getArticle().getDocument());

      JToolBar toolBar = appManager.getMainFrame().getToolBar();
      toolBar.setVisible(!toolBar.isVisible());
      XProp.put("ToolBar.IsVisible", toolBar.isVisible());
      appManager.updateWidget("ShowToolBar", toolBar.isVisible());

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null);
   }
}
