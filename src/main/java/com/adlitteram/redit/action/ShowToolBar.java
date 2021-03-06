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
import com.adlitteram.jasmin.property.XProp;
import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.StyleManager;
import java.awt.event.ActionEvent;
import javax.swing.JToolBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowToolBar extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(ShowToolBar.class);
   //
   private final AppManager appManager;

   public ShowToolBar(AppManager appManager) {
      super("ShowToolBar");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      logger.info("ShowToolBar");

      StyleManager styleManager = appManager.getStyleManager();
      styleManager.increaseFontSize(appManager.getArticle().getDocument());

      JToolBar toolBar = appManager.getMainFrame().getToolBar();
      toolBar.setVisible(!toolBar.isVisible());
      XProp.put("ToolBar.IsVisible", toolBar.isVisible());
      appManager.updateWidget("ShowToolBar", toolBar.isVisible());

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null);
   }
}
