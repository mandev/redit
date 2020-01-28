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
import com.adlitteram.jasmin.property.XProp;
import com.adlitteram.jasmin.gui.widget.KComboBox;
import com.adlitteram.jasmin.gui.widget.ObjectString;
import com.adlitteram.jasmin.utils.GuiUtils;
import com.adlitteram.jasmin.utils.StrUtils;
import com.adlitteram.redit.*;
import com.adlitteram.redit.gui.MonitorDialog;
import com.adlitteram.redit.gui.UploadListener;
import com.adlitteram.redit.gui.panel.GeneralPropertiesPanel;
import com.adlitteram.redit.gui.panel.WebPropertiesPanel;
import com.adlitteram.redit.inputfilter.Response;
import com.adlitteram.redit.outputfilter.EidosZipArticleWriter;
import cz.autel.dmi.HIGConstraints;
import cz.autel.dmi.HIGLayout;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadDialog extends JDialog {

    private static final Logger logger = LoggerFactory.getLogger(UploadDialog.class);

    private final AppManager appManager;
    private final Article article;
    private final ArticleMetadata meta;

    private JTextField docField;
    private KComboBox columnCombo;
    private JCheckBox sendPictureCheck;
    private GeneralPropertiesPanel generalPanel;
    private WebPropertiesPanel webPanel;
    private JTabbedPane tabbedPane;

    public static void create(AppManager appManager) {
        UploadDialog dialog = new UploadDialog(appManager);
        dialog.setVisible(true);
    }

    private UploadDialog(AppManager appManager) {
        super(appManager.getMainFrame(), Message.get("UploadDialog.Title"), true);
        this.appManager = appManager;
        this.article = appManager.getArticle();
        this.meta = article.getArticleMetadata();

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buildGeneralPanel(), BorderLayout.CENTER);
        panel.add(buildTabbedPane(), BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buildButtonPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getParent());
    }

    private JPanel buildButtonPanel() {

        JButton okButton = new JButton(Message.get("UploadDialog.Upload"));
        getRootPane().setDefaultButton(okButton);
        okButton.addActionListener(e -> okPressed());

        JButton cancelButton = new JButton(Message.get("Cancel"));
        cancelButton.addActionListener(e -> cancelPressed());

        int w[] = {5, 0, 5, -6, 5, -4, 5};
        int h[] = {5, 0, 10};
        HIGLayout l = new HIGLayout(w, h);
        HIGConstraints c = new HIGConstraints();
        l.setColumnWeight(3, 1);

        JPanel buttonPanel = new JPanel(l);
        buttonPanel.add(okButton, c.xy(4, 2));
        buttonPanel.add(cancelButton, c.xy(6, 2));
        return buttonPanel;
    }

    private JPanel buildGeneralPanel() {

        User user = appManager.getUser();

        JTextField userField = new JTextField(30);
        userField.setText(user.getUserName() + " [" + user.getUnitDescription() + "]");
        userField.setEditable(false);

        JButton userButton = new JButton("...");
        userButton.addActionListener( e -> {
            dispose();
            ConnectDialog.create(appManager);
        });

        docField = new JTextField(30);
        docField.setText(FilenameUtils.getBaseName(appManager.getArticle().getName()));

        ObjectString[] objs = getColumns();
        columnCombo = new KComboBox(objs);
        columnCombo.setSelectedObject(getColumn(objs, user.getColumnName()));

        sendPictureCheck = new JCheckBox(Message.get("UploadDialog.SendPicture"));
        sendPictureCheck.setSelected(XProp.getBoolean("Upload.SendPicture", true));
        sendPictureCheck.setEnabled(appManager.getArticle().getExplorerModel().size() > 0);

        // Gui
        int layouts[] = {10, 0, 5, 0, 5, 0, 10};
        int constraints[] = {10, 0, 5, 0, 5, 0, 5, 0, 10};

        HIGLayout l = new HIGLayout(layouts, constraints);
        HIGConstraints c = new HIGConstraints();
        l.setColumnWeight(4, 1);

        JPanel panel = new JPanel(l);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createEtchedBorder()));

        panel.add(new JLabel(Message.get("UploadDialog.User")), c.xy(2, 2, "r"));
        panel.add(userField, c.xy(4, 2));
        panel.add(userButton, c.xy(6, 2));

        panel.add(new JLabel(Message.get("UploadDialog.Docname")), c.xy(2, 4, "r"));
        panel.add(docField, c.xywh(4, 4, 3, 1));
        panel.add(new JLabel(Message.get("UploadDialog.Column")), c.xy(2, 6, "r"));
        panel.add(columnCombo, c.xywh(4, 6, 3, 1));
        panel.add(sendPictureCheck, c.xywh(4, 8, 3, 1));
        return panel;
    }

    private JTabbedPane buildTabbedPane() {
        generalPanel = new GeneralPropertiesPanel(meta);
        webPanel = new WebPropertiesPanel(meta);

        tabbedPane = new JTabbedPane();
        //tabbedPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createEtchedBorder()));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        tabbedPane.add(Message.get("ArticleMetadataDialog.GeneralProperties"), generalPanel);
        tabbedPane.add(Message.get("ArticleMetadataDialog.WebProperties"), webPanel);
        return tabbedPane;
    }

    private Column getColumn(ObjectString[] objs, String columName) {
        if (columName == null) {
            return null;
        }
        for (ObjectString obj : objs) {
            Column column = (Column) obj.getObject();
            if (columName.equals(column.getColumnName())) {
                return column;
            }
        }
        return null;
    }

    private ObjectString[] getColumns() {
        User user = appManager.getUser();
        if (user == null) {
            return new ObjectString[0];
        }

        Group group = user.getGroup();
        if (group == null) {
            return new ObjectString[0];
        }

        ArrayList<Column> columnList = group.getColumnList();
        ObjectString[] obs = new ObjectString[columnList.size()];
        for (int i = 0; i < obs.length; i++) {
            Column column = columnList.get(i);
            obs[i] = new ObjectString(column, column.getColumnDescription());
        }
        return obs;
    }

    private void setBusy(boolean b) {
        if (b) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            columnCombo.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
        else {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            columnCombo.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        }
    }

    private void cancelPressed() {
        dispose();
    }

    private void okPressed() {
        article.setDirty();

        meta.setAuthor(generalPanel.getAuthor());
        meta.setCountry(generalPanel.getCountry());
        meta.setCity(generalPanel.getCity());
        meta.setAddress(generalPanel.getAddress());
        meta.setKeyword(generalPanel.getKeyword());
        meta.setWebProfile(webPanel.getWebProfile());
        meta.setWebComment(webPanel.getWebComment());
        meta.setWebPosition(webPanel.getWebPosition());
        meta.setType(webPanel.getType());

        // TODO : implement multi-categories
        Column column = (Column) columnCombo.getSelectedObject();
        String topic = column.getTextStorageDirectory() + ";;" + webPanel.getWebPosition() + "*";
        meta.setWebTopic(topic);

        if (docField.getText().length() < 3) {
            GuiUtils.showError(Message.get("UploadDialog.DocnameError"));
            return;
        }

        if (sendPictureCheck.isSelected()) {
            User user = appManager.getUser();
            if (!user.getGroup().isAccessPictureAssociation()) {
                if (article.getExplorerModel().size() > 0) {
                    int option = JOptionPane.showOptionDialog(Main.getApplication().getMainFrame(), Message.get("UploadDialog.Continue"),
                            Message.get("UploadDialog.CannotSendPicture"), JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE, null, null, null);

                    if (option == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                    else if (option == JOptionPane.NO_OPTION) {
                        dispose();
                        return;
                    }
                }
            }
        }

        XProp.put("Upload.SendPicture", sendPictureCheck.isSelected());

        setBusy(true);
        MonitorDialog monitor = new MonitorDialog(this, Message.get("MonitorDialog.SendingToEidos"));
        monitor.setMessage(Message.get("MonitorDialog.Compressing"));

        sendFile(monitor, sendPictureCheck.isSelected());
        monitor.setVisible(true);
        setBusy(false);
    }

    private void sendFile(final MonitorDialog monitor, final boolean sendPicture) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                User user = appManager.getUser();
                String docName = StrUtils.toFilename(docField.getText());
                docName = docName.replace(".", "_").replace("!", "_").replace("%", "_");

                Column column = (Column) columnCombo.getSelectedObject();
                user.setColumnName(column.getColumnName());

                UploadListener uploadListner = null;
                File file = null;

                try {
                    // Export File
                    file = File.createTempFile("redit_", ".zip");
                    file.deleteOnExit();
                    EidosZipArticleWriter articleWriter = new EidosZipArticleWriter(article, user, column, docName, sendPicture);
                    articleWriter.write(file.getPath());

                    // Upload File
                    UploadManager uploadManager = appManager.getRemoteManager();
                    uploadListner = new UploadListener(monitor);
                    Response response = uploadManager.uploadFile(user.getUserName(), user.getPassword(), column.getColumnName(), docName, file, uploadListner);

                    // Clean
                    file.delete();
                    setBusy(false);

                    // Analyse Response
                    if (response != null && response.getError() == 0) {
                        dispose();
                        GuiUtils.showMessage(Message.get("UploadDialog.UploadSuccess"));
                    }
                    else if (!uploadListner.isCanceled()) {
                        String message = Message.get("UploadDialog.ConnectionError");
                        if (response != null) {
                            message += " : " + response.getComment();
                        }
                        GuiUtils.showError(message);
                    }
                }
                catch (IOException ex) {
                    logger.warn("UploadDialog.uploadFile()", ex);
                    if (file != null) {
                        file.delete();
                    }
                    setBusy(false);

                    if (uploadListner == null || !uploadListner.isCanceled()) {
                        dispose();
                        logger.warn("UploadDialog.uploadFile()", ex);
                        GuiUtils.showError(Message.get("UploadDialog.ConnectionError") + "\n" + ex.getLocalizedMessage());
                    }
                }
            }
        };
        thread.start();
    }
}
