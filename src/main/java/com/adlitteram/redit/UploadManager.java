package com.adlitteram.redit;

import com.adlitteram.jasmin.XProp;
import com.adlitteram.jasmin.io.ProgressListener;
import com.adlitteram.redit.inputfilter.Response;
import com.adlitteram.redit.inputfilter.ResponseReader;
import com.adlitteram.redit.inputfilter.ResponseUser;
import com.adlitteram.redit.inputfilter.ResponseUserReader;
import com.adlitteram.redit.net.ProgressEntity;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.GZIPInputStream;
import org.apache.http.*;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadManager {

   private static final Logger logger = LoggerFactory.getLogger(UploadManager.class);

   private final ConnectionURI[] connectionUris = {
      new ConnectionURI("http://site1:80/hub2/connect", "http://site1:80/userinfo", "http://site1:80/uploadfile"),
      new ConnectionURI("http://site2:80/hub2/connect", "http://site2:80/userinfo", "http://site2:80/uploadfile"),
      new ConnectionURI("http://site3:80/hub2/connect", "http://site3:80/userinfo", "http://site3:80/uploadfile"),};

   private final AppManager appManager;
   private DefaultHttpClient httpClient;
   private int defautConnectionIndex = XProp.getInt("DefaultConnection.Index", 0);
   private int connectionIndex = -1;

   public UploadManager(AppManager appManager) {
      this.appManager = appManager;
   }

   private ConnectionURI getConnectionURI() {
      return connectionUris[XProp.getInt("DefaultConnection.Index", 0)];
   }

   private void nextConnectionURI() {
      connectionIndex++;
      if (connectionIndex == defautConnectionIndex) {
         connectionIndex++;
         defautConnectionIndex = -1;
      }
      if (connectionIndex >= connectionUris.length) {
         connectionIndex = 0;
      }
      XProp.put("DefaultConnection.Index", connectionIndex);
   }

   private DefaultHttpClient getHttpClient() {
      if (httpClient == null) {
         httpClient = new DefaultHttpClient();
         httpClient.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
         httpClient.getParams().setParameter("http.connection.timeout", 3 * 1000);
         httpClient.getParams().setParameter("http.socket.timeout", 60 * 1000);

         httpClient.addRequestInterceptor((final HttpRequest request, final HttpContext context) -> {
            if (!request.containsHeader("Accept-Encoding")) {
               request.addHeader("Accept-Encoding", "gzip");
            }
         });

         httpClient.addResponseInterceptor((final HttpResponse response, final HttpContext context) -> {
            HttpEntity entity = response.getEntity();
            Header ceheader = entity.getContentEncoding();
            if (ceheader != null) {
               HeaderElement[] codecs = ceheader.getElements();
               for (HeaderElement codec : codecs) {
                  logger.info(codec.getName());
                  if (codec.getName().equalsIgnoreCase("gzip")) {
                     response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                     return;
                  }
               }
            }
         });
      }
      return httpClient;
   }

   /*
     * <response> <error>0|1</error> <comment>comment</comment> </response>
    */
   public Response connect(String username, String password) throws IOException {
      logger.info("ENTRY");

      Response response = null;

      int times = 0;

      while (response == null && times < connectionUris.length) {

         DefaultHttpClient client = getHttpClient();
         logger.info("Trying to connect with " + getConnectionURI().getConnectUri());
         HttpPost httpPost = new HttpPost(getConnectionURI().getConnectUri());

         List<NameValuePair> nvps = new ArrayList<>();
         nvps.add(new BasicNameValuePair("username", username));
         nvps.add(new BasicNameValuePair("password", password));
         httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

         try {
            HttpResponse httpResponse = client.execute(httpPost);

            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() >= 300) {
               throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
            }

            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
               InputStream is = entity.getContent();
               if (is != null) {
                  ResponseReader responseReader = new ResponseReader();
                  response = responseReader.read(is);
                  if (response == null) {
                     logger.info("Response from server is null");
                     releaseConnection();
                     nextConnectionURI();
                  }
               }
            }
         }
         catch (IOException ex) {
            releaseConnection();
            nextConnectionURI();
            if (times >= connectionUris.length) {
               throw ex;
            }
            else {
               logger.info("Failed #" + times + " - " + ex.getLocalizedMessage());
            }
         }
         finally {
            times++;
         }
      }

      logger.info("RETURN");
      return response;
   }

   /*
     * <response> <error>0|1</error> <comment>comment</comment> </response>
    */
   public Response uploadFile(String username, String password, String columnName, String docName, File file, ProgressListener progressListener) throws IOException {
      logger.info("ENTRY");

      Response response = null;
      int times = 0;

      while (response == null && times < connectionUris.length) {

         DefaultHttpClient client = getHttpClient();
         logger.info("Trying to upload with " + getConnectionURI().getUploadUri());
         HttpPost httpPost = new HttpPost(getConnectionURI().getUploadUri());

         try {
            MultipartEntity multipartEntity = new MultipartEntity();
            multipartEntity.addPart("username", new StringBody(username));
            multipartEntity.addPart("password", new StringBody(password));
            multipartEntity.addPart("columnname", new StringBody(columnName));
            multipartEntity.addPart("checksum", new StringBody(String.valueOf(getChecksum(file))));
            multipartEntity.addPart(docName, new FileBody(file));

            ProgressEntity progressEntity = new ProgressEntity(multipartEntity, progressListener);
            httpPost.setEntity(progressEntity);

            progressListener.init(httpPost);
            HttpResponse httpResponse = client.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();

            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() >= 300) {
               throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
            }

            if (entity != null) {
               InputStream is = entity.getContent();
               if (is != null) {
                  ResponseReader responseReader = new ResponseReader();
                  response = responseReader.read(is);
                  if (response == null) {
                     logger.info("Response from server is null");
                     releaseConnection();
                     nextConnectionURI();
                  }
               }
            }
         }
         catch (IOException ex) {
            releaseConnection();
            if (times >= connectionUris.length) {
               nextConnectionURI();
               throw ex;
            }
            else if (httpPost.isAborted()) {
               logger.info("httpPost is explicitly aborted - not retrying.");
               times = connectionUris.length;
            }
            else {
               logger.info("Failed #" + times + " - " + ex.getLocalizedMessage());
               nextConnectionURI();
            }
         }
         finally {
            progressListener.finished(this);
            times++;
         }
      }

      logger.info("RETURN");
      return response;
   }

   /*
     * <response> <error>0|1</error> <comment>comment</comment> <user> ...
     * <group> ... <column> ... <iptcFields> ... </user> </response>
    */
   public ResponseUser getUserInfo(String username, String password) throws IOException {
      logger.info("ENTRY");

      ResponseUser response = null;
      int times = 0;

      while (response == null && times < connectionUris.length) {

         DefaultHttpClient client = getHttpClient();
         logger.info("Trying to get user info with " + getConnectionURI().getUserinfoUri());
         HttpPost httpPost = new HttpPost(getConnectionURI().getUserinfoUri());

         List<NameValuePair> nvps = new ArrayList<>();
         nvps.add(new BasicNameValuePair("username", username));
         nvps.add(new BasicNameValuePair("password", password));
         httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

         try {
            HttpResponse httpResponse = client.execute(httpPost);

            StatusLine statusLine = httpResponse.getStatusLine();
            logger.info("StatusLine: " + statusLine);

            if (statusLine.getStatusCode() >= 300) {
               throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
            }

            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
               InputStream is = entity.getContent();
               if (is != null) {
                  ResponseUserReader userReader = new ResponseUserReader();
                  response = (ResponseUser) userReader.read(is);
                  if (response == null) {
                     logger.info("Response from server is null");
                     releaseConnection();
                     nextConnectionURI();
                  }
               }
            }
         }
         catch (IOException ex) {
            releaseConnection();
            nextConnectionURI();
            if (times >= connectionUris.length) {
               throw ex;
            }
            else {
               logger.info("Failed #" + times + " - " + ex.getLocalizedMessage());
            }
         }
         finally {
            times++;
         }
      }

      logger.info("RETURN");
      return response;
   }

   public void releaseConnection() {
      if (httpClient != null) {
         httpClient.getConnectionManager().shutdown();
         httpClient = null;
      }
   }

   // Compute an adler checksum for the file
   @SuppressWarnings("empty-statement")
   public static long getChecksum(File file) throws IOException {
      Adler32 inChecker = new Adler32();
      byte[] data = new byte[4096];
      try (CheckedInputStream cis = new CheckedInputStream(new FileInputStream(file), inChecker)) {
         while (cis.read(data, 0, data.length) != -1);
      }
      return inChecker.getValue();
   }

   class ConnectionURI {

      private final String connectUri;
      private final String userinfoUri;
      private final String uploadUri;

      public ConnectionURI(String connectUri, String userinfoUri, String uploadUri) {
         this.connectUri = connectUri;
         this.userinfoUri = userinfoUri;
         this.uploadUri = uploadUri;
      }

      public String getConnectUri() {
         return connectUri;
      }

      public String getUploadUri() {
         return uploadUri;
      }

      public String getUserinfoUri() {
         return userinfoUri;
      }
   }

   static class GzipDecompressingEntity extends HttpEntityWrapper {

      public GzipDecompressingEntity(final HttpEntity entity) {
         super(entity);
      }

      @Override
      public InputStream getContent() throws IOException, IllegalStateException {
         // the wrapped entity's getContent() decides about repeatability
         return new GZIPInputStream(wrappedEntity.getContent());
      }

      @Override
      public long getContentLength() {
         // length of ungzipped content is not known
         return -1;
      }
   }
}
