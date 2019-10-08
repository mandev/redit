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
import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.XProp;
import com.adlitteram.jasmin.gui.RecentFilesItems.RecentFileItem;
import com.adlitteram.jasmin.utils.GuiUtils;
import com.adlitteram.redit.action.OpenArticle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecentFilesManager implements ActionListener {

   private static final Logger logger = LoggerFactory.getLogger(RecentFilesManager.class);
   //
   private final AppManager appManager;
   private final ArrayList<String> filenameList;

   public RecentFilesManager(AppManager appManager) {
      this.appManager = appManager;

      int keep = XProp.getInt("RecentFiles.Keep", 4);
      filenameList = new ArrayList<>(keep);

      for (int i = 0; i < keep; i++) {
         String fileName = XProp.get("RecentFiles.Filename_" + i);
         if (fileName != null) {
            filenameList.add(fileName);
         }
      }
   }

   public ArrayList<String> getFilenameList() {
      return filenameList;
   }

   public ActionListener getActionListener() {
      return this;
   }

   public void saveProperties() {
      int keep = XProp.getInt("RecentFiles.Keep", 4);
      int max = Math.min(keep, filenameList.size());
      for (int i = 0; i < max; i++) {
         XProp.put("RecentFiles.Filename_" + i, filenameList.get(i));
      }

      for (int i = max;; i++) {
         String filename = "RecentFiles.Filename_" + i;
         if (XProp.get(filename) == null) {
            break;
         }
         XProp.unsetProperty(filename);
      }
   }

   public void addFilename(String filename) {
      if (filename != null) {
         int index = filenameList.indexOf(filename);
         if (index >= 0) {
            filenameList.remove(index);
         }
         filenameList.add(0, filename);
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      try {
         String filename = ((RecentFileItem) e.getSource()).getFileName();
         OpenArticle.action(appManager, new File(filename), true);

         appManager.getMainFrame().requestTextFocus();
         appManager.getActionManager().enableActions();
      }
      catch (IOException ex) {
         logger.warn("", ex);
         GuiUtils.showError(Message.get("OpenArticle.Error") + "\n" + ex.getLocalizedMessage());
      }
   }
}
