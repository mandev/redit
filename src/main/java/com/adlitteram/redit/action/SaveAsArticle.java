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
import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.XProp;
import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.jasmin.gui.widget.FileChooser;
import com.adlitteram.jasmin.utils.ExtFilter;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.Main;
import com.adlitteram.redit.ReditApplication;
import java.awt.event.ActionEvent;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveAsArticle extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(SaveAsArticle.class);
   //
   private final AppManager appManager;

   public SaveAsArticle(AppManager appManager) {
      super("SaveAsArticle");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager);

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(AppManager appManager) {
      logger.info("SaveAsArticle");
      action(appManager.getArticle());
   }

   public static void action(Article article) {
      logger.info("SaveAsArticle");

      FileChooser fc = new FileChooser(Main.getApplication().getMainFrame(), Message.get("SaveAsArticle.Title"));
      fc.setDirectory(XProp.get("OpenArticle.Dir", ReditApplication.HOME_DIR));
      fc.addFileFilter(new ExtFilter("rdz", "Redit files"));

      while (fc.showSaveDialog() == FileChooser.APPROVE_OPTION) {
         File file = fc.getSelectedFile();
         if (file != null) {
            XProp.put("OpenArticle.Dir", file.getParent());
            article.setPath(file.getPath());
            SaveArticle.action(article);
            return;
         }
      }
   }
}
