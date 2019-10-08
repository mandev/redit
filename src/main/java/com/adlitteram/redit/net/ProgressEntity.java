/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.redit.net;

import com.adlitteram.jasmin.io.ProgressListener;
import com.adlitteram.jasmin.io.ProgressOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author manu
 */
public class ProgressEntity extends HttpEntityWrapper {

   private static final Logger logger = LoggerFactory.getLogger(ProgressEntity.class);

   private final ProgressListener listener;

   public ProgressEntity(HttpEntity entity, ProgressListener listener) {
      super(entity);
      this.listener = listener;
   }

   @Override
   public void writeTo(final OutputStream out) throws IOException {
      logger.info("ENTRY");
      ProgressOutputStream pos = new ProgressOutputStream(out, 500);
      pos.addProgressListener(listener);
      wrappedEntity.writeTo(pos);
      logger.info("RETURN");

   }
}
