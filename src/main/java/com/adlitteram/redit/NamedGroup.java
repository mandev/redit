/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit;

import java.util.ArrayList;
import javax.swing.text.Style;

public class NamedGroup {

   private final String name;
   private final ArrayList<Style> styleList;

   public NamedGroup(String name) {
      this.styleList = new ArrayList<>();
      this.name = name;
   }

   /**
    * @return the name
    */
   public String getName() {
      return name;
   }

   public void addStyle(Style style) {
      styleList.add(style);
      style.addAttribute(StyleManager.GROUP, this);
   }
}
