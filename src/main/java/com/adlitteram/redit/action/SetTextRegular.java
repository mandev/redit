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

public class SetTextRegular extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(SetTextRegular.class);
   //
   public static final AttributeSet REGULAR_STYLE = getRegularStyle();
   private final AppManager appManager;

   public SetTextRegular(AppManager appManager) {
      super("SetTextRegular");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager.getArticle());

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(Article article) {
      logger.info("SetTextRegular");

      REditorKit editorKit = article.getEditorKit();
      MutableAttributeSet inputAttributes = editorKit.getInputAttributes();

      StyledDocument document = article.getDocument();
      Caret caret = article.getCaret();
      StyleManager.setCharacterStyle(document, caret.getDot(), caret.getMark(), REGULAR_STYLE, true);
      inputAttributes.removeAttributes(inputAttributes);  // replace = true
      inputAttributes.addAttributes(REGULAR_STYLE);
   }

   private static AttributeSet getRegularStyle() {
      return new SimpleAttributeSet();
   }
}
