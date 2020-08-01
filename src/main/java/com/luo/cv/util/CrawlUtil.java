package com.luo.cv.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author shkstart
 * @create 2020-07-23 20:18
 */
public class CrawlUtil {
    // 获取连接
    public static HttpURLConnection getConnectionByUrl(String linkAddress) throws IOException {
        URL url = new URL(linkAddress);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 初始化请求头
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36");

        return conn;
    }

    // 根据链接获取数据
    public static String getSource(String linkAddress) {
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream bao = null;

        try {
            // 获取连接
            conn = getConnectionByUrl(linkAddress);
            // 发送请求，获取连接
            conn.connect();

            // 如果请求成功
            if (conn.getResponseCode() == 200) {
                // 获取输入流
                bis = new BufferedInputStream(conn.getInputStream());

                // 将数据暂时保存
                bao = new ByteArrayOutputStream();

                // 10M的缓存
                byte[] buff = new byte[10485760];
                int len = 0;

                // 读取数据
                while ((len = bis.read(buff)) != -1) {
                    bao.write(buff, 0, len);
                }

                return bao.toString();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 关闭资源
            if (conn != null)
            {
                conn.disconnect();
            }
            closeSource(bis, bao);
        }

        return null;
    }

    // 关闭资源
    private static void closeSource(BufferedInputStream bis, ByteArrayOutputStream bao) {
        if (bis != null) {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (bao != null) {
            try {
                bao.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
