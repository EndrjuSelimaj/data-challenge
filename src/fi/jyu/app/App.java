package fi.jyu.app;

import java.io.IOException;

import fi.jyu.data.twitter.Auth;
import fi.jyu.data.twitter.Client;
import fi.jyu.data.twitter.Config;


public class App {

	public static void main(String[] args) {
		Config config = new Config();
		Client client = null;
		
		if (args.length == 0) {
			System.out.println("twelper.jar [config|test|search] <what to search> <how many tweets>");
			System.exit(0);
		}
		
		switch (args[0]) {
		case "config": 
			config = Auth.doAuth(config);
			break;
		
		case "test":
			client = new Client(config);

			try {
				client.test();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "search":
			client = new Client(config);
			
			if (null != args[1] && !args[1].isEmpty()
					&& null != args[2] && !args[2].isEmpty()) {
				try {
					client.search(args[1], Integer.parseInt(args[2]));
					System.out.println("Done.");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			} else {
				System.out.println("Don't know what to search and how many tweets to return.");
			}
			break;
		
		default:
			System.out.println("Usage");
			System.out.println("twelper.jar [config|test|search] <what to search> <how many tweets>");
		}
	}

}
