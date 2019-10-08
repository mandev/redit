package com.adlitteram.redit.inputfilter;

/**
 * Encapsulate a general SAX error or warning.
 */
public class SAXStopException extends RuntimeException {

   public SAXStopException(String message) {
      super(message);
   }

}
