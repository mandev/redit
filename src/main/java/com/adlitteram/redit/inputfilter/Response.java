/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit.inputfilter;

public class Response {

//    private int status;
   private int error;
   private String comment;

   public Response() {
   }

//    public int getStatus() {
//        return status;
//    }
//    public void setStatus(int status) {
//        this.status = status;
//    }
   public int getError() {
      return error;
   }

   public void setError(int error) {
      this.error = error;
   }

   public String getComment() {
      return comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }
}
