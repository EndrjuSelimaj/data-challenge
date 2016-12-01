package fi.jyu.data.twitter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import fi.jyu.util.CommHelpers;

public class Auth {
	
	public static Config doAuth(Config config) {
		try {
			config.setBearerToken(
					requestBearerToken(
							"https://api.twitter.com/oauth2/token", 
							encodeKeys(config.getConsumerKey(), config.getConsumerSecret())
						)
				);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return config;
	}
	
	// Encodes the consumer key and secret to create the basic authorization key
	private static String encodeKeys(String consumerKey, String consumerSecret) {
		try {
			String encodedConsumerKey = URLEncoder.encode(consumerKey, "UTF-8");
			String encodedConsumerSecret = URLEncoder.encode(consumerSecret, "UTF-8");
			
			String fullKey = encodedConsumerKey + ":" + encodedConsumerSecret;
			byte[] encodedBytes = Base64.encodeBase64(fullKey.getBytes());
			return new String(encodedBytes);  
		}
		catch (UnsupportedEncodingException e) {
			return new String();
		}
	}

	// Constructs the request for requesting a bearer token and returns that token as a string
	private static String requestBearerToken(String endPointUrl, String encodedCredentials) throws IOException {
		HttpsURLConnection connection = null;
		//String encodedCredentials = encodeKeys("<consumerkey>","<consumersecret>");
			
		try {
			URL url = new URL(endPointUrl); 
			connection = (HttpsURLConnection) url.openConnection();           
			connection.setDoOutput(true);
			connection.setDoInput(true); 
			connection.setRequestMethod("POST"); 
			connection.setRequestProperty("Host", "api.twitter.com");
			connection.setRequestProperty("User-Agent", "Your Program Name");
			connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"); 
			connection.setRequestProperty("Content-Length", "29");
			connection.setUseCaches(false);
				
			CommHelpers.writeRequest(connection, "grant_type=client_credentials");
				
			// Parse the JSON response into a JSON mapped object to fetch fields from.
			JSONObject obj = (JSONObject)JSONValue.parse(CommHelpers.readResponse(connection));
				
			if (obj != null) {
				String tokenType = (String)obj.get("token_type");
				String token = (String)obj.get("access_token");
			
				return ((tokenType.equals("bearer")) && (token != null)) ? token : "";
			}
			return new String();
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
