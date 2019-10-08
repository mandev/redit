package com.adlitteram.redit.action;

import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import java.awt.event.ActionEvent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Redo extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(Redo.class);
   //
   private final AppManager appManager;

   public Redo(AppManager appManager) {
      super("Redo");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager.getArticle());

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(Article article) {
      logger.info("Redo");

      UndoManager undoManager = article.getUndoManager();
      try {
         if (undoManager.canRedo()) {
            undoManager.redo();
         }
      }
      catch (CannotRedoException ex) {
         logger.warn("", ex);
      }
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null
              && appManager.getArticlePane().getArticle().getUndoManager().canRedo());

//        if (appManager.getArticlePane() != null &&
//            appManager.getArticlePane().getArticle().getUndoManager().canRedo()) {
//            setEnabled(true);
//            putValue(Action.NAME, appManager.getArticlePane().getArticle().getUndoManager().getRedoPresentationName());
//        }
//        else {
//            setEnabled(false);
//            putValue(Action.NAME, getText());
//        }
   }
}