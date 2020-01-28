package com.adlitteram.redit.action;

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
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import com.adlitteram.redit.Main;
import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.action.XAction;
import org.slf4j.Logger;
import com.adlitteram.jasmin.property.XProp;
import com.adlitteram.jasmin.gui.explorer.ExplorerPane;
import com.adlitteram.jasmin.gui.explorer.ImageFile;
import com.adlitteram.jasmin.gui.widget.FileChooser;
import com.adlitteram.jasmin.gui.widget.ImagePreview;
import com.adlitteram.jasmin.utils.ExtFilter;
import com.adlitteram.redit.ReditApplication;
import com.adlitteram.redit.gui.ArticlePane;
import java.awt.event.ActionEvent;
import java.io.File;
import org.slf4j.LoggerFactory;

public class AddPicture extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(AddPicture.class);

   private final AppManager appManager;

   public AddPicture(AppManager appManager) {
      super("AddPicture");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager.getArticle());
   }

   public static void action(Article article) {
      logger.info("AddPicture");

      FileChooser fc = new FileChooser(Main.getApplication().getMainFrame(), Message.get("AddPicture.DialogTitle"));
      fc.setDirectory(XProp.get("AddPicture.Dir", ReditApplication.HOME_DIR));
      fc.addFileFilter(ExtFilter.IMG);
      ImagePreview.decorate(fc);

      File file = null;
      while (fc.showOpenDialog() == FileChooser.APPROVE_OPTION) {
         file = fc.getSelectedFile();
         if (file != null) {
            XProp.put("AddPicture.Dir", file.getParent());
            break;
         }
      }

      if (file != null && file.isFile() && file.canRead()) {
         ImageFile picture = article.addPicture(file);
         ArticlePane articlePane = article.getArticlePane();
         if (picture != null && articlePane != null) {
            ExplorerPane pictureList = articlePane.getExplorerPane();
            pictureList.setSelectedImageFile(picture);
            // TODO: sort
         }
      }
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null);
   }
}
