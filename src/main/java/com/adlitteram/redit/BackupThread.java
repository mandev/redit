package com.adlitteram.redit;

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

   private static final BckThread bckThread = new BckThread();

   public static void startBackup() {
      bckThread.setPriority(Thread.MIN_PRIORITY);
      bckThread.start();
   }

   public static void suspendBackup() {
      bckThread.suspendBackup();
   }

   public static void resumeBackup() {
      bckThread.resumeBackup();
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
//        private FileLock lock;
//        private FileChannel channel;
//        private void acquireFileLock() {
//            int i = 0;
//            while ((lock == null || !lock.isValid())) {
//                if (i > 99) {
//                    logger.info("Cannot acquire lock");
//                    break;
//                }
//                try {
//                    File lockFile = new File(Main.USER_LCK_DIR, "lock." + (i++));
//                    if (!lockFile.exists()) lockFile.createNewFile();
//                    channel = new RandomAccessFile(lockFile, "rw").getChannel();
//                    lock = channel.tryLock();
//                }
//                catch (IOException ex) {
//                    logger.warn("", ex);
//                    closeQuietly(channel);
//                    lock = null;
//                }
//            }
//        }
//        private void releaseFileLock() {
//            if (channel != null) {
//                try {
//                    channel.close();
//                    lock = null;
//                    channel = null;
//                }
//                catch (IOException ex) {
//                    logger.warn("", ex);
//                }
//            }
//        }
