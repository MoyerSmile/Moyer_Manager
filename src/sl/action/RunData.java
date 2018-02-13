package sl.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import sl.util.JsonReader;
import web.xzy.base.HttpAction;
import web.xzy.base.HttpInfo;

public class RunData extends HttpAction{

	public String runData(HttpInfo httpInfo,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject json1 = JsonReader.receivePost(request);
		System.out.println(json1);
    	JSONArray descJarr = new JSONArray();
    	int j=22;
    	for(int i=0;i<j;i++){
    		JSONObject descJ = new JSONObject();
    		descJ.put("id", i+1);
    		descJ.put("date","2017-01-01");
    		descJ.put("address","上海"+Math.random());
    		descJ.put("phone",(900+Math.random()*1000));
    		descJarr.put(descJ);
    	}
    	
    	JSONObject json = new JSONObject();
    	json.put("list",descJarr);
    	json.put("total",22);
    	this.writeJson(httpInfo, request, response, json);
    	return null;
    }
	
	public String showById(HttpInfo httpInfo,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject json1 = JsonReader.receivePost(request);
		System.out.println(json1);
    		JSONObject json = new JSONObject();
    		json.put("id", 1);
    		json.put("date",2017-01-01);
    		json.put("address","上海"+Math.random());
    		json.put("phone",38543785);
    	this.writeJson(httpInfo, request, response, json);
    	return null;
    }
	
	
 }

