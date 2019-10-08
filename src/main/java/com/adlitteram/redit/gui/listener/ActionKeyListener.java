package com.adlitteram.redit.gui.listener;

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
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.action.DecreaseFontSize;
import com.adlitteram.redit.action.IncreaseFontSize;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
