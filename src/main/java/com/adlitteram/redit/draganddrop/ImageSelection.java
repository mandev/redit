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
import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;

public class ImageSelection implements Transferable, ClipboardOwner {

   private static final DataFlavor flavors[] = {DataFlavor.imageFlavor};
   private final Image image;

   public ImageSelection(Image img) {
      image = img;
   }

   @Override
   public Object getTransferData(DataFlavor flavor) {
      return (flavors[0].equals(flavor)) ? image : null;
   }

   @Override
   public DataFlavor[] getTransferDataFlavors() {
      return flavors;
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor flavor) {
      return flavors[0].equals(flavor);
   }

   @Override
   public void lostOwnership(Clipboard clipboard, Transferable contents) {
   }

   // Export Data from the component
   public static Transferable createTransferable(JComponent comp) {
//        ImagePanel imagePanel = (ImagePanel) comp ;
//        AppManager imgManager = imagePanel.getImageManager() ;
//        Image image = imgManager.getImage() ;
//        return ( image == null ) ? null : new ImageSelection(image) ;
      return null;
   }

   // Paste Data to the component
   public static boolean importData(JComponent comp, Transferable t) {
//        ImagePanel imagePanel = (ImagePanel) comp ;
//        AppManager imgManager = imagePanel.getImageManager() ;
//
//        System.err.println("ImageSelection.importData");
//
//        if( t.isDataFlavorSupported(flavors[0]) ) {
//            try {
//                Image image = (Image) t.getTransferData(flavors[0]) ;
//                //if ( image != null ) imgManager.pasteImage(image) ;
//                return true;
//            } catch (Exception e) {
//                System.err.println(e) ;
//            }
//        }
      return false;
   }

   // Return the import capabilitiy
   public static boolean canImport(JComponent comp, DataFlavor flav[]) {
      for (int i = 0, n = flav.length; i < n; i++) {
         if (flavors[0].equals(flav[i])) {
            return true;
         }
      }
      return false;
   }
}
