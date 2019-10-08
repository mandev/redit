/*
 * IptcDialog.java
 *
 * Created on 9 avril 2005, 20:39
 */
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
import com.adlitteram.redit.Article;
import com.adlitteram.redit.ArticleMetadata;
import com.adlitteram.redit.gui.panel.GeneralPropertiesPanel;
import com.adlitteram.redit.gui.panel.SystemPropertiesPanel;
import com.adlitteram.redit.gui.panel.WebPropertiesPanel;
import cz.autel.dmi.HIGConstraints;
import cz.autel.dmi.HIGLayout;
import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticlePropertiesDialog extends JDialog {

   private static final Logger logger = LoggerFactory.getLogger(ArticlePropertiesDialog.class);
   //
   private final Article article;
   private final ArticleMetadata meta;
   //
   private GeneralPropertiesPanel generalPanel;
   private WebPropertiesPanel webPanel;
   private SystemPropertiesPanel sysPanel;
   private JTabbedPane tabbedPane;

   public static void create(Frame frame, Article article) {
      ArticlePropertiesDialog dialog = new ArticlePropertiesDialog(frame, article);
      dialog.setVisible(true);
   }

   private ArticlePropertiesDialog(Frame frame, Article article) {
      super(frame, Message.get("ArticleMetadataDialog.Title"), true);
      this.article = article;
      this.meta = article.getArticleMetadata();

      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      getContentPane().add(buildTabbedPane(), BorderLayout.CENTER);
      getContentPane().add(buildButtonPanel(), BorderLayout.SOUTH);

      pack();
      setLocationRelativeTo(getParent());
   }

   private JPanel buildButtonPanel() {
      JButton okButton = new JButton(Message.get("Ok"));
      getRootPane().setDefaultButton(okButton);
      okButton.addActionListener(e -> okPressed());

      JButton cancelButton = new JButton(Message.get("Cancel"));
      cancelButton.addActionListener(e -> cancelPressed());

      int w0[] = {5, 0, 5, -6, 5, -4, 5};
      int h0[] = {5, 0, 10};
      HIGLayout l0 = new HIGLayout(w0, h0);
      HIGConstraints c0 = new HIGConstraints();
      l0.setColumnWeight(3, 1);

      JPanel buttonPanel = new JPanel(l0);
      buttonPanel.add(okButton, c0.xy(4, 2));
      buttonPanel.add(cancelButton, c0.xy(6, 2));
      return buttonPanel;
   }

   private JTabbedPane buildTabbedPane() {

      sysPanel = new SystemPropertiesPanel(meta);
      generalPanel = new GeneralPropertiesPanel(meta);
      webPanel = new WebPropertiesPanel(meta);

      tabbedPane = new JTabbedPane();
      tabbedPane.add(Message.get("ArticleMetadataDialog.SystemProperties"), sysPanel);
      tabbedPane.add(Message.get("ArticleMetadataDialog.GeneralProperties"), generalPanel);
      tabbedPane.add(Message.get("ArticleMetadataDialog.WebProperties"), webPanel);
      return tabbedPane;
   }

   private void cancelPressed() {
      dispose();
   }

   public void okPressed() {
      meta.setAuthor(generalPanel.getAuthor());
      meta.setCountry(generalPanel.getCountry());
      meta.setCity(generalPanel.getCity());
      meta.setAddress(generalPanel.getAddress());
      meta.setKeyword(generalPanel.getKeyword());

      meta.setType(webPanel.getType());
      meta.setWebProfile(webPanel.getWebProfile());
      meta.setWebComment(webPanel.getWebComment());
      meta.setWebPosition(webPanel.getWebPosition());

      article.setDirty();
      dispose();
   }
}
