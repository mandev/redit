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
import com.adlitteram.jasmin.utils.StrUtils;
import com.adlitteram.redit.ArticleMetadata;
import com.adlitteram.redit.IptcCountry;
import cz.autel.dmi.HIGConstraints;
import cz.autel.dmi.HIGLayout;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GeneralPropertiesPanel extends JPanel {

   private final JTextField authorField;
   private final JTextField cityField;
   private final JComboBox countryCombo;
   private final JTextField addressField;
   private final JTextField keyField;

   public GeneralPropertiesPanel(ArticleMetadata meta) {

      authorField = new JTextField(35);
      authorField.setText(meta.getAuthor());

      // TODO ArrayList<String> keywords = new ArrayList<String>();
      keyField = new JTextField(35);
      keyField.setText(meta.getKeyword());

      String country = meta.getCountry();
      countryCombo = new JComboBox(IptcCountry.COUNTRIES);
      countryCombo.setSelectedItem(IptcCountry.getInstanceName(country));

      cityField = new JTextField(35);
      cityField.setText(meta.getCity());

      addressField = new JTextField(35);
      addressField.setText(meta.getAddress());

      JLabel tipLabel = new JLabel(Message.get("ArticleMetadataDialog.Tip"));
      tipLabel.setFont(tipLabel.getFont().deriveFont(tipLabel.getFont().getSize2D() - 1));
      tipLabel.setForeground(Color.BLUE);

      int w[] = {10, 0, 5, 0, 10};
      int h[] = {10, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 10, 0, 5};
      HIGLayout l = new HIGLayout(w, h);
      HIGConstraints c = new HIGConstraints();
      l.setColumnWeight(4, 1);

      setLayout(l);

      add(new JLabel(Message.get("ArticleMetadataDialog.Author")), c.xy(2, 2, "r"));
      add(authorField, c.xy(4, 2, "lr"));

      add(new JLabel(Message.get("ArticleMetadataDialog.Keywords")), c.xy(2, 4, "r"));
      add(keyField, c.xy(4, 4, "lr"));

      add(new JLabel(Message.get("ArticleMetadataDialog.Country")), c.xy(2, 6, "r"));
      add(countryCombo, c.xy(4, 6, "lr"));

      add(new JLabel(Message.get("ArticleMetadataDialog.City")), c.xy(2, 8, "r"));
      add(cityField, c.xy(4, 8, "lr"));

      add(new JLabel(Message.get("ArticleMetadataDialog.Address")), c.xy(2, 10, "r"));
      add(addressField, c.xy(4, 10, "lr"));

      add(tipLabel, c.xywh(2, 14, 3, 1, "r"));
   }

   public String getAddress() {
      return StrUtils.stripXmlSpace(addressField.getText());
   }

   public String getAuthor() {
      return StrUtils.stripXmlSpace(authorField.getText());
   }

   public String getCity() {
      return StrUtils.stripXmlSpace(cityField.getText());
   }

   public String getCountry() {
      return ((IptcCountry) countryCombo.getSelectedItem()).getName();
   }

   public String getKeyword() {
      return StrUtils.stripXmlSpace(keyField.getText());
   }
}
