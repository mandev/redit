/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit.gui.dialog;

import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.utils.ExtFilter;
import com.adlitteram.jasmin.utils.GuiUtils;
import com.adlitteram.jasmin.utils.NumUtils;
import com.adlitteram.redit.*;
import com.adlitteram.redit.action.OpenArticle;
import com.adlitteram.redit.inputfilter.RdzMetadataReader;
import cz.autel.dmi.HIGConstraints;
import cz.autel.dmi.HIGLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestoreDialog extends JDialog {

   private static final Logger logger = LoggerFactory.getLogger(RestoreDialog.class);

   private static final String[] TABLE_HEADER = {Message.get("RestoreDialog.Name"), Message.get("RestoreDialog.Date"), Message.get("RestoreDialog.Size")};

   private final AppManager appManager;
   private Article article;
   private JTable restoreTable;
   private ArrayList<Metadata> metadataList;

   public static void create(AppManager appManager) {
      RestoreDialog dialog = new RestoreDialog(appManager);
      dialog.setVisible(true);
   }

   private RestoreDialog(AppManager appManager) {
      super(appManager.getMainFrame(), Message.get("RestoreDialog.Title"), true);
      this.appManager = appManager;

      BackupThread.suspendBackup();

      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      getContentPane().add(buildGeneralPanel(), BorderLayout.CENTER);
      getContentPane().add(buildButtonPanel(), BorderLayout.SOUTH);

      pack();
      setLocationRelativeTo(getParent());
      BackupThread.resumeBackup();
   }

   private JPanel buildButtonPanel() {

      JButton okButton = new JButton(Message.get("RestoreDialog.Open"));
      getRootPane().setDefaultButton(okButton);
      okButton.addActionListener((ActionEvent e) -> {
         okPressed();
      });

      JButton closeButton = new JButton(Message.get("Close"));
      closeButton.addActionListener((ActionEvent e) -> {
         closePressed();
      });

      int w[] = {5, 0, 5, -6, 5, -4, 5};
      int h[] = {5, 0, 10};
      HIGLayout l = new HIGLayout(w, h);
      HIGConstraints c = new HIGConstraints();
      l.setColumnWeight(3, 1);

      JPanel buttonPanel = new JPanel(l);
      buttonPanel.add(okButton, c.xy(4, 2));
      buttonPanel.add(closeButton, c.xy(6, 2));
      return buttonPanel;
   }

   private JPanel buildGeneralPanel() {

      AbstractTableModel model = createRestoreTableModel();

      TableRowSorter<AbstractTableModel> sorter = new TableRowSorter<>(model);
      List<RowSorter.SortKey> sortKeys = new ArrayList<>();
      sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
      sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
      sorter.setSortKeys(sortKeys);

      restoreTable = new JTable(model);
      restoreTable.setDefaultRenderer(Date.class, createDateRenderer());
      restoreTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      restoreTable.setPreferredScrollableViewportSize(new Dimension(500, 300));
      restoreTable.getColumnModel().getColumn(0).setPreferredWidth(240);
      restoreTable.getColumnModel().getColumn(1).setPreferredWidth(160);
      restoreTable.getColumnModel().getColumn(2).setPreferredWidth(100);
      restoreTable.setFillsViewportHeight(true);
      restoreTable.setRowSorter(sorter);

      // Gui
      int layouts[] = {10, 0, 10};
      int constraints[] = {10, 0, 10};

      HIGLayout l = new HIGLayout(layouts, constraints);
      HIGConstraints c = new HIGConstraints();
      l.setColumnWeight(2, 1);
      l.setRowWeight(2, 1);

      JPanel panel = new JPanel(l);
      panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createEtchedBorder()));

      panel.add(new JScrollPane(restoreTable), c.xy(2, 2));
      return panel;
   }

   private DefaultTableCellRenderer createDateRenderer() {
      return new DefaultTableCellRenderer() {
         private final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

         @Override
         public void setValue(Object value) {
            setText((value == null) ? "" : formatter.format(value));
         }
      };
   }

   private AbstractTableModel createRestoreTableModel() {

      File[] files = new File(ReditApplication.USER_BCK_DIR).listFiles((FileFilter) new ExtFilter("rdz", "Redit files"));
      metadataList = new ArrayList<>(files.length);
      for (File file : files) {
         try {
            Metadata metadata = RdzMetadataReader.read(file);
            metadata.setFilename(file.getPath());
            metadataList.add(metadata);
         }
         catch (IOException ex) {
            logger.warn("Unable to open restaured file: " + file, ex);
         }
      }

      return new AbstractTableModel() {
         @Override
         public String getColumnName(int c) {
            return TABLE_HEADER[c];
         }

         @Override
         public Object getValueAt(int row, int col) {
            switch (col) {
               case 0:
                  return metadataList.get(row).getName();
               case 1:
                  return metadataList.get(row).getDate();
               default:
                  long len = metadataList.get(row).getLength();
                  return (len < 0) ? "" : NumUtils.toByteSize(len);
            }
         }

         @Override
         public Class getColumnClass(int c) {
            switch (c) {
               case 0:
                  return String.class;
               case 1:
                  return Date.class;
               default:
                  return long.class;
            }
         }

         @Override
         public boolean isCellEditable(int row, int col) {
            return false;
         }

         @Override
         public int getRowCount() {
            return metadataList.size();
         }

         @Override
         public int getColumnCount() {
            return TABLE_HEADER.length;
         }
      };
   }

   private void closePressed() {
      if (article != null) {
         article.setDirty();
         article.setBackupClean();
      }

      appManager.getMainFrame().requestTextFocus();
      appManager.getActionManager().enableActions();
      dispose();
   }

   private void okPressed() {

      int viewRow = restoreTable.getSelectedRow();

      if (viewRow >= 0) {
         try {
            int modelRow = restoreTable.convertRowIndexToModel(viewRow);
            Metadata metadata = metadataList.get(modelRow);
            OpenArticle.action(appManager, new File(metadata.getFilename()), true);
            article = appManager.getArticle();
         }
         catch (IOException ex) {
            logger.warn("", ex);
            GuiUtils.showError(Message.get("OpenArticle.Error") + "\n" + ex.getLocalizedMessage());
         }
      }
   }
}