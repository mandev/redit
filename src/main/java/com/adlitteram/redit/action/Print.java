package com.adlitteram.redit.action;

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
import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.jasmin.utils.GuiUtils;
import com.adlitteram.redit.AppManager;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.text.JTextComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Print extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(Print.class);
   private static PrintService printService;
   private static HashPrintRequestAttributeSet printSet;
   //
   private final AppManager appManager;

   public Print(AppManager appManager) {
      super("Print");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      logger.info("Print");

      String jobName = appManager.getArticle().getName();
      MessageFormat header = new MessageFormat(jobName);
      MessageFormat footer = new MessageFormat("page {0}");
      JTextComponent text = appManager.getArticlePane().getTextPane();
      action(jobName, text, header, footer, true);

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
   }

   public static void action(String name, JTextComponent text, MessageFormat header, MessageFormat footer, boolean interactive) {
      try {
         // TODO : fix to retrieve the printSevice (does not work at the moment)
         if (printService == null) {
            printService = PrintServiceLookup.lookupDefaultPrintService();
         }

         if (printSet == null) {
            printSet = new HashPrintRequestAttributeSet();
            printSet.add(new MediaPrintableArea(15, 15, 180, 267, MediaPrintableArea.MM));
            printSet.add(MediaSizeName.ISO_A4);
         }

         printSet.add(new JobName(name, null));
         text.print(header, footer, true, printService, printSet, interactive);
      }
      catch (PrinterException ex) {
         logger.warn("", ex);
         GuiUtils.showError(Message.get("Print.Error") + "\n" + ex.getLocalizedMessage());
      }
      catch (SecurityException ex) {
         logger.warn("", ex);
         GuiUtils.showError(Message.get("Print.SecurityError") + "\n" + ex.getLocalizedMessage());
      }
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null && (appManager.getArticle().getDocument().getLength() > 0));
   }
}
