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
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.Main;
import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.action.XAction;
import org.slf4j.Logger;
import com.adlitteram.jasmin.property.XProp;
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
