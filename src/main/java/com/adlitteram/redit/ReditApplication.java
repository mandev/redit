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
import com.adlitteram.jasmin.Application;
import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.property.XProp;
import com.adlitteram.jasmin.log.XLog;
import com.adlitteram.jasmin.utils.GuiUtils;
import com.adlitteram.jasmin.utils.PlatformUtils;
import com.adlitteram.redit.action.OpenArticle;
import com.adlitteram.redit.gui.MainFrame;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import javax.swing.SwingUtilities;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReditApplication extends Application {

   private static final Logger logger = LoggerFactory.getLogger(ReditApplication.class);

   public static final String HOME_DIR = PlatformUtils.getHomeDir();
   public static final String PROG_DIR = PlatformUtils.getProgDir(Main.class);

   // Program confiduration files
   public static final String CONF_DIR = PROG_DIR + "config" + File.separator;
   public static final String LANG_DIR = CONF_DIR + "locales" + File.separator;         // Locales Messages
   public static final String DICT_DIR = CONF_DIR + "dictionaries" + File.separator;    // Dictionary

   // User's configuration files
   public static final String USER_CONF_DIR = HOME_DIR + "." + "rEdit" + File.separator;
   public static final String USER_LOG_DIR = USER_CONF_DIR + "log" + File.separator;
   public static final String USER_BCK_DIR = USER_CONF_DIR + "backup" + File.separator;
   public static final String USER_FILE = USER_CONF_DIR + "user.xml";
   public static final String PROP_FILE = USER_CONF_DIR + "props.xml";
   public static final String COUNT_FILE = USER_CONF_DIR + "counter";

   private MainFrame mainFrame;
   private AppManager appManager;
   private final String[] arguments;

   public ReditApplication(String[] arguments) {
      this.arguments = arguments;
   }

   public AppManager getAppManager() {
      return appManager;
   }

   public String[] getArguments() {
      return arguments;
   }

   // Init Directories
   @Override
   public void init() {
      super.init();

      System.setProperty("swing.aatext", "true");
      System.setProperty("swing.boldMetal", "false");

      if (SystemUtils.IS_OS_MAC_OSX) {
         if (XProp.get("FileChooser.IsNative") == null) {
            XProp.put("FileChooser.IsNative", true);
         }
         if (XProp.get("DirChooser.IsNative") == null) {
            XProp.put("DirChooser.IsNative", true);
         }
      }

      File dir = new File(USER_CONF_DIR);
      if (!dir.exists()) {
         dir.mkdirs();
      }

      dir = new File(USER_LOG_DIR);
      if (!dir.exists()) {
         dir.mkdirs();
      }

      dir = new File(USER_BCK_DIR);
      if (!dir.exists()) {
         dir.mkdirs();
      }
   }

   // Create the MainFrame - Schedule a job for the event-dispatching thread:
   public void start() {

      SwingUtilities.invokeLater(() -> {
         appManager = new AppManager();                  // Model
         logger.info("Launching MainFrame");
         mainFrame = new MainFrame(appManager);          // View
         appManager.addArticle(new Article(appManager));

         if (arguments != null && arguments.length > 0) {
            try {
               OpenArticle.action(appManager, new File(arguments[0]), false);
            }
            catch (IOException ex) {
               logger.warn("", ex);
               GuiUtils.showError(Message.get("OpenArticle.Error") + "\n" + ex.getLocalizedMessage());
            }
         }

         appManager.getActionManager().enableActions();
         BackupThread.startBackup();
      });
   }

   public void quit() {
      mainFrame.saveProperties();
      XProp.saveProperties(PROP_FILE, "1", "1");

      XLog.close();
      System.exit(0);
   }

   @Override
   public Window getMainFrame() {
      return mainFrame;
   }

   @Override
   public String getUserConfDir() {
      return USER_CONF_DIR;
   }

   @Override
   public String getUserLogDir() {
      return USER_LOG_DIR;
   }

   @Override
   public String getUserPropFile() {
      return PROP_FILE;
   }

   @Override
   public String getLangDir() {
      return LANG_DIR;
   }

   @Override
   public String getLogName() {
      return "redit";
   }

   @Override
   public Class getMainClass() {
      return ReditApplication.class;
   }

   @Override
   public String getApplicationName() {
      return "rEdit";
   }

   @Override
   public String getApplicationRelease() {
      return "1";
   }

   @Override
   public String getApplicationBuild() {
      return "100";
   }
}
