package com.adlitteram.redit.gui.dialog;

import com.adlitteram.jasmin.Message;
import com.adlitteram.imagetool.metadata.IptcMetadata;
import com.adlitteram.imagetool.metadata.iptctag.IptcTag;
import com.adlitteram.imagetool.writer.jpeg.JpegImageWriter;
import com.adlitteram.jasmin.utils.GuiUtils;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.IptcCountry;
import com.adlitteram.redit.IptcData;
import com.adlitteram.redit.IptcManager;
import com.toedter.calendar.JDateChooser;
import cz.autel.dmi.HIGConstraints;
import cz.autel.dmi.HIGLayout;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IptcDialog extends JDialog {

   private static final Logger logger = LoggerFactory.getLogger(IptcDialog.class);
   //
   private static final DateFormat[] DATE_FORMATS = {
      new SimpleDateFormat("yyyyMMdd"), new SimpleDateFormat("yyyyMM"), new SimpleDateFormat("yyyy"),
      new SimpleDateFormat("yyyy:MM:dd HH:mm:ss"), new SimpleDateFormat("yyyy:MM:dd HH:mm"), new SimpleDateFormat("yyyy:MM:dd"),
      new SimpleDateFormat("yyyy:MM"),
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd HH:mm"), new SimpleDateFormat("yyyy-MM-dd"),
      new SimpleDateFormat("yyyy-MM"),
      new SimpleDateFormat("yyyyMMdd HH:mm:ss"), new SimpleDateFormat("yyyyMMdd HH:mm"),};
   //
   private final Article article;
   private final IptcMetadata[] metadatas;
   private final File[] files;
   //
   private JTextField headLineField;  // n� de reportage
   private JTextField subjectField;   // sujet
   private JTextArea captionArea;    // Legende
   private JTextArea specialArea;    // Instructions speciales
   private JTextField creditField;    // Credit
   private JTextField sourceField;    // Source
   private JTextField writerField;    // Byline- Caption Writer
   private JTextField byLineField;    // ByLine
   private JTextField cityField;         // Ville
   private JTextField stateField;        // departement
   private JComboBox countryCodeCombo;   // Code pays
   private JTextField countryNameField;  // Pays
   private JDateChooser createdDateChooser;
   private JTextArea keywordsField;
   private JTextField byLineTitleField;
   private JDateChooser releaseDateChooser;
   private JTextField transmissionRefField;
   private JTextField copyrightField;
   private JTextArea subCategoryField;
   private JTextField categoryField;
   private JTextField urgencyField;
   private JTextField programField;

   public static void create(Frame frame, Article article, File[] files) {
      IptcDialog dialog = new IptcDialog(frame, article, files);
      dialog.setVisible(true);
   }

   private IptcDialog(Frame frame, Article article, File[] files) {
      super(frame, Message.get("IptcDialog.Title"), true);
      this.article = article;
      this.files = files;

      metadatas = new IptcMetadata[files.length];
      for (int i = 0; i < files.length; i++) {
         metadatas[i] = JpegImageWriter.getIptcMetadata(files[i]);
         if (metadatas[i] == null) {
            metadatas[i] = new IptcMetadata();
         }
      }

      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      getContentPane().add(buildGeneralPanel(), BorderLayout.CENTER);
      getContentPane().add(buildButtonPanel(), BorderLayout.SOUTH);

      pack();
      setLocationRelativeTo(getParent());
   }

   private JPanel buildButtonPanel() {
      JButton okButton = new JButton(Message.get("Ok"));
      getRootPane().setDefaultButton(okButton);
      okButton.addActionListener((ActionEvent e) -> {
         okPressed();
      });

      JButton cancelButton = new JButton(Message.get("Cancel"));
      cancelButton.addActionListener((ActionEvent e) -> {
         cancelPressed();
      });

      int w0[] = {5, 0, 5, -6, 5, -4, 5};
      int h0[] = {5, 0, 10};
      HIGLayout l0 = new HIGLayout(w0, h0);
      HIGConstraints c0 = new HIGConstraints();
      l0.setColumnWeight(3, 1);

      JPanel buttonPanel = new JPanel(l0);
      buttonPanel.add(okButton, c0.xy(4, 2));
      buttonPanel.add(cancelButton, c0.xy(6, 2));
      return buttonPanel;
   }

   private boolean equalsOrNull(Object o1, Object o2) {
      if (o1 == o2) {
         return true;
      }
      return (o1 != null) ? o1.equals(o2) : false;
   }

   private String getMetaString(IptcTag tag) {
      String str = metadatas[0].getValue(tag);
      for (int i = 1; i < metadatas.length; i++) {
         String s = metadatas[i].getValue(tag);
         if (!equalsOrNull(str, s)) {
            return null;
         }
      }
      return str;
   }

   private Date getMetaDate(IptcTag tag) {
      Date date = getParsedDate(metadatas[0].getValue(tag));
      for (int i = 1; i < metadatas.length; i++) {
         Date d = getParsedDate(metadatas[i].getValue(tag));
         if (!equalsOrNull(date, d)) {
            return null;
         }
      }
      return date;
   }

   private Date getParsedDate(String date) {
      if (date != null) {
         for (DateFormat format : DATE_FORMATS) {
            try {
               return format.parse(date);
            }
            catch (ParseException ex) {
               // Just try the next format
            }
         }
      }
      return null;
   }

   private JPanel buildGeneralPanel() {

      // Image
      headLineField = new JTextField(40);
      headLineField.setText(getMetaString(IptcTag.HEADLINE));

      subjectField = new JTextField(40);
      subjectField.setText(getMetaString(IptcTag.OBJECT_NAME));

      captionArea = new JTextArea();
      GuiUtils.invertFocusTraversalBehaviour(captionArea);
      captionArea.setLineWrap(true);
      captionArea.setWrapStyleWord(true);
      captionArea.setText(getMetaString(IptcTag.CAPTION));

      writerField = new JTextField(40);
      writerField.setText(getMetaString(IptcTag.WRITER));

      specialArea = new JTextArea();
      GuiUtils.invertFocusTraversalBehaviour(specialArea);
      specialArea.setLineWrap(true);
      specialArea.setWrapStyleWord(true);
      specialArea.setText(getMetaString(IptcTag.SPECIAL_INSTRUCTIONS));

      // Credits
      byLineField = new JTextField(30);
      byLineField.setText(getMetaString(IptcTag.BYLINE));

      byLineTitleField = new JTextField(30);
      byLineTitleField.setText(getMetaString(IptcTag.BYLINE_TITLE));

      creditField = new JTextField(30);
      creditField.setText(getMetaString(IptcTag.CREDIT));

      sourceField = new JTextField(30);
      sourceField.setText(getMetaString(IptcTag.SOURCE));

      //  Origin
      cityField = new JTextField(30);
      cityField.setText(getMetaString(IptcTag.CITY));

      stateField = new JTextField(30);
      stateField.setText(getMetaString(IptcTag.PROVINCE));

      countryCodeCombo = new JComboBox(IptcCountry.COUNTRIES);
      countryCodeCombo.setSelectedItem(IptcCountry.getInstanceCode(getMetaString(IptcTag.COUNTRY_CODE)));
      countryCodeCombo.addActionListener((ActionEvent e) -> {
         IptcCountry country = (IptcCountry) countryCodeCombo.getSelectedItem();
         countryNameField.setText(country.getName());
      });

      countryNameField = new JTextField(30);
      countryNameField.setText(getMetaString(IptcTag.COUNTRY_NAME));

      createdDateChooser = new JDateChooser(null, "d MMMMM yyyy");
      Date cdate = getMetaDate(IptcTag.DATE_CREATED);
      if (cdate != null) {
         createdDateChooser.setDate(cdate);
      }

      releaseDateChooser = new JDateChooser(null, "d MMMMM yyyy");
      Date rdate = getMetaDate(IptcTag.RELEASE_DATE);
      if (rdate != null) {
         releaseDateChooser.setDate(rdate);
      }

      copyrightField = new JTextField(30);
      copyrightField.setText(getMetaString(IptcTag.COPYRIGHT_NOTICE));

      urgencyField = new JTextField(30);
      urgencyField.setText(getMetaString(IptcTag.URGENCY));

      transmissionRefField = new JTextField(30);
      transmissionRefField.setText(getMetaString(IptcTag.ORIGINAL_TRANSMISSION_REF));

      programField = new JTextField(30);
      programField.setText(getMetaString(IptcTag.ORIGINATING_PROGRAM));

      keywordsField = new JTextArea();
      keywordsField.setLineWrap(true);
      keywordsField.setWrapStyleWord(true);
      keywordsField.setText(getMetaString(IptcTag.KEYWORDS));

      categoryField = new JTextField(30);
      categoryField.setText(getMetaString(IptcTag.CATEGORY));

      subCategoryField = new JTextArea();
      subCategoryField.setLineWrap(true);
      subCategoryField.setWrapStyleWord(true);
      subCategoryField.setText(getMetaString(IptcTag.SUPPLEMENTAL_CATEGORIES));

      // Gui
      //int w0[] = {10, 0, 20, 0, 20, 0, 10};
      int w0[] = {10, 0, 20, 0, 10};
      int h0[] = {10, 0, 0, 5, 0, 0, 25, 0, 0, 5, 0, 0, 25, 0, 0, 5, 0, 0,
         5, 0, 0, 5, 0, 0, 25, 0, 0, 5, 0, 0, 0, 15
      };

      HIGLayout l0 = new HIGLayout(w0, h0);
      HIGConstraints c0 = new HIGConstraints();
      l0.setColumnWeight(3, 1);
      l0.setRowWeight(10, 1);
      l0.setRowWeight(22, 1);

      JPanel panel = new JPanel(l0);
      panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createEtchedBorder()));

      panel.add(new JLabel(Message.get("IptcDialog.HeadLine")), c0.xy(2, 2, "l"));
      panel.add(headLineField, c0.xy(2, 3));

      panel.add(new JLabel(Message.get("IptcDialog.Subject")), c0.xy(2, 5, "l"));
      panel.add(subjectField, c0.xy(2, 6));

      panel.add(new JLabel(Message.get("IptcDialog.Caption")), c0.xy(2, 8, "l"));
      panel.add(new JScrollPane(captionArea), c0.xywh(2, 9, 1, 16));

