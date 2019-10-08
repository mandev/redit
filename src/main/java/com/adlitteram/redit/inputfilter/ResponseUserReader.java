/**
 * Copyright (C) 1999-2002 Emmanuel Deviller
 *
 * @version 1.0
 * @author Emmanuel Deviller
 */
package com.adlitteram.redit.inputfilter;

public class ResponseUserReader extends ResponseReader {

   public ResponseUserReader() {
      super(new ResponseUserHandler());
   }
}
