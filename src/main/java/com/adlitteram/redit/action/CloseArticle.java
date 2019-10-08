package com.adlitteram.redit.action;

import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.Main;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseArticle extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(CloseArticle.class);

   private final AppManager appManager;

   public CloseArticle(AppManager appManager) {
      super("Close");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager.getArticle());
      appManager.getActionManager().enableActions();
   }

   public static void action(Article article) {
      logger.info("Close");

      if (article.isDirty()) {
         switch (JOptionPane.showOptionDialog(Main.getApplication().getMainFrame(),
                 Message.get("SaveArticle.Confirm") + " \"" + article.getName() + " \" ?",
                 Message.get("SaveArticle.Title"), JOptionPane.YES_NO_CANCEL_OPTION,
                 JOptionPane.WARNING_MESSAGE, null, null, null)) {

            case JOptionPane.OK_OPTION:
               SaveArticle.action(article);
               break;
            case JOptionPane.CLOSED_OPTION:
               break;
            case JOptionPane.CANCEL_OPTION:
               break;
         }
      }
   }

   @Override
   public void enable() {
      setEnabled(false);
   }
}
