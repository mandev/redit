package com.adlitteram.redit;

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
import com.adlitteram.imagetool.ImageInfo;
import com.adlitteram.imagetool.ImageUtils;
import com.adlitteram.imagetool.Imager;
import com.adlitteram.imagetool.XImage;
import com.adlitteram.imagetool.writer.jpeg.JpegImageWriter;
import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.gui.explorer.ExplorerModel;
import com.adlitteram.jasmin.gui.explorer.ImageFile;
import com.adlitteram.redit.gui.ArticlePane;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Caret;
import javax.swing.undo.UndoManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Article implements UndoableEditListener, DocumentListener, ListDataListener {

   private static final Logger logger = LoggerFactory.getLogger(Article.class);

   private REditorKit editorKit;
   private ArticlePane articlePane;
   private AppManager appManager;
   private UndoManager undoManager;
   private RDocument document;
   private ExplorerModel explorerModel;
   private ArticleMetadata metadata;
   private String path;
   private boolean isDirty;
   private boolean isBackupDirty;
   private Date creationDate;

   public Article(AppManager appManager) {
      this(appManager, null);
   }

   public Article(AppManager appManager, String path) {
      this.appManager = appManager;
      this.path = path;
      this.creationDate = new Date();

      editorKit = new REditorKit();
      undoManager = new UndoManager();
      explorerModel = new ExplorerModel();
      document = new RDocument(this);
      metadata = new ArticleMetadata(this);

      isDirty = false;
      isBackupDirty = false;
   }

   public void addListeners() {
      explorerModel.addListDataListener(this);
      document.addDocumentListener(this);
      document.addUndoableEditListener(this);
   }

   public void setArticlePane(ArticlePane articlePane) {
      this.articlePane = articlePane;
   }

   public ArticlePane getArticlePane() {
      return articlePane;
   }

   public AppManager getAppManager() {
      return appManager;
   }

   public ArticleMetadata getArticleMetadata() {
      return metadata;
   }

   public String getName() {
      return (path == null) ? Message.get("NewDocument") : FilenameUtils.getName(path);
   }

   public Date getCreationDate() {
      return creationDate;
   }

   public void setCreationDate(Date date) {
      creationDate = date;
   }

   public String getApplication() {
      return Version.getCNAME();
   }

   public String getVersion() {
      return Version.getVERSION();
   }

   public long getLength() {
      long length = document.getLength();
      for (int i = 0; i < explorerModel.size(); i++) {
         length += explorerModel.getImageFile(i).getLength();
      }
      return length;
   }

   /**
    * Messaged when the Document has created an edit, the edit is added to <code>undo</code>, an instance of
    * UndoManager.
    *
    * @param e
    */
   @Override
   public void undoableEditHappened(UndoableEditEvent e) {
      undoManager.addEdit(e.getEdit());
      appManager.getActionManager().enableActions();
   }

   @Override
   public void contentsChanged(ListDataEvent e) {
      //setDirty();
   }

   @Override
   public void intervalAdded(ListDataEvent e) {
      if (articlePane != null) {
         articlePane.setPicturePaneVisible(true);
      }
      setDirty();
   }

   @Override
   public void intervalRemoved(ListDataEvent e) {
      setDirty();
   }

   public void close() {
      document.removeUndoableEditListener(this);
      undoManager.discardAllEdits();
   }

   public UndoManager getUndoManager() {
      return undoManager;
   }

   public RDocument getDocument() {
      return document;
   }

   public boolean containsPicture(File file) {
      for (int i = 0; i < explorerModel.size(); i++) {
         if (file.equals(explorerModel.get(i))) {
            return true;
         }
      }
      return false;
   }

   public ImageFile addPicture(File file, String name) {
      ImageFile imageFile = null;
      ImageInfo info = Imager.readImageInfo(file);

      if (info != null && info.isValidImage() && !containsPicture(file)) {
         imageFile = new ImageFile(file, name);
         imageFile.setFormat(info.getFormat());
         imageFile.setHeight(info.getHeight());
         imageFile.setWidth(info.getWidth());

         explorerModel.addImageFile(imageFile);
      }
      return imageFile;
   }

   public ImageFile addPicture(File file) {
      ImageFile imageFile = null;
      ImageInfo info = Imager.readImageInfo(file);

      if (info != null && info.isValidImage() && !containsPicture(file)) {
         File tmpFile = null;
         try {
            tmpFile = File.createTempFile("redit_", ".jpg");
            tmpFile.deleteOnExit();

            if (ImageUtils.isJpeg(info.getFormat())) {
               FileUtils.copyFile(file, tmpFile);
            }
            else {
               convertToJpeg(file, tmpFile);
            }

            imageFile = new ImageFile(tmpFile, file.getName());
            imageFile.setFormat(info.getFormat());
            imageFile.setHeight(info.getHeight());
            imageFile.setWidth(info.getWidth());

            explorerModel.addImageFile(imageFile);
         }
         catch (IOException ex) {
            logger.warn("", ex);
            if (tmpFile != null && tmpFile.exists()) {
               tmpFile.delete();
            }
         }
      }
      return imageFile;
   }

   public void removePicture(File file) {
      for (int i = explorerModel.size() - 1; i >= 0; i--) {
         if (explorerModel.get(i).equals(file)) {
            explorerModel.remove(i);
            FileUtils.deleteQuietly(file);
         }
      }
   }

   private void convertToJpeg(File srcFile, File dstFile) throws IOException {
      try {
         XImage bi = Imager.readXImage(srcFile);
         JpegImageWriter.write(bi, dstFile);
      }
      catch (IOException | ImageReadException | ImageWriteException ex) {
         throw new IOException(ex);
      }
   }

   public ExplorerModel getExplorerModel() {
      return explorerModel;
   }

   public REditorKit getEditorKit() {
      return editorKit;
   }

   public Caret getCaret() {
      return (articlePane == null) ? null : articlePane.getCaret();
   }

   /**
    * @return the path
    */
   public String getPath() {
      return path;
   }

   /**
    * @param path the path to set
    */
   public void setPath(String path) {
      this.path = path;
      if (articlePane != null) {
         articlePane.setTitle(getName());
      }
   }

   public void setDirty() {
      isDirty = true;
      setBackupDirty();
   }

   public boolean isDirty() {
      return isDirty;
   }

   public void setClean() {
      isDirty = false;
   }

   private void setBackupDirty() {
      isBackupDirty = true;
   }

   public boolean isBackupDirty() {
      return isBackupDirty;
   }

   public void setBackupClean() {
      isBackupDirty = false;
   }

   @Override
   public void insertUpdate(DocumentEvent e) {
      setDirty();
   }

   @Override
   public void removeUpdate(DocumentEvent e) {
      setDirty();
   }

   @Override
   public void changedUpdate(DocumentEvent e) {
      setDirty();
   }
}
