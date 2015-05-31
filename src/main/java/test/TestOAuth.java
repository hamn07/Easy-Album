package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;




/**
 * Servlet implementation class TestOAuth
 */
@WebServlet("/TestOAuth")
public class TestOAuth extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestOAuth() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
		  // Google取得access_token的url
		  URL urlObtainToken =  new URL("https://accounts.google.com/o/oauth2/token");
		  HttpURLConnection connectionObtainToken =  (HttpURLConnection) urlObtainToken.openConnection();
		    
		  // 設定此connection使用POST
		  connectionObtainToken.setRequestMethod("POST");
		  connectionObtainToken.setDoOutput(true);
		   System.out.println("code:"+request.getParameter("code"));
		  // 開始傳送參數 
		  OutputStreamWriter writer  = new OutputStreamWriter(connectionObtainToken.getOutputStream());
		  writer.write("code="+request.getParameter("code")+"&");   // 取得Google回傳的參數code
		  writer.write("client_id=829442868190-g6l54n2aosmp90s3hudkoc44j437k01b.apps.googleusercontent.com&");   // 這裡請將xxxx替換成自己的client_id
		  writer.write("client_secret=2SuH_Q2C4b40iRFAiXZYkc3f&");   // 這裡請將xxxx替換成自己的client_serect
		  writer.write("redirect_uri=http://easyalbum.ddns.net:8080/lib/TestOAuth&");   // 這裡請將xxxx替換成自己的redirect_uri
		  writer.write("grant_type=authorization_code");  
		  writer.close();
		  
		  // 如果認證成功
		  if (connectionObtainToken.getResponseCode() == HttpURLConnection.HTTP_OK)
		  {
		   StringBuilder sbLines   = new StringBuilder("");
		   
		   // 取得Google回傳的資料(JSON格式)
		   BufferedReader reader = 
		       new BufferedReader(new InputStreamReader(connectionObtainToken.getInputStream(),"utf-8"));
		   String strLine = "";
		   while((strLine=reader.readLine())!=null){
			System.out.println(strLine);
		    sbLines.append(strLine);
		   }
		  
		   
		   
		   //取得相簿id
		   try {
			   
			   JSONObject jo = new JSONObject(sbLines.toString());
			   
//			   response.addHeader("GData-?Version", "2.0");
			   response.setContentType("text/html");
			   response.setCharacterEncoding("UTF-8");
			   response.getWriter().println("access_token:  "+jo.getString("access_token"));
			   
			   
			   			   
			   // 创建URL对象
//			   String urlStringGetFeed = "https://picasaweb.google.com/data/feed/api/user/default?kind=photo&max-results=10&access_token="+jo.getString("access_token");
			   String urlStringGetFeed = "https://picasaweb.google.com/data/feed/api/user/default/album/easyalbum?"
//			   		                   + "max-results=10&"
			   		                   + "alt=json&"
			   		                   + "access_token="+jo.getString("access_token");

			   System.out.println("access_token : "+jo.getString("access_token"));
			   URL urlPicasaData = new URL(urlStringGetFeed); 
			   
			   // 打开URL连接
			   HttpURLConnection connObtainPicasaData =  (HttpURLConnection) urlPicasaData.openConnection();
			   // 設定使用gdata2.0
			   connObtainPicasaData.addRequestProperty("GData-Version", "2.0");
				   
			   connObtainPicasaData.connect();
               // 响应头部获取
			   Map<String, List<String>> headers = connObtainPicasaData.getHeaderFields();
			   // 遍历所有的响应头字段
			   for (String key : headers.keySet())
			   {
			       System.out.println(key + "\t：\t" + headers.get(key));
			   }
			
			   if (connObtainPicasaData.getResponseCode() == HttpURLConnection.HTTP_OK)
		       {
    			   InputStream is = connObtainPicasaData.getInputStream();
    			   TestImageReader ir = new TestImageReader(is,this.getServletContext().getRealPath("/media"));
    			   ir.generateVideo();
    			   this.forwardNextPage(request, response);
//	               this.outputPhotoAndDescriptionDirectly(request,response,is);
		       }
		   }catch(Exception e){
			   e.printStackTrace();
		   }
		   
		   
	    }
		  else {
			response.getWriter().write(connectionObtainToken.getResponseCode());
		}
	
		  
		  
		  
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	private void forwardNextPage(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
//	       request.setAttribute("jsonPicasaData", is);
	       //request.getRequestDispatcher("/PhotoGallery.jsp").forward(request, response);
	       request.getRequestDispatcher("/SimplePhotoWithDesc.jsp").forward(request, response);
	       return;
	}
	/**
	 * 使用DOM物件來擷取XML檔案，並將之直接輸出HTML
	 * @param request
	 * @param response
	 * @param is
	 * @throws Exception
	 */
	private void outputPhotoAndDescriptionDirectly(HttpServletRequest request,HttpServletResponse response, InputStream is) throws Exception
	{
    		       DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			       DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			       Document doc = dBuilder.parse(is);
			       
			       //optional, but recommended
			       //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			       doc.getDocumentElement().normalize();
			       
			       response.getWriter().println("Root element :" + doc.getDocumentElement().getNodeName());
			   
			       NodeList nList = doc.getElementsByTagName("entry");
			       
			       for (int temp = 0; temp < nList.getLength(); temp++) {
			    	   Node nNode = nList.item(temp);
			    	   
			    	   Element entry = (Element) nNode;
			    	   
			    	   Element content = (Element) entry.getElementsByTagName("content").item(0);
			    	   response.getWriter().println("<img src=\"" +content.getAttribute("src")+"\">");
			    	   
			    	   Element summary = (Element) entry.getElementsByTagName("summary").item(0);
			    	   response.getWriter().println(summary.getTextContent());
			    	   
			    	   
			       }
			       
		       
			   
			   
			   
			   
			   
			   
			   
			   
//				使用rome來parse	   
//			   SyndFeedInput input = new SyndFeedInput();
//			   SyndFeed feed = input.build(new XmlReader(urlPicasaData));
//			   
//			   response.getWriter().println("getUri: "+feed.getUri());
//
//			   
//
//			   response.getWriter().println("getTitleEx: "+feed.getTitleEx());
//			   
//
//			    
//			   java.util.List<SyndEntry> entryList = feed.getEntries();
//			   
//			   for(SyndEntry entry : entryList) {
//				   //response.getWriter().println("entry.getLink(): "+entry.getContents());
//				   java.util.List<SyndContent> contentList = entry.getContents();
//				   
//				   
//				   for (SyndContent content : contentList){
//					   
//					   
//				   }
//					   
//		            
//		        }
			   
			   
			   
	}
	private String getAlbumId(String token, HttpServletResponse response) throws Exception
	{
		String albumId="";
	
		   URL urlPicasaData =
		    		 new URL("https://picasaweb.google.com/data/feed/api/user/default?access_token="+token); 
		   HttpURLConnection connObtainPicasaData =  (HttpURLConnection) urlPicasaData.openConnection();
		   
	       if (connObtainPicasaData.getResponseCode() == HttpURLConnection.HTTP_OK)
	       {
		   	   InputStream is = connObtainPicasaData.getInputStream();
		   	   
		       DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		       DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		       Document doc = dBuilder.parse(is);
		       
		       //optional, but recommended
		       //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		       doc.getDocumentElement().normalize();
		       
		       response.getWriter().println("Root element :" + doc.getDocumentElement().getNodeName());
		   
		       NodeList nList = doc.getElementsByTagName("gphoto:id");
		       
		       for (int temp = 0; temp < 2/*nList.getLength()*/; temp++) {
		    	   Node nNode = nList.item(temp);
		    	   albumId = nNode.getTextContent();		    	   
		       }
		   }
		
		return albumId;
	}
	
	private void getPhotoList(String token, HttpServletResponse response) throws Exception
	{
		   try {
			   
			   
			   String albumId = this.getAlbumId(token, response);
			   response.getWriter().println("\nCurrent Element :" + albumId);
			   
			   
			   
			   URL urlPicasaData =
			    		 new URL("https://picasaweb.google.com/data/feed/api/user/default/albumid/"+albumId+"?access_token="+token); 
			   HttpURLConnection connObtainPicasaData =  (HttpURLConnection) urlPicasaData.openConnection();
			   
			   System.out.print(connObtainPicasaData.getResponseCode());
			   if (connObtainPicasaData.getResponseCode() == HttpURLConnection.HTTP_OK)
		       {
			   	   InputStream is = connObtainPicasaData.getInputStream();
			   	   
			       DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			       DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			       Document doc = dBuilder.parse(is);
			       
			       //optional, but recommended
			       //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			       doc.getDocumentElement().normalize();
			       
			       response.getWriter().println("Root element :" + doc.getDocumentElement().getNodeName());
			   
			       NodeList nList = doc.getElementsByTagName("id");
			       
			       for (int temp = 0; temp < 1/*nList.getLength()*/; temp++) {
			    	   Node nNode = nList.item(temp);
			    	   response.getWriter().println("\nCurrent Element :" + nNode.getTextContent());
			    	   
			       }
			       
		       }
			   
			   
			   
			   

			   
		   }catch(Exception e){
			   e.printStackTrace();
		   }
	}
}
