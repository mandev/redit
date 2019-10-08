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
import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.gui.ArticlePane;
import java.awt.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowPicturePane extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(ShowPicturePane.class);
   //
   private final AppManager appManager;

   public ShowPicturePane(AppManager appManager) {
      super("ShowPicturePane");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager.getMainFrame().getArticlePane());

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(ArticlePane articlePanel) {
      logger.info("ShowPicturePane");
      articlePanel.setPicturePaneVisible(!articlePanel.isPicturePaneVisible());
   }

   public static void action(ArticlePane articlePanel, boolean value) {
      logger.info("ShowPicturePane");
      articlePanel.setPicturePaneVisible(value);
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null);
   }
}
