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
import com.adlitteram.redit.ArticleMetadata;
import cz.autel.dmi.HIGConstraints;
import cz.autel.dmi.HIGLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WebPropertiesPanel extends JPanel {

   private static final String[] WEB_TYPES = {
      Message.get("ArticleMetadataDialog.webTypeStory"),
      Message.get("ArticleMetadataDialog.webTypeGallery")};

   private static final String[] WEB_PROFILES = {
      Message.get("ArticleMetadataDialog.webProfileNormal"),
      Message.get("ArticleMetadataDialog.webProfileInfo"),
      Message.get("ArticleMetadataDialog.webProfileExclusif"),
      Message.get("ArticleMetadataDialog.webProfileUrgent")};

   private static final String[] WEB_COMMENTS = {
      Message.get("ArticleMetadataDialog.webCommentEnable"),
      Message.get("ArticleMetadataDialog.webCommentClose"),
      Message.get("ArticleMetadataDialog.webCommentForbidden")};

   private static final String[] WEB_POS = {
      "NP", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
      "13", "14", "15", "16", "17", "18", "19", "20"};

   private final JComboBox typeCombo;
   private final JComboBox webProfileCombo;
   private final JComboBox webCommentCombo;
   private final JComboBox webPosCombo;

   // TODO : implement multi-categories (column + position)
   public WebPropertiesPanel(ArticleMetadata meta) {
      typeCombo = new JComboBox(WEB_TYPES);
      typeCombo.setSelectedIndex(ArticleMetadata.getTypeIndex(meta.getType()));

      webProfileCombo = new JComboBox(WEB_PROFILES);
      webProfileCombo.setSelectedIndex(meta.getWebProfile());

      webCommentCombo = new JComboBox(WEB_COMMENTS);
      webCommentCombo.setSelectedIndex(meta.getWebComment());

      webPosCombo = new JComboBox(WEB_POS);
      webPosCombo.setSelectedItem(meta.getWebPosition());

      int w[] = {10, 0, 5, 0, 10};
      int h[] = {10, 0, 5, 0, 5, 0, 5, 0, 5, 0, 10};
      HIGLayout l = new HIGLayout(w, h);
      HIGConstraints c = new HIGConstraints();
      l.setColumnWeight(4, 1);

      setLayout(l);

      add(new JLabel(Message.get("ArticleMetadataDialog.Type")), c.xy(2, 2, "r"));
      add(typeCombo, c.xy(4, 2, "lr"));

      add(new JLabel(Message.get("ArticleMetadataDialog.Profile")), c.xy(2, 4, "r"));
      add(webProfileCombo, c.xy(4, 4, "lr"));

      add(new JLabel(Message.get("ArticleMetadataDialog.Comment")), c.xy(2, 6, "r"));
      add(webCommentCombo, c.xy(4, 6, "lr"));

      add(new JLabel(Message.get("ArticleMetadataDialog.Position")), c.xy(2, 8, "r"));
      add(webPosCombo, c.xy(4, 8, "lr"));

   }

   public String getType() {
      return ArticleMetadata.TYPES[typeCombo.getSelectedIndex()];
   }

   public int getWebProfile() {
      return webProfileCombo.getSelectedIndex();
   }

   public int getWebComment() {
      return webCommentCombo.getSelectedIndex();
   }

   public String getWebPosition() {
      return (String) webPosCombo.getSelectedItem();
   }
}
