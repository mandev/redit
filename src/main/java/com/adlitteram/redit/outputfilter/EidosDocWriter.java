package com.adlitteram.redit.outputfilter;

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
