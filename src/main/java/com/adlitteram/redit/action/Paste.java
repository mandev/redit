package com.adlitteram.redit.action;

import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.REditorKit;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.text.DefaultEditorKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Paste extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(Paste.class);
   //
   private final AppManager appManager;

   public Paste(AppManager appManager) {
      super("Paste");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      logger.info("Paste");

      REditorKit editorKit = appManager.getArticle().getEditorKit();
      Action action = editorKit.getAction(DefaultEditorKit.pasteAction);
      action.actionPerformed(e);

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   @Override
   public void enable() {
      setEnabled(true);
   }
}
