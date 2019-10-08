/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit;

import com.adlitteram.redit.gui.MainFrame;

public class Main {

   private static ReditApplication application;

   public static void main(String[] args) {
      application = new ReditApplication(args);
      application.init();
      application.start();
   }

   public static ReditApplication getApplication() {
      return application;
   }

   public static void setApplication(ReditApplication app) {
      application = app;
   }

   public static MainFrame getMainFrame() {
      return (MainFrame) application.getMainFrame();
   }

   public static AppManager getAppManager() {
      return application.getAppManager();
   }
}
