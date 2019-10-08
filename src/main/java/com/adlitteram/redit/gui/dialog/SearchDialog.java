package com.adlitteram.redit.gui.dialog;

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
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import cz.autel.dmi.HIGConstraints;
import cz.autel.dmi.HIGLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.CompoundBorder;

public class SearchDialog extends AbstractSearchDialog {

   public SearchDialog(AppManager appManager) {
      super(appManager, Message.get("SearchDialog.Title"));
   }

   @Override
   protected JPanel buildButtonPanel() {

      JButton searchButton = new JButton(Message.get("SearchDialog.FindNext"));
      getRootPane().setDefaultButton(searchButton);
      searchButton.addActionListener((ActionEvent e) -> {
         findPressed();
      });

      JButton cancelButton = new JButton(Message.get("Close"));
      cancelButton.addActionListener((ActionEvent e) -> {
         cancelPressed();
      });

      int w[] = {5, 0, 25, 0, -7, 5, -5, 6};
      int h[] = {5, 0, 10};
      HIGLayout l = new HIGLayout(w, h);
      HIGConstraints c = new HIGConstraints();
      l.setColumnWeight(4, 1);

      JPanel panel = new JPanel(l);
      panel.add(searchButton, c.xy(5, 2));
      panel.add(cancelButton, c.xy(7, 2));
      return panel;
   }

   @Override
   protected JPanel buildGeneralPanel() {

      Article article = appManager.getArticle();
      String str = (article == null || article.getArticlePane() == null) ? "" : article.getArticlePane().getTextPane().getSelectedText();
      searchField = new JTextField(str, 30);

      int w0[] = {5, 0, 5, 0, 5};
      int h0[] = {5, 0, 5};
      HIGLayout l0 = new HIGLayout(w0, h0);
      HIGConstraints c0 = new HIGConstraints();
      l0.setColumnWeight(4, 1);

      JPanel p0 = new JPanel(l0);
      p0.add(new JLabel(Message.get("SearchDialog.SearchPattern")), c0.xy(2, 2, "r"));
      p0.add(searchField, c0.xy(4, 2));

      matchCaseCheck = new JCheckBox(Message.get("SearchDialog.MatchCase"));
      matchCaseCheck.setSelected(XProp.getBoolean("Search.MatchCase", false));

      wholeWordCheck = new JCheckBox(Message.get("SearchDialog.WholeWord"));
      wholeWordCheck.setSelected(XProp.getBoolean("Search.WholeWord", false));

      regexpCheck = new JCheckBox(Message.get("SearchDialog.Regexp"));
      regexpCheck.setSelected(XProp.getBoolean("Search.Regexp", false));

      backwardCheck = new JCheckBox(Message.get("SearchDialog.Backward"));
      backwardCheck.setSelected(!XProp.getBoolean("Search.Forward", true));

      int w1[] = {5, 0, 5, 0, 5};
      int h1[] = {0, 0, 0, 0, 5};
      HIGLayout l1 = new HIGLayout(w1, h1);
      HIGConstraints c1 = new HIGConstraints();

      JPanel p1 = new JPanel(l1);
      p1.add(matchCaseCheck, c1.xy(2, 2, "l"));
      p1.add(regexpCheck, c1.xy(4, 2, "l"));
      p1.add(wholeWordCheck, c1.xy(2, 4, "l"));
      p1.add(backwardCheck, c1.xy(4, 4, "l"));

      int w[] = {5, 0, 5};
      int h[] = {5, 0, 0, 0, 5};
      HIGLayout l = new HIGLayout(w, h);
      HIGConstraints c = new HIGConstraints();
      l.setColumnWeight(2, 1);

      JPanel panel = new JPanel(l);
      panel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createEtchedBorder()));
      panel.add(p0, c.xy(2, 2, "lr"));
      panel.add(p1, c.xy(2, 4, "r"));
      return panel;
   }

   @Override
   protected void cancelPressed() {
      XProp.put("Search.SearchPattern", searchField.getText());
      XProp.put("Search.MatchCase", matchCaseCheck.isSelected());
      XProp.put("Search.Forward", !backwardCheck.isSelected());
      XProp.put("Search.WholeWord", wholeWordCheck.isSelected());
      XProp.put("Search.Regexp", regexpCheck.isSelected());
      super.cancelPressed();
   }
}
