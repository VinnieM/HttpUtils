package code.http.util.main;

import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

public class HttpsClientWrapper
{
   private final static Logger logger = Logger.getLogger( HttpsClientWrapper.class );

   public static HttpClient getHttpClient()
   {
      try
      {

         final KeyStore trustStore = KeyStore.getInstance( KeyStore.getDefaultType() );
         
         final SSLContext sslcontext = SSLContexts.custom()
               .loadTrustMaterial( trustStore, new TrustSelfSignedStrategy() ).build();
         // Allow TLSv1 protocol only
         final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory( sslcontext, new String[]
         { "TLSv1" }, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );
         final CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory( sslsf ).build();

         return httpclient;
      }
      catch (Exception ex)
      {
         logger.error( ex );
         return null;
      }
   }
}
