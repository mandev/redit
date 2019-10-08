/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit.gui.dialog;

import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.utils.GuiUtils;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Main;
import com.adlitteram.redit.gui.MainFrame;
import com.adlitteram.redit.search.SearchEngine;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;

abstract public class AbstractSearchDialog extends JDialog implements WindowListener {

   protected AppManager appManager;
   //
   protected JTextField replaceField;
   protected JTextField searchField;
   protected JCheckBox backwardCheck;
   protected JCheckBox matchCaseCheck;
   protected JCheckBox wholeWordCheck;
   protected JCheckBox regexpCheck;

   public AbstractSearchDialog(AppManager appManager, String title) {
      super((MainFrame) Main.getApplication().getMainFrame(), title, false);
      this.appManager = appManager;

      addWindowListener(this);

      getContentPane().add(buildGeneralPanel(), BorderLayout.CENTER);
      getContentPane().add(buildButtonPanel(), BorderLayout.SOUTH);

      pack();
      setLocationRelativeTo(getParent());
   }

   abstract protected JPanel buildButtonPanel();

   abstract protected JPanel buildGeneralPanel();

   protected void cancelPressed() {
      setVisible(false);
      appManager.getActionManager().enableActions();
      if (appManager.getArticlePane() != null) {
         appManager.getArticlePane().getTextPane().requestFocusInWindow();
      }
   }

   protected void replaceAllPressed() {
      if (appManager.getArticlePane() == null) {
         return;
      }
      JTextPane textPane = appManager.getArticlePane().getTextPane();

      int count = SearchEngine.replaceAll(textPane, searchField.getText(), replaceField.getText(), matchCaseCheck.isSelected(),
              wholeWordCheck.isSelected(), regexpCheck.isSelected());

      GuiUtils.showMessage(count + " " + Message.get("Search.Occurences"));
   }

   protected void replacePressed() {
      if (appManager.getArticlePane() == null) {
         return;
      }
      JTextPane textPane = appManager.getArticlePane().getTextPane();

      if (searchField.getText().length() > 0) {

         if (!SearchEngine.replace(textPane, searchField.getText(), replaceField.getText(), !backwardCheck.isSelected(),
                 matchCaseCheck.isSelected(), wholeWordCheck.isSelected(), regexpCheck.isSelected())) {

            if ((backwardCheck.isSelected() && textPane.getCaret().getDot() == textPane.getDocument().getLength())
                    || (!backwardCheck.isSelected() && textPane.getCaret().getDot() == 0)) {
               GuiUtils.showMessage(Message.get("Search.NotFound") + " : " + searchField.getText());
            }
            else if (JOptionPane.showOptionDialog(Main.getApplication().getMainFrame(), Message.get("Search.Continue"),
                    Message.get("Search.EndOfText"), JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE, null, null, null) == JOptionPane.OK_OPTION) {

               int oldDot = textPane.getCaret().getDot();
               textPane.getCaret().setDot(backwardCheck.isSelected() ? textPane.getDocument().getLength() : 0);
               if (!SearchEngine.replace(textPane, searchField.getText(), replaceField.getText(), !backwardCheck.isSelected(),
                       matchCaseCheck.isSelected(), wholeWordCheck.isSelected(), regexpCheck.isSelected())) {

                  textPane.getCaret().setDot(oldDot);
                  GuiUtils.showMessage(Message.get("Search.NotFound") + " : " + searchField.getText());
               }
            }
         }
         else if (!SearchEngine.find(textPane, searchField.getText(), !backwardCheck.isSelected(),
                 matchCaseCheck.isSelected(), wholeWordCheck.isSelected(), regexpCheck.isSelected())) {

            GuiUtils.showMessage(Message.get("Search.NotFound") + " : " + searchField.getText());
         }
      }
   }

   protected void findPressed() {
      if (appManager.getArticlePane() == null) {
         return;
      }
      JTextPane textPane = appManager.getArticlePane().getTextPane();

      if (searchField.getText().length() > 0) {

         if (!SearchEngine.find(textPane, searchField.getText(), !backwardCheck.isSelected(),
                 matchCaseCheck.isSelected(), wholeWordCheck.isSelected(), regexpCheck.isSelected())) {
            if ((backwardCheck.isSelected() && textPane.getCaret().getDot() == textPane.getDocument().getLength())
                    || (!backwardCheck.isSelected() && textPane.getCaret().getDot() == 0)) {
               GuiUtils.showMessage(Message.get("Search.NotFound") + " : " + searchField.getText());
            }
            else if (JOptionPane.showOptionDialog(Main.getApplication().getMainFrame(), Message.get("Search.Continue"),
                    Message.get("Search.EndOfText"), JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE, null, null, null) == JOptionPane.OK_OPTION) {

               int oldDot = textPane.getCaret().getDot();
               textPane.getCaret().setDot(backwardCheck.isSelected() ? textPane.getDocument().getLength() : 0);
               if (!SearchEngine.find(textPane, searchField.getText(), !backwardCheck.isSelected(),
                       matchCaseCheck.isSelected(), wholeWordCheck.isSelected(), regexpCheck.isSelected())) {
                  textPane.getCaret().setDot(oldDot);
                  GuiUtils.showMessage(Message.get("Search.NotFound") + " : " + searchField.getText());
               }
            }
         }
      }
   }

   @Override
   public void windowOpened(WindowEvent e) {
   }

   @Override
   public void windowClosing(WindowEvent e) {
      cancelPressed();
   }

   @Override
   public void windowClosed(WindowEvent e) {
   }

   @Override
   public void windowIconified(WindowEvent e) {
   }

   @Override
   public void windowDeiconified(WindowEvent e) {
   }

   @Override
   public void windowActivated(WindowEvent e) {
   }

   @Override
   public void windowDeactivated(WindowEvent e) {
   }
}
