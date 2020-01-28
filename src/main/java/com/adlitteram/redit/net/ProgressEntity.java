package com.adlitteram.redit.net;

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
import com.adlitteram.jasmin.io.ProgressListener;
import com.adlitteram.jasmin.io.ProgressOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
