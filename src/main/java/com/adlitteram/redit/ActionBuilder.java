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
import com.adlitteram.jasmin.action.ActionManager;
import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.action.*;

public final class ActionBuilder extends ActionManager {

   public ActionBuilder(AppManager appManager) {
      super();
      putActions(getActions(appManager));
   }

   // Init Actions
   public static XAction[] getActions(AppManager appManager) {
      return new XAction[]{
         new About(appManager),
         new AddBoloBolo(appManager),
         new AddPicture(appManager),
         new ArticleProperties(appManager),
         new Copy(appManager),
         new Cut(appManager),
         new DecreaseFontSize(appManager),
         new DeletePicture(appManager),
         new EditPicture(appManager),
         new EditPictureMeta(appManager),
         new IncreaseFontSize(appManager),
         new NewArticle(appManager),
         new OpenArticle(appManager),
         new Paste(appManager),
         new Print(appManager),
         new Quit(appManager),
         new Redo(appManager),
         new RestoreArticle(appManager),
         new SaveArticle(appManager),
         new SaveAsArticle(appManager),
         new Search(appManager),
         new SearchNext(appManager),
         new SearchAndReplace(appManager),
         new SetStyleBody(appManager),
         new SetStyleCaption(appManager),
         new SetStyleChapo(appManager),
         new SetStyleHeading(appManager),
         new SetStyleNote(appManager),
         new SetStyleResult(appManager),
         new SetStyleSignature(appManager),
         new SetStyleSubtitle(appManager),
         new SetStyleSubheading(appManager),
         new SetStyleThrow(appManager),
         new SetStyleTitle(appManager),
         new SetTextBold(appManager),
         new SetTextItalic(appManager),
         new SetTextRegular(appManager),
         new ShowPicture(appManager),
         new ShowPicturePane(appManager),
         new ShowStatusBar(appManager),
         new ShowToolBar(appManager),
         new Undo(appManager),
         new UploadArticle(appManager),};
   }
}
