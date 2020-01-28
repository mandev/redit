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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User {

   private static final Logger logger = LoggerFactory.getLogger(User.class);

   private String userName;
   private String password;
   private String unitName;
   private String unitDescription;
   private String role;
   private Group group;
   private String columnName;     // Current Column

   public User() {
   }

   /**
    * @return the userName
    */
   public String getUserName() {
      return userName;
   }

   /**
    * @param userName the userName to set
    */
   public void setUserName(String userName) {
      this.userName = userName;
   }

   /**
    * @return the password
    */
   public String getPassword() {
      return password;
   }

   /**
    * @param password the password to set
    */
   public void setPassword(String password) {
      this.password = password;
   }

   /**
    * @return the unitDescription
    */
   public String getUnitDescription() {
      return unitDescription;
   }

   /**
    * @param unitDescription the unitDescription to set
    */
   public void setUnitDescription(String unitDescription) {
      this.unitDescription = unitDescription;
   }

   /**
    * @return the role
    */
   public String getRole() {
      return role;
   }

   /**
    * @param role the role to set
    */
   public void setRole(String role) {
      this.role = role;
   }

   /**
    * @return the group
    */
   public Group getGroup() {
      return group;
   }

   /**
    * @param group the group to set
    */
   public void setGroup(Group group) {
      this.group = group;
   }

   /**
    * @return the unitName
    */
   public String getUnitName() {
      return unitName;
   }

   /**
    * @param unitName the unitName to set
    */
   public void setUnitName(String unitName) {
      this.unitName = unitName;
   }

   /**
    * @return the column
    */
   public String getColumnName() {
      return columnName;
   }

   /**
    * @param column the column to set
    */
   public void setColumnName(String column) {
      this.columnName = column;
   }
}
