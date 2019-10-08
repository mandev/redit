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

public class Column {

   private String columnName;
   private String columnDescription;
   private String paper;
   private String paperDescription;
   private String textStorageDirectory;
   private String pictureStorageDirectory;
   private ArrayList<IptcField> iptcFieldList;

   public Column() {
   }

   /**
    * @return the columnName
    */
   public String getColumnName() {
      return columnName;
   }

   /**
    * @param columnName the columnName to set
    */
   public void setColumnName(String columnName) {
      this.columnName = columnName;
   }

   /**
    * @return the columnDescription
    */
   public String getColumnDescription() {
      return columnDescription;
   }

   /**
    * @param columnDescription the columnDescription to set
    */
   public void setColumnDescription(String columnDescription) {
      this.columnDescription = columnDescription;
   }

   /**
    * @return the paper
    */
   public String getPaper() {
      return paper;
   }

   /**
    * @param paper the paper to set
    */
   public void setPaper(String paper) {
      this.paper = paper;
   }

   /**
    * @return the paperDescription
    */
   public String getPaperDescription() {
      return paperDescription;
   }

   /**
    * @param paperDescription the paperDescription to set
    */
   public void setPaperDescription(String paperDescription) {
      this.paperDescription = paperDescription;
   }

   /**
    * @return the textStorageDirectory
    */
   public String getTextStorageDirectory() {
      return textStorageDirectory;
   }

   /**
    * @param textStorageDirectory the textStorageDirectory to set
    */
   public void setTextStorageDirectory(String textStorageDirectory) {
      this.textStorageDirectory = textStorageDirectory;
   }

   /**
    * @return the pictureStorageDirectory
    */
   public String getPictureStorageDirectory() {
      return pictureStorageDirectory;
   }

   /**
    * @param pictureStorageDirectory the pictureStorageDirectory to set
    */
   public void setPictureStorageDirectory(String pictureStorageDirectory) {
      this.pictureStorageDirectory = pictureStorageDirectory;
   }

   /**
    * @return the iptcFiledList
    */
   public ArrayList<IptcField> getIptcFieldList() {
      return iptcFieldList;
   }

   /**
    * @param iptcFiledList the iptcFiledList to set
    */
   public void setIptcFieldList(ArrayList<IptcField> iptcFiledList) {
      this.iptcFieldList = iptcFiledList;
   }

   public void addIptcField(IptcField iptcField) {
      if (iptcFieldList == null) {
         iptcFieldList = new ArrayList<>();
      }
      iptcFieldList.add(iptcField);
   }

   @Override
   public String toString() {
      StringBuilder toStringBuilder = new StringBuilder();
      toStringBuilder.append(super.toString());
      toStringBuilder.append("\n");
      toStringBuilder.append("\ncolumnName: ");
      toStringBuilder.append(columnName);
      toStringBuilder.append("\ncolumnDescription: ");
      toStringBuilder.append(columnDescription);
      toStringBuilder.append("\npaper: ");
      toStringBuilder.append(paper);
      toStringBuilder.append("\npaperDescription: ");
      toStringBuilder.append(paperDescription);
      toStringBuilder.append("\ntextStorageDirectory: ");
      toStringBuilder.append(textStorageDirectory);
      toStringBuilder.append("\npictureStorageDirectory: ");
      toStringBuilder.append(pictureStorageDirectory);
      toStringBuilder.append("\niptcFiledList: ");
      if (iptcFieldList != null) {
         toStringBuilder.append("\nSize: ");
         toStringBuilder.append(iptcFieldList.size());
         java.util.Iterator collectionIiterator = iptcFieldList.iterator();
         for (int i = 0; collectionIiterator.hasNext(); ++i) {
            toStringBuilder.append("\nIndex ");
            toStringBuilder.append(i);
            toStringBuilder.append(": ");
            toStringBuilder.append(collectionIiterator.next());
         }
      }
      else {
         toStringBuilder.append("NULL");
      }
      return toStringBuilder.toString();
   }
}
