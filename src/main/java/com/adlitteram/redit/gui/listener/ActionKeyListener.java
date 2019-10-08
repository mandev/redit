/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit.gui.listener;

import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.action.DecreaseFontSize;
import com.adlitteram.redit.action.IncreaseFontSize;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author EDEVILLER
 */
public class ActionKeyListener extends KeyAdapter {

   private final AppManager appManager;

   public ActionKeyListener(AppManager appManager) {
      this.appManager = appManager;
   }

   @Override
   public void keyPressed(KeyEvent e) {

      if (e.isControlDown()) {
         switch (e.getKeyChar()) {
            case '+':
               IncreaseFontSize.action(appManager);
               break;
            case '-':
               DecreaseFontSize.action(appManager);
               break;
            default:
         }
      }
   }
}
