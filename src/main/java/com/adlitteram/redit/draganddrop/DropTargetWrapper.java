package com.adlitteram.redit.draganddrop;

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
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class DropTargetWrapper extends DropTarget {

   private final JComponent component;
   private final DropTarget dropTarget;
   private final TransferHandler transHandler;

   // cmpt accepts the additionnal flavors
   public DropTargetWrapper(JComponent cmpt, DropTarget dt) {
      this.component = cmpt;
      this.dropTarget = dt;
      this.transHandler = cmpt.getTransferHandler();
   }

   @Override
   public void dragEnter(DropTargetDragEvent e) {
      //System.err.println("Drag Enter");

      if (transHandler.canImport(component, e.getCurrentDataFlavors())) {
         e.acceptDrag(e.getDropAction());
      }
      else {
         dropTarget.dragEnter(e);
      }
   }

   @Override
   public void dragOver(DropTargetDragEvent e) {
      //System.err.println("Drag Over");

      if (transHandler.canImport(component, e.getCurrentDataFlavors())) {
         e.acceptDrag(e.getDropAction());
      }
      else {
         dropTarget.dragOver(e);
      }
   }

   @Override
   public void drop(DropTargetDropEvent e) {
      //System.err.println("Drop");

      if (transHandler.canImport(component, e.getCurrentDataFlavors())) {
         e.acceptDrop(e.getDropAction());
         Transferable t = e.getTransferable();
         if (t != null) {
            e.dropComplete(transHandler.importData(component, t));
         }
      }
      else {
         dropTarget.drop(e);
      }
   }
}
