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
import java.io.Serializable;
import java.util.Date;

public class IptcData implements Serializable {

   private static final long serialVersionUID = 4625550456259117836L;

   private String filename;
   private String headline;
   private String subject;
   private String caption;
   private String special;
   private String credit;
   private String source;
   private String city;
   private String state;
   private String country;
   private String countryCode;
   private String byLine;
   private String byLineTitle;
   private Date createdDate;
   private Date releasedDate;

   public IptcData() {
   }

   public String getHeadline() {
      return headline;
   }

   public void setHeadline(String headline) {
      this.headline = headline;
   }

   public String getSubject() {
      return subject;
   }

   public void setSubject(String subject) {
      this.subject = subject;
   }

   public String getCaption() {
      return caption;
   }

   public void setCaption(String caption) {
      this.caption = caption;
   }

   public String getSpecial() {
      return special;
   }

   public void setCredit(String credit) {
      this.credit = credit;
   }

   public String getCredit() {
      return credit;
   }

   public void setSpecial(String special) {
      this.special = special;
   }

   public String getFilename() {
      return filename;
   }

   public void setFilename(String filename) {
      this.filename = filename;
   }

   /**
    * @return the city
    */
   public String getCity() {
      return city;
   }

   /**
    * @param city the city to set
    */
   public void setCity(String city) {
      this.city = city;
   }

   /**
    * @return the state
    */
   public String getState() {
      return state;
   }

   /**
    * @param state the state to set
    */
   public void setState(String state) {
      this.state = state;
   }

   /**
    * @return the country
    */
   public String getCountry() {
      return country;
   }

   /**
    * @param country the country to set
    */
   public void setCountry(String country) {
      this.country = country;
   }

   /**
    * @return the countryCode
    */
   public String getCountryCode() {
      return countryCode;
   }

   /**
    * @param countryCode the countryCode to set
    */
   public void setCountryCode(String countryCode) {
      this.countryCode = countryCode;
   }

   /**
    * @return the byLine
    */
   public String getByLine() {
      return byLine;
   }

   /**
    * @param byLine the byLine to set
    */
   public void setByLine(String byLine) {
      this.byLine = byLine;
   }

   /**
    * @return the byLineTitle
    */
   public String getByLineTitle() {
      return byLineTitle;
   }

   /**
    * @param byLineTitle the byLineTitle to set
    */
   public void setByLineTitle(String byLineTitle) {
      this.byLineTitle = byLineTitle;
   }

   /**
    * @return the createdDate
    */
   public Date getCreatedDate() {
      return createdDate;
   }

   /**
    * @param createdDate the createdDate to set
    */
   public void setCreatedDate(Date createdDate) {
      this.createdDate = createdDate;
   }

   /**
    * @return the releasedDate
    */
   public Date getReleasedDate() {
      return releasedDate;
   }

   /**
    * @param releasedDate the releasedDate to set
    */
   public void setReleasedDate(Date releasedDate) {
      this.releasedDate = releasedDate;
   }

   /**
    * @return the source
    */
   public String getSource() {
      return source;
   }

   /**
    * @param source the source to set
    */
   public void setSource(String source) {
      this.source = source;
   }
}
