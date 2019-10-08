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

public class SetTextItalic extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(SetTextItalic.class);
   //
   public static final AttributeSet ITALIC_STYLE = getItalicStyle();
   public static final AttributeSet NO_ITALIC_STYLE = getNoItalicStyle();
   //
   private final AppManager appManager;

   public SetTextItalic(AppManager appManager) {
      super("SetTextItalic");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager.getArticle());

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(Article article) {
      logger.info("SetTextItalic");

      REditorKit editorKit = article.getEditorKit();
      MutableAttributeSet inputAttributes = editorKit.getInputAttributes();
      AttributeSet style = (StyleConstants.isItalic(inputAttributes)) ? NO_ITALIC_STYLE : ITALIC_STYLE;

      // Set italic style - Use with replace = false
      StyledDocument document = article.getDocument();
      Caret caret = article.getCaret();
      StyleManager.setCharacterStyle(document, caret.getDot(), caret.getMark(), style, false);
      inputAttributes.addAttributes(style);
   }

   private static AttributeSet getItalicStyle() {
      SimpleAttributeSet as = new SimpleAttributeSet();
      StyleConstants.setItalic(as, true);
      return as;
   }

   private static AttributeSet getNoItalicStyle() {
      SimpleAttributeSet as = new SimpleAttributeSet();
      StyleConstants.setItalic(as, false);
      return as;
   }
}
