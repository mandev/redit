/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit;

/**
 *
 * @author EDEVILLER
 */
public class WebTopic {

   private String section;
   private String subSection;
   private String subSubSection;
   private int position;

   public WebTopic() {
   }

   public int getPosition() {
      return position;
   }

   public void setPosition(int position) {
      this.position = position;
   }

   public String getSection() {
      return section;
   }

   public void setSection(String section) {
      this.section = section;
   }

   public String getSubSection() {
      return subSection;
   }

   public void setSubSection(String subSection) {
      this.subSection = subSection;
   }

   public String getSubSubSection() {
      return subSubSection;
   }

   public void setSubSubSection(String subSubSection) {
      this.subSubSection = subSubSection;
   }

   @Override
   public String toString() {
      return "WebTopic{" + "section=" + section + ", subSection=" + subSection + ", subSubSection=" + subSubSection + ", position=" + position + '}';
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final WebTopic other = (WebTopic) obj;
      if ((this.section == null) ? (other.section != null) : !this.section.equals(other.section)) {
         return false;
      }
      if ((this.subSection == null) ? (other.subSection != null) : !this.subSection.equals(other.subSection)) {
         return false;
      }
      if ((this.subSubSection == null) ? (other.subSubSection != null) : !this.subSubSection.equals(other.subSubSection)) {
         return false;
      }
      return this.position == other.position;
   }

   @Override
   public int hashCode() {
      int hash = 7;
      hash = 79 * hash + (this.section != null ? this.section.hashCode() : 0);
      hash = 79 * hash + (this.subSection != null ? this.subSection.hashCode() : 0);
      hash = 79 * hash + (this.subSubSection != null ? this.subSubSection.hashCode() : 0);
      hash = 79 * hash + this.position;
      return hash;
   }
}
