package com.adlitteram.redit.action;

import com.adlitteram.jasmin.action.XAction;
import com.adlitteram.redit.AppManager;
import com.adlitteram.redit.Article;
import java.awt.event.ActionEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.StyledDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddBoloBolo extends XAction {

   private static final Logger logger = LoggerFactory.getLogger(AddBoloBolo.class);
   private static final String BOLOBOLO = "Vos ordres sont charmants; votre façon de les donner est plus aimable encore; vous feriez chérir le despotisme. Ce n'est pas la première fois, comme vous savez, que je regrette de ne plus être votre esclave; et tout _ monstre _ que vous dites que je suis, je ne me rappelle jamais sans plaisir le temps où vous m'honoriez de noms plus doux. Souvent même je dssire de les mériter de nouveau, et de finir par donner, avec vous, un exemple de constance au monde. Mais de plus grands intérets nous appellent; conquérir est notre destin; il faut le suivre: peut-ếtre au bout de la carrière nous rencontrerons-nous encore; car, soit dit sans vous fâcher, ma très belle Marquise, vous me suivez au moins d'un pas égal; et depuis que, nous séparant pour le bonheur du monde, nous préchons la foi chacun de notre côté, il me semble que dans cette mission d'amour, vous avez fait plus de prosélytes que moi. Je connais votre zèle, votre ardente ferveur; et si ce Dieu-là nous jugeait sur nos oeuvres, vous seriez un jour la Patronne de quelque grande ville, tandis que votre ami serait au plus un Saint de village. Ce langage vous étonne, n'est-il pas vrai? Mais depuis huit jours, je n'en entends, je n'en parle pas d'autre; et c'est pour m'y perfectionner, que je me vois forcé de vous désobéir.\n";
   //
   private final AppManager appManager;

   public AddBoloBolo(AppManager appManager) {
      super("AddBoloBolo");
      this.appManager = appManager;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      action(appManager);
      appManager.getActionManager().enableActions();
   }

   public static void action(AppManager appManager) {
      logger.info("AddBoloBolo");

      Article article = appManager.getArticle();
      StyledDocument document = article.getDocument();
      Caret caret = article.getCaret();
      try {
         document.insertString(caret.getDot(), BOLOBOLO, null);
         appManager.getMainFrame().requestTextFocus();
      }
      catch (BadLocationException ex) {
         logger.warn("", ex);
      }
   }

   @Override
   public void enable() {
      setEnabled(appManager.getArticlePane() != null);
   }
}
