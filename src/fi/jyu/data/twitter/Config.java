package fi.jyu.data.twitter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Config {
	
	private String consumerKey;
	private String consumerSecret;
	private String bearerToken;	
	
	public Config() {
		File configFile = new File("config.properties");
		 
		try {
		    FileReader reader = new FileReader(configFile);
		    Properties props = new Properties();
		    props.load(reader);
		    this.bearerToken = props.getProperty("oauth.bearerToken");
		    this.consumerKey = props.getProperty("oauth.consumerKey");
		    this.consumerSecret = props.getProperty("oauth.consumerSecret");
		    reader.close();
		} catch (FileNotFoundException ex) {
		    // file does not exist
		} catch (IOException ex) {
		    // I/O error
		}
	}
	
	private void save() {
		File configFile = new File("config.properties");
		 
		try {
		    FileReader reader = new FileReader(configFile);
		    Properties props = new Properties();
		    props.load(reader);
		    reader.close();
			FileWriter writer = new FileWriter("config.properties");
			props.setProperty("oauth.consumerKey", this.consumerKey);
			props.setProperty("oauth.consumerSecret", this.consumerSecret);
		    props.setProperty("oauth.bearerToken", this.bearerToken);
			props.store(writer, null);
			writer.close();
		} catch (FileNotFoundException ex) {
		    // file does not exist
		} catch (IOException ex) {
		    // I/O error
		}		
	}


	public String getBearerToken() {
		return bearerToken;
	}

	public void setBearerToken(String bearerToken) {
		this.bearerToken = bearerToken;
		this.save();
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
		this.save();
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
		this.save();
	}
}
