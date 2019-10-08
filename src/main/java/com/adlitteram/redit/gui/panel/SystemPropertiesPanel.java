/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit.gui.panel;

import com.adlitteram.jasmin.Message;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.ArticleMetadata;
import cz.autel.dmi.HIGConstraints;
import cz.autel.dmi.HIGLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.io.FileUtils;

public class SystemPropertiesPanel extends JPanel {

   private final JTextField appField;
   private final JTextField versionField;
   private final JTextField lengthField;
   private final JTextField filenameField;
   private final JTextField nameField;
   private final JTextField dateField;

   public SystemPropertiesPanel(ArticleMetadata meta) {

      nameField = new JTextField(35);
      nameField.setText(meta.getName());
      nameField.setEditable(false);

      dateField = new JTextField(35);
      dateField.setText(AppManager.DATE_FORMATTER.format(meta.getDate()));
      dateField.setEditable(false);

      filenameField = new JTextField(35);
      filenameField.setText(meta.getFilename());
      filenameField.setEditable(false);

      lengthField = new JTextField(35);
      lengthField.setText(FileUtils.byteCountToDisplaySize(meta.getLength()));
      lengthField.setEditable(false);

      appField = new JTextField(35);
      appField.setText(meta.getApplication());
      appField.setEditable(false);

      versionField = new JTextField(35);
      versionField.setText(meta.getVersion());
      versionField.setEditable(false);

      int w[] = {10, 0, 5, 0, 10};
      int h[] = {10, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 10};
      HIGLayout l = new HIGLayout(w, h);
      HIGConstraints c = new HIGConstraints();
      l.setColumnWeight(4, 1);

      setLayout(l);

      add(new JLabel(Message.get("ArticleMetadataDialog.Name")), c.xy(2, 2, "r"));
      add(nameField, c.xy(4, 2, "lr"));

      add(new JLabel(Message.get("ArticleMetadataDialog.Date")), c.xy(2, 4, "r"));
      add(dateField, c.xy(4, 4, "lr"));

      add(new JLabel(Message.get("ArticleMetadataDialog.Filename")), c.xy(2, 6, "r"));
      add(filenameField, c.xy(4, 6, "lr"));

      add(new JLabel(Message.get("ArticleMetadataDialog.Length")), c.xy(2, 8, "r"));
      add(lengthField, c.xy(4, 8, "lr"));

      add(new JLabel(Message.get("ArticleMetadataDialog.Application")), c.xy(2, 10, "r"));
      add(appField, c.xy(4, 10, "lr"));

      add(new JLabel(Message.get("ArticleMetadataDialog.Version")), c.xy(2, 12, "r"));
      add(versionField, c.xy(4, 12, "lr"));

   }
}
