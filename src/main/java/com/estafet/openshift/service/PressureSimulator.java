package com.estafet.openshift.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

@Path(value = "/")
public class PressureSimulator {
	private final Logger log = Logger.getLogger(PressureSimulator.class);

	@GET
	public String hello() {
		return "Welcome";
	}
	
	@GET
	@Path(value = "/setPressure")
	@Produces(MediaType.APPLICATION_JSON)
	public void hello1(@QueryParam("deviceName") String deviceName, @QueryParam("iterations") int iterations,
			@QueryParam("interval") int interval) throws InterruptedException {
		Map<String, Object> result = new HashMap<>();
		double random = 0;
		for (int i = 0; i < iterations; i++) {
			result.clear();
			random = Math.random() * 50 + 1;
			result.put("thingName", deviceName);
			result.put("pressure", random);			
			result.put("timestamp", new Timestamp(System.currentTimeMillis()).getTime());
			try {
				makePostJsonRequest("http://iot-reg-iot-registry.192.168.42.182.nip.io/send", result.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Thread.sleep(interval*1000);
			
		}
		
	}

	public static int makePostJsonRequest(String url, String jsonString) throws IOException {
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		StringEntity params = new StringEntity(jsonString);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("content-type", "application/json");
		httpPost.setEntity(params);
	    HttpResponse response = client.execute(httpPost);
		
		return response.getStatusLine().getStatusCode();
	}
}
