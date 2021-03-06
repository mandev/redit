package com.adlitteram.redit.action;

/*-
 * #%L
 * rEdit
 * %%
 * Copyright (C) 2009 - 2019 mandev
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