//      panel.add(new JLabel(Message.get("IptcDialog.Writer"), c0.xy(2, 23, "l"));
//      panel.add(writerField, c0.xy(2, 24));
      panel.add(new JLabel(Message.get("IptcDialog.Special")), c0.xy(2, 26, "l"));
      panel.add(new JScrollPane(specialArea), c0.xywh(2, 27, 1, 4));

      panel.add(new JLabel(Message.get("IptcDialog.ByLine")), c0.xy(4, 2, "l"));
      panel.add(byLineField, c0.xy(4, 3));

      panel.add(new JLabel(Message.get("IptcDialog.ByLineTitle")), c0.xy(4, 5, "l"));
      panel.add(byLineTitleField, c0.xy(4, 6));

      panel.add(new JLabel(Message.get("IptcDialog.Credit")), c0.xy(4, 8, "l"));
      panel.add(creditField, c0.xy(4, 9));

      panel.add(new JLabel(Message.get("IptcDialog.Source")), c0.xy(4, 11, "l"));
      panel.add(sourceField, c0.xy(4, 12));

      panel.add(new JLabel(Message.get("IptcDialog.City")), c0.xy(4, 14, "l"));
      panel.add(cityField, c0.xy(4, 15));

      panel.add(new JLabel(Message.get("IptcDialog.State")), c0.xy(4, 17, "l"));
      panel.add(stateField, c0.xy(4, 18));

      panel.add(new JLabel(Message.get("IptcDialog.CountryCode")), c0.xy(4, 20, "l"));
      panel.add(countryCodeCombo, c0.xy(4, 21));

      panel.add(new JLabel(Message.get("IptcDialog.Country")), c0.xy(4, 23, "l"));
      panel.add(countryNameField, c0.xy(4, 24));

      panel.add(new JLabel(Message.get("IptcDialog.CreatedDate")), c0.xy(4, 26, "l"));
      panel.add(createdDateChooser, c0.xy(4, 27));

      panel.add(new JLabel(Message.get("IptcDialog.ReleaseDate")), c0.xy(4, 29, "l"));
      panel.add(releaseDateChooser, c0.xy(4, 30));

