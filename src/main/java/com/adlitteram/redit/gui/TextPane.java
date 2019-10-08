/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit.gui;

import com.adlitteram.jasmin.gui.GuiBuilder;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.RDocument;
import com.adlitteram.redit.REditorKit;
import com.adlitteram.redit.draganddrop.DropTargetWrapper;
import com.adlitteram.redit.gui.listener.ActionKeyListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.text.DefaultEditorKit;

public class TextPane extends JTextPane {

   private final ArticlePane articlePane;

   public TextPane(ArticlePane articlePane, REditorKit editorKit, RDocument document) {
      super();
      this.articlePane = articlePane;

      setDragEnabled(true);
      setDropMode(DropMode.INSERT);
      setEditorKit(editorKit);
      setDocument(document);

      TextPaneMouseListener mouseListener = new TextPaneMouseListener(this);
      addMouseListener(mouseListener);
      addMouseMotionListener(mouseListener);
      addKeyListener(new ActionKeyListener(articlePane.getArticle().getAppManager()));

      // Stupid unix like backspace keybinding
      removeKeyBinding(KeyStroke.getKeyStroke("ctrl H"));

      // First horrible hack : fake the SelectLineAction to perform a SelectParagraphAction
      Action selectParagraphAction = getActionMap().get(DefaultEditorKit.selectParagraphAction);
      getActionMap().put(DefaultEditorKit.selectLineAction, selectParagraphAction);

      // Second horrible hack : wrap the current drop Target to allow more flavors
      setDropTarget(new DropTargetWrapper(articlePane.getExplorerPane(), getDropTarget()));
   }

   private void removeKeyBinding(KeyStroke ks) {
      for (InputMap im = getInputMap(); im != null; im = im.getParent()) {
         im.remove(ks);
      }
   }

   public ArticlePane getArticlePane() {
      return articlePane;
   }

   protected JPopupMenu createPopupMenu() {
      AppManager appManager = articlePane.getArticle().getAppManager();
      GuiBuilder guiBuilder = appManager.getMainFrame().getGuiBuilder();

      JPopupMenu menu = new JPopupMenu();
      menu.add(guiBuilder.buildMenuItem("Copy"));
      menu.add(guiBuilder.buildMenuItem("Cut"));
      menu.add(guiBuilder.buildMenuItem("Paste"));

      return menu;
   }

   public void showPopup(MouseEvent e) {
      JPopupMenu popupMenu = createPopupMenu();
      if (popupMenu != null) {
         popupMenu.show(e.getComponent(), e.getX(), e.getY());
      }
   }

   public void updateCaret() {
      CaretEvent event = new CaretEvent(this) {
         @Override
         public int getDot() {
            return ((TextPane) source).getCaret().getDot();
         }

         @Override
         public int getMark() {
            return ((TextPane) source).getCaret().getMark();
         }
      };

      super.fireCaretUpdate(event);
   }

   @Override
   public void paintComponent(Graphics g) {
      if (g instanceof Graphics2D) {
         Graphics2D g2d = (Graphics2D) g;
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
         g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
      }
      super.paintComponent(g);
   }
}
