/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit;

import java.util.ArrayList;

/**
 *
 * @author manu
 */
public class Group {

   private String groupName;
   private String groupDescription;
   private boolean accessRedaction;
   private boolean accessPictureAssociation;
   private ArrayList<Column> columnList;

   public Group() {
   }

   /**
    * @return the groupName
    */
   public String getGroupName() {
      return groupName;
   }

   /**
    * @param groupName the groupName to set
    */
   public void setGroupName(String groupName) {
      this.groupName = groupName;
   }

   /**
    * @return the groupDescription
    */
   public String getGroupDescription() {
      return groupDescription;
   }

   /**
    * @param groupDescription the groupDescription to set
    */
   public void setGroupDescription(String groupDescription) {
      this.groupDescription = groupDescription;
   }

   /**
    * @return the accessRedaction
    */
   public boolean isAccessRedaction() {
      return accessRedaction;
   }

   /**
    * @param accessRedaction the accessRedaction to set
    */
   public void setAccessRedaction(boolean accessRedaction) {
      this.accessRedaction = accessRedaction;
   }

   /**
    * @return the accessPictureAssociation
    */
   public boolean isAccessPictureAssociation() {
      return accessPictureAssociation;
   }

   /**
    * @param accessPictureAssociation the accessPictureAssociation to set
    */
   public void setAccessPictureAssociation(boolean accessPictureAssociation) {
      this.accessPictureAssociation = accessPictureAssociation;
   }

   /**
    * @return the columnList
    */
   public ArrayList<Column> getColumnList() {
      return columnList;
   }

   /**
    * @param columnList the columnList to set
    */
   public void setColumnList(ArrayList<Column> columnList) {
      this.columnList = columnList;
   }

   public void addColumn(Column column) {
      if (columnList == null) {
         columnList = new ArrayList<>();
      }
      columnList.add(column);
   }
}
