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
import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.jasmin.utils.GuiUtils;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.Main;
import com.adlitteram.redit.outputfilter.RdzArticleWriter;
import java.awt.event.ActionEvent;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveArticle extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(SaveArticle.class);
   //
   private final AppManager appManager;

   public SaveArticle(AppManager appManager) {
      super("SaveArticle");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager);
   }

   public static void action(AppManager appManager) {
      logger.info("SaveArticle");
      action(appManager.getArticle());

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(Article article) {
      logger.info("SaveArticle");

      String path = article.getPath();
      if (path == null || path.length() == 0) {
         SaveAsArticle.action(article);
      }
      else {
         try {
            Main.getMainFrame().setBusy(true);
            article.setPath(FilenameUtils.removeExtension(path) + ".rdz");
            RdzArticleWriter articleWriter = new RdzArticleWriter(article);
            articleWriter.write();
            article.setClean();
            article.getAppManager().getRecentFilesManager().addFilename(article.getPath());
         }
         catch (IOException ex) {
            logger.warn("", ex);
            Main.getMainFrame().setBusy(false);
            GuiUtils.showError(Message.get("SaveArticle.Error") + "\n" + ex.getLocalizedMessage());
         }
         finally {
            Main.getMainFrame().setBusy(false);
         }
      }
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null
              && appManager.getArticle().isDirty());
   }
}
