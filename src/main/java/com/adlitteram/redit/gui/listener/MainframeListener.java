/*
 * MainframeListener.java
 *
 * Created on 1 mars 2007, 11:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.adlitteram.redit.gui.listener;

import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.action.Quit;
import com.adlitteram.redit.gui.MainFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainframeListener extends WindowAdapter {

   private final MainFrame mainFrame;

   public MainframeListener(MainFrame mainFrame) {
      this.mainFrame = mainFrame;
   }

   @Override
   public void windowClosing(WindowEvent e) {
      AppManager appManager = mainFrame.getAppManager();
      Quit.action(appManager);
   }
//    public void windowActivated(WindowEvent e) {
//    }
//    
//    public void windowDeactivated(WindowEvent e) {
//    }
//    
//    public void windowIconified(WindowEvent e) {
//    }
//    
//    public void windowClosed(WindowEvent e) {
//    }
//    
//    public void windowOpened(WindowEvent e) {
//    }
//
//    public void windowDeiconified(WindowEvent e) {        
//    }
}
