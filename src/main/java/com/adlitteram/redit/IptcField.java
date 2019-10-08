/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit;

/**
 *
 * @author manu
 */
public class IptcField {

   private String label;
   private String value;

   public IptcField() {
   }

   /**
    * @return the label
    */
   public String getLabel() {
      return label;
   }

   /**
    * @param label the label to set
    */
   public void setLabel(String label) {
      this.label = label;
   }

   /**
    * @return the value
    */
   public String getValue() {
      return value;
   }

   /**
    * @param value the value to set
    */
   public void setValue(String value) {
      this.value = value;
   }
}
