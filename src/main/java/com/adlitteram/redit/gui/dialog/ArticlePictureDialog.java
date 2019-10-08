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
import com.adlitteram.jasmin.gui.explorer.ImageFile;
import com.adlitteram.jasmin.utils.GuiUtils;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.PictureMetadata;
import cz.autel.dmi.HIGConstraints;
import cz.autel.dmi.HIGLayout;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticlePictureDialog extends JDialog {

   private static final Logger logger = LoggerFactory.getLogger(ArticlePictureDialog.class);
   //
   private final Article article;
   private final ImageFile[] files;
   private JTextField creditField;
   private JTextArea captionArea;

   public static void create(Frame frame, Article article, ImageFile[] files) {
      ArticlePictureDialog dialog = new ArticlePictureDialog(frame, article, files);
      dialog.setVisible(true);
   }

   private ArticlePictureDialog(Frame frame, Article article, ImageFile[] files) {
      super(frame, Message.get("ArticlePictureDialog.Title"), true);
      this.article = article;
      this.files = files;

      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      getContentPane().add(buildPicturePanel(), BorderLayout.CENTER);
      getContentPane().add(buildButtonPanel(), BorderLayout.SOUTH);

      pack();
      setLocationRelativeTo(getParent());
   }

   private JPanel buildButtonPanel() {
      JButton okButton = new JButton(Message.get("Ok"));
      getRootPane().setDefaultButton(okButton);
      okButton.addActionListener((ActionEvent e) -> okPressed());

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

   private JPanel buildPicturePanel() {
      creditField = new JTextField(35);
      creditField.setText(getCredit());

      captionArea = new JTextArea(10, 10);
      captionArea.setText(getCaption());
      GuiUtils.invertFocusTraversalBehaviour(captionArea);
      captionArea.setLineWrap(true);
      captionArea.setWrapStyleWord(true);

      int w[] = {5, 0, 5, 0, 5};
      int h[] = {10, 0, 5, 0, 10};
      HIGLayout l = new HIGLayout(w, h);
      HIGConstraints c = new HIGConstraints();
      l.setColumnWeight(4, 1);

      JPanel panel = new JPanel(l);
      panel.add(new JLabel(Message.get("ArticlePictureDialog.Credit")), c.xy(2, 2, "r"));
      panel.add(creditField, c.xy(4, 2, "lr"));

      panel.add(new JLabel(Message.get("ArticlePictureDialog.Caption")), c.xy(2, 4, "r"));
      panel.add(new JScrollPane(captionArea), c.xy(4, 4, "lr"));
      return panel;
   }

   private String getCredit() {
      String str = null;
      for (ImageFile file : files) {
         PictureMetadata meta = (PictureMetadata) file.getProperties();
         if (meta != null) {
            if (str == null) {
               str = meta.getCredit();
            }
            else if (!str.equals(meta.getCredit())) {
               str = "";
            }
         }
         else {
            str = "";
         }
      }
      return str;
   }

   private String getCaption() {
      String str = null;
      for (ImageFile file : files) {
         PictureMetadata meta = (PictureMetadata) file.getProperties();
         if (meta != null) {
            if (str == null) {
               str = meta.getCaption();
            }
            else if (!str.equals(meta.getCaption())) {
               str = "";
            }
         }
         else {
            str = "";
         }
      }
      return str;
   }

   private void cancelPressed() {
      dispose();
   }

   public void okPressed() {
      for (ImageFile imageFile : files) {
         PictureMetadata meta = (PictureMetadata) imageFile.getProperties();
         if (meta == null) {
            meta = new PictureMetadata();
            imageFile.setProperties(meta);
         }
         meta.setCredit(creditField.getText());
         meta.setCaption(captionArea.getText());
      }

      article.setDirty();
      dispose();
   }
}
