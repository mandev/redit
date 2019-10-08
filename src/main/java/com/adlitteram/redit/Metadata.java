package com.adlitteram.redit;

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
