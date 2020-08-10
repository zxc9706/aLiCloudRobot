/**
 * @Author zxc
 * @DateTime 2020/8/10 9:42 上午
 */

import Result.Result;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.storm.shade.org.apache.commons.lang.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * desc: http 工具类
 * date: 2020/6/4 22:27
 *
 * @author: jianjun liu
 */
public class HttpClientManager {
    private static int connectionTimeout = 3000;// 连接超时时间,毫秒
    private static int soTimeout = 10000;// 读取数据超时时间，毫秒
    /**
     * HttpClient对象
     */
    private static CloseableHttpClient httpclient = HttpClients.custom().disableAutomaticRetries().build();
    private static CloseableHttpClient httpsClient;

    static {
        try {
            // 采用绕过验证的方式处理https请求
            SSLContext sslcontext = createIgnoreVerifySSL();
            // 设置协议http和https对应的处理socket链接工厂的对象
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslcontext,
                            SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER))
                    .build();
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
                    socketFactoryRegistry);
            HttpClients.custom().setConnectionManager(connManager);

            // 创建自定义的httpclient对象
            httpsClient = HttpClients.custom().setConnectionManager(connManager).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*** 超时设置 ****/
    private static RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(soTimeout)
            .setConnectTimeout(connectionTimeout)
            .build();//设置请求和传输超时时间

    public static HttpClientManager getInstance() {
        return SingleClass.instance;
    }

    private static class SingleClass {
        private final static HttpClientManager instance = new HttpClientManager();
    }



    /**
     * desc:  发送json post  请求
     * param: url,jsonStrParam
     * return: Result<String>
     * date: 2019/2/21 9:12
     *
     * @author: jianjun liu
     */
    public Result<String> sendPostByJSON(String url, String jsonStrParam) {

        try {
            HttpPost httppost = new HttpPost(url);
            httppost.setConfig(requestConfig);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
            if (StringUtils.isNotEmpty(jsonStrParam)) {
                StringEntity se = new StringEntity(jsonStrParam, "utf-8");
                httppost.setEntity(se);
            }
            return execute((HttpRequestBase) httppost, false);
        } catch (Exception e) {


        }
        return new Result<String>();
    }

    private Result<String> execute(HttpRequestBase httpMethod, boolean isHttps) {
        CloseableHttpResponse response = null;
        try {
            if (isHttps) {
                response = httpsClient.execute(httpMethod);
            } else {
                response = httpclient.execute(httpMethod);
            }
            if (response != null && response.getStatusLine() != null) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String result = EntityUtils.toString(response.getEntity());
                    return new Result<String>().ok(result);
                }
            }
        } catch (Exception e) {
        } finally {
            httpMethod.releaseConnection();
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
            }
        }
        return new Result<String>();
    }

    private static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }


}



