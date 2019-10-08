package com.adlitteram.redit.action;

import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.gui.ArticlePane;
import java.awt.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowPicturePane extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(ShowPicturePane.class);
   //
   private final AppManager appManager;

   public ShowPicturePane(AppManager appManager) {
      super("ShowPicturePane");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager.getMainFrame().getArticlePane());

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(ArticlePane articlePanel) {
      logger.info("ShowPicturePane");
      articlePanel.setPicturePaneVisible(!articlePanel.isPicturePaneVisible());
   }

   public static void action(ArticlePane articlePanel, boolean value) {
      logger.info("ShowPicturePane");
      articlePanel.setPicturePaneVisible(value);
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null);
   }
}
