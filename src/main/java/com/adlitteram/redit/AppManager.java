/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit;

import com.adlitteram.jasmin.XProp;
import com.adlitteram.jasmin.action.ActionManager;
import com.adlitteram.redit.gui.ArticlePane;
import com.adlitteram.redit.gui.MainFrame;
import org.apache.commons.lang3.time.FastDateFormat;

public class AppManager {

   public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
   public static final FastDateFormat DATE_FORMATTER = FastDateFormat.getInstance(DATE_FORMAT);

   private final ActionManager actionManager;    // Controller
   private final StyleManager styleManager;
   private final UploadManager remoteManager;
   private final RecentFilesManager recentFilesManager;

   private User user;
   private MainFrame mainFrame;

   public AppManager() {

      user = new User();
      user.setUserName(XProp.get("UserName"));
      user.setPassword(XProp.get("UserPasswd"));
      user.setColumnName(XProp.get("UserColumn"));

      remoteManager = new UploadManager(this);
      styleManager = new StyleManager(this);
      actionManager = new ActionBuilder(this);
      recentFilesManager = new RecentFilesManager(this);
   }

   public UploadManager getRemoteManager() {
      return remoteManager;
   }

   public ActionManager getActionManager() {
      return actionManager;
   }

   public StyleManager getStyleManager() {
      return styleManager;
   }

   public RecentFilesManager getRecentFilesManager() {
      return recentFilesManager;
   }

   public void setMainFrame(MainFrame mainFrame) {
      this.mainFrame = mainFrame;
   }

   public MainFrame getMainFrame() {
      return mainFrame;
   }

   public void updateWidget(String action, Object value) {
      if (mainFrame != null) {
         mainFrame.updateWidget(action, value);
      }
   }

   public ArticlePane getArticlePane() {
      return (mainFrame != null) ? mainFrame.getArticlePane() : null;
   }

   public Article getArticle() {
      return (mainFrame != null) ? mainFrame.getArticle() : null;
   }

   public void addArticle(Article article) {
      if (mainFrame != null) {
         mainFrame.addArticle(article);
      }
   }

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }

   public void saveProperties() {
      if (mainFrame != null) {
         mainFrame.saveProperties();
      }
      recentFilesManager.saveProperties();

      if (user != null) {
         XProp.put("UserName", user.getUserName());
         XProp.put("UserPasswd", user.getPassword());
         XProp.put("UserColumn", user.getColumnName());
      }

   }
}
