package com.adlitteram.redit.draganddrop;

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
