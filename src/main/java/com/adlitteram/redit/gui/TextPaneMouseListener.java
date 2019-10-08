package com.adlitteram.redit.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.SwingUtilities;

public class TextPaneMouseListener implements MouseListener, MouseMotionListener, FocusListener {

   private final TextPane textPane;

   public TextPaneMouseListener(TextPane textPane) {
      this.textPane = textPane;
   }

   @Override
   public void focusGained(FocusEvent e) {
   }

   @Override
   public void focusLost(FocusEvent e) {
   }

   @Override
   public void mouseDragged(MouseEvent e) {
      if (SwingUtilities.isLeftMouseButton(e)) {
         textPane.updateCaret();
      }
   }

   @Override
   public void mousePressed(MouseEvent e) {
      if (SwingUtilities.isLeftMouseButton(e)) {
         textPane.updateCaret();
      }
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      if (SwingUtilities.isRightMouseButton(e)) {
         textPane.showPopup(e);
      }
   }

   @Override
   public void mouseMoved(MouseEvent e) {
   }

   @Override
   public void mouseClicked(MouseEvent e) {
   }

   @Override
   public void mouseEntered(MouseEvent e) {
   }

   @Override
   public void mouseExited(MouseEvent e) {
   }
}
