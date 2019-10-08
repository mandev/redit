package com.adlitteram.redit.gui;

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
import com.adlitteram.redit.Article;
import com.adlitteram.redit.RDocument;
import com.adlitteram.redit.StyleManager;
import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.gui.explorer.FileListModel;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.Document;
import javax.swing.text.Style;

public class StatusBar extends JToolBar implements CaretListener, DocumentListener, ListDataListener {

   private static final int CHAR_PER_LINE = 34;
   //
   private final JLabel styleLabel;
   private final JLabel totalSizeLabel;
   private final JLabel selectionSizeLabel;
   private final JLabel pictureNumberLabel;

   public StatusBar() {
      //setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
      setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
      setFloatable(false);

      styleLabel = new JLabel();
      styleLabel.setPreferredSize(new Dimension(150, 18));

      totalSizeLabel = new JLabel();
      totalSizeLabel.setPreferredSize(new Dimension(90, 18));

      selectionSizeLabel = new JLabel();
      selectionSizeLabel.setPreferredSize(new Dimension(140, 18));

      pictureNumberLabel = new JLabel();
      pictureNumberLabel.setPreferredSize(new Dimension(80, 18));

      add(javax.swing.Box.createHorizontalStrut(5));
      add(styleLabel);
      add(totalSizeLabel);
      add(selectionSizeLabel);
      add(pictureNumberLabel);
      add(javax.swing.Box.createHorizontalGlue());
   }

//    @Override
//    public Component add(Component cmpt) {
//        return super.add(cmpt, FlowLayout.TRAILING) ;
//    }
   @Override
   public void caretUpdate(CaretEvent e) {
      TextPane textPane = (TextPane) e.getSource();
      Article article = textPane.getArticlePane().getArticle();
      updateStyleLabel(article);
      updateSelectionSizeLabel(article);
   }

   @Override
   public void changedUpdate(DocumentEvent e) {
      RDocument document = (RDocument) e.getDocument();
      updateStyleLabel(document.getArticle());
      updateTotalSizeLabel(document);
   }

   @Override
   public void insertUpdate(DocumentEvent e) {
      updateTotalSizeLabel(e.getDocument());
   }

   @Override
   public void removeUpdate(DocumentEvent e) {
      updateTotalSizeLabel(e.getDocument());
   }

   public void updateStyleLabel(Article article) {
      if (article == null) {
         styleLabel.setText(null);
      }
      else {
         RDocument document = article.getDocument();
         int pos = article.getCaret().getDot();

         Style logicalStyle = document.getLogicalStyle(pos);
         String styleName = (logicalStyle != null) ? "\u25BA" + logicalStyle.getName() : "";

         if (pos > 0) {
            pos--;
         }
         Style charStyle = StyleManager.getCharacterStyle(document, pos);
         if (charStyle != null) {
            styleName += "\u25BA" + charStyle.getName();
         }
         styleLabel.setText(styleName);
      }
   }

   public void updateTotalSizeLabel(Document document) {
      if (document == null) {
         totalSizeLabel.setText(null);
      }
      else {
         int totalSize = document.getLength();
         String text = (totalSize > 0) ? totalSize + " / " + ((int) (totalSize / CHAR_PER_LINE) + 1) : null;
         totalSizeLabel.setText(text);
      }
   }

   public void updateSelectionSizeLabel(Article article) {
      if (article == null) {
         selectionSizeLabel.setText(null);
      }
      else {
         int selectionSize = Math.abs(article.getCaret().getDot() - article.getCaret().getMark());
         String text = (selectionSize > 0) ? Message.get("StatusBar.Selection") + ": " + selectionSize + " / " + ((int) (selectionSize / 32) + 1) : null;
         selectionSizeLabel.setText(text);
      }
   }

   public void updatePictureNumberLabel(int number) {
      if (number == 0) {
         pictureNumberLabel.setText(null);
      }
      else {
         pictureNumberLabel.setText(Message.get("StatusBar.Pictures") + ": " + number);
      }
   }

   @Override
   public void intervalAdded(ListDataEvent e) {
      FileListModel plm = (FileListModel) e.getSource();
      updatePictureNumberLabel(plm.size());
   }

   @Override
   public void intervalRemoved(ListDataEvent e) {
      FileListModel plm = (FileListModel) e.getSource();
      updatePictureNumberLabel(plm.size());
   }

   @Override
   public void contentsChanged(ListDataEvent e) {
   }
}
