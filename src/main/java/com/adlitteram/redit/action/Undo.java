package com.adlitteram.redit.action;

import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import java.awt.event.ActionEvent;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Undo extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(Undo.class);
   //
   private final AppManager appManager;

   public Undo(AppManager appManager) {
      super("Undo");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager.getArticle());

      appManager.getMainFrame().requestFocusInWindow();
      appManager.getActionManager().enableActions();
   }

   public static void action(Article article) {
      logger.info("Undo");

      UndoManager undoManager = article.getUndoManager();
      try {
         if (undoManager.canUndo()) {
            undoManager.undo();
         }
      }
      catch (CannotUndoException ex) {
         logger.warn(null, ex);
      }
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null
              && appManager.getArticlePane().getArticle().getUndoManager().canUndo());

//        if (appManager.getArticlePane() != null &&
//            appManager.getArticlePane().getArticle().getUndoManager().canUndo()) {
//            setEnabled(true);
//            putValue(Action.NAME, appManager.getArticlePane().getArticle().getUndoManager().getUndoPresentationName());
//        }
//        else {
//            setEnabled(false);
//            putValue(Action.NAME, getText());
//        }
   }
}
