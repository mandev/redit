package com.adlitteram.redit.gui;

import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.action.ActionManager;
import com.adlitteram.jasmin.gui.explorer.ImageFile;
import com.adlitteram.imagetool.ImageInfo;
import com.adlitteram.imagetool.ImageUtils;
import com.adlitteram.imagetool.Imager;
import com.adlitteram.imagetool.ReadParam;
import com.adlitteram.jasmin.utils.GuiUtils;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.Main;
import com.adlitteram.redit.gui.dialog.ArticlePictureDialog;
import com.adlitteram.redit.gui.dialog.IptcDialog;
import cz.autel.dmi.HIGConstraints;
import cz.autel.dmi.HIGLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PictureViewer extends JFrame {

   private static final Logger logger = LoggerFactory.getLogger(PictureViewer.class);
   //
   private ActionManager actionManager;
   private final Article article;
   private final ArrayList<ImageFile> fileList;
   private final ImagePanel imagePanel;
   private BufferedImage image;
   private int index;
   private final int width;
   private final int height;
   private final int topMargin;

   public PictureViewer(ImageFile[] files, ImageFile file, Article article) {

      this.fileList = new ArrayList<>(Arrays.asList(files));
      this.article = article;
      this.index = (file == null) ? 0 : getIndex(file);

      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      topMargin = SystemUtils.IS_OS_MAC_OSX ? 25 : 0;
      width = screen.width;
      height = screen.height - topMargin;
      image = getPreview(width, height);

      initActions();

      imagePanel = new ImagePanel();
      imagePanel.setBackground(Color.LIGHT_GRAY);
      imagePanel.setOpaque(true);
      imagePanel.requestFocusInWindow();

      JPanel panel = new JPanel(new BorderLayout());
      panel.setBackground(Color.LIGHT_GRAY);
      panel.setOpaque(true);
      panel.setActionMap(actionManager.getActionMap());
      panel.setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, actionManager.getInputMap());

      JPanel topPanel = buildTopPanel();
      if (SystemUtils.IS_OS_MAC_OSX) {
         topPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
      }

      panel.add(topPanel, BorderLayout.NORTH);
      panel.add(imagePanel, BorderLayout.CENTER);

      setIconImages(MainFrame.ICONS);
      setUndecorated(true);
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      setContentPane(panel);
      setBounds(0, 0, width, height);
      toFront();
      setVisible(true);
   }

   private void initActions() {
      actionManager = new ActionManager();

      AbstractAction action = new AbstractAction("CloseViewer") {
         @Override
         public void actionPerformed(ActionEvent e) {
            close();
         }
      };
      action.putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
      actionManager.putAction(action);

      action = new AbstractAction("PrevPicture") {
         @Override
         public void actionPerformed(ActionEvent e) {
            backIndex();
            displayImage();
         }
      };
      action.putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
      actionManager.putAction(action);

      action = new AbstractAction("NextPicture") {
         @Override
         public void actionPerformed(ActionEvent e) {
            nextIndex();
            displayImage();
         }
      };
      action.putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
      actionManager.putAction(action);

      action = new AbstractAction("DelPicture") {
         @Override
         public void actionPerformed(ActionEvent e) {
            deletePicture();
         }
      };
      action.putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
      actionManager.putAction(action);

      action = new AbstractAction("EditPictureMeta") {
         @Override
         public void actionPerformed(ActionEvent e) {
            editPictureMeta();
         }
      };
      action.putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_M, 0));
      actionManager.putAction(action);

      action = new AbstractAction("EditPicture") {
         @Override
         public void actionPerformed(ActionEvent e) {
            editPicture();
         }
      };
      action.putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, 0));
      actionManager.putAction(action);
   }

   private JPanel buildTopPanel() {

      JButton delButton = new JButton(actionManager.getAction("DelPicture"));
      delButton.setText(Message.get("PictureViewer.DelPicture"));

      JButton prevButton = new JButton(actionManager.getAction("PrevPicture"));
      prevButton.setText(Message.get("PictureViewer.PrevPicture"));

      JButton nextButton = new JButton(actionManager.getAction("NextPicture"));
      nextButton.setText(Message.get("PictureViewer.NextPicture"));

      JButton editButton = new JButton(actionManager.getAction("EditPicture"));
      editButton.setText(Message.get("PictureViewer.EditPicture"));

      JButton metaButton = new JButton(actionManager.getAction("EditPictureMeta"));
      metaButton.setText(Message.get("PictureViewer.EditPictureMeta"));

      JButton closeButton = new JButton(actionManager.getAction("CloseViewer"));
      closeButton.setText(Message.get("PictureViewer.CloseViewer"));
      //getRootPane().setDefaultButton(closeButton);

      int w[] = {5, -12, 5, 0, 25, -8, 5, -6, 25, 0, 5, 0, 5, 0, 5};
      int h[] = {2, 0, 2};
      HIGLayout l = new HIGLayout(w, h);
      HIGConstraints c = new HIGConstraints();
      l.setColumnWeight(3, 1);
      l.setColumnWeight(13, 1);

      JPanel panel = new JPanel(l);
      panel.add(delButton, c.xy(4, 2));
      panel.add(prevButton, c.xy(6, 2));
      panel.add(nextButton, c.xy(8, 2));
      panel.add(metaButton, c.xy(10, 2));
      panel.add(editButton, c.xy(12, 2));
      panel.add(closeButton, c.xy(14, 2));
      return panel;
   }

   private int getIndex(ImageFile selectedFile) {
      for (int i = 0; i < fileList.size(); i++) {
         if (selectedFile == fileList.get(i)) {
            return i;
         }
      }
      return 0;
   }

   private void nextIndex() {
      index++;
      if (index >= fileList.size()) {
         index = 0;
      }
   }

   private void backIndex() {
      index--;
      if (index < 0) {
         index = fileList.size() - 1;
      }
   }

   public void displayImage() {
      image = getPreview(width, height);
      imagePanel.repaint();
   }

   public void editPictureMeta() {
      ImageFile[] files = new ImageFile[]{fileList.get(index)};
      ArticlePictureDialog.create(this, article, files);
   }

   public void editPicture() {
      File[] files = new File[]{fileList.get(index).getFile()};
      IptcDialog.create(this, article, files);
   }

   public void deletePicture() {
      ImageFile file = fileList.remove(index);
      if (file != null) {
         article.removePicture(file.getFile());
         index = Math.min(index, fileList.size() - 1);
         if (fileList.isEmpty()) {
            close();
         }
         else {
            displayImage();
         }
      }
   }

   public void close() {
      dispose();
      if (!fileList.isEmpty()) {
         Main.getMainFrame().getArticlePane().getExplorerPane().setSelectedFile(fileList.get(index).getFile());
      }
   }

   private BufferedImage getPreview(int w, int h) {
      GuiUtils.setCursorOnWait(this, true);
      BufferedImage img = null;
      File file = fileList.get(index).getFile();
      ImageInfo info = Imager.readImageInfo(file);
      if (info != null) {
         int sampling = Math.max(info.getWidth() / w, info.getHeight() / h) + 1;
         img = Imager.readImage(file, new ReadParam(sampling));
         if (img != null && (sampling > 1 || img.getWidth() > w || img.getHeight() > h)) {
            img = ImageUtils.getScaledRGBImage(img, w, h, true);
         }
      }
      GuiUtils.setCursorOnWait(this, false);
      return img;
   }

   class ImagePanel extends JPanel {

      @Override
      protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         if (image != null) {
            int x = (int) ((width - image.getWidth()) / 2f);
            int y = (int) ((height - image.getHeight()) / 2f);
            g.drawImage(image, x, y + topMargin, null);
         }
      }
   }
}
