package test;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

public class TestImageReader {

	String urlAudioFile;
	String urlVideoOutput;
	String urlMediaPath;
	int timeIntervalPerPhoto;
	InputStream is;
	String photoQuality;
	
	TestImageReader(InputStream is, String mediaPath) {
		// TODO Auto-generated constructor stub
		this.urlAudioFile = mediaPath+"\\Country Love Songs For Wedding.mp3";
		this.urlVideoOutput = mediaPath+"\\ouput.mp4";
		this.is = is;
		this.urlMediaPath = mediaPath;
		this.timeIntervalPerPhoto = 8;
		this.photoQuality = "/s1280";
//		this.photoQuality = "/s0";
	}
	
	public int generateVideo() throws IOException, InterruptedException {
		
		
		
		
		//CaptureScreenToFile captureScreenToFile = new CaptureScreenToFile();
		final TimeUnit TIME_UNIT = Global.DEFAULT_TIME_UNIT;

		final IRational FRAME_RATE=IRational.make(6,1);//FPS(Frames Per Second)每秒顯示幀數, 6/1=6, 每秒跑6張畫面
		final int fontSize = 35;
		
		final IMediaWriter writer = ToolFactory.makeWriter(urlVideoOutput);
		

		IContainer containerAudio = IContainer.make();
//		IContainerFormat formatAudio = IContainerFormat.make();
//		formatAudio.setInputFormat("mp3");
		if (containerAudio.open(urlAudioFile, IContainer.Type.READ, null) < 0)
	        throw new IllegalArgumentException("Cant find " + urlAudioFile);

        // 查询流的数目
        int numStreams = containerAudio.getNumStreams();

        // 查询总时长
        long duration = containerAudio.getDuration();

        // 查询总文件大小
        long fileSize = containerAudio.getFileSize();

        // 查询比特率
        long bitRate = containerAudio.getBitRate();

        System.out.println("Number of streams: " + numStreams);
        System.out.println("Duration (ms): " + duration);
        System.out.println("File Size (bytes): " + fileSize);
        System.out.println("Bit Rate: " + bitRate);
		
		// read audio file and create stream
        IStream streamAudio = containerAudio.getStream(0);
	    IStreamCoder coderAudio = streamAudio.getStreamCoder();
		
	    if (coderAudio.open(null, null) < 0)
	        throw new RuntimeException("Cant open audio coder");
	    
        System.out.println("*** Start of Stream Info ***");

        System.out.printf("type: %s; ", coderAudio.getCodecType());
        System.out.printf("codec: %s; ", coderAudio.getCodecID());
        System.out.printf("duration: %s; ", streamAudio.getDuration());
        System.out.printf("start time: %s; ", containerAudio.getStartTime());
        System.out.printf("timebase: %d/%d; ",
        		streamAudio.getTimeBase().getNumerator(),
        		streamAudio.getTimeBase().getDenominator());
        System.out.printf("coder tb: %d/%d; ",
                coderAudio.getTimeBase().getNumerator(),
                coderAudio.getTimeBase().getDenominator());
        System.out.println();
        System.out.printf("sample rate: %d; ", coderAudio.getSampleRate());
        System.out.printf("channels: %d; ", coderAudio.getChannels());
        System.out.printf("format: %s", coderAudio.getSampleFormat());
        
        System.out.println();
        System.out.println("*** End of Stream Info ***");
	    
	    
	    IPacket packetaudio = IPacket.make();
		
//		URL url = new URL("https://lh3.googleusercontent.com/-pJAr-DuF4KE/VNIV4d8nrEI/AAAAAAAEM6I/W8dS7lKpbCg/_MG_0727.JPG");
//		Image pictureImage = ImageIO.read(url);
//		
		BufferedImage frameImage =new BufferedImage(1280, 720, BufferedImage.TYPE_3BYTE_BGR);
		
		Graphics2D g = frameImage.createGraphics();
		g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

		Font font = new Font(Font.DIALOG_INPUT, Font.ITALIC, fontSize);
		
		g.setFont(font);
		
		FontRenderContext frc = g.getFontRenderContext();

		writer.addVideoStream(0, 0,
		          FRAME_RATE,
//				  ICodec.ID.CODEC_ID_MPEG4,
		          frameImage.getWidth(), frameImage.getHeight());
		

		writer.addAudioStream(1, 0, coderAudio.getChannels(), coderAudio.getSampleRate());
		

	    StringBuilder sbLines   = new StringBuilder("");
	    BufferedReader reader = 
	                      new BufferedReader(new InputStreamReader(is,"utf-8"));
	    String strLine = "";
	    while((strLine=reader.readLine())!=null)
	    {
	        sbLines.append(strLine);
	    }
	    JSONObject jo = new JSONObject(sbLines.toString());
	    
	    
	    JSONObject feed = jo.getJSONObject("feed");
	    
	    JSONArray entryArray = feed.getJSONArray("entry");
		
//		String[] urlStrings = {"https://lh3.googleusercontent.com/-pJAr-DuF4KE/VNIV4d8nrEI/AAAAAAAEM6I/W8dS7lKpbCg/_MG_0727.JPG",
//				               "https://lh6.googleusercontent.com/-bFvwyNEKDUk/VNIcEhDFbBI/AAAAAAAEM6g/d2iZ3XJtYX0/_MG_1172.JPG",
//				               "https://lh5.googleusercontent.com/-mDcYwsufUVM/VNn-k5c0GDI/AAAAAAAENMM/18cWhev7Rbs/upload_-1.jpg",
//				               "https://lh3.googleusercontent.com/-pJAr-DuF4KE/VNIV4d8nrEI/AAAAAAAEM6I/W8dS7lKpbCg/_MG_0727.JPG",
//		                      };
//		String[] descStrings ={"彩衣娛親之水母貝貝","跟七七老師的臨別一照","破壞力超強的小妹","彩衣娛親之水母貝貝",};
	    
	    JSONObject photoJsonObject;
	    int videoDuration=0;
		for (int i=0;i<entryArray.length();i++)
		{
			photoJsonObject = entryArray.getJSONObject(i);
			
			// 取得圖片位置
			String urlString = photoJsonObject.getJSONObject("content").getString("src");
			// 取得最後一個slash字元的位置
			int lastSlashPosition = urlString.lastIndexOf("/");
			
			
			
			URL url = new URL(urlString.substring(0,lastSlashPosition)+
			                  photoQuality+
			                  urlString.substring(lastSlashPosition));
			Image pictureImage = ImageIO.read(url);
			
		    g.setColor(Color.BLACK);
		    g.fillRect(0, 0, frameImage.getWidth(), frameImage.getHeight());
			
		    //放照片
		    int wImage = pictureImage.getWidth(null);
		    int hImage = pictureImage.getHeight(null);
		    int wResizeImage;
		    int hResizeImage;
		    
		    int xImage;
		    int yImage; 
		    if (wImage>hImage){
		    	wResizeImage = frameImage.getWidth();
		    	hResizeImage = Math.floorDiv(hImage*frameImage.getWidth(), wImage);
		    	
		    	xImage = 0;
		    	yImage = (frameImage.getHeight()-hResizeImage) / 2;
		    }
		    else if (wImage<hImage) {
		    	wResizeImage = Math.floorDiv(wImage*frameImage.getHeight(), hImage);
		    	hResizeImage = frameImage.getHeight();
		    	
		    	xImage = (frameImage.getWidth()-wResizeImage) / 2;
		    	yImage = 0;
			}
		    else {
		    	wResizeImage = frameImage.getWidth();
		    	hResizeImage = Math.floorDiv(hImage*frameImage.getWidth(), wImage);
		    	
		    	xImage = 0;
		    	yImage = (frameImage.getHeight()-hResizeImage) / 2;
			}
		    
			g.drawImage(pictureImage, 
					       xImage, yImage, 
					       wResizeImage, hResizeImage, null);
			
			//設定文字區底色
			float alpha = 0.35f;
			int type = AlphaComposite.SRC_OVER; 
			AlphaComposite composite = 
			  AlphaComposite.getInstance(type, alpha);
			Color color = new Color(1, 0, 0, alpha); //Red 
			g.setColor(color);
			g.fillRect(0, frameImage.getHeight()-70, frameImage.getWidth(), 50);
			
			
			//放文字
			int wText = (int) font.getStringBounds((String)photoJsonObject.getJSONObject("summary").get("$t"), frc).getWidth();
			int xText = (frameImage.getWidth() - wText) / 2;
			g.setColor(Color.WHITE);
			g.drawString((String)photoJsonObject.getJSONObject("summary").get("$t"), xText, frameImage.getHeight()-35);
			long ts = TIME_UNIT.convert(timeIntervalPerPhoto*i, TimeUnit.SECONDS);
	        writer.encodeVideo(0, frameImage, ts, TIME_UNIT);
	        
		}
        videoDuration = timeIntervalPerPhoto*entryArray.length();
        System.out.println(videoDuration);
        
        // audio packet 
		
	    while (containerAudio.readNextPacket(packetaudio) >= 0 
	    		&& packetaudio.getTimeStamp() / //擷取60秒左右的音樂
	    		   packetaudio.getTimeBase().getDenominator()<videoDuration) {

	       // System.out.println(packetaudio.getDuration());
//	    	System.out.println(i++);
//	    	System.out.println(packetaudio.getDuration());
//	    	System.out.println(packetaudio.getTimeBase().getDenominator());
//	    	System.out.println(packetaudio.getTimeStamp());

	        // audio packet 
	        IAudioSamples samples = IAudioSamples.make(512, coderAudio.getChannels(), IAudioSamples.Format.FMT_S32);
	        coderAudio.decodeAudio(samples, packetaudio, 0);
	        if (samples.isComplete()) 
	            writer.encodeAudio(1, samples);

	    }
        
        
        coderAudio.close();
        containerAudio.close();
        writer.close();
        
        Transcoding("ogg");
        
        
        
        return 0;
	}
	
	private int Transcoding(String format)
	{
		IMediaReader reader = ToolFactory.makeReader(urlVideoOutput);
		
		IMediaWriter writer = ToolFactory.makeWriter(urlMediaPath + "\\output." + format , reader);
		
		reader.addListener(writer);
		
		
//		IMediaViewer viewer = ToolFactory.makeViewer(true);
//		
//		reader.addListener(viewer);
		
		while (reader.readPacket()==null) ;
		
//		writer.close();
//		reader.close();
		return 0;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		TestImageReader ir = new TestImageReader(null, "d:\\dvp");
		ir.Transcoding("ogg");
	}
}
