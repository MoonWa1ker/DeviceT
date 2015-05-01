package globaldata;

public class GlobalData {

	//!<------------ Server  ------------>!//
	/**
	 * IP Address of the Server
	 */
	static final public String serverIP = "192.168.43.223";
	
	/**
	 * Port of the Server
	 */
	static final public String serverPort = "9995";
	
	/**
	 * MAC Address of the Server
	 */
	static final public String serverMacAddress = "34:23:87:78:86:2f";
	
	//!<------------ Server  ------------>!//
	
	//!<------------ HTTP Server (this device)  ------------>!//
	/**
	 * Message as a Response from Android Server Service to the UI thread to make a change in a TextView.
	 */
	static final public String ANDROID_SERVER_RESPONSE_MSG = "com.example.asfapp15.AndroidServerService.ANDROID_SERVER_RESPONSE_MSG";
	
	/**
	 * Android device listening for HTTP requests on this port.
	 */
	static final public int httpRequestsRcvPort = 9990;
	
	//!<------------ HTTP Server (this device)  ------------>!//
	
	static final public String LOG_WARNING = "AsfApp13_LOGWARNING";
	
}
