package com.adlitteram.redit.action;

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

public class SetStyleSignature extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(SetStyleSignature.class);
   //
   private final AppManager appManager;

   public SetStyleSignature(AppManager appManager) {
      super("SetStyleSignature");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager.getArticle());

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(Article article) {
      logger.info("SetStyleSignature");

      REditorKit editorKit = article.getEditorKit();
      MutableAttributeSet inputAttributes = editorKit.getInputAttributes();

      StyledDocument document = article.getDocument();
      Style style = document.getStyle(StyleManager.SIGNATURE_STYLE);
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
