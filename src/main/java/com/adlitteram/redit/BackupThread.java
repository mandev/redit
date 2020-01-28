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
import com.adlitteram.redit.outputfilter.RdzArticleWriter;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.FileLock;
import javax.swing.SwingUtilities;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BackupThread {

   private static final Logger logger = LoggerFactory.getLogger(BackupThread.class);

   private static final BckThread BACKUP_THREAD = new BckThread();

   public static void startBackup() {
      BACKUP_THREAD.setPriority(Thread.MIN_PRIORITY);
      BACKUP_THREAD.start();
   }

   public static void suspendBackup() {
      BACKUP_THREAD.suspendBackup();
   }

   public static void resumeBackup() {
      BACKUP_THREAD.resumeBackup();
   }

   // Real backup Thread
   static class BckThread extends Thread {

      private boolean isSuspended = false;
      private final int maxFiles = 99;

      public void suspendBackup() {
         logger.info("Suspending backup thread");
         isSuspended = true;
      }

      public synchronized void resumeBackup() {
         if (isSuspended) {
            logger.info("Resuming backup thread");
            isSuspended = false;
            notify();
         }
      }

      @Override
      public void run() {
         logger.info("Backup thread running");
         while (true) {
            try {
               sleep(60000l);  // 60 seconds
               if (isSuspended) {
                  synchronized (this) {
                     while (isSuspended) {
                        wait();
                     }
                  }
                  continue;
               }

               backupArticle();
            }
            catch (InterruptedException ex) {
               logger.warn("", ex);
            }
         }
      }

      private int readCounter() {
         int count = -1;
         RandomAccessFile raf = null;
         try {
            raf = new RandomAccessFile(new File(ReditApplication.COUNT_FILE), "rw");
            FileLock fileLock = raf.getChannel().tryLock();
            if (fileLock != null) {
               try {
                  count = raf.readInt() + 1;
                  if (count <= 0 || count > maxFiles) {
                     count = 1;
                  }
               }
               catch (IOException ex) {
                  count = 1;
               }
               raf.seek(0);
               raf.writeInt(count);
               raf.setLength(raf.getFilePointer());
            }
         }
         catch (IOException ex1) {
            logger.warn("", ex1);
            count = -1;
         }
         finally {
            closeQuietly(raf);
         }

         return count;
      }

      private void backupArticle() {

         final Article article = Main.getApplication().getAppManager().getArticle();

         if (article != null && article.isBackupDirty()) {
            try {
               SwingUtilities.invokeAndWait(() -> {
                  try {
                     int counter = readCounter();
                     if (counter > 0) {
                        String nn = StringUtils.leftPad(String.valueOf(counter), 2, '0');
                        RdzArticleWriter articleWriter = new RdzArticleWriter(article);
                        articleWriter.write(ReditApplication.USER_BCK_DIR + "backup_" + nn + ".rdz");
                        article.setBackupClean();
                     }
                     else {
                        logger.info("Cannot read counter. Backup aborted.");
                     }
                  }
                  catch (IOException ex) {
                     logger.warn("", ex);
                  }
               });
            }
            catch (InterruptedException | InvocationTargetException ex) {
               logger.warn("", ex);
            }
         }
      }

      private void closeQuietly(Closeable c) {
         if (c != null) {
            try {
               c.close();
            }
            catch (IOException ex) {
            }
         }
      }
   }
}

