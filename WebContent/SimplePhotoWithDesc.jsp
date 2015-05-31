<%@ page import="org.json.JSONArray"%>
<%@ page import="java.io.InputStreamReader"%>
<%@ page import="java.io.BufferedReader"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="org.json.JSONObject"%> 
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<video id="movie" autoplay loop  width="1280" height="720">
  <source src="./media/ouput.mp4" type="video/mp4" />
  <source src="./media/ouput.ogg" type="video/ogg" />
  您的瀏覽器不支援HTML 5影片播放標籤<video>格式。
  Your browser doesn't support the <video> tag.
</video>
</body>
</html>