package com.ydqp.common.utils;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientPool {
    //全局参数
    private static PoolingHttpClientConnectionManager cm;
    static{
        cm = new PoolingHttpClientConnectionManager();
        // 最大连接数
        cm.setMaxTotal(50);
        // 每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(5);
    }
    public static CloseableHttpClient getHttpClient(){
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
        return HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();
    }
}