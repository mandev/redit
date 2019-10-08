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

public class Metadata {

   // System metadata
   private String creator;
   private String version;
   private String filename;
   private String name;
   private Date date;
   private long length;

   public Metadata(String creator, String version, String filename, String name, Date date, long length) {
      this.creator = creator;
      this.version = version;
      this.filename = filename;
      this.name = name;
      this.date = date;
      this.length = length;
   }

   /**
    * @return the creator
    */
   public String getCreator() {
      return creator;
   }

   /**
    * @param creator the creator to set
    */
   public void setCreator(String creator) {
      this.creator = creator;
   }

   /**
    * @return the version
    */
   public String getVersion() {
      return version;
   }

   /**
    * @param version the version to set
    */
   public void setVersion(String version) {
      this.version = version;
   }

   /**
    * @return the filename
    */
   public String getFilename() {
      return filename;
   }

   /**
    * @param filename the filename to set
    */
   public void setFilename(String filename) {
      this.filename = filename;
   }

   /**
    * @return the name
    */
   public String getName() {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * @return the date
    */
   public Date getDate() {
      return date;
   }

   /**
    * @param date the date to set
    */
   public void setDate(Date date) {
      this.date = date;
   }

   /**
    * @return the length
    */
   public long getLength() {
      return length;
   }

   /**
    * @param length the length to set
    */
   public void setLength(long length) {
      this.length = length;
   }
}
