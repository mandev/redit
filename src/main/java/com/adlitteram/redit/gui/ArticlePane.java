/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit.gui;

import com.adlitteram.jasmin.XProp;
import com.adlitteram.jasmin.gui.GuiBuilder;
import com.adlitteram.jasmin.gui.explorer.ExplorerModel;
import com.adlitteram.jasmin.gui.explorer.ExplorerPane;
import com.adlitteram.jasmin.gui.explorer.ExplorerPopup;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.RDocument;
import com.adlitteram.redit.action.ShowPicture;
import com.adlitteram.redit.draganddrop.ArticlePanelTransHandler;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.text.Caret;

public class ArticlePane extends JPanel implements ListSelectionListener, CaretListener {

   private static final int DIVIDER_SIZE = 12;
   private Article article;
   private final MainFrame mainFrame;
   //
   private final JSplitPane splitPane;
   private final JTextPane textPane;
   private final ExplorerPane explorerPane;

   public ArticlePane(MainFrame mainFrame, Article article) {
      this.mainFrame = mainFrame;
      this.article = article;

      article.setArticlePane(this);
      article.addListeners();

      // Transfer Handler for drag & drop
      TransferHandler transHandler = new ArticlePanelTransHandler();

      // Picture Pane
      ExplorerModel pictureListModel = article.getExplorerModel();
      pictureListModel.addListDataListener(mainFrame.getStatusBar());

      explorerPane = new ExplorerPane(pictureListModel);
      explorerPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
      explorerPane.setInfoDetail(ExplorerPane.FULL_INFO);
      explorerPane.setTransferHandler(transHandler);
      explorerPane.addListSelectionListener(this);
      explorerPane.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
               ShowPicture.action(ArticlePane.this.article);
            }
         }
      });

      GuiBuilder guiBuilder = mainFrame.getGuiBuilder();
      JPopupMenu popupMenu = new JPopupMenu();
      popupMenu.add(guiBuilder.buildMenuItem("AddPicture"));
      popupMenu.add(guiBuilder.buildMenuItem("ShowPicture"));
      popupMenu.add(guiBuilder.buildMenuItem("EditPictureMeta"));
      popupMenu.add(guiBuilder.buildMenuItem("EditPicture"));
      popupMenu.add(new JSeparator());
      popupMenu.add(guiBuilder.buildMenuItem("DeletePicture"));
      popupMenu.add(new JSeparator());

      ExplorerPopup expMenu = new ExplorerPopup(explorerPane);
      expMenu.getFullScreenItem().setVisible(false);
      explorerPane.setPopupMenu(expMenu.addToPopupMenu(popupMenu));

      // Text Pane
      RDocument document = article.getDocument();
      document.addDocumentListener(mainFrame.getStatusBar());

      textPane = new TextPane(this, article.getEditorKit(), document);
      textPane.addCaretListener(mainFrame.getStatusBar());
      textPane.addCaretListener(this);

      JScrollPane scrollPane = new JScrollPane(textPane);
      scrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));

      // Split Pane
      splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, explorerPane);
      splitPane.setResizeWeight(1d);
      splitPane.setContinuousLayout(true);
      splitPane.setOneTouchExpandable(false);
      splitPane.setBorder(null);
      splitPane.setDividerSize(DIVIDER_SIZE);
      splitPane.putClientProperty("ExplorerPane", explorerPane);

      //((BasicSplitPaneUI) splitPane.getUI()).getDivider().setBorder(BorderFactory.createMatteBorder(1,0,1,0,Color.LIGHT_GRAY));
      ((BasicSplitPaneUI) splitPane.getUI()).getDivider().setBorder(null);
      splitPane.setTransferHandler(transHandler);

      setLayout(new BorderLayout());
      setBorder(BorderFactory.createEmptyBorder(1, 4, 4, 4));
      add(splitPane, BorderLayout.CENTER);

      setPicturePaneVisible(article.getExplorerModel().size() > 0);
   }

   public void saveProperties() {
      if (isDisplayable() && explorerPane.isVisible()) {
         XProp.put("PicturePaneHeight", explorerPane.getHeight());
      }
   }

   public void setBusy(boolean b) {
      if (b) {
         setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         textPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      }
      else {
         setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
         textPane.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
      }
   }

   public void setLock(boolean b) {
      textPane.setEnabled(!b);
      explorerPane.setEnabled(!b);
   }

   public void setPicturePaneVisible(boolean isVisible) {
      //if (picturePane.isVisible() == isVisible) return;

      if (isVisible) {
         splitPane.setDividerSize(DIVIDER_SIZE);
         splitPane.setDividerLocation(splitPane.getHeight() - splitPane.getDividerSize() - XProp.getInt("PicturePaneHeight", 80));
      }
      else {
         if (isDisplayable() && explorerPane.isVisible()) {
            XProp.put("PicturePaneHeight", explorerPane.getHeight());
         }
         splitPane.setDividerSize(0);
      }

      explorerPane.setVisible(isVisible);
      mainFrame.updateWidget("ShowPicturePane", isVisible);
      XProp.put("PicturePane.IsVisible", isVisible);
      revalidate();
   }

   public boolean isPicturePaneVisible() {
      return explorerPane.isVisible();
   }

   public Article getArticle() {
      return article;
   }

   public JTextPane getTextPane() {
      return textPane;
   }

   public ExplorerPane getExplorerPane() {
      return explorerPane;
   }

   public Caret getCaret() {
      return textPane.getCaret();
   }

   public void setTitle(String title) {
      mainFrame.setTitle(title);
   }

   @Override
   public void valueChanged(ListSelectionEvent e) {
      //if (!e.getValueIsAdjusting()) {
      mainFrame.getAppManager().getActionManager().enableActions();
      //}
   }

   @Override
   public void caretUpdate(CaretEvent e) {
      mainFrame.getAppManager().getActionManager().enableActions();
   }

   @Override
   public String toString() {
      StringBuilder toStringBuilder = new StringBuilder();
      toStringBuilder.append(super.toString());
      toStringBuilder.append("\n");
      toStringBuilder.append("\narticle: ");
      toStringBuilder.append(article);
      toStringBuilder.append("\nmainFrame: ");
      toStringBuilder.append(mainFrame);
      toStringBuilder.append("\nsplitPane: ");
      toStringBuilder.append(splitPane);
      toStringBuilder.append("\ntextPane: ");
      toStringBuilder.append(textPane);
      toStringBuilder.append("\npicturePane: ");
      toStringBuilder.append(explorerPane);
      toStringBuilder.append("\npictureList: ");
      toStringBuilder.append(explorerPane);
      return toStringBuilder.toString();
   }
}
