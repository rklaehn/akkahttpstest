package akkahttpstest

import java.io.InputStream
import java.security.cert.X509Certificate
import java.security.{SecureRandom, KeyStore}
import javax.net.ssl.{X509TrustManager, KeyManager, KeyManagerFactory, SSLContext}

import akka.http.scaladsl.HttpsContext

object DebugHttpsContexts {

  private def resourceStream(resourceName: String): InputStream = {
    val is = getClass.getClassLoader.getResourceAsStream(resourceName)
    require(is ne null, s"Resource $resourceName not found")
    is
  }

  // WARNING: This is dangerous, only use on host level, never globally
  val trustfulSslContext: SSLContext = {

    object NoCheckX509TrustManager extends X509TrustManager {
      override def checkClientTrusted(chain: Array[X509Certificate], authType: String) = ()
      override def checkServerTrusted(chain: Array[X509Certificate], authType: String) = ()
      override def getAcceptedIssuers = Array[X509Certificate]()
    }

    val context = SSLContext.getInstance("TLS")
    context.init(Array[KeyManager](), Array(NoCheckX509TrustManager), null)
    context
  }

  val clientContext: HttpsContext =
    HttpsContext(trustfulSslContext)

  val serverContext: HttpsContext = {
    val password = "abcdef".toCharArray
    val context = SSLContext.getInstance("TLS")
    val ks = KeyStore.getInstance("PKCS12")
    ks.load(resourceStream("keys/server.p12"), password)
    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    keyManagerFactory.init(ks, password)
    context.init(keyManagerFactory.getKeyManagers, null, new SecureRandom)
    // start up the web server
    HttpsContext(context)
  }
}
