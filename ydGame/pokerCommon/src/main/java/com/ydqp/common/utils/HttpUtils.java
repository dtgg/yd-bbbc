package com.ydqp.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    private HttpUtils() {
    }

    private static HttpUtils instance = new HttpUtils();

    public static HttpUtils getInstance() {
        if (instance == null) instance = new HttpUtils();
        return instance;
    }

    // 编码格式。发送编码格式统一用UTF-8
    private static final String ENCODING = "UTF-8";

    // 设置连接超时时间，单位毫秒。
    private static final int CONNECT_TIMEOUT = 6000;

    // 请求获取数据的超时时间(即响应时间)，单位毫秒。
    private static final int SOCKET_TIMEOUT = 6000;

    public String sendGet(String url, Map<String, String> headParams, JSONObject params, boolean isProxy) {
        //创建 CloseableHttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = null;
        try {
            URIBuilder uri = new URIBuilder(url);
            //get请求带参数
            List<NameValuePair> list = new LinkedList<>();
            if (params != null) {
                params.forEach((key, value) -> {
                    BasicNameValuePair pair = new BasicNameValuePair(key, String.valueOf(value));
                    list.add(pair);
                });
            }
            uri.setParameters(list);
            HttpGet httpGet = new HttpGet(uri.build());
            if (headParams != null) {
                headParams.forEach(httpGet::setHeader);
            }
            //设置请求状态参数
            RequestConfig requestConfig;
            if (isProxy) {
                HttpHost proxy = new HttpHost("127.0.0.1", 10809, "http");
                requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).setProxy(proxy).build();
            } else {
                requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
            }
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();//获取返回状态值
            if (status == HttpStatus.SC_OK) {//请求成功
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    result = EntityUtils.toString(httpEntity, "UTF-8");
                    EntityUtils.consume(httpEntity);//关闭资源
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public String sendPost(String url, Map<String, String> headParams, JSONObject params, boolean isProxy) {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig;
        if (isProxy) {
            HttpHost proxy = new HttpHost("127.0.0.1", 10809, "http");
            requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).setProxy(proxy).build();
        } else {
            requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        }
        httpPost.setConfig(requestConfig);
        if (headParams != null) {
            headParams.forEach(httpPost::setHeader);
        }
        // 封装请求参数
//        packageParam(params, httpPost);
        if (params != null) httpPost.setEntity(new StringEntity(params.toString(), ENCODING));

        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;

        String content = "";
        try {
            // 执行请求
            httpResponse = httpClient.execute(httpPost);
            // 获取返回结果
            if (httpResponse != null && httpResponse.getStatusLine() != null) {
                if (httpResponse.getEntity() != null) {
                    content = EntityUtils.toString(httpResponse.getEntity(), ENCODING);
                }
            } else {
                throw new Exception("Error when accessing cashfree");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    public String sendPostWithAuth(String url, UsernamePasswordCredentials credentials, Map<String, String> headParams, JSONObject params, boolean isProxy) {
        // 创建httpClient对象
        CredentialsProvider provider = new BasicCredentialsProvider();
//        UsernamePasswordCredentials credentials
//                = new UsernamePasswordCredentials(headParams.get("keyId"), headParams.get("keySecret"));
        provider.setCredentials(AuthScope.ANY, credentials);
        HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig;
        if (isProxy) {
            HttpHost proxy = new HttpHost("127.0.0.1", 10809, "http");
            requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).setProxy(proxy).build();
        } else {
            requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        }
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "application/json");
        if (headParams != null) {
            headParams.forEach(httpPost::setHeader);
        }
        // 封装请求参数
//        packageParam(params, httpPost);
        if (params != null) httpPost.setEntity(new StringEntity(params.toString(), ENCODING));

        // 创建httpResponse对象
        HttpResponse httpResponse = null;

        String content = "";
        try {
            // 执行请求
            httpResponse = httpClient.execute(httpPost);
            // 获取返回结果
            if (httpResponse != null && httpResponse.getStatusLine() != null) {
                if (httpResponse.getEntity() != null) {
                    content = EntityUtils.toString(httpResponse.getEntity(), ENCODING);
                }
            } else {
                throw new Exception("Error when accessing cashfree");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return content;
    }

    public String sendPostNoSsl(String url, Map<String, String> headParams, JSONObject params, boolean isProxy) {
        // 创建httpClient对象
        SSLContext sslContext = null;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(null, (TrustStrategy) (x509Certificates, s) -> true).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
        }

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLContext(sslContext).
                setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();

        // 创建http对象
        HttpPut httpPut = new HttpPut(url);
        RequestConfig requestConfig;
        if (isProxy) {
            HttpHost proxy = new HttpHost("127.0.0.1", 2802, "http");
            requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).setProxy(proxy).build();
        } else {
            requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        }
        httpPut.setConfig(requestConfig);
        if (headParams != null) {
            headParams.forEach(httpPut::setHeader);
        }
        // 封装请求参数
//        packageParam(params, httpPost);
        if (params != null) httpPut.setEntity(new StringEntity(params.toString(), ENCODING));

        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;

        String content = "";
        try {
            // 执行请求
            httpResponse = httpClient.execute(httpPut);
            // 获取返回结果
            if (httpResponse != null && httpResponse.getStatusLine() != null) {
                if (httpResponse.getEntity() != null) {
                    content = EntityUtils.toString(httpResponse.getEntity(), ENCODING);
                }
            } else {
                throw new Exception("Error when accessing http request");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    public String post(String url, Map<String, String> headParams, JSONObject params, boolean isProxy) {
        String result = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            // 创建httpClient对象
            SSLContext sslContext = null;
            try {
                sslContext = SSLContexts.custom().loadTrustMaterial(null, (TrustStrategy) (x509Certificates, s) -> true).build();
            } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                e.printStackTrace();
            }

            // ConnectionSocketFactory注册
            Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new MyConnectionSocketFactory())
                    .register("https", new MySSLConnectionSocketFactory()).build();
            // HTTP客户端连接管理池
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(reg);
            httpclient = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(new NoopHostnameVerifier())
//                    .setHostnameVerifier(new HostnameVerifier())
                    .setConnectionManager(connManager).build();
            // 请求目标
            HttpPost httpPut = new HttpPost(url);
            if (headParams != null) {
                headParams.forEach(httpPut::setHeader);
            }
            if (params != null) httpPut.setEntity(new StringEntity(params.toString(), ENCODING));
            System.out.println("执行请求 ：" + httpPut.getRequestLine());

            if (isProxy) {
                // socks代理地址 , socks 地址和端口
                InetSocketAddress socksAddr = new InetSocketAddress("127.0.0.1", 2801);
                HttpClientContext context = HttpClientContext.create();
                context.setAttribute("socks.address", socksAddr);
                System.out.println("通过代理： " + socksAddr);

                response = httpclient.execute(httpPut, context);
            } else {
                response = httpclient.execute(httpPut);
            }
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
            System.out.println("返回响应：" + response.getStatusLine());
            System.out.println("响应内容：" + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    /**
     * 实现 http 链接的socket 工厂
     */
    static class MyConnectionSocketFactory extends PlainConnectionSocketFactory {
        @Override
        public Socket createSocket(final HttpContext context) throws IOException {
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
            // socket代理
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
            return new Socket(proxy);
        }
    }


    /**
     * 实现 https 链接的socket 工厂
     */
    static class MySSLConnectionSocketFactory extends SSLConnectionSocketFactory {
        public MySSLConnectionSocketFactory() {
            super(SSLContexts.createDefault(), getDefaultHostnameVerifier());
        }

        @Override
        public Socket createSocket(final HttpContext context) {
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
//      // socket代理
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
            return new Socket(proxy);
        }
    }

    public static void main(String[] args) throws Exception {
        HttpUtils.getInstance().post("https://www.cnblogs.com/", null, null, true);
    }
}
