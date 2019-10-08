/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit.outputfilter;

import com.adlitteram.redit.NamedGroup;
import com.adlitteram.redit.StyleManager;
import java.io.IOException;
import java.io.Writer;
import javax.swing.text.StyleContext.NamedStyle;
import javax.swing.text.*;

public class XmlDocWriter extends AbstractWriter {

   private static final int BOLD = 0x01;
   private static final int ITALIC = 0x02;
   private static final int UNDERLINE = 0x04;
   //
   protected String encoding;
   //
   protected NamedGroup currentGroup;
   protected NamedStyle currentStyle;
   protected AttributeSet spanAttributes;
   protected int fontMask = 0;

   public XmlDocWriter(Writer writer, StyledDocument doc, String encoding) {
      super(writer, doc);
      this.encoding = encoding;
      setCanWrapLines(false);
   }

   @Override
   public void write() throws IOException, BadLocationException {
      writeHeader();
      writeBody();
      writeTrailer();
   }

   protected void writeHeader() throws IOException {
      write("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\r\n");
   }

   protected void writeTrailer() throws IOException {
   }

   /**
    * Iterates over the elements in the document and processes elements based on whether they are branch elements or
    * leaf elements.This method specially handles leaf elements that are text.
    *
    * @exception IOException on any I/O error
    * @throws javax.swing.text.BadLocationException
    */
   protected void writeBody() throws IOException, BadLocationException {

      ElementIterator it = getElementIterator();
      // This will be a section element for a styled document.
      // We represent this element in HTML as the body tags. Therefore we ignore it.
      it.current();

      write("<article>");

      Element element;
      boolean inContent = false;

      while ((element = it.next()) != null) {
         if (!inRange(element)) {
            continue;
         }
         if (element instanceof AbstractDocument.BranchElement) {
            if (inContent) {
               writeEndParagraph();
               inContent = false;
               fontMask = 0;
            }
            writeStartParagraph(element);
         }
         else if (isText(element)) {
            writeContent(element, !inContent);
            inContent = true;
         }
      }

      if (inContent) {
         writeEndParagraph();
      }

      if (currentStyle != null && currentStyle.getAttribute(StyleManager.VISIBLE) != Boolean.FALSE) {
         write("</" + currentStyle.getName() + ">");
      }

      if (currentGroup != null) {
         write("</" + currentGroup.getName() + ">");
      }

      write("</article>");
   }

   /**
    * Emits an end tag for a &lt;p&gt; tag. Before writing out the tag, this method ensures that all other tags that
    * have been opened are appropriately closed off.
    *
    * @exception IOException on any I/O error
    */
   protected void writeEndParagraph() throws IOException {
      writeEndMask(fontMask);
      writeEndSpanTag();
      write("</p>");
   }

   /**
    * Emits the start tag for a paragraph.If the paragraph has a named style associated with it, then this method also
    * generates a class attribute for the &lt;p&gt; tag and sets its value to be the name of the style.
    *
    * @param elem
    * @exception IOException on any I/O error
    */
   protected void writeStartParagraph(Element elem) throws IOException {
      AttributeSet attr = elem.getAttributes();
      Object resolveAttr = attr.getAttribute(StyleConstants.ResolveAttribute);
      if (resolveAttr instanceof StyleContext.NamedStyle) {
         writeStyle((StyleContext.NamedStyle) resolveAttr);
      }
      else {
         write("<p>");
      }
   }

   protected void writeStyle(StyleContext.NamedStyle style) throws IOException {

      NamedGroup group = (NamedGroup) style.getAttribute(StyleManager.GROUP);
      if (group != null) {
         if (currentStyle != null && currentStyle.getAttribute(StyleManager.VISIBLE) != Boolean.FALSE) {
            write("</" + currentStyle.getName() + ">");
         }

         if (!group.equals(currentGroup)) {
            if (currentGroup != null) {
               write("</" + currentGroup.getName() + ">");
            }
            write("<" + group.getName() + ">");
            currentGroup = group;
         }

         if (style.getAttribute(StyleManager.VISIBLE) != Boolean.FALSE) {
            write("<" + style.getName() + ">");
         }

         write("<p>");
         currentStyle = style;
      }

   }

   /**
    * Returns true if the element is a text element.
    *
    * @param elem
    * @return
    */
   protected boolean isText(Element elem) {
      return (AbstractDocument.ContentElementName.equals(elem.getName()));
   }

   /**
    * Writes out the attribute set in an HTML-compliant manner.
    *
    * @param elem
    * @param needsIndenting
    * @exception IOException on any I/O error
    * @exception BadLocationException if pos represents an invalid location within the document.
    */
   protected void writeContent(Element elem, boolean needsIndenting) throws IOException, BadLocationException {
      AttributeSet attr = elem.getAttributes();
      writeNonHTMLAttributes(attr);
      writeHTMLTags(attr);
      text(elem);
   }

