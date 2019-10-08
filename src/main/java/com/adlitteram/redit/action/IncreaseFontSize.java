package com.adlitteram.redit.action;

import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.StyleManager;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncreaseFontSize extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(IncreaseFontSize.class);
   //
   private final AppManager appManager;

   public IncreaseFontSize(AppManager appManager) {
      super("IncreaseFontSize");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager);
   }

   public static void action(final AppManager appManager) {
      logger.info("IncreaseFontSize");

      StyleManager styleManager = appManager.getStyleManager();

      final Article article = appManager.getArticle();
      boolean isDirty = article.isDirty();
      styleManager.increaseFontSize(article.getDocument());

      // IncreaseFontSize is invoked later on on the EDT
      if (!isDirty) {
         SwingUtilities.invokeLater(() -> {
            article.setClean();
            appManager.getMainFrame().requestTextFocus();
            appManager.getActionManager().enableActions();
         });
      }
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null);
   }
}
