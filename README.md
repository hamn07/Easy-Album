#easyalbum開發



### OAuth
![流程](https://dl.dropboxusercontent.com/u/16245733/-Image.png)

``` html
<a href="https://accounts.google.com/o/oauth2/auth?scope=https://picasaweb.google.com/data/&state=/profile&redirect_uri=http://easyalbum.ddns.net:8080/lib/TestOAuth&response_type=code&client_id=829442868190-g6l54n2aosmp90s3hudkoc44j437k01b.apps.googleusercontent.com">使用Google登入</a>
```

### 圖片效果
請詳讀
[Lesson: Overview of the Java 2D API Concepts](https://docs.oracle.com/javase/tutorial/2d/overview/index.html)
[2D Graphics GUI example code](http://www.java2s.com/Code/Java/2D-Graphics-GUI/Catalog2D-Graphics-GUI.htm)
做出照片透明度的效果
[官方文件](https://docs.oracle.com/javase/tutorial/2d/images/drawimage.html)
```java
/* Create an ARGB BufferedImage */
BufferedImage img = ImageIO.read(imageSrc);
int w = img.getWidth(null);
int h = img.getHeight(null);
BufferedImage bi = new
    BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
Graphics g = bi.getGraphics();
g.drawImage(img, 0, 0, null);

/*
 * Create a rescale filter op that makes the image
 * 50% opaque.
 */
float[] scales = { 1f, 1f, 1f, 0.5f };
float[] offsets = new float[4];
RescaleOp rop = new RescaleOp(scales, offsets, null);

/* Draw the image, applying the filter */
g2d.drawImage(bi, rop, 0, 0);
```
### Maven設定
![Alt text](https://dl.dropboxusercontent.com/u/16245733/-1429743605269.png)
![Alt text](https://dl.dropboxusercontent.com/u/16245733/-1429743613246.png)
![Alt text](https://dl.dropboxusercontent.com/u/16245733/-1429743618995.png)
![Alt text](https://dl.dropboxusercontent.com/u/16245733/-1429743625046.png)


### 開發記事
4.9存檔點: study  graphic2D的tutorial https://docs.oracle.com/javase/tutorial/2d/overview/index.html
4.10進度: 已有文字配照片的效果囉
![Alt text](https://dl.dropboxusercontent.com/u/16245733/-%E5%9C%96%E7%89%87%201.jpg)

4.13已有音樂但會有missing header的問題

4.16釐清mp3的相關原理與名詞
![mp3取樣圖](https://dl.dropboxusercontent.com/u/16245733/http---wp.tweakheadz.com-wp-content-uploads-2013-02-bit-depth-vs-sample-rate-tweakheadz-dot-com.png)
以CD為例:
- channel: 2, 2聲道
- sample rate : 44.1khz, 表示1秒鐘取樣44,100次
- bit depth: 16 bit, 表示每個sample用16個位元記錄
- bit rate: 2 x 44,100 x 16 = 1,411,200, 表示1秒鐘存取1,411,200個位元，約為1.35 Mbit/sec

File Sizes For Stereo Digital Audio

| Bit Depth | Sample Rate    | Bit Rate      | 1 Stereo Minute | 3 Minute Song |
|-----------|----------------|---------------|-----------------|---------------|
| 16        | 44,100         | 1.35 Mbit/sec | 10.1 MB         | 30.3 MB       |
| 16        | 48,000         | 1.46 Mbit/sec | 11.0 MB         | 33 MB         |
| 24        | 96,000         | 4.39 Mbit/sec | 33.0 MB         | 99 MB         |
| mp3       | 128 k/bit rate | 0.13 Mbit/sec | 0.94 MB         | 2.82 MB       |

4.20 使用以下方式擷取固定時間的音樂
```java
	    while (containerAudio.readNextPacket(packetaudio) >= 0 
	    		&& packetaudio.getTimeStamp() / //擷取60秒左右的音樂
	    		   packetaudio.getTimeBase().getDenominator()<60) {
 //decodeAudio
 //encodeAudio
}
```
4.23 已可使用picasa上傳照片後，直接透過picasa api存取照片產生影片
![Alt text](https://dl.dropboxusercontent.com/u/16245733/-1429743299299.png)
![Alt text](https://dl.dropboxusercontent.com/u/16245733/-1429743371993.png)

5.5 找到取出full-size的方式，將影片格式改為1280x720
```java
			URL url = new URL(urlString.substring(0,lastSlashPosition)+
			                  "/s0"+
			                  urlString.substring(lastSlashPosition));
```

5.6 音樂已配上照片撥放時間


**Next Step**: 
- 照片撥放時間若超過音樂怎麼辦, 先上youtube找音樂是超過30min的.
- 直接用servlet重覆轉檔
- 照片存入server後再轉檔，檢查Etag有無更新
- 活潑的轉檔效果

**Enhancement**: 照片順序
### QA
[CI Server 30 - Jenkins總回顧 - iT邦幫忙::IT知識分享社群](http://ithelp.ithome.com.tw/question/10109773)
