package com.adlitteram.redit.gui.dialog;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IptcDialog extends JDialog {

    private static final Logger logger = LoggerFactory.getLogger(IptcDialog.class);

    private static final DateFormat[] DATE_FORMATS = {
        new SimpleDateFormat("yyyyMMdd"), new SimpleDateFormat("yyyyMM"), new SimpleDateFormat("yyyy"),
        new SimpleDateFormat("yyyy:MM:dd HH:mm:ss"), new SimpleDateFormat("yyyy:MM:dd HH:mm"), new SimpleDateFormat("yyyy:MM:dd"),
        new SimpleDateFormat("yyyy:MM"),
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd HH:mm"), new SimpleDateFormat("yyyy-MM-dd"),
        new SimpleDateFormat("yyyy-MM"),
        new SimpleDateFormat("yyyyMMdd HH:mm:ss"), new SimpleDateFormat("yyyyMMdd HH:mm"),};

    private final Article article;
    private final File[] files;

    private JTextField headLineField;           // Numero de reportage
    private JTextField subjectField;            // Sujet
    private JTextArea captionArea;              // Legende
    private JTextArea specialArea;              // Instructions speciales
    private JTextField creditField;             // Credit
    private JTextField sourceField;             // Source
    private JTextField writerField;             // Byline- Caption Writer
    private JTextField byLineField;             // ByLine
    private JTextField cityField;               // Ville
    private JTextField stateField;              // departement
    private JComboBox countryCodeCombo;         // Code pays
    private JTextField countryNameField;        // Pays
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

        // TODO retrieve IPTC metadatas from files
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().add(buildGeneralPanel(), BorderLayout.CENTER);
        getContentPane().add(buildButtonPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getParent());
    }

    private JPanel buildButtonPanel() {
        JButton okButton = new JButton(Message.get("Ok"));
        getRootPane().setDefaultButton(okButton);
        okButton.addActionListener(e -> okPressed());

        JButton cancelButton = new JButton(Message.get("Cancel"));
        cancelButton.addActionListener(e -> cancelPressed());

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

    private JPanel buildGeneralPanel() {

        // Image
        headLineField = new JTextField(40);
        headLineField.setText("HEADLINE");

        subjectField = new JTextField(40);
        subjectField.setText("OBJECT_NAME");

        captionArea = new JTextArea();
        GuiUtils.invertFocusTraversalBehaviour(captionArea);
        captionArea.setLineWrap(true);
        captionArea.setWrapStyleWord(true);
        captionArea.setText("CAPTION");

        writerField = new JTextField(40);
        writerField.setText("WRITER");

        specialArea = new JTextArea();
        GuiUtils.invertFocusTraversalBehaviour(specialArea);
        specialArea.setLineWrap(true);
        specialArea.setWrapStyleWord(true);
        specialArea.setText("SPECIAL_INSTRUCTIONS");

        // Credits
        byLineField = new JTextField(30);
        byLineField.setText("BYLINE");

        byLineTitleField = new JTextField(30);
        byLineTitleField.setText("BYLINE_TITLE");

        creditField = new JTextField(30);
        creditField.setText("CREDIT");

        sourceField = new JTextField(30);
        sourceField.setText("SOURCE");

        //  Origin
        cityField = new JTextField(30);
        cityField.setText("CITY");

        stateField = new JTextField(30);
        stateField.setText("PROVINCE");

        countryCodeCombo = new JComboBox(IptcCountry.COUNTRIES);
        countryCodeCombo.setSelectedItem("COUNTRY_CODE");
        countryCodeCombo.addActionListener((ActionEvent e) -> {
            IptcCountry country = (IptcCountry) countryCodeCombo.getSelectedItem();
            countryNameField.setText(country.getName());
        });

        countryNameField = new JTextField(30);
        countryNameField.setText("COUNTRY_NAME");

        createdDateChooser = new JDateChooser(null, "d MMMMM yyyy");
        Date cdate = new Date(); // DATE_CREATED
        if (cdate != null) {
            createdDateChooser.setDate(cdate);
        }

        releaseDateChooser = new JDateChooser(null, "d MMMMM yyyy");
        Date rdate = new Date(); // RELEASE_DATE
        if (rdate != null) {
            releaseDateChooser.setDate(rdate);
        }

        copyrightField = new JTextField(30);
        copyrightField.setText("COPYRIGHT_NOTICE");

        urgencyField = new JTextField(30);
        urgencyField.setText("URGENCY");

        transmissionRefField = new JTextField(30);
        transmissionRefField.setText("ORIGINAL_TRANSMISSION_REF");

        programField = new JTextField(30);
        programField.setText("ORIGINATING_PROGRAM");

        keywordsField = new JTextArea();
        keywordsField.setLineWrap(true);
        keywordsField.setWrapStyleWord(true);
        keywordsField.setText("KEYWORDS");

        categoryField = new JTextField(30);
        categoryField.setText("CATEGORY");

        subCategoryField = new JTextArea();
        subCategoryField.setLineWrap(true);
        subCategoryField.setWrapStyleWord(true);
        subCategoryField.setText("SUPPLEMENTAL_CATEGORIES");

        // Gui
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
