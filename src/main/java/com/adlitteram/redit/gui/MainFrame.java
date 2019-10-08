/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit.gui;

import com.adlitteram.jasmin.WidgetManager;
import com.adlitteram.jasmin.XProp;
import com.adlitteram.jasmin.gui.GuiBuilder;
import com.adlitteram.jasmin.utils.GuiUtils;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.Version;
import com.adlitteram.redit.gui.listener.MainframeListener;
import com.adlitteram.redit.gui.xml.XmlGuiReader;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

public final class MainFrame extends JFrame {

   public static final ArrayList<Image> ICONS = loadIconImages();
   //
   private final AppManager appManager;
   private final WidgetManager widgetManager;
   private final GuiBuilder guiBuilder;
   private final JMenuBar menubar;
   private final JToolBar toolBar;
   private ArticlePane articlePane;
   private final StatusBar statusBar;

   public MainFrame(AppManager appManager) {
      super(Version.getNAME() + " " + Version.getVERSION());

      this.appManager = appManager;
      appManager.setMainFrame(this);

      widgetManager = new WidgetManager(this);
      guiBuilder = new GuiBuilder(appManager.getActionManager());

      // Create Widgets
      XmlGuiReader guiReader = new XmlGuiReader(this);
      menubar = (JMenuBar) guiReader.read(XProp.getResource("menubar.xml"));

      toolBar = (JToolBar) guiReader.read(XProp.getResource("toolbar.xml"));
      toolBar.setVisible(XProp.getBoolean("ToolBar.IsVisible", true));

      statusBar = (StatusBar) guiReader.read(XProp.getResource("statusbar.xml"));
      statusBar.setVisible(XProp.getBoolean("StatusBar.IsVisible", true));

      // Layout Widgets
      setJMenuBar(menubar);
      getContentPane().add(toolBar, BorderLayout.NORTH);
      getContentPane().add(statusBar, BorderLayout.SOUTH);

      setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      addWindowListener(new MainframeListener(this));

      // Update Widgets
      updateWidget("ShowToolBar", toolBar.isVisible());
      updateWidget("ShowStatusBar", statusBar.isVisible());

      // Locate and display
      setIconImages(ICONS);
      loadProperties();
      setVisible(true);
   }

   public void loadProperties() {
      GuiUtils.loadBounds(this, "MainFrame");
   }

   public void saveProperties() {
      articlePane.saveProperties();
      GuiUtils.saveBounds(this, "MainFrame");
   }

   public Article getArticle() {
      return (articlePane == null) ? null : articlePane.getArticle();
   }

   public void addArticle(Article article) {
      if (articlePane != null) {
         articlePane.saveProperties();
         getContentPane().remove(articlePane);
      }
      articlePane = new ArticlePane(this, article);

      getContentPane().add(articlePane, BorderLayout.CENTER);
      setTitle(article.getName());
      validate();

      statusBar.updateStyleLabel(article);
      statusBar.updateTotalSizeLabel(article.getDocument());
      statusBar.updatePictureNumberLabel(article.getExplorerModel().size());

   }

   public ArticlePane getArticlePane() {
      return articlePane;
   }

   public StatusBar getStatusBar() {
      return statusBar;
   }

   public JToolBar getToolBar() {
      return toolBar;
   }

   public AppManager getAppManager() {
      return appManager;
   }

   public WidgetManager getWidgetManager() {
      return widgetManager;
   }

   public GuiBuilder getGuiBuilder() {
      return guiBuilder;
   }

   public void updateWidget(String action, Object value) {
      widgetManager.updateWidgets(action, value);
   }

   public void requestTextFocus() {
      if (articlePane != null) {
         articlePane.getTextPane().requestFocusInWindow();
      }
   }

   public void setBusy(boolean b) {
      setCursor(Cursor.getPredefinedCursor(b ? Cursor.WAIT_CURSOR : Cursor.DEFAULT_CURSOR));
      if (articlePane != null) {
         articlePane.setBusy(b);
      }
   }

   private static ArrayList<Image> loadIconImages() {
      ArrayList<Image> iconList = new ArrayList<>();
      iconList.add(GuiUtils.loadImage("resource/icon/" + Version.getNAME().toLowerCase() + "_16.png"));
      iconList.add(GuiUtils.loadImage("resource/icon/" + Version.getNAME().toLowerCase() + "_24.png"));
      iconList.add(GuiUtils.loadImage("resource/icon/" + Version.getNAME().toLowerCase() + "_32.png"));
      iconList.add(GuiUtils.loadImage("resource/icon/" + Version.getNAME().toLowerCase() + "_48.png"));
      iconList.add(GuiUtils.loadImage("resource/icon/" + Version.getNAME().toLowerCase() + "_64.png"));
      iconList.add(GuiUtils.loadImage("resource/icon/" + Version.getNAME().toLowerCase() + "_128.png"));
      return iconList;
   }
   // Update UI of linked components
//    public void updateComponentUI() {
//    }
}
