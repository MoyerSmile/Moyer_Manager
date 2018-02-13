package sl.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;






public class JsonReader {
	 public static JSONObject receivePost(HttpServletRequest request) throws IOException, UnsupportedEncodingException {  
		  
	        // 读取请求内容  
	        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));  
	        String line = null;  
	        StringBuilder sb = new StringBuilder();  
	        while ((line = br.readLine()) != null) {  
	            sb.append(line);  
	        }  
	        //将json字符串转换为json对象  
	        JSONObject json;
			try {
				json = new JSONObject(sb.toString());
				return json;  
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	        
	    }  
}
