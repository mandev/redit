package com.adlitteram.redit.inputfilter;

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
import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.gui.explorer.ImageFile;
import com.adlitteram.jasmin.utils.GuiUtils;
import com.adlitteram.jasmin.utils.NumUtils;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.ArticleMetadata;
import com.adlitteram.redit.PictureMetadata;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import nu.xom.Builder;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class RdzReader {

   private static final Logger logger = LoggerFactory.getLogger(RdzReader.class);

   private final Article article;
   private ZipFile zipFile;

   public RdzReader(Article article) {
      this.article = article;
   }

   private void parseStructure(InputStream is) throws IOException, ParsingException, ParserConfigurationException, SAXException {
      Builder builder = new Builder();
      nu.xom.Document xmlDoc = builder.build(is);

      Element articleElement = xmlDoc.getRootElement();
      processArticle(articleElement);
      processMetadata(getFirstChild(articleElement, "metadadata"));
      processFiles(getFirstChild(articleElement, "files"));
   }

   private void processFiles(Element filesElement) throws ParserConfigurationException, SAXException, IOException {
      Elements fileElements = filesElement.getChildElements("file");
      for (int i = 0; i < fileElements.size(); i++) {
         Element elt = fileElements.get(i);
         String type = elt.getAttributeValue("type");
         String path = elt.getAttributeValue("path");
         String name = elt.getAttributeValue("name");
         if ("img".equals(type)) {
            PictureMetadata meta = new PictureMetadata();
            meta.setCredit(getFirstChildValue(elt, "credit"));
            meta.setCaption(getFirstChildValue(elt, "caption"));
            addImageFile(path, name, meta);
         }
         else if ("doc".equals(type)) {
            addDocument(path, name);
         }
      }
   }

   private void processMetadata(Element metaElement) {
      ArticleMetadata meta = article.getArticleMetadata();

      Element genElt = getFirstChild(metaElement, "general");
      if (genElt != null) {
         meta.setAuthor(notNull(getFirstChildValue(genElt, "author")));
         meta.setKeyword(notNull(getFirstChildValue(genElt, "keyword")));
         meta.setCountry(notNull(getFirstChildValue(genElt, "country")));
         meta.setCity(notNull(getFirstChildValue(genElt, "city")));
         meta.setAddress(notNull(getFirstChildValue(genElt, "address")));
      }

      Element webElt = getFirstChild(metaElement, "web");
      if (webElt != null) {
         meta.setType(notNull(getFirstChildValue(webElt, "type")));
         meta.setWebProfile(NumUtils.intValue(getFirstChildValue(webElt, "profile"), meta.getWebProfile()));
         meta.setWebComment(NumUtils.intValue(getFirstChildValue(webElt, "comment"), meta.getWebComment()));
         meta.setWebTopic(notNull(getFirstChildValue(webElt, "topic")));
      }
   }

   private void processArticle(Element articleElement) {
      try {
         String dateValue = articleElement.getAttributeValue("date");
         SimpleDateFormat fmt = new SimpleDateFormat(AppManager.DATE_FORMAT);
         article.setCreationDate(fmt.parse(dateValue));
      }
      catch (ParseException ex) {
         logger.warn(null, ex);
      }
   }

   private void addDocument(String path, String name) throws ParserConfigurationException, SAXException, IOException {
      XMLReader parser = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
      RDocumentHandler xmh = new RDocumentHandler(article.getDocument());
      parser.setContentHandler(xmh);
      parser.setErrorHandler(xmh);
      parser.setFeature("http://xml.org/sax/features/validation", false);
      parser.setFeature("http://xml.org/sax/features/namespaces", false);
      parser.parse(new InputSource(zipFile.getInputStream(zipFile.getEntry(path))));
   }

   private void addImageFile(String path, String name, PictureMetadata meta) {
      try {
         String ext = FilenameUtils.getExtension(name);
         if (ext.length() < 3) {
            ext = "img";
         }
         File file = File.createTempFile("redit_", "." + ext);
         file.deleteOnExit();
         copyStreamToFile(zipFile.getInputStream(zipFile.getEntry(path)), file);
         ImageFile imageFile = new ImageFile(file, name);
         imageFile.setProperties(meta);

         article.getExplorerModel().addImageFile(imageFile);
      }
      catch (IOException ex) {
         logger.warn("", ex);
         GuiUtils.showMessage(Message.get("StructureHandler.AddImageFileError"));
      }
   }

   private static void copyStreamToFile(InputStream is, File file) throws IOException {
      try (FileOutputStream os = new FileOutputStream(file)) {
         IOUtils.copy(is, os);
      }
   }

   private static Element getFirstChild(Element root, String... names) {
      Element child = root;
      for (String name : names) {
         if (child == null) {
            return null;
         }
         child = child.getFirstChildElement(name);
      }
      return child;
   }

   private static String getFirstChildValue(Element root, String... names) {
      Element child = getFirstChild(root, names);
      return child == null ? null : child.getValue();
   }

   public Article read(String path) throws IOException {

      zipFile = null;

      try {
         zipFile = new ZipFile(path);
         ZipEntry entry = zipFile.getEntry("structure.xml");
         if (entry == null) {
            throw new IOException(Message.get("ZipReader.StructureNotFound"));
         }
         parseStructure(zipFile.getInputStream(entry));
      }
      catch (ParsingException | ParserConfigurationException | SAXException ex) {
         throw new IOException(ex);
      }
      finally {
         if (zipFile != null) {
            zipFile.close();
         }
      }

      article.setPath(path);
      return article;
   }

   private String notNull(String str) {
      return (str == null) ? null : str;
   }
}
