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
