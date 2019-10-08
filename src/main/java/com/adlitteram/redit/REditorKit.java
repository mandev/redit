package com.adlitteram.redit;

import java.util.HashMap;
import javax.swing.Action;
import javax.swing.text.StyledEditorKit;

public class REditorKit extends StyledEditorKit {

   private final HashMap<String, Action> actionMap;

   public REditorKit() {
      super();

      // Store standard actions in a Map for quick access
      actionMap = new HashMap<>();
      for (Action action : getActions()) {
         actionMap.put((String) action.getValue(Action.NAME), action);
      }
   }

   public Action getAction(String actionName) {
      return actionMap.get(actionName);
   }
}
