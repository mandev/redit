package com.adlitteram.redit.outputfilter;

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
import java.io.IOException;
import java.io.Writer;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyledDocument;

public class EidosDocWriter extends XmlDocWriter {

   public EidosDocWriter(Writer writer, StyledDocument doc, String encoding) {
      super(writer, doc, encoding);
   }

   @Override
   protected void writeHeader() throws IOException {
      super.writeHeader();

      write("<!DOCTYPE doc SYSTEM \"/SysConfig/Editor/Rules/editor.dtd\">\n");
      write("<?EM-dtdExt /SysConfig/Editor/Rules/editor.dtx?>\n");
      write("<?EM-templateName /SysConfig/Editor/Templates/Standard.xml?>\n");
      write("<?xml-stylesheet type=\"text/css\" href=\"/SysConfig/Editor/Styles/default.css\"?>\n");
      write("<doc xml:lang=\"fr\">");
   }

   @Override
   protected void writeTrailer() throws IOException {
      write("</doc>");
   }

   @Override
   protected void writeHTMLTags(AttributeSet attr) throws IOException {
      if (!inSpanTag()) {
         super.writeHTMLTags(attr);
      }
   }
}
