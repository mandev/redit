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
import javax.swing.text.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetTextBold extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(SetTextBold.class);

   public static final AttributeSet BOLD_STYLE = getBoldStyle();
   public static final AttributeSet NO_BOLD_STYLE = getNoBoldStyle();

   private final AppManager appManager;

   public SetTextBold(AppManager appManager) {
      super("SetTextBold");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager.getArticle());

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(Article article) {
      logger.info("SetTextBold");

      REditorKit editorKit = article.getEditorKit();
      MutableAttributeSet inputAttributes = editorKit.getInputAttributes();
      AttributeSet style = (StyleConstants.isBold(inputAttributes)) ? NO_BOLD_STYLE : BOLD_STYLE;

      StyledDocument document = article.getDocument();
      Caret caret = article.getCaret();
      StyleManager.setCharacterStyle(document, caret.getDot(), caret.getMark(), style, false);
      inputAttributes.addAttributes(style);
   }

   private static AttributeSet getBoldStyle() {
      SimpleAttributeSet sas = new SimpleAttributeSet();
      StyleConstants.setBold(sas, true);
      return sas;
   }

   private static AttributeSet getNoBoldStyle() {
      SimpleAttributeSet sas = new SimpleAttributeSet();
      StyleConstants.setBold(sas, false);
      return sas;
   }
}
