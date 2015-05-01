package globaldata;

public enum ServiceType {
	/**
	 * Start the Android Server Service.
	 */
	ANDROID_SERVER_START,
	
	/**
	 * Stop the Android Server Service.
	 */
	ANDROID_SERVER_STOP,
	
	/**
	 * Response from Android Server Service to the UI thread to make a change in a TextView.
	 */
	ANDROID_SERVER_RESPONSE,

}
