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
import com.adlitteram.jasmin.gui.explorer.ExplorerPane;
import com.adlitteram.jasmin.gui.explorer.ExplorerView;
import com.adlitteram.jasmin.gui.explorer.ImageFile;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.Main;
import com.adlitteram.redit.action.ShowPicturePane;
import com.adlitteram.redit.gui.ArticlePane;
import java.awt.datatransfer.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageFileListData implements Transferable, ClipboardOwner {

   private static final Logger logger = LoggerFactory.getLogger(ImageFileListData.class);
   private static final DataFlavor fileListFlavor = DataFlavor.javaFileListFlavor;
   //
   private final List<File> fileList;

   private ImageFileListData(List<File> fileList) {
      this.fileList = fileList;
   }

   @Override
   public Object getTransferData(DataFlavor flavor) {
      return (fileListFlavor.equals(flavor)) ? fileList : null;
   }

   @Override
   public DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[]{fileListFlavor};
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor flavor) {
      return (fileListFlavor.equals(flavor));
   }

   @Override
   public void lostOwnership(Clipboard clipboard, Transferable contents) {
   }

   public static boolean canImport(JComponent comp, DataFlavor flavors[]) {
      for (DataFlavor flavor : flavors) {
         if (fileListFlavor.equals(flavor)) {
            return true;
         }
      }
      return false;
   }

   // Copy/Export from component
   protected static Transferable createTransferable(JComponent comp) {
      if (comp instanceof ExplorerView) {
         ExplorerView explorerView = (ExplorerView) comp;
         ExplorerPane explorerPane = explorerView.getExplorerPane();
         File[] values = explorerPane.getSelectedFiles();
         if (values != null && values.length > 0) {
            return new ImageFileListData(Arrays.asList(values));
         }
      }
      return null;

   }

   // Paste/drop
   public static boolean importData(JComponent comp, Transferable t) {
      return t.isDataFlavorSupported(fileListFlavor) ? importPictures(t) : false;
   }

   private static boolean importPictures(Transferable t) {
      try {
         List<File> fileList = (List<File>) t.getTransferData(fileListFlavor);
         File[] files = fileList.toArray(new File[fileList.size()]);

         Article article = Main.getAppManager().getArticle();
         ArticlePane articlePane = article.getArticlePane();

         for (File file : files) {
            if (file.isFile() && file.canRead()) {
               ImageFile picture = article.addPicture(file);
               if (picture != null && articlePane != null) {
                  ExplorerPane pictureList = articlePane.getExplorerPane();
                  pictureList.setSelectedImageFile(picture);
               }
            }
         }
         if (!fileList.isEmpty()) {
            // TODO: Sort pictures
            ShowPicturePane.action(articlePane, true);
         }
         return true;
      }
      catch (UnsupportedFlavorException | IOException ex) {
         logger.warn("", ex);
      }
      return false;
   }
}
