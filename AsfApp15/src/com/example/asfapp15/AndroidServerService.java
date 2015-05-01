package com.example.asfapp15;

import globaldata.GlobalData;
import globaldata.ServiceType;

import java.io.EOFException;
import java.io.IOException;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import nanohttpd.NanoHTTPD;
import nanohttpd.NanoHTTPD.Response.IStatus;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;

public class AndroidServerService extends IntentService{

	LocalBroadcastManager broadcaster;
	static AndroidServer httpServer;
	
	
	public AndroidServerService() {
		super("");
	}
	
	

	@Override
	public void onCreate() {
		super.onCreate();
		broadcaster = LocalBroadcastManager.getInstance(this);
	}



	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();
		ServiceType t = ServiceType.valueOf(action);
		switch(t){
			case ANDROID_SERVER_START:
				httpServer = new AndroidServer(GlobalData.httpRequestsRcvPort);
				try {
					httpServer.start();
				} catch (IOException e) {
					System.out.println("EERROORR: "+e.getMessage());
				}
				break;
			case ANDROID_SERVER_STOP:
				httpServer.stop();
				break;
			default:
				Log.w(GlobalData.LOG_WARNING, "AndroidServerService: Did nothing...");
				
		}
		
	}
	
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.w(GlobalData.LOG_WARNING, "IM DYIIIINGGGGG");
		super.onDestroy();
	}



	public class AndroidServer extends  NanoHTTPD{
		private int i = 0;
		
		public AndroidServer(int port) {
			super(port);
		}

		@Override 
		public Response serve(IHTTPSession session) {
	        Method method = session.getMethod();
	        String uri = session.getUri();

	        String msg = "";//"<html><body><h1>Hello server "+"katimagiko"+method + " '" + uri + "' " +"</h1>\n";
	        Map<String, String> parms = session.getParms();
//	        if (parms.get("lala") == null)
//	            msg +=
//	                    "<form action='?' method='get'>\n" +
//	                            "  <p>Your name: <input type='text' name='username'></p>\n" +
//	                            "</form>\n";
//	        else
//	            msg += "<p>Hello, " + parms.get("lala") + "!</p>";
	        if(parms.get("key") == null || parms.get("mac") == null)
	        	msg =  "Wrong parameters";
	        String serverResponse = authenticationRequest(parms.get("key"), parms.get("mac"));
	        
	        if(!(parms.get("key") == null || parms.get("mac") == null))
	        	msg += method + " " + serverResponse + " " + parms.get("key") + " " + parms.get("mac");
//	        msg += "</body></html>\n";

	        
	        // Workaround because of duplicates
	        //Call notify every other call of this function.
//			++i;
//	        if(i % 2 == 1){	
		        notifyUIThread("YOLO");
//	        }else
//	        	i = 0;
	        return new NanoHTTPD.Response(Response.Status.OK, MIME_HTML, msg);
	    }
		
		private String authenticationRequest(String privateSharedKey, String macAddress){
			//Creating the SoapObject. SoapObject has the arguments of the function we want to call.
			//Here we have authenticate(String macAddress, String sharedKey) 
			//so macAddress is arg0 and sharedKey is arg1.
			SoapObject sO = new SoapObject("http://webservice/","authenticate");
			PropertyInfo pO = new PropertyInfo();
			pO.name = "arg0";
			pO.setValue(macAddress);
			sO.addProperty(pO);
			pO = new PropertyInfo();
			pO.name = "arg1";
			pO.setValue(privateSharedKey);
			sO.addProperty(pO);
			
			//Building the message
			//System.setProperty("http.keepAlive", "false");
			SoapSerializationEnvelope sEnv = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			sEnv.setOutputSoapObject(sO);
			HttpTransportSE httpTrans = null;
			httpTrans = new HttpTransportSE("http://" + GlobalData.serverIP + ":" +GlobalData.serverPort + "/authserver?wsdl");
			
			try {

				httpTrans.call("\"authenticate\"", sEnv);
				//Make the call. If everything goes well we will return the response.
				SoapPrimitive response = (SoapPrimitive) sEnv.getResponse();
				
				return response.toString();
				
			}catch(EOFException e){
				e.printStackTrace();
				//return "Timeout Error";
			} catch (HttpResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//return "Error1";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//return "Error2";
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//return "Error3";
			}finally{
				httpTrans = null;
			}
			return null;
		}
		
		private void notifyUIThread(String message){
			Intent intent = new Intent(ServiceType.ANDROID_SERVER_RESPONSE.toString());
		    intent.putExtra(GlobalData.ANDROID_SERVER_RESPONSE_MSG, message);
		    broadcaster.sendBroadcast(intent);
		}
		
		
		

	}
	

}
