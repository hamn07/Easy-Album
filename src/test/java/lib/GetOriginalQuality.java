package lib;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class GetOriginalQuality {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String urlString = "https://lh3.googleusercontent.com/-OR_V7RZNvag/VT3n_7Dqd8I/AAAAAAAEbNg/AAjTYT0le7g/IMG_1849.JPG"; 
		System.out.println(urlString);
		
		int lastSlashPosition = urlString.lastIndexOf("/");
		System.out.println(urlString.substring(0,lastSlashPosition));
		
		System.out.println(urlString.substring(lastSlashPosition));
		
		System.out.println(urlString.substring(0,lastSlashPosition)+
				           "/s0"+
				           urlString.substring(lastSlashPosition));
		
	}

}
