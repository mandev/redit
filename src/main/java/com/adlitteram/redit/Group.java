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
import java.util.ArrayList;

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
