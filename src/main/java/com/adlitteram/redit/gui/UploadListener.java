package com.adlitteram.redit.gui;

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
import com.adlitteram.jasmin.Message;
import com.adlitteram.jasmin.io.ProgressListener;
import java.awt.AWTEvent;
import javax.swing.SwingUtilities;
import org.apache.http.client.methods.HttpPost;

public class UploadListener implements ProgressListener, MonitorDialog.CancelListener {

   private final MonitorDialog monitorDialog;
   //
   private HttpPost httpPost;
   private long maxLength;
   private long startTime;

   public UploadListener(MonitorDialog monitorDialog) {
      this.monitorDialog = monitorDialog;
      monitorDialog.setCancelListener(this);
   }

   @Override
   public void init(Object object) {
      httpPost = (HttpPost) object;
      maxLength = httpPost.getEntity().getContentLength();
      startTime = System.currentTimeMillis();

      SwingUtilities.invokeLater(() -> {
         monitorDialog.setMessage(Message.get("MonitorDialog.Uploading"));
         monitorDialog.setMinimum(0);
         monitorDialog.setMaximum((int) maxLength);
      });
   }

   @Override
   public void bytesTransferred(final long count) {

      if (count > 0) {
         double kos = (double) (count * 1000) / (double) (1024 * (System.currentTimeMillis() - startTime));
         double res = ((double) (maxLength - count)) / (1024d * kos) + 1;

         final String str0 = Math.round(count / 1024d) + " Ko / " + Math.round(maxLength / 1024d) + " Ko";
         final String str1 = Math.round(kos) + " Ko/s";

         final String str2;
         if (Math.round(res) < 60) {
            str2 = Math.round(res) + " sec";
         }
         else if (Math.round(res) < 3600) {
            int tpm = (int) (res / 60);
            int tps = (int) (res - tpm * 60);
            str2 = tpm + " min " + tps + " sec";
         }
         else {
            int tph = (int) (res / 3600);
            int tpm = (int) ((res - tph * 3600) / 60);
            str2 = tph + " hr " + tpm + " min";
         }

         SwingUtilities.invokeLater(() -> {
            monitorDialog.setLabel1(Message.get("MonitorDialog.Uploaded") + " : " + str0);
            monitorDialog.setLabel2(Message.get("MonitorDialog.Speed") + " : " + str1);
            monitorDialog.setLabel3(Message.get("MonitorDialog.Time") + " : " + str2);
            monitorDialog.setProgress((int) count);
         });
      }
   }

   public boolean isCanceled() {
      return monitorDialog.isCanceled();
   }

   @Override
   public void cancel(AWTEvent event) {
      monitorDialog.dispose();
      if (httpPost != null) {
         httpPost.abort();
      }
   }

   @Override
   public void finished(Object object) {
      monitorDialog.dispose();
   }
}