//      panel.add(new JLabel(Message.get("IptcDialog.Copyright"), c0.xy(6, 2, "l"));
//      panel.add(copyrightField, c0.xy(6, 3));
//
//      panel.add(new JLabel(Message.get("IptcDialog.Urgency"), c0.xy(6, 5, "l"));
//      panel.add(urgencyField, c0.xy(6, 6));
//
//      panel.add(new JLabel(Message.get("IptcDialog.OriginalTransmissionReference"), c0.xy(6, 8, "l"));
//      panel.add(transmissionRefField, c0.xy(6, 9));
//
//      panel.add(new JLabel(Message.get("IptcDialog.OriginalProgram"), c0.xy(6, 11, "l"));
//      panel.add(programField, c0.xy(6, 12));
//
//      panel.add(new JLabel(Message.get("IptcDialog.Category"), c0.xy(6, 14, "l"));
//      panel.add(categoryField, c0.xy(6, 15));
//
//      panel.add(new JLabel(Message.get("IptcDialog.SubCategory"), c0.xy(6, 17, "l"));
//      panel.add(new JScrollPane(subCategoryField), c0.xywh(6, 18, 1, 7));
//
//      panel.add(new JLabel(Message.get("IptcDialog.Keywords"), c0.xy(6, 26, "l"));
//      panel.add(new JScrollPane(keywordsField), c0.xywh(6, 27, 1, 4));
      return panel;
   }

   private void cancelPressed() {
      dispose();
   }

   public void okPressed() {
      try {
         setBusy(true);
         writeIptc();
         setBusy(false);
         article.setDirty();
         dispose();

      }
      catch (IOException | InterruptedException ex) {
         logger.warn("", ex);
         setBusy(false);
         GuiUtils.showError(Message.get("IptcDialog.IptcError") + "\n" + ex.getLocalizedMessage());
      }
   }

   private void writeIptc() throws IOException, InterruptedException {
      IptcData iptcData = new IptcData();
      iptcData.setHeadline(headLineField.getText());
      iptcData.setSubject(subjectField.getText());
      iptcData.setCaption(captionArea.getText());
      iptcData.setSpecial(specialArea.getText());
      iptcData.setCredit(creditField.getText());
      iptcData.setSource(sourceField.getText());
      iptcData.setCity(cityField.getText());
      iptcData.setState(stateField.getText());
      iptcData.setCountry(countryNameField.getText());
      iptcData.setCountryCode(((IptcCountry) countryCodeCombo.getSelectedItem()).getCode());
      iptcData.setByLine(byLineField.getText());
      iptcData.setByLineTitle(byLineTitleField.getText());
      iptcData.setCreatedDate(createdDateChooser.getDate());
      iptcData.setReleasedDate(releaseDateChooser.getDate());

      File iptcFile = null;
      try {
         iptcFile = File.createTempFile("iptc_", ".tmp");
         iptcFile.deleteOnExit();
         IptcManager.writeIptcData(iptcData, iptcFile);
         for (File file : files) {
            IptcManager.setIptcFile(iptcFile, file);
         }
      }
      finally {
         if (iptcFile != null && iptcFile.exists()) {
            iptcFile.delete();
         }
      }

   }

   private void setBusy(boolean b) {
      Cursor cc = Cursor.getPredefinedCursor(b ? Cursor.WAIT_CURSOR : Cursor.DEFAULT_CURSOR);
      Cursor tc = Cursor.getPredefinedCursor(b ? Cursor.WAIT_CURSOR : Cursor.TEXT_CURSOR);

      setCursor(cc);
      headLineField.setCursor(tc);
      subjectField.setCursor(tc);
      captionArea.setCursor(tc);
      specialArea.setCursor(tc);
      creditField.setCursor(tc);
      sourceField.setCursor(tc);
      writerField.setCursor(tc);
      byLineField.setCursor(tc);
      cityField.setCursor(tc);
      stateField.setCursor(tc);
      countryNameField.setCursor(tc);
      keywordsField.setCursor(tc);
      byLineTitleField.setCursor(tc);
      transmissionRefField.setCursor(tc);
      copyrightField.setCursor(tc);
      subCategoryField.setCursor(tc);
      categoryField.setCursor(tc);
      urgencyField.setCursor(tc);
      programField.setCursor(tc);
   }
}
//
//    private void resetFields() {
//        filesCombo.setSelectedIndex(0);
//
//        headLineField.setText("");
//        subjectField.setText("");
//        captionField.setText("");
//        specialField.setText("");
//
//        creditField.setText("");
//        sourceField.setText("");
//        writerField.setText("");
//
//        cityField.setText("");
//        stateField.setText("");
//        countryCodeCombo.setSelectedIndex(1);
//        countryNameField.setText("France");
//
//        //createdDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date())) ;
//        createdDateChooser.setDate(new Date());
//        byLineField.setText("");
//    }

