package cn.epaylinks.common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class HttpUtils
{
	
	public static byte[] sendPost(String url,byte[] msg,String encoding)throws Exception{
		byte[] resp = null;
    	msg = new String(msg,encoding).getBytes();
    	URLConnection request = createRequest(url, "POST");
        
        //request.setRequestProperty("USER-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)");
        request.setRequestProperty("Content-type", "application/json;charset=utf-8");
        request.setRequestProperty("Content-length", String.valueOf(msg.length));

        OutputStream reqStream = request.getOutputStream();

        reqStream.write(msg);
        reqStream.close();

        ByteArrayOutputStream ms = new ByteArrayOutputStream();
    	InputStream resStream = request.getInputStream();
        
        byte[] buf = new byte[4096];
        int count,totalBytes = 0;
        
        while ((count = resStream.read(buf, 0, buf.length)) > 0) {
            ms.write(buf, 0, count);
            totalBytes += count;
        }
        resStream.close();
        resp = new String(ms.toByteArray(),encoding).getBytes();
		return resp;
	}
	
	private static URLConnection createRequest(String strUrl, String strMethod) throws Exception {
    	URL url = new URL(strUrl);
    	URLConnection conn = url.openConnection();
    	conn.setDoInput(true);
    	conn.setDoOutput(true);
    	conn.setConnectTimeout(60000);  //设置连接主机超时（单位：毫秒）
    	conn.setReadTimeout(60000); 	//设置从主机读取数据超时（单位：毫秒）
    	if (conn instanceof HttpsURLConnection){
    		HttpsURLConnection httpsConn = (HttpsURLConnection)conn;
    		httpsConn.setRequestMethod(strMethod);
    		httpsConn.setSSLSocketFactory(SSLUtil.getInstance().getSSLSocketFactory());
    		httpsConn.setHostnameVerifier(simpleVerifier);
    	} else if (conn instanceof HttpURLConnection){
    		HttpURLConnection httpConn = (HttpURLConnection)conn;
    		httpConn.setRequestMethod(strMethod);
    	}
    	return conn;
    }
	
	private static final HostnameVerifier simpleVerifier = new HostnameVerifier(){
		public boolean verify(String arg0, SSLSession arg1) {
			return true;
		}  	
    };
}
