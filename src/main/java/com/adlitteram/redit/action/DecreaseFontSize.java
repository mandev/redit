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
import com.adlitteram.redit.Article;
import com.adlitteram.redit.StyleManager;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecreaseFontSize extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(DecreaseFontSize.class);

    public static void action(final AppManager appManager) {
        logger.info("DecreaseFontSize");
        StyleManager styleManager = appManager.getStyleManager();
        final Article article = appManager.getArticle();
        boolean isDirty = article.isDirty();
        styleManager.decreaseFontSize(article.getDocument());
        // decreaseFontSize is invoked later on on the EDT
        if (!isDirty) {
            SwingUtilities.invokeLater(() -> {
                article.setClean();
                appManager.getMainFrame().requestTextFocus();
                appManager.getActionManager().enableActions();
            });
        }
    }

   private final AppManager appManager;

   public DecreaseFontSize(AppManager appManager) {
      super("DecreaseFontSize");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager);
   }


   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null);
   }
}
