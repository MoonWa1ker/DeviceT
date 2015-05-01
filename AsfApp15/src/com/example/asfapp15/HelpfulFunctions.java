package com.example.asfapp15;

import globaldata.GlobalData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class HelpfulFunctions {
	
	
	
	public HelpfulFunctions(){
		
	}
	
	public Set<String> getConnectedDevicesMac(){
		BufferedReader br = null;
		Set<String> macSet = new HashSet<String>(0);
	    try {
	        br = new BufferedReader(new FileReader("/proc/net/arp"));
	        String line;
	        while ((line = br.readLine()) != null) {

	            String[] splitted = line.split(" +");
	            if (splitted != null && splitted.length >= 4) {
	            	if(splitted[3].contains("type") || splitted[3].contains(GlobalData.serverMacAddress))
	            		continue;
	            	macSet.add(splitted[3]);
	            }
	        }
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    finally {
	        try {
	            br.close();
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    return macSet;
	}

}
