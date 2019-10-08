package com.adlitteram.redit.action;

import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.jasmin.utils.GuiUtils;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Version;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.plaf.basic.BasicButtonUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class About extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(About.class);
   //
   private static final String MESSAGE = "<html><center>"
           + "<h3><strong>" + Version.getNAME() + " v" + Version.getVERSION() + "</strong></h3>"
           + "<h5>" + Version.getCOPYRIGHT() + " - " + Version.getDATE() + "</h5>"
           + "</html>";
   //
   private final AppManager appManager;

   public About(AppManager appManager) {
      super("About");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager);
   }

   public static void action(AppManager appManager) {
      final JDialog dialog = new JDialog(appManager.getMainFrame(), true);
      dialog.setUndecorated(true);

      JButton button = new JButton(MESSAGE, GuiUtils.loadIcon("resource/icon/about.png"));
      button.setHorizontalTextPosition(JButton.RIGHT);
      button.setVerticalTextPosition(JButton.CENTER);
      button.setIconTextGap(10);
      button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY), BorderFactory.createEmptyBorder(5, 5, 5, 10)));
      button.setFocusPainted(false);
      button.setRolloverEnabled(false);
      button.setBackground(Color.WHITE);
      button.setUI(new BasicButtonUI());

      button.addActionListener(e -> dialog.dispose());

      button.addKeyListener(new KeyAdapter() {
         @Override
         public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
               dialog.dispose();
            }
         }
      });

      dialog.setContentPane(button);
      dialog.pack();
      dialog.setLocationRelativeTo(dialog.getParent());
      dialog.setVisible(true);
   }
}