   /**
    * Writes out text. If a range is specified when the constructor is invoked, then only the appropriate range of text
    * is written out.
    *
    * @param elem an Element
    * @exception IOException on any I/O error
    * @exception BadLocationException if pos represents an invalid location within the document.
    */
   @Override
   protected void text(Element elem) throws BadLocationException, IOException {
      int start = Math.max(getStartOffset(), elem.getStartOffset());
      int end = Math.min(getEndOffset(), elem.getEndOffset());
      if (start < end) {
         // The text is normalized to enforce the XML standard
         write(normalize(getDocument().getText(start, end - start)));
      }
   }

   /**
    * Generates bold &lt;b&gt;, italic &lt;i&gt;, and &lt;u&gt; tags for the text based on its attribute settings.
    *
    * @param attr
    * @exception IOException on any I/O error
    */
   protected void writeHTMLTags(AttributeSet attr) throws IOException {

      int oldFontMask = fontMask;

      fontMask = 0;
      if (StyleConstants.isBold(attr) && attr.isDefined(StyleConstants.Bold)) {
         fontMask |= BOLD;
      }
      if (StyleConstants.isItalic(attr) && attr.isDefined(StyleConstants.Italic)) {
         fontMask |= ITALIC;
      }
      if (StyleConstants.isUnderline(attr) && attr.isDefined(StyleConstants.Underline)) {
         fontMask |= UNDERLINE;
      }

      if (fontMask != oldFontMask) {
         writeEndMask(oldFontMask);
         writeStartMask(fontMask);
      }
   }

   /**
    * Writes out start tags &lt;u&gt;, &lt;i&gt;, and &lt;b&gt; based on the mask settings.
    *
    * @exception IOException on any I/O error
    */
   private void writeStartMask(int mask) throws IOException {
      if (mask != 0) {
         if ((mask & BOLD) != 0) {
            write("<b>");
         }
         if ((mask & ITALIC) != 0) {
            write("<i>");
         }
         if ((mask & UNDERLINE) != 0) {
            write("<u>");
         }
      }
   }

   /**
    * Writes out end tags for &lt;u&gt;, &lt;i&gt;, and &lt;b&gt; based on the mask settings.
    *
    * @exception IOException on any I/O error
    */
   private void writeEndMask(int mask) throws IOException {
      if (mask != 0) {
         if ((mask & UNDERLINE) != 0) {
            write("</u>");
         }
         if ((mask & ITALIC) != 0) {
            write("</i>");
         }
         if ((mask & BOLD) != 0) {
            write("</b>");
         }
      }
   }

   /**
    * Writes out the remaining character-level attributes (attributes other than bold, italic, and underline) in an
    * HTML-compliant way.Given that attributes such as font family and font size have no direct mapping to HTML tags, a
    * &lt;span&gt; tag is generated and its style attribute is set to contain the list of remaining attributes just like
    * inline styles.
    *
    * @param attr
    * @exception IOException on any I/O error
    */
   protected void writeNonHTMLAttributes(AttributeSet attr) throws IOException {
      String oldName;
      String name = (String) attr.getAttribute(StyleConstants.NameAttribute);

      if (inSpanTag()) {
         if (spanAttributes.isEqual(attr)) {
            return;
         }
         oldName = (String) spanAttributes.getAttribute(StyleConstants.NameAttribute);
         if (oldName.equals(name)) {
            return;
         }
      }

      Style style = ((StyledDocument) getDocument()).getStyle(name);
      if (style != null && !style.isDefined(StyleManager.GROUP)) {
         writeEndMask(fontMask);
         fontMask = 0;
         writeEndSpanTag();
         writeStartSpanTag(name);
         spanAttributes = attr;
      }
      else if (inSpanTag()) {
         writeEndMask(fontMask);
         fontMask = 0;
         writeEndSpanTag();
      }
   }

   /**
    * Returns true if we are currently in a &lt;font&gt; tag.
    *
    * @return
    */
   protected boolean inSpanTag() {
      return (spanAttributes != null);
   }

   /**
    * Writes out a start tag for the &lt;font&gt; tag. Because font tags cannot be nested, this method closes out any
    * enclosing font tag before writing out a new start tag.
    *
    * @exception IOException on any I/O error
    */
   private void writeStartSpanTag(String span) throws IOException {
      write("<" + span + ">");
   }

   /**
    * Writes out an end tag for the &lt;span&gt; tag.
    *
    * @exception IOException on any I/O error
    */
   private void writeEndSpanTag() throws IOException {
      if (inSpanTag()) {
         write("</" + (String) spanAttributes.getAttribute(StyleConstants.NameAttribute) + ">");
         spanAttributes = null;
      }
   }

   // Normalisation XML
   public static String normalize(String str) {
      StringBuilder buffer = new StringBuilder(str.length());
      for (int i = 0; i < str.length(); i++) {
         char ch = str.charAt(i);
         switch (ch) {
            case '<':
               buffer.append("&lt;");
               break;
            case '>':
               buffer.append("&gt;");
               break;
            case '&':
               buffer.append("&amp;");
               break;
            case '"':
               buffer.append("&quot;");
               break;
            default:
               // '\r' '\n' '\t' (and more)
               if (ch < ' ' || ch > 127) {
                  buffer.append("&#");
                  buffer.append(Integer.toString(ch));
                  buffer.append(';');
               }
               else {
                  buffer.append(ch);
               }
         }
      }
      return buffer.toString();
   }
}
