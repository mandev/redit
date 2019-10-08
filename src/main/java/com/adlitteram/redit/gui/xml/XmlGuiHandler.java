/**
 * Copyright (C) 1999-2002 Emmanuel Deviller
 *
 * @version 1.0
 * @author Emmanuel Deviller
 */
package com.adlitteram.redit.gui.xml;

import com.adlitteram.jasmin.WidgetManager;
import com.adlitteram.jasmin.XProp;
import com.adlitteram.jasmin.gui.GuiBuilder;
import com.adlitteram.jasmin.gui.RecentFilesItems;
import com.adlitteram.redit.gui.MainFrame;
import com.adlitteram.redit.gui.StatusBar;
import com.adlitteram.redit.RecentFilesManager;
import java.awt.FlowLayout;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlGuiHandler extends DefaultHandler {

   private static final Logger logger = LoggerFactory.getLogger(XmlGuiHandler.class);
   //
   private final MainFrame mainFrame;
   private final WidgetManager widgetManager;
   private final GuiBuilder guiBuilder;
   //
   private Stack stateStack;

   public XmlGuiHandler(MainFrame mainFrame) {
      this.mainFrame = mainFrame;
      this.widgetManager = mainFrame.getWidgetManager();
      this.guiBuilder = mainFrame.getGuiBuilder();
   }

   public JComponent getComponent() {
      return (stateStack == null) ? null : (JComponent) stateStack.peek();
   }

   @Override
   public void startElement(String uri, String local, String raw, Attributes attrs) {
      if (raw.equalsIgnoreCase("item")) {
         JMenuItem item = guiBuilder.buildMenuItem(attrs.getValue("action"), attrs.getValue("icon"), attrs.getValue("shortcut"), attrs.getValue("label"),
                 attrs.getValue("tip"), null);
         if (item == null) {
            return;
         }
         ((JComponent) stateStack.peek()).add(item);
         widgetManager.registerWidget(item);
      }
      else if (raw.equalsIgnoreCase("checkitem")) {
         JCheckBoxMenuItem item = guiBuilder.buildCheckMenuItem(attrs.getValue("action"), attrs.getValue("icon"), attrs.getValue("shortcut"),
                 attrs.getValue("label"), attrs.getValue("tip"), null);

         if (item == null) {
            return;
         }
         ((JComponent) stateStack.peek()).add(item);
         widgetManager.registerWidget(item);
      }
      else if (raw.equalsIgnoreCase("listitem")) {
         String action = attrs.getValue("action").toLowerCase();
         if (action.equals("recentfiles")) {
            RecentFilesItems item = new RecentFilesItems((JMenu) stateStack.peek());
            RecentFilesManager rfm = mainFrame.getAppManager().getRecentFilesManager();
            item.setActionListener(rfm.getActionListener());
            item.setFilenameList(rfm.getFilenameList());
         }
      }
      else if (raw.equalsIgnoreCase("listbutton")) {
         String action = attrs.getValue("action");

         JToggleButton item = null;
         if (item == null) {
            return;
         }

         if ("true".equals(attrs.getValue("label"))) {
            item.setVerticalTextPosition(SwingConstants.BOTTOM);
            item.setHorizontalTextPosition(SwingConstants.CENTER);
            item.setFont(item.getFont().deriveFont(10f));
            item.setText(XProp.get(action + ".label"));
         }

         ((JComponent) stateStack.peek()).add(item);

         // mainFrame.registerWidget(item) ;
      }
      else if (raw.equalsIgnoreCase("button")) {
         JButton item = guiBuilder.buildButton(attrs.getValue("action"), attrs.getValue("icon"), attrs.getValue("shortcut"),
                 attrs.getValue("label"), attrs.getValue("tip"), null);
         if (item == null) {
            return;
         }
         ((JComponent) stateStack.peek()).add(item);
         widgetManager.registerWidget(item);
      }
      else if (raw.equalsIgnoreCase("togglebutton")) {
         JToggleButton item = guiBuilder.buildToggleButton(attrs.getValue("action"), attrs.getValue("icon"), attrs.getValue("shortcut"),
                 attrs.getValue("label"), attrs.getValue("tip"), null);
         if (item == null) {
            return;
         }
         ((JComponent) stateStack.peek()).add(item);
         widgetManager.registerWidget(item);
      }
      else if (raw.equalsIgnoreCase("separator")) {
         JComponent cp = (JComponent) stateStack.peek();

         // if ( cp instanceof JToolBar) cp.add(javax.swing.Box.createRigidArea(new Dimension(10,10))) ;
         if (cp instanceof JToolBar) {
            cp.add(javax.swing.Box.createHorizontalStrut(10));
         }
         else {
            cp.add(new JSeparator());
         }
      }
      else if (raw.equalsIgnoreCase("combo")) {
         String comboName = attrs.getValue("action").toLowerCase();
         JComboBox item = null;

//         if (comboName.equals("fontfamily")) item = new FontFamilyCombo("FontFamily") ;
//         else if (comboName.equals("fontsize")) item = new FontSizeCombo("FontSize") ;
//         else if (comboName.equals("fontcolor")) item = new ColorCombo("FontColor") ;
//         else if (comboName.equals("setstyle")) item = new RefStyleCombo("SetStyle") ;
//         else if (comboName.equals("setbackgroundcolor")) item = new ColorCombo("SetBackgroundColor") ;
//         else if (comboName.equals("setbordercolor")) item = new ColorCombo("SetBorderColor") ;
//         else if (comboName.equals("setborderwidth")) item = new LineWidthCombo("SetBorderWidth") ;
//         else if (comboName.equals("setborderposition")) item = new BorderPositionCombo("SetBorderPosition") ;
//         else if (comboName.equals("setverticaljustif")) item = new VerticalJustifCombo("SetVerticalJustif") ;
//         else if (comboName.equals("zoom")) item = new ZoomCombo() ;
//         else if (comboName.equals("layout")) item = new LayoutCombo() ;
         if (item == null) {
            return;
         }
         ((JComponent) stateStack.peek()).add(item);
         widgetManager.registerWidget(item);
      }
      else if (raw.equalsIgnoreCase("listmenu")) {
         String action = attrs.getValue("action").toLowerCase();
         JMenu item = null;

//         if (action.equals("fontfamily")) item = new FontFamilyMenu("FontFamily") ;
//         else if (action.equals("fontsize")) item = new FontSizeMenu("FontSize") ;
//         else if (action.equals("setstyle")) item = new RefStyleMenu("SetStyle") ;
//         else if (action.equals("setbackgroundcolor")) item = new ColorMenu("SetBackgroundColor") ;
//         else if (action.equals("fontcolor")) item = new ColorMenu("FontColor") ;
//         else if (action.equals("setbordercolor")) item = new ColorMenu("SetBorderColor") ;
//         else if (action.equals("setborderwidth")) item = new LineWidthMenu("SetBorderWidth") ;
//         else if (action.equals("setborderposition")) item = new BorderPositionMenu("SetBorderPosition") ;
//         else if (action.equals("setverticaljustif")) item = new VerticalJustifMenu("SetVerticalJustif") ;
//         else if (action.equals("zoom")) item = new ZoomMenu() ;
//         else if (action.equals("layout")) item = new LayoutMenu() ;
         // Comme il s'agit de menu les items sont registeres individuellement
         if (item == null) {
            return;
         }
         ((JComponent) stateStack.peek()).add(item);

      }
      else if (raw.equalsIgnoreCase("menu")) {
         JMenu menu = guiBuilder.buildMenu(attrs.getValue("name"));
         JComponent component = (JComponent) stateStack.peek();
         component.add(menu);
         stateStack.push(menu);
      }
      else if (raw.equalsIgnoreCase("menubar")) {
         stateStack = new Stack();
         JMenuBar menuBar = new JMenuBar();
         menuBar.setOpaque(true);
         stateStack.push(menuBar);
      }
      else if (raw.equalsIgnoreCase("popup")) {
         stateStack = new Stack();
         JPopupMenu popup = new JPopupMenu();
         stateStack.push(popup);

      }
      else if (raw.equalsIgnoreCase("toolbar")) {
         stateStack = new Stack();
         JToolBar toolBar = new JToolBar();
         toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
         toolBar.setOpaque(true);
         toolBar.setFloatable(false);
         //toolBar.setFocusable(false) ;

         // toolBar.setBorderPainted(true) ;
         stateStack.push(toolBar);
      }
      else if (raw.equalsIgnoreCase("statusbar")) {
         stateStack = new Stack();
         StatusBar statusBar = new StatusBar();
         stateStack.push(statusBar);
      }

   }

   @Override
   public void endElement(String uri, String local, String raw) {
      if (raw.equalsIgnoreCase("menu")) {
         stateStack.pop();
      }
      else if (raw.equalsIgnoreCase("toolbar")) {
         JComponent component = (JComponent) stateStack.peek();
         component.add(javax.swing.Box.createHorizontalGlue());
      }
      else if (raw.equalsIgnoreCase("statusbar")) {
         JComponent component = (JComponent) stateStack.peek();
         component.add(javax.swing.Box.createHorizontalStrut(5));
         //component.add(javax.swing.Box.createHorizontalGlue());
      }
   }

   // ErrorHandler methods
   @Override
   public void warning(SAXParseException ex) {
      logger.warn(getLocationString(ex), ex);
   }

   @Override
   public void error(SAXParseException ex) {
      logger.warn(getLocationString(ex), ex);
   }

   @Override
   public void fatalError(SAXParseException ex) throws SAXException {
      logger.warn(getLocationString(ex), ex);
   }

   // Returns a string of the location.
   private String getLocationString(SAXParseException ex) {
      StringBuilder str = new StringBuilder();

      String systemId = ex.getSystemId();
      if (systemId != null) {
         int index = systemId.lastIndexOf('/');
         if (index != -1) {
            systemId = systemId.substring(index + 1);
         }
         str.append(systemId);
      }
      str.append(':').append(ex.getLineNumber());
      str.append(':').append(ex.getColumnNumber());
      return str.toString();
   }
}
