package com.adlitteram.redit.gui.panel;

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
