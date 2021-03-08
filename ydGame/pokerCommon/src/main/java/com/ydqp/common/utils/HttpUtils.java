package com.ydqp.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Classname CloseableHttpClientUtils
 * @Description TODO
 * @Date 2019/6/10 20:07
 * @Created by A
 */
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

    public String sendGet(String url, Map<String, String> headParams, JSONObject params) {
        //创建 CloseableHttpClient
//        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = HttpClientPool.getHttpClient();
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
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(3000).setSocketTimeout(3000).setConnectTimeout(3000).build();
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
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            if (httpClient != null) {
//                try {
//                    httpClient.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        return result;
    }

    public String sendPost(String url, Map<String, String> headParams, JSONObject params) {
        // 创建httpClient对象
//        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = HttpClientPool.getHttpClient();

        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        /**
         * setConnectTimeout：设置连接超时时间，单位毫秒。
         * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection
         * 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
         * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
         */
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
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
//                if (httpClient != null) {
//                    httpClient.close();
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    public String sendPostWithAuth(String url, Map<String, String> headParams, JSONObject params) {
        // 创建httpClient对象
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials(headParams.get("keyId"), headParams.get("keySecret"));
        provider.setCredentials(AuthScope.ANY, credentials);
        HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

        headParams.remove("keyId");
        headParams.remove("keySecret");

        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        /**
         * setConnectTimeout：设置连接超时时间，单位毫秒。
         * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection
         * 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
         * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
         */
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
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

    public String fbLoginCheckToken(String uri) {
        String result = null;
        CloseableHttpResponse response = null;
        // 创建一个 HttpClient 对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            // 创建一个 HttpGet 对象
            HttpGet httpGet = new HttpGet(uri);

            //Printing the method used
            System.out.println("Request Type: " + httpGet.getMethod());

            //Executing the Get request
            //HttpResponse httpresponse = httpclient.execute(httpget);

            //设置请求状态参数
//            RequestConfig requestConfig = RequestConfig.custom()
//                    .setConnectionRequestTimeout(3000).setSocketTimeout(3000).setConnectTimeout(3000).build();
           // httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();//获取返回状态值
            if (status == HttpStatus.SC_OK) {//请求成功
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    result = EntityUtils.toString(httpEntity, "UTF-8");
                    EntityUtils.consume(httpEntity);//关闭资源
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            if (httpClient != null) {
//                try {
//                    httpClient.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        return result;

    }

    public static void main(String[] args) {
        //HttpUtils.getInstance().fbLoginCheckToken("https://graph.facebook.com/debug_token?access_token=285795112836266%7Cae9912bff0e8c027679f3712e36f8868&input_token=EAAED7dwt3KoBAJPKctHu4zkdce0RiM11hr6q49y7MRMddybRZAZAMv8SH2evCnbS3xHcPxOrbRWCFDpTtAwLHZCeWKp4Edkwx8t0zvWToX7nk5vFTLIe6j0oOYKvP2WfcDpm8lSiSFSYPk6ALZAB7VKecZAdZAJbSbYiZCajZCcgoL9xHoJFwGAGcUeg3hRihBF0ZCIDmxFkZB5AZDZD");

//        Map<String, String> headParams = new HashMap<>();
//        headParams.put("Content-Type", "application/x-www-form-urlencoded");
//        headParams.put("Connection", "Keep-Alive");
//        JSONObject params = new JSONObject();
//        params.put("access_token","285795112836266" + "|" + "ae9912bff0e8c027679f3712e36f8868");
//        params.put("input_token", "EAAED7dwt3KoBAJPKctHu4zkdce0RiM11hr6q49y7MRMddybRZAZAMv8SH2evCnbS3xHcPxOrbRWCFDpTtAwLHZCeWKp4Edkwx8t0zvWToX7nk5vFTLIe6j0oOYKvP2WfcDpm8lSiSFSYPk6ALZAB7VKecZAdZAJbSbYiZCajZCcgoL9xHoJFwGAGcUeg3hRihBF0ZCIDmxFkZB5AZDZD");
//        HttpUtils.getInstance().sendGet("https://graph.facebook.com/debug_token", headParams, params);
//
//        try {
//            FbHelper.getUserAcc("EAAED7dwt3KoBAJPKctHu4zkdce0RiM11hr6q49y7MRMddybRZAZAMv8SH2evCnbS3xHcPxOrbRWCFDpTtAwLHZCeWKp4Edkwx8t0zvWToX7nk5vFTLIe6j0oOYKvP2WfcDpm8lSiSFSYPk6ALZAB7VKecZAdZAJbSbYiZCajZCcgoL9xHoJFwGAGcUeg3hRihBF0ZCIDmxFkZB5AZDZD");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        for (int i = 0; i < 10; i++) {
            String url = "http://localhost:80/statistics/collect";
            Map<String, String> headParams = new HashMap<>();
            headParams.put("Content-Type", "application/json");
            JSONObject params = new JSONObject();
            params.put("type", "ORDER");
            params.put("data", new JSONObject());
            String s = HttpUtils.getInstance().sendPost(url, headParams, params);
            System.out.println(s);
        }
    }


}
