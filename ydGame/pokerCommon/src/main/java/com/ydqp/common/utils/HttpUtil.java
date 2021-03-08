package com.ydqp.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 工具类
 */
public class HttpUtil
{

    /**
     * http请求
     *
     * @param url
     * @param data
     * @param gfw
     * @return
     * @throws IOException
     */
    public static String requestPostUrl(String url, HashMap<String, String> data, boolean gfw) throws IOException
    {
        String str = httpBuildQuery(data);
        return requestPostUrl(url, str, gfw);
    }
    
    public static String requestGetUrl(String url, boolean gfw) throws IOException
    {
        HttpURLConnection conn;
        try
        {
			//if GET....
            //URL requestPostUrl = new URL(url + "?" + httpBuildQuery(data));
            URL requestUrl = new URL(url);
            
            if(gfw)
            {
                InetSocketAddress addr = new InetSocketAddress("192.168.10.240",1080);
                Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr);
                conn = (HttpURLConnection) requestUrl.openConnection(proxy);
            }
            else
            {
                conn = (HttpURLConnection) requestUrl.openConnection();
            }
            
            conn.setConnectTimeout(5000);
        }
        catch (MalformedURLException e)
        {
            return e.getMessage();
        }

//        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        conn.setDoInput(true);
        conn.setDoOutput(true);

//        try (PrintWriter writer = new PrintWriter(conn.getOutputStream()))
//        {
//            writer.print(str);
//            writer.flush();
//        }

        String line;
        BufferedReader bufferedReader;
        StringBuilder sb = new StringBuilder();
        InputStreamReader streamReader = null;
        try
        {
            streamReader = new InputStreamReader(conn.getInputStream());
        }
        catch (IOException e)
        {
            streamReader = new InputStreamReader(conn.getErrorStream());
        }
        finally
        {
            if (streamReader != null)
            {
                bufferedReader = new BufferedReader(streamReader);
                sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null)
                {
                    sb.append(line);
                }
            }
            
            conn.disconnect();
        }
        return sb.toString();
    }
    
    /**
     * http请求
     *
     * @param url
     * @param str
     * @param gfw
     * @return
     * @throws IOException
     */
    public static String requestPostUrl(String url, String str, boolean gfw) throws IOException
    {

        HttpURLConnection conn;
        try
        {   
            URL requestUrl = new URL(url);
            
            if(gfw)
            {
                InetSocketAddress addr = new InetSocketAddress("127.0.0.1",1080);  
                Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr);
                conn = (HttpURLConnection) requestUrl.openConnection(proxy);
            }
            else
            {
                conn = (HttpURLConnection) requestUrl.openConnection();
            }
            
            conn.setConnectTimeout(5000);
            
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        }
        catch (MalformedURLException e)
        {
            return e.getMessage();
        }

        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        try (PrintWriter writer = new PrintWriter(conn.getOutputStream()))
        {
            writer.print(str);
            writer.flush();
        }

        String line;
        BufferedReader bufferedReader;
        StringBuilder sb = new StringBuilder();
        InputStreamReader streamReader = null;
        try
        {
            streamReader = new InputStreamReader(conn.getInputStream());
        }
        catch (IOException e)
        {
            streamReader = new InputStreamReader(conn.getErrorStream());
        }
        finally
        {
            if (streamReader != null)
            {
                bufferedReader = new BufferedReader(streamReader);
                sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null)
                {
                    sb.append(line);
                }
            }
            
            conn.disconnect();
        }
        return sb.toString();
    }

    /**
     * 参数编码
     *
     * @param data
     * @return
     */
    public static String httpBuildQuery(HashMap<String, String> data)
    {
        if(data == null) return "";
        
        String ret = "";
        String k, v;
        Iterator<String> iterator = data.keySet().iterator();
        while (iterator.hasNext())
        {
            k = iterator.next();
            v = data.get(k);
            ret += (k + "=" + v);
            ret += "&";
        }
        return ret.substring(0, ret.length() - 1);
    }
}
