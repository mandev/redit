/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit;

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
//    public static User createFromFile(String filename) {
//        try {
//            return (User) XmlTools.decodeFromFile(new File(filename));
//        }
//        catch (Exception ex) {
//            logger.warn( "", ex);
//        }
//        return null;
//    }
//
//    public void saveToFile(String filename) {
//        try {
//            XmlTools.encodeToFile(this, new File(filename));
//        }
//        catch (IOException ex) {
//            logger.warn( "", ex);
//        }
//    }
}
