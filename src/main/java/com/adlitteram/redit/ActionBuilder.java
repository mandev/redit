/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit;

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
