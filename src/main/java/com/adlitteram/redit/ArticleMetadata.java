package com.adlitteram.redit;

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
import java.util.Date;

public class ArticleMetadata {

   public static final String[] TYPES = {
      "story",
      "gallery"
   };

   public static final String[] WEB_COMMENTS = {
      "Commentaires possibles et liste affichée",
      "Commentaires fermés et liste affichée",
      "Pas de commentaire possible et liste non affichée"
   };

   public static final String[] WEB_PROFILES = {
      "Normal",
      "Info",
      "Exclusif",
      "Urgent"
   };

   public static final String[] WEB_STATUS = {
      "Publié",
      "A valider"
   };

   public static final int WEB_COMMENT_ENABLED = 0;
   public static final int WEB_COMMENT_CLOSED = 1;
   public static final int WEB_COMMENT_FORBIDDEN = 2;
   public static final int WEB_PROFILE_NORMAL = 0;
   public static final int WEB_PROFILE_INFO = 1;
   public static final int WEB_PROFILE_EXCLUSIF = 2;
   public static final int WEB_PROFILE_URGENT = 3;
   public static final int WEB_STATUS_PUBLISHED = 0;
   public static final int WEB_STATUS_VALIDATE = 1;

   private final Article article;

   // User metatadata
   private String type = TYPES[0];
   private String author = "";
   private String keyword = "";
   private String country = "France";
   private String city = "";
   private String address = "";

   // Web metatadata
   private int webStatus = WEB_STATUS_VALIDATE;
   private int webProfile = WEB_PROFILE_NORMAL;
   private int webComment = WEB_COMMENT_ENABLED;
   private String webPosition = "1";
   private String webTopic = "";

   public ArticleMetadata(Article article) {
      this.article = article;
   }

   public String getApplication() {
      return article.getApplication();
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public static int getTypeIndex(String type) {
      for (int i = 0; i < TYPES.length; i++) {
         if (TYPES[i].equals(type)) {
            return i;
         }
      }
      return 0;
   }

   /**
    * @return the version
    */
   public String getVersion() {
      return article.getVersion();
   }

   /**
    * @return the filename
    */
   public String getFilename() {
      return article.getPath();
   }

   /**
    * @return the name
    */
   public String getName() {
      return article.getName();
   }

   /**
    * @return the date
    */
   public Date getDate() {
      return article.getCreationDate();
   }

   /**
    * @return the length
    */
   public long getLength() {
      return article.getLength();
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getAuthor() {
      return author;
   }

   public void setAuthor(String author) {
      this.author = author;
   }

   public String getCity() {
      return city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public String getCountry() {
      return country;
   }

   public void setCountry(String country) {
      this.country = country;
   }

   public String getKeyword() {
      return keyword;
   }

   public void setKeyword(String keyword) {
      this.keyword = keyword;
   }

   public int getWebComment() {
      return webComment;
   }

   public void setWebComment(int webComment) {
      this.webComment = webComment;
   }

   public int getWebProfile() {
      return webProfile;
   }

   public void setWebProfile(int webProfile) {
      this.webProfile = webProfile;
   }

   public int getWebStatus() {
      return webStatus;
   }

   public void setWebStatus(int webStatus) {
      this.webStatus = webStatus;
   }

   public String getWebPosition() {
      return webPosition;
   }

   public void setWebPosition(String webPosition) {
      this.webPosition = webPosition;
   }

   public String getWebTopic() {
      return webTopic;
   }

   public void setWebTopic(String webTopic) {
      this.webTopic = webTopic;
   }

}
