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
import com.adlitteram.jasmin.utils.GuiUtils;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Main;
import com.adlitteram.redit.search.SearchEngine;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchNext extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(SearchNext.class);
   //
   private final AppManager appManager;

   public SearchNext(AppManager appManager) {
      super("SearchNext");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager);

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(AppManager appManager) {
      logger.info("SearchNext");

      JTextPane textPane = appManager.getArticlePane().getTextPane();

      String pattern = XProp.get("Search.SearchPattern", null);
      if (pattern != null && pattern.length() > 0) {

         boolean forward = XProp.getBoolean("Search.Forward", true);
         boolean matchCase = XProp.getBoolean("Search.MatchCase", false);
         boolean wholeWord = XProp.getBoolean("Search.WholeWord", false);
         boolean regexp = XProp.getBoolean("Search.Regexp", false);

         if (!SearchEngine.find(textPane, pattern, forward, matchCase, wholeWord, regexp)) {
            if ((!forward && textPane.getCaret().getDot() == textPane.getDocument().getLength())
                    || (forward && textPane.getCaret().getDot() == 0)) {
               GuiUtils.showMessage(Message.get("Search.NotFound") + " : " + pattern);
            }
            else if (JOptionPane.showOptionDialog(Main.getApplication().getMainFrame(), Message.get("Search.Continue"),
                    Message.get("Search.EndOfText"), JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE, null, null, null) == JOptionPane.OK_OPTION) {

               int oldDot = textPane.getCaret().getDot();
               textPane.getCaret().setDot(forward ? 0 : textPane.getDocument().getLength());
               if (!SearchEngine.find(textPane, pattern, forward, matchCase, wholeWord, regexp)) {
                  textPane.getCaret().setDot(oldDot);
                  GuiUtils.showMessage(Message.get("Search.NotFound") + " : " + pattern);
               }
            }
         }
      }
   }

   @Override
   public void enable() {
      //logger.info("enable");

      String pattern = XProp.get("Search.SearchPattern", null);

      setEnabled(pattern != null && pattern.length() > 0
              && appManager.getArticlePane() != null
              && appManager.getArticlePane().getTextPane().getDocument().getLength() > 0);
   }
}
