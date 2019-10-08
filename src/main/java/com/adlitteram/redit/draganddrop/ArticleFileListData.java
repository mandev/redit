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
import com.adlitteram.redit.Main;
import com.adlitteram.redit.action.OpenArticle;
import java.awt.datatransfer.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleFileListData implements Transferable, ClipboardOwner {

   private static final Logger logger = LoggerFactory.getLogger(ArticleFileListData.class);
   private static final DataFlavor FILE_LIST_FLAVOR = DataFlavor.javaFileListFlavor;
   //
   private final ArrayList<File> fileList;

   private ArticleFileListData(ArrayList<File> fileList) {
      this.fileList = fileList;
   }

   @Override
   public Object getTransferData(DataFlavor flavor) {
      return (FILE_LIST_FLAVOR.equals(flavor)) ? fileList : null;
   }

   @Override
   public DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[]{FILE_LIST_FLAVOR};
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor flavor) {
      return (FILE_LIST_FLAVOR.equals(flavor));
   }

   @Override
   public void lostOwnership(Clipboard clipboard, Transferable contents) {
   }

   public static boolean canImport(JComponent comp, DataFlavor flavors[]) {
      for (DataFlavor flavor : flavors) {
         if (FILE_LIST_FLAVOR.equals(flavor)) {
            return true;
         }
      }
      return false;
   }

   // Copy/Export from component
   protected static Transferable createTransferable(JComponent comp) {
      return null;
   }

   // Paste/drop
   public static boolean importData(JComponent comp, Transferable t) {
      if (t.isDataFlavorSupported(FILE_LIST_FLAVOR)) {
         return importArticle(t);
      }
      return false;
   }

   private static boolean importArticle(Transferable t) {
      try {
         List<File> fileList = (List<File>) t.getTransferData(FILE_LIST_FLAVOR);
         File[] files = fileList.toArray(new File[fileList.size()]);
         if (files.length > 0) {
            OpenArticle.action(Main.getAppManager(), files[0], true);
         }
         return true;
      }
      catch (UnsupportedFlavorException ex) {
         logger.warn("The object flavor is not supported", ex.getMessage());
      }
      catch (IOException ex) {
         logger.warn("Error while importing the object", ex.getMessage());
      }
      return false;
   }
}
