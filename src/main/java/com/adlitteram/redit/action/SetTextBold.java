package com.adlitteram.redit.action;

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
   //
   public static final AttributeSet BOLD_STYLE = getBoldStyle();
   public static final AttributeSet NO_BOLD_STYLE = getNoBoldStyle();
   //
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
