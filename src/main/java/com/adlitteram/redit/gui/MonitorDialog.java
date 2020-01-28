package com.adlitteram.redit.gui;

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
import cz.autel.dmi.HIGConstraints;
import cz.autel.dmi.HIGLayout;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;

public class MonitorDialog extends JDialog {

   public interface CancelListener {
      public void cancel(AWTEvent event);
   }
   
   protected JLabel messageLabel;
   protected JLabel label1;
   protected JLabel label2;
   protected JLabel label3;
   protected JProgressBar progressBar;
   private CancelListener cancelListener;
   private boolean isCanceled = false;

   public MonitorDialog(Dialog dialog, String title) {
      super(dialog, title, true);
      initDialog();
   }

   public MonitorDialog(Frame frame, String title) {
      super(frame, title, true);
      initDialog();
   }

   private void initDialog() {
      setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            isCanceled = true;
            if (cancelListener != null) {
               cancelListener.cancel(e);
            }
         }
      });

      getContentPane().add(buildGeneralPanel(), BorderLayout.CENTER);
      getContentPane().add(buildButtonPanel(), BorderLayout.SOUTH);

      pack();
      setLocationRelativeTo(getParent());
   }

   private JComponent buildButtonPanel() {
      JButton cancelButton = new JButton(Message.get("Cancel"));
      cancelButton.addActionListener( e -> {
         isCanceled = true;
         if (cancelListener != null) {
            cancelListener.cancel(e);
         }
      });

      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
      panel.add(cancelButton);
      return panel;
   }

   private JComponent buildGeneralPanel() {
      messageLabel = new JLabel(" ");
      label1 = new JLabel(" ");
      label2 = new JLabel(" ");
      label3 = new JLabel(" ");

      progressBar = new JProgressBar();
      progressBar.setMinimum(0);
      progressBar.setMaximum(100);
      progressBar.setStringPainted(true);

      int w[] = {10, 300, 10};
      int h[] = {10, 0, 10, 0, 5, 0, 5, 0, 15, 0, 10};
      HIGLayout l = new HIGLayout(w, h);
      HIGConstraints c = new HIGConstraints();
      l.setColumnWeight(2, 1);
      l.setRowWeight(2, 1);

      JPanel panel = new JPanel(l);
      panel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createEtchedBorder()));
      panel.add(messageLabel, c.xy(2, 2));
      panel.add(label1, c.xy(2, 4));
      panel.add(label2, c.xy(2, 6));
      panel.add(label3, c.xy(2, 8));
      panel.add(progressBar, c.xy(2, 10));
      return panel;
   }

   public void setMinimum(int n) {
      progressBar.setMinimum(n);
   }

   public void setMaximum(int n) {
      progressBar.setMaximum(n);
   }

   public void setProgress(int value) {
      progressBar.setValue(value);
   }

   public void setMessage(String str) {
      messageLabel.setText(str);
   }

   public void setLabel1(String str) {
      label1.setText(str);
   }

   public void setLabel2(String str) {
      label2.setText(str);
   }

   public void setLabel3(String str) {
      label3.setText(str);
   }

   public boolean isCanceled() {
      return isCanceled;
   }

   public void setCanceled(boolean isCanceled) {
      this.isCanceled = isCanceled;
   }

   public CancelListener getCancelListener() {
      return cancelListener;
   }

   public void setCancelListener(CancelListener cancelListener) {
      this.cancelListener = cancelListener;
   }
}
