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
