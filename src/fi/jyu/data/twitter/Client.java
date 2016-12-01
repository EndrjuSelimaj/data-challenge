package fi.jyu.data.twitter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import fi.jyu.util.CommHelpers;

public class Client {

	private Config config;
	
	public Client(Config config) {
		this.config = config;
	}
	
	public void search(String keyword, int count) throws IOException {
		HttpsURLConnection connection = null;
		
		String request = "https://api.twitter.com/1.1/search/tweets.json?q=" + URLEncoder.encode(keyword, "UTF-8") + "&count=" + count;
		
		try {
			URL url = new URL(request); 
			connection = (HttpsURLConnection) url.openConnection();           
			connection.setDoOutput(true);
			connection.setDoInput(true); 
			connection.setRequestMethod("GET"); 
			connection.setRequestProperty("Host", "api.twitter.com");
			connection.setRequestProperty("User-Agent", "Data Challenge");
			connection.setRequestProperty("Authorization", "Bearer " + config.getBearerToken());
			connection.setUseCaches(false);
				
			File f = new File("twitter_data.json");
			if (f.exists()) f.delete();
			
			FileWriter file = new FileWriter("twitter_data.json");
			file.write(CommHelpers.readResponse(connection));
			file.close();
		}
		catch (MalformedURLException e) {
			throw new IOException("Invalid endpoint URL specified.", e);
		}
		finally {
			if (connection != null) {
				connection.disconnect();
			}
		}		
	}
	
	public void test() throws IOException {
		HttpsURLConnection connection = null;
					
		try {
			URL url = new URL("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=jotudin&count=2"); 
			connection = (HttpsURLConnection) url.openConnection();           
			connection.setDoOutput(true);
			connection.setDoInput(true); 
			connection.setRequestMethod("GET"); 
			connection.setRequestProperty("Host", "api.twitter.com");
			connection.setRequestProperty("User-Agent", "Your Program Name");
			connection.setRequestProperty("Authorization", "Bearer " + config.getBearerToken());
			connection.setUseCaches(false);
				
			JSONArray obj = (JSONArray)JSONValue.parse(CommHelpers.readResponse(connection));
			if (obj != null) {
				String tweet = ((JSONObject)obj.get(0)).get("text").toString();
				
				System.out.println("Last thing Mikko Lampi said in Twitter was...");
				System.out.println((tweet != null) ? tweet : "");
			}
		}
		catch (MalformedURLException e) {
			throw new IOException("Invalid endpoint URL specified.", e);
		}
		finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
 
}
