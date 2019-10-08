package com.adlitteram.redit.outputfilter;

import com.adlitteram.jasmin.gui.explorer.ImageFile;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.ArticleMetadata;
import com.adlitteram.redit.PictureMetadata;
import java.io.*;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.znerd.xmlenc.XMLOutputter;

public class RdzArticleWriter extends ArticleWriter {

   private static final Logger logger = LoggerFactory.getLogger(RdzArticleWriter.class);
   //

   public RdzArticleWriter(Article article) {
      super(article);
   }

   @Override
   public void write(String fileName) throws IOException {
      ZipOutputStream zout = null;
      File tmpFile = null;
      try {
         XMLOutputter xmlWriter = getXmlWriter();
         tmpFile = File.createTempFile("redit_", ".rdz");
         zout = new ZipOutputStream(new FileOutputStream(tmpFile));

         writeHeader(xmlWriter);

         writeDocument(zout, xmlWriter);

         writePictures(zout, xmlWriter);

         writeStructure(zout, xmlWriter);

         zout.flush();
         zout.close();

         File file = new File(fileName);
         if (file.exists()) {
            file.delete();
         }
         FileUtils.moveFile(tmpFile, file);
      }
      catch (IOException ex) {
         IOUtils.closeQuietly(zout);
         if (tmpFile != null && tmpFile.exists()) {
            tmpFile.delete();
         }
         throw ex;
      }
   }

   protected XMLOutputter getXmlWriter() throws IOException {
      XMLOutputter xmlWriter = new XMLOutputter(new CharArrayWriter(), encoding);
      //xmlWriter.setLineBreak(LineBreak.UNIX);
      //xmlWriter.setIndentation("  ");
      xmlWriter.declaration();
      return xmlWriter;
   }

   protected void writeHeader(XMLOutputter xmlWriter) throws IOException {
      ArticleMetadata meta = article.getArticleMetadata();

      xmlWriter.startTag("article");

      // System metadata
      xmlWriter.attribute("application", notNull(meta.getApplication()));
      xmlWriter.attribute("version", notNull(meta.getVersion()));
      xmlWriter.attribute("filename", notNull(meta.getFilename()));
      xmlWriter.attribute("name", notNull(meta.getName()));
      xmlWriter.attribute("date", AppManager.DATE_FORMATTER.format(meta.getDate()));
      xmlWriter.attribute("length", String.valueOf(meta.getLength()));

      // User metadata
      xmlWriter.startTag("metadadata");

      xmlWriter.startTag("general");
      writeTag(xmlWriter, "author", notNull(meta.getAuthor()));
      writeTag(xmlWriter, "keyword", notNull(meta.getKeyword()));
      writeTag(xmlWriter, "country", notNull(meta.getCountry()));
      writeTag(xmlWriter, "city", notNull(meta.getCity()));
      writeTag(xmlWriter, "address", notNull(meta.getAddress()));
      xmlWriter.endTag(); // general

      xmlWriter.startTag("web");
      writeTag(xmlWriter, "profile", String.valueOf(meta.getWebProfile()));
      writeTag(xmlWriter, "comment", String.valueOf(meta.getWebComment()));
      writeTag(xmlWriter, "topic", notNull(meta.getWebTopic()));
      writeTag(xmlWriter, "type", notNull(meta.getType()));
      xmlWriter.endTag(); // web

      xmlWriter.endTag(); // metadata

      // Release history
      xmlWriter.startTag("releases");
      writeTag(xmlWriter, "release", AppManager.DATE_FORMATTER.format(meta.getDate()));
      writeTag(xmlWriter, "release", AppManager.DATE_FORMATTER.format(new Date()));
      xmlWriter.endTag();

      // Files
      xmlWriter.startTag("files");
   }

   protected void writeDocument(ZipOutputStream zout, XMLOutputter xmlWriter) throws IOException {
      zout.setLevel(9);
      ZipEntry entry = new ZipEntry("document.xml");
      zout.putNextEntry(entry);
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zout, encoding), 2048);
      writeDocument(writer, article.getDocument());
      writer.flush();

      xmlWriter.startTag("file");
      xmlWriter.attribute("type", "doc");
      xmlWriter.attribute("path", entry.getName());
      xmlWriter.attribute("name", entry.getName());
      xmlWriter.endTag(); // file
   }

   protected void writePictures(ZipOutputStream zout, XMLOutputter xmlWriter) throws IOException {
      int size = article.getExplorerModel().size();
      if (size > 0) {
         zout.setLevel(0);
         int counter = 0;
         for (int i = 0; i < article.getExplorerModel().size(); i++) {
            ImageFile picture = article.getExplorerModel().getImageFile(i);
            ZipEntry entry = new ZipEntry("picture_" + (++counter) + "." + getPictureFormat(picture.getFormat()));
            zout.putNextEntry(entry);
            copyFileToStream(picture.getFile(), zout);
            writePictureTag(xmlWriter, picture, entry.getName());
         }
      }
   }

   protected void writePictureTag(XMLOutputter xmlWriter, ImageFile picture, String path) throws IOException {
      xmlWriter.startTag("file");
      xmlWriter.attribute("type", "img");
      xmlWriter.attribute("path", path);
      xmlWriter.attribute("name", picture.getName());

      PictureMetadata meta = (PictureMetadata) picture.getProperties();
      if (meta != null) {
         writeTag(xmlWriter, "credit", notNull(meta.getCredit()));
         writeTag(xmlWriter, "caption", notNull(meta.getCaption()));
      }

      xmlWriter.endTag(); // file
   }

   protected void writeStructure(ZipOutputStream zout, XMLOutputter xmlWriter) throws IOException {
      xmlWriter.endTag();         // files
      xmlWriter.endTag();         // article
      xmlWriter.endDocument();    // closes all tags and flushes the stream

      zout.setLevel(9);
      zout.putNextEntry(new ZipEntry("structure.xml"));
      OutputStreamWriter writer = new OutputStreamWriter(zout, encoding);
      writer.write(((CharArrayWriter) xmlWriter.getWriter()).toCharArray());
      writer.flush();
   }

   private void writeTag(XMLOutputter writer, String tag, String value) throws IOException {
      writer.startTag(tag);
      if (value != null && value.length() > 0) {
         writer.pcdata(value);
      }
      writer.endTag();
   }

   private String notNull(String str) {
      return (str == null) ? "" : str;
   }
}
