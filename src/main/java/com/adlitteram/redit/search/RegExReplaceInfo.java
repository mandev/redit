package com.adlitteram.redit.search;

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

 /*
 * 02/19/2006
 *
 * RegExReplaceInfo.java - Information about a regex text match.
 * Copyright (C) 2006 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA.
 */
/**
 * Information on how to implement a regular expression "replace" operation.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RegExReplaceInfo {

   private final String matchedText;
   private final int startIndex;
   private final int endIndex;
   private final String replacement;

   /**
    * Constructor.
    *
    * @param matchedText The text that matched the regular expression.
    * @param start The start index of the matched text in the <code>CharSequence</code> searched.
    * @param end The end index of the matched text in the <code>CharSequence</code> searched.
    * @param replacement The text to replace the matched text with. This string has any matched groups and character
    * escapes replaced.
    */
   public RegExReplaceInfo(String matchedText, int start, int end,
           String replacement) {
      this.matchedText = matchedText;
      this.startIndex = start;
      this.endIndex = end;
      this.replacement = replacement;
   }

   /**
    * Returns the end index of the matched text.
    *
    * @return The end index of the matched text in the document searched.
    * @see #getMatchedText()
    * @see #getEndIndex()
    */
   public int getEndIndex() {
      return endIndex;
   }

   /**
    * Returns the text that matched the regular expression.
    *
    * @return The matched text.
    */
   public String getMatchedText() {
      return matchedText;
   }

   /**
    * Returns the string to replaced the matched text with.
    *
    * @return The string to replace the matched text with.
    */
   public String getReplacement() {
      return replacement;
   }

   /**
    * Returns the start index of the matched text.
    *
    * @return The start index of the matched text in the document searched.
    * @see #getMatchedText()
    * @see #getEndIndex()
    */
   public int getStartIndex() {
      return startIndex;
   }
}
