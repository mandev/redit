package com.adlitteram.redit.draganddrop;

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
