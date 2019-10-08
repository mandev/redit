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
import com.adlitteram.jasmin.gui.explorer.ImageFile;
import com.adlitteram.jasmin.utils.StrUtils;
import com.adlitteram.redit.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.znerd.xmlenc.XMLOutputter;

public class EidosZipArticleWriter extends EidosArticleWriter {

   private static final Logger logger = LoggerFactory.getLogger(EidosZipArticleWriter.class);
   //
   private static final String SUPPLIER = "Editor";
   private static final FastDateFormat DF0 = FastDateFormat.getInstance("ddMMyy'_'HHmmss");
   private static final FastDateFormat DF2 = FastDateFormat.getInstance("yyyyMMdd");
   private static final FastDateFormat DF3 = FastDateFormat.getInstance("yyyyMMddHHmmss");
   //
   private final User user;
   private final Column column;
   private String docName;
   private final boolean sendPicture;
   private final String dateOnly;
   private final String dateTime;
   private int count = 1;

   public EidosZipArticleWriter(Article article, User user, Column column, String docName, boolean sendPicture) {
      super(article);
      this.user = user;
      this.column = column;
      this.docName = docName;
      this.sendPicture = sendPicture;
      this.docName = docName + "_" + user.getUserName() + "_" + DF0.format(new Date());

      // Tomorrow
      Date date = new Date(System.currentTimeMillis() + 1000 * 3600 * 22); // +22h
      dateOnly = DF2.format(date);
      dateTime = DF3.format(date);
   }

   @Override
   public void write(String fileName) throws IOException {

      try (FileOutputStream fos = new FileOutputStream(fileName);
              ZipOutputStream zout = new ZipOutputStream(fos)) {

         XMLOutputter xmlWriter = getXmlWriter();
         writeDocument(zout, xmlWriter);
         if (sendPicture && user.getGroup().isAccessPictureAssociation() && article.getExplorerModel().size() > 0) {
            writePictures(zout, xmlWriter);
         }
         writeStructure(zout, xmlWriter);
         zout.flush();
      }
   }

   protected XMLOutputter getXmlWriter() throws IOException {
      XMLOutputter xmlWriter = new XMLOutputter(new CharArrayWriter(), encoding);
      // Don't use the following options with XML
      // xmlWriter.setLineBreak(LineBreak.UNIX);
      // xmlWriter.setIndentation("  ");
      xmlWriter.declaration();
      xmlWriter.startTag("package");
      return xmlWriter;
   }

