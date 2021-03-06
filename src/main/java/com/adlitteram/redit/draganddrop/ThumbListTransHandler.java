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
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class ThumbListTransHandler extends TransferHandler {

   public ThumbListTransHandler() {
      super();
   }

   @Override
   public int getSourceActions(JComponent c) {
      return COPY_OR_MOVE;
   }

   // Called after a drag was generated by the drawing area 
   @Override
   protected void exportDone(JComponent comp, Transferable t, int action) {
   }

   @Override
   public boolean canImport(JComponent comp, DataFlavor flavors[]) {
      return false;
   }

   // Copy
   @Override
   protected Transferable createTransferable(JComponent comp) {
      return ImageFileListData.createTransferable(comp);
   }

   // Paste/drop
   @Override
   public boolean importData(JComponent comp, Transferable t) {
      return false;
   }
}
