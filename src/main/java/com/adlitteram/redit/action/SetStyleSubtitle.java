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

public class SetStyleSubtitle extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(SetStyleSubtitle.class);
   //
   private final AppManager appManager;

   public SetStyleSubtitle(AppManager appManager) {
      super("SetStyleSubtitle");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager.getArticle());

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(Article article) {
      logger.info("SetStyleSubtitle");

      StyledDocument document = article.getDocument();
      Style style = document.getStyle(StyleManager.SUBTITLE_STYLE);
      Caret caret = article.getCaret();
      StyleManager.setParagraphStyle(document, caret.getDot(), caret.getMark(), style);

      REditorKit editorKit = article.getEditorKit();
      MutableAttributeSet inputAttributes = editorKit.getInputAttributes();
      inputAttributes.removeAttributes(inputAttributes);
   }
}
