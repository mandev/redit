package com.adlitteram.redit.gui.dialog;

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
import com.adlitteram.jasmin.utils.GuiUtils;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Main;
import com.adlitteram.redit.UploadManager;
import com.adlitteram.redit.User;
import com.adlitteram.redit.gui.MainFrame;
import com.adlitteram.redit.inputfilter.ResponseUser;
import cz.autel.dmi.HIGConstraints;
import cz.autel.dmi.HIGLayout;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectDialog extends JDialog {

   private static final Logger logger = LoggerFactory.getLogger(ConnectDialog.class);
   //
   private final AppManager appManager;
   private JTextField usernameField;
   private JPasswordField passwordField;

   public static void create(AppManager appManager) {
      ConnectDialog dialog = new ConnectDialog(appManager);
      dialog.setVisible(true);
   }

   private ConnectDialog(AppManager appManager) {
      super((MainFrame) Main.getApplication().getMainFrame(), Message.get("ConnectDialog.Title"), true);
      this.appManager = appManager;

      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      getContentPane().add(buildGeneralPanel(), BorderLayout.CENTER);
      getContentPane().add(buildButtonPanel(), BorderLayout.SOUTH);

      pack();
      setLocationRelativeTo(getParent());
   }

   private JPanel buildButtonPanel() {

      JButton okButton = new JButton(Message.get("ConnectDialog.Connect"));
      getRootPane().setDefaultButton(okButton);
      okButton.addActionListener((ActionEvent e) -> okPressed());

      JButton cancelButton = new JButton(Message.get("Cancel"));
      cancelButton.addActionListener((ActionEvent e) -> cancelPressed());

      int w[] = {5, 0, 5, -6, 5, -4, 5};
      int h[] = {5, 0, 10};
      HIGLayout l = new HIGLayout(w, h);
      HIGConstraints c = new HIGConstraints();
      l.setColumnWeight(3, 1);

      JPanel buttonPanel = new JPanel(l);
      buttonPanel.add(okButton, c.xy(4, 2));
      buttonPanel.add(cancelButton, c.xy(6, 2));
      return buttonPanel;
   }

   private JPanel buildGeneralPanel() {

      usernameField = new JTextField(20);
      User user = appManager.getUser();
      if (user != null) {
         usernameField.setText(user.getUserName());
      }

      passwordField = new JPasswordField(20);
      if (user != null) {
         passwordField.setText(user.getPassword());
      }

      // Gui
      int w[] = {10, 0, 5, 0, 10};
      int h[] = {10, 0, 5, 0, 10};

      HIGLayout l = new HIGLayout(w, h);
      HIGConstraints c = new HIGConstraints();
      l.setColumnWeight(4, 1);

      JPanel panel = new JPanel(l);
      panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createEtchedBorder()));

      panel.add(new JLabel(Message.get("ConnectDialog.Login")), c.xy(2, 2, "r"));
      panel.add(usernameField, c.xy(4, 2));
      panel.add(new JLabel(Message.get("ConnectDialog.Password")), c.xy(2, 4, "r"));
      panel.add(passwordField, c.xy(4, 4));
      return panel;
   }

   private void setBusy(boolean b) {
      if (b) {
         setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         usernameField.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         passwordField.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      }
      else {
         setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
         usernameField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
         passwordField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
      }
   }

   private void cancelPressed() {
      dispose();
   }

   private void okPressed() {

      String username = usernameField.getText();
      String password = new String(passwordField.getPassword());

      if (username.length() == 0) {
         GuiUtils.showError(Message.get("ConnectDialog.UsernameError"));
      }
      else {
         try {
            UploadManager uploadManager = appManager.getRemoteManager();
            setBusy(true);
            ResponseUser userResponse = uploadManager.getUserInfo(username, password);
            setBusy(false);
            if (userResponse != null && userResponse.getError() == 0) {
               User user = userResponse.getUser();
               user.setPassword(password);
               appManager.setUser(user);
               dispose();
               UploadDialog.create(appManager);
            }
            else {
               String message = Message.get("ConnectDialog.ConnectionError");
               if (userResponse != null) {
                  message += "\n" + userResponse.getComment();
               }
               GuiUtils.showError(message);
            }
         }
         catch (IOException ex) {
            logger.warn("ConnectDialog.okPressed()", ex);
            setBusy(false);
            dispose();
            GuiUtils.showError(Message.get("ConnectDialog.ConnectionError") + "\n" + ex.getLocalizedMessage());
         }
      }
   }
}
