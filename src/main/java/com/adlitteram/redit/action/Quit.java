package com.adlitteram.redit.action;

import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.Main;
import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.action.XAction;
import org.slf4j.Logger;
import com.adlitteram.jasmin.XProp;
import com.adlitteram.redit.Version;
import java.awt.event.ActionEvent;
import org.slf4j.LoggerFactory;
import javax.swing.JOptionPane;

public class Quit extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(Quit.class);
   //
   private final AppManager appManager;

   public Quit(AppManager appManager) {
      super("Quit");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager);
   }

   public static void action(AppManager appManager) {
      logger.info("Quit");

      Article article = appManager.getArticle();
      if (article != null && article.isDirty()) {
         switch (JOptionPane.showOptionDialog(Main.getMainFrame(), Message.get("SaveArticle.Confirm") + " \"" + article.getName() + " \" ?",
                 Message.get("SaveArticle.Title"),
                 JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null)) {

            case JOptionPane.OK_OPTION:
               SaveArticle.action(article);
               if (article.isDirty()) {
                  return;
               }
               break;
            case JOptionPane.CLOSED_OPTION:
               break;
            case JOptionPane.CANCEL_OPTION:
               return;
         }
      }

      appManager.saveProperties();
      XProp.saveProperties(Main.getApplication().getUserPropFile(), Version.getVERSION(), Version.getBUILD());

      System.exit(0);
   }
}
