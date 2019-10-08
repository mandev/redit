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
public class Version {

   private static final String COPYRIGHT = "ED";
   private static final String DATE = "2009-2019";
   private static final String AUTHOR = "ED";
   private static final String CNAME = "redit";
   private static final String NAME = "rEdit";
   private static final String WEB = "";
   private static final String EMAIL = "";
   private static final String VERSION_NUM = "1.6";
   private static final String BUILD_NUM = "116";

   public static String getCOPYRIGHT() {
      return COPYRIGHT;
   }

   public static String getDATE() {
      return DATE;
   }

   public static String getAUTHOR() {
      return AUTHOR;
   }

   public static String getBUILD() {
      return BUILD_NUM;
   }

   public static String getCNAME() {
      return CNAME;
   }

   public static String getNAME() {
      return NAME;
   }

   public static String getVERSION() {
      return VERSION_NUM;
   }

   public static String getWEB() {
      return WEB;
   }

   public static String getEMAIL() {
      return EMAIL;
   }
}
