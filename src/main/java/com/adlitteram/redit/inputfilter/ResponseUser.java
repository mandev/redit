/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit.inputfilter;

import com.adlitteram.redit.User;

public class ResponseUser extends Response {

   private User user;

   public ResponseUser() {
      super();
   }

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }
}
