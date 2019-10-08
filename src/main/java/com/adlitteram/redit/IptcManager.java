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
import com.adlitteram.jasmin.utils.ExecUtils;
import com.adlitteram.jasmin.utils.StrUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IptcManager {

   private static final Logger logger = LoggerFactory.getLogger(IptcManager.class);
   //
   public static final String EXIFTOOL_WIN = ReditApplication.PROG_DIR + "ext/windows/exiftool.exe";
   public static final String EXIFTOOL_LIN = ReditApplication.PROG_DIR + "ext/linux/exiftool";
   public static final String EXIFTOOL_MAC = ReditApplication.PROG_DIR + "ext/macosx/exiftool";
   //
   public static final String IPTC1 = "1IPTC:";
   //
   public static final String RECORD_VERSION = IPTC1 + "RecordVersion";
   public static final String OBJECT_TYPE = IPTC1 + "ObjectType";
   public static final String OBJECT_ATTRIBUTE = IPTC1 + "ObjectAttributev";
   public static final String OBJECT_NAME = IPTC1 + "ObjectName";
   public static final String EDIT_STATUS = IPTC1 + "EditStatus";
   public static final String EDITORIAL_UPDATE = IPTC1 + "EditorialUpdate";
   public static final String URGENCY = IPTC1 + "Urgency";
   public static final String SUBJECT = IPTC1 + "Subject";
   public static final String CATEGORY = IPTC1 + "Category";
   public static final String SUPP_CATEGORY = IPTC1 + "SuppCategory";
   public static final String FIXTURE_ID = IPTC1 + "FixtureId";
   public static final String KEYWORDS = IPTC1 + "Keywords";
   public static final String LOCATION_CODE = IPTC1 + "LocationCode";
   public static final String LOCATION_NAME = IPTC1 + "LocationName";
   public static final String RELEASE_DATE = IPTC1 + "ReleaseDate";
   public static final String RELEASE_TIME = IPTC1 + "ReleaseTime";
   public static final String EXPIRATION_DATE = IPTC1 + "ExpirationDate";
   public static final String EXPIRATION_TIME = IPTC1 + "ExpirationTime";
   public static final String SPECIAL_INSTRUCTIONS = IPTC1 + "SpecialInstructions";
   public static final String ACTION_ADVISED = IPTC1 + "ActionAdvised";
   public static final String REFERENCE_SERVICE = IPTC1 + "ReferenceService";
   public static final String REFERENCE_DATE = IPTC1 + "ReferenceDate";
   public static final String REFRENCE_NUMBER = IPTC1 + "ReferenceNumber";
   public static final String DATE_CREATED = IPTC1 + "DateCreated";
   public static final String TIME_CREATED = IPTC1 + "TimeCreated";
   public static final String DIGITIZATION_DATE = IPTC1 + "DigitizationDate";
   public static final String DIGITIZATION_TIME = IPTC1 + "DigitizationTime";
   public static final String PROGRAM = IPTC1 + "Program";
   public static final String PROGRAM_VERSION = IPTC1 + "ProgramVersion";
   public static final String OBJECT_CYCLE = IPTC1 + "ObjectCycle";
   public static final String BYLINE = IPTC1 + "By-line";
   public static final String BYLINE_TITLE = IPTC1 + "By-lineTitle";
   public static final String CITY = IPTC1 + "City";
   public static final String SUBLOCATION = IPTC1 + "SubLocation";
   public static final String PROVINCE_STATE = IPTC1 + "Province-State";
   public static final String COUNTRY_CODE = IPTC1 + "Country-PrimaryLocationCode";
   public static final String COUNTRY_NAME = IPTC1 + "Country-PrimaryLocationName";
   public static final String TRANSMISSION_REFERENCE = IPTC1 + "TransmissionReference";
   public static final String HEADLINE = IPTC1 + "Headline";
   public static final String CREDIT = IPTC1 + "Credit";
   public static final String SOURCE = IPTC1 + "Source";
   public static final String COPYRIGHT = IPTC1 + "Copyright";
   public static final String CONTACT = IPTC1 + "Contact";
   public static final String CAPTION = IPTC1 + "Caption-Abstract";
   public static final String WRITER = IPTC1 + "Writer";
   public static final String RASTERIZED_CAPTION = IPTC1 + "RasterizedCaption";
   public static final String IMAGE_TYPE = IPTC1 + "ImageType";
   public static final String IMAGE_ORIENTATION = IPTC1 + "ImageOrientation";
   public static final String LANGUAGE = IPTC1 + "Language";
   public static final String AUDIO_TYPE = IPTC1 + "AudioType";
   public static final String AUDIO_RATE = IPTC1 + "AudioRate";
   public static final String AUDIO_RESOLUTION = IPTC1 + "AudioResolution";
   public static final String AUDIO_DURATION = IPTC1 + "AudioDuration";
   public static final String AUDIO_OUTCUE = IPTC1 + "AudioOutcue";
   public static final String PREVIEW_FORMAT = IPTC1 + "PreviewFormat";
   public static final String PREVIEW_VERSION = IPTC1 + "PreviewVersion";
   public static final String PREVIEW = IPTC1 + "Preview";
   //
   static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");
   //

   private static void writeTag(Writer out, String tag, String value) throws IOException {
      if (value != null && value.length() > 0) {
         out.write("-" + tag + "=" + StrUtils.toAscii(value) + "\n");
      }
   }

   private static void writeTag(Writer out, String tag, Date value) throws IOException {
      if (value != null) {
         out.write("-" + tag + "=" + dateFormat.format(value) + "\n");
      }
   }

   public static void writeIptcData(IptcData iptcData, File file) throws IOException {
      logger.info("ENTRY");

      try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "US-ASCII"))) {
         writeTag(out, HEADLINE, iptcData.getHeadline());
         writeTag(out, CAPTION, iptcData.getCaption());
         writeTag(out, CREDIT, iptcData.getCredit());
         writeTag(out, OBJECT_NAME, iptcData.getSubject());
         writeTag(out, SPECIAL_INSTRUCTIONS, iptcData.getSpecial());
         writeTag(out, SOURCE, iptcData.getSource());
         writeTag(out, BYLINE, iptcData.getByLine());
         writeTag(out, BYLINE_TITLE, iptcData.getByLineTitle());
         writeTag(out, CITY, iptcData.getCity());
         writeTag(out, PROVINCE_STATE, iptcData.getState());
         writeTag(out, COUNTRY_NAME, iptcData.getCountry());
         writeTag(out, COUNTRY_CODE, iptcData.getCountryCode());

         writeTag(out, DATE_CREATED, iptcData.getCreatedDate());
         writeTag(out, RELEASE_DATE, iptcData.getReleasedDate());
      }

      logger.info("RETURN");
   }

   public static void writeIptcFields(IptcField[] iptcFields, File file) throws IOException {
      logger.info("ENTRY");

      try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "US-ASCII"))) {
         for (IptcField iptcField : iptcFields) {
            writeTag(out, iptcField.getLabel(), iptcField.getValue());
         }
      }

      logger.info("RETURN");
   }

   public static void setIptcDataToFile(IptcData iptcData, File file) throws IOException, InterruptedException {
      File iptcFile = null;
      try {
         iptcFile = File.createTempFile("iptc_", ".tmp");
         iptcFile.deleteOnExit();
         writeIptcData(iptcData, iptcFile);
         setIptcFile(iptcFile, file);
      }
      finally {
         if (iptcFile != null && iptcFile.exists()) {
            iptcFile.delete();
         }
      }
   }

   public static void setIptcFieldsToFile(IptcField[] iptcFields, File imageFile) throws IOException {
      File iptcFile = null;
      try {
         iptcFile = File.createTempFile("iptc_", ".tmp");
         iptcFile.deleteOnExit();
         writeIptcFields(iptcFields, iptcFile);
         setIptcFile(iptcFile, imageFile);
      }
      finally {
         if (iptcFile != null && iptcFile.exists()) {
            iptcFile.delete();
         }
      }
   }

   public static void setIptcFile(File iptcFile, File file) throws IOException {
      String exe = SystemUtils.IS_OS_WINDOWS ? EXIFTOOL_WIN : SystemUtils.IS_OS_MAC_OSX ? EXIFTOOL_MAC : EXIFTOOL_LIN;
      String[] cmd = {"-overwrite_original", "-@", iptcFile.getPath(), file.getPath()};

      logger.info("Exec: {} {}", exe, Arrays.asList(cmd));
      ExecUtils.exec(exe, cmd);
   }
}