   protected void writeDocument(ZipOutputStream zout, XMLOutputter xmlWriter) throws IOException {
      zout.setLevel(9);
      String name = StrUtils.toFilename(docName).toLowerCase();
      if (!name.endsWith(".xml")) {
         name += ".xml";
      }
      zout.putNextEntry(new ZipEntry(name));
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zout, encoding), 4096);
      writeDocument(writer, article.getDocument());
      writer.flush();
      writeDocFileTag(xmlWriter, name);
   }

   protected void writePictures(ZipOutputStream zout, XMLOutputter xmlWriter) throws IOException {

      File iptcFile = null;
      try {
         ArrayList<IptcField> iptcList = column.getIptcFieldList();
         if (iptcList != null && !iptcList.isEmpty()) {
            iptcFile = File.createTempFile("iptc_", ".tmp");
            iptcFile.deleteOnExit();
            IptcManager.writeIptcFields(iptcList.toArray(new IptcField[iptcList.size()]), iptcFile);
         }

         zout.setLevel(0);
         int counter = 0;

         for (int i = 0; i < article.getExplorerModel().size(); i++) {
            ImageFile picture = article.getExplorerModel().getImageFile(i);
            String name = StrUtils.toFilename(FilenameUtils.getBaseName(picture.getName()).toLowerCase()) + "_" + (++counter) + "_" + user.getUserName() + "_" + System.currentTimeMillis() + "." + getPictureFormat(picture.getFormat());
            zout.putNextEntry(new ZipEntry(name));

            writePicture(zout, picture, iptcFile);
            writePictureFileTag(xmlWriter, picture, name);
         }
      }
      finally {
         if (iptcFile != null && iptcFile.exists()) {
            iptcFile.delete();
         }
      }
   }

   protected void writePicture(ZipOutputStream zout, ImageFile picture, File iptcFile) throws IOException {

      if (iptcFile == null) {
         FileUtils.copyFile(picture.getFile(), zout);
      }
      else {
         File pictureFile = null;
         try {
            pictureFile = File.createTempFile("img_", ".tmp");
            FileUtils.copyFile(picture.getFile(), pictureFile);
            pictureFile.deleteOnExit();
            IptcManager.setIptcFile(iptcFile, pictureFile);
            FileUtils.copyFile(pictureFile, zout);
         }
         finally {
            if (pictureFile != null && pictureFile.exists()) {
               pictureFile.delete();
            }
         }
      }
   }

   protected void writeStructure(ZipOutputStream zout, XMLOutputter xmlWriter) throws IOException {
      xmlWriter.endTag();     // package
      xmlWriter.endDocument();    // closes all tags and flushes the stream

      zout.setLevel(9);
      zout.putNextEntry(new ZipEntry("structure.cfg"));
      OutputStreamWriter writer = new OutputStreamWriter(zout, encoding);
      writer.write(((CharArrayWriter) xmlWriter.getWriter()).toCharArray());
      writer.flush(); // Mandatory
   }

   protected void writeDocFileTag(XMLOutputter xmlWriter, String name) throws IOException {

      ArticleMetadata meta = article.getArticleMetadata();

      xmlWriter.startTag("file");
      xmlWriter.attribute("type", meta.getType());
      xmlWriter.attribute("name", name);
      xmlWriter.startTag("extMetadata");

      writeTag(xmlWriter, "DbPath", column.getTextStorageDirectory());
      writeTag(xmlWriter, "Creator", user.getUserName());
      writeTag(xmlWriter, "Supplier", SUPPLIER);
      writeTag(xmlWriter, "ObjectID", StringUtils.leftPad(String.valueOf(count++), 5, "0"));
      writeTag(xmlWriter, "DateTime", dateTime);
      writeTag(xmlWriter, "Rubrique", column.getColumnName());
      writeTag(xmlWriter, "RevisionID", "1");
      writeTag(xmlWriter, "IssueDate", dateOnly);

      // Metadata
      writeTag(xmlWriter, "Title", meta.getName());
      writeTag(xmlWriter, "Author", meta.getAuthor());
      writeTag(xmlWriter, "Category", null);
      writeTag(xmlWriter, "Keyword", meta.getKeyword());

      writeTag(xmlWriter, "Country", meta.getCountry());
      writeTag(xmlWriter, "City", meta.getCity());
      writeTag(xmlWriter, "Address", meta.getAddress());

      writeTag(xmlWriter, "Status", ArticleMetadata.WEB_STATUS[meta.getWebStatus()]);
      writeTag(xmlWriter, "TopicsList", meta.getWebTopic());
      writeTag(xmlWriter, "Profile", ArticleMetadata.WEB_PROFILES[meta.getWebProfile()]);
      writeTag(xmlWriter, "Comment", ArticleMetadata.WEB_COMMENTS[meta.getWebComment()]);

      xmlWriter.endTag(); // extMetadata
      xmlWriter.endTag(); // file
   }

   protected void writePictureFileTag(XMLOutputter xmlWriter, ImageFile picture, String name) throws IOException {
      PictureMetadata meta = (PictureMetadata) picture.getProperties();
      if (meta == null) {
         meta = new PictureMetadata();
      }

      xmlWriter.startTag("file");
      xmlWriter.attribute("type", "image");
      xmlWriter.attribute("name", name);
      xmlWriter.startTag("extMetadata");

      writeTag(xmlWriter, "DbPath", column.getPictureStorageDirectory());
      writeTag(xmlWriter, "Creator", user.getUserName());
      writeTag(xmlWriter, "Supplier", SUPPLIER);
      writeTag(xmlWriter, "ObjectID", StringUtils.leftPad(String.valueOf(count++), 5, "0"));
      writeTag(xmlWriter, "DateTime", dateTime);
      writeTag(xmlWriter, "Credit", notNull(meta.getCredit()));
      writeTag(xmlWriter, "Caption", notNull(meta.getCaption()));

      xmlWriter.endTag(); // extMetadata
      xmlWriter.endTag(); // file
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
