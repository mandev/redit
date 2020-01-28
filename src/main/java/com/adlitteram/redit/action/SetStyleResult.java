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
import com.adlitteram.redit.REditorKit;
import com.adlitteram.redit.StyleManager;
import java.awt.event.ActionEvent;
import javax.swing.text.Caret;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetStyleResult extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(SetStyleResult.class);
   
   private final AppManager appManager;

   public SetStyleResult(AppManager appManager) {
      super("SetStyleResult");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager.getArticle());

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(Article article) {
      logger.info("SetStyleResult");

      REditorKit editorKit = article.getEditorKit();
      MutableAttributeSet inputAttributes = editorKit.getInputAttributes();

      StyledDocument document = article.getDocument();
      Style style = document.getStyle(StyleManager.RESULT_STYLE);
      Caret caret = article.getCaret();
      StyleManager.setCharacterStyle(document, caret.getDot(), caret.getMark(), style, true);

      inputAttributes.removeAttributes(inputAttributes);  // replace = true
      inputAttributes.addAttributes(style);
   }

   @Override
   public void enable() {
      if (appManager.getArticlePane() == null) {
         setEnabled(false);
         return;
      }

      Caret caret = appManager.getArticlePane().getCaret();
      StyleManager styleManager = appManager.getStyleManager();
      StyledDocument document = appManager.getArticle().getDocument();
      setEnabled(styleManager.getStyle(StyleManager.BODY_STYLE).equals(StyleManager.getLogicalStyle(document, caret.getDot(), caret.getMark())));
   }
}
