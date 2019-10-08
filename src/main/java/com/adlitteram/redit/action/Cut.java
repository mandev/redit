package com.adlitteram.redit.action;

import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.REditorKit;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.text.DefaultEditorKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cut extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(Cut.class);
   //
   private final AppManager appManager;

   public Cut(AppManager appManager) {
      super("Cut");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      logger.info("Cut");

      REditorKit editorKit = appManager.getArticle().getEditorKit();
      Action action = editorKit.getAction(DefaultEditorKit.cutAction);
      action.actionPerformed(e);

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null
              && appManager.getArticlePane().getCaret().getDot() != appManager.getArticlePane().getCaret().getMark());
   }
}
