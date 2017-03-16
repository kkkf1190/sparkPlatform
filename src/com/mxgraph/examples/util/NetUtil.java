package com.mxgraph.examples.util;

import java.io.IOException;  
import java.net.InetAddress;  
import java.net.Socket;  
import java.net.UnknownHostException;

import com.mxgraph.examples.web.Constants;  
  
public class NetUtil {  
      
    /*** 
     *  true:already in using  false:not using  
     * @param port 
     */  
    public static boolean isLoclePortUsing(int port){  
        boolean flag = true;  
        try {  
            flag = isPortUsing("127.0.0.1", port) && isPortUsing(Constants.LOCAL_HOST, port);
        } catch (Exception e) {  
        }  
        return flag;  
    }  
    /*** 
     *  true:already in using  false:not using  
     * @param host 
     * @param port 
     * @throws UnknownHostException  
     */  
    public static boolean isPortUsing(String host,int port) throws UnknownHostException{  
        boolean flag = false;  
        InetAddress theAddress = InetAddress.getByName(host);  
        try {  
            Socket socket = new Socket(theAddress,port);
            flag = true;  
        } catch (IOException e) {  
              
        }  
        return flag;  
    }
    
    public static int getAvailablePort(int start){
    	for(int i=start;i<start+1000;i++){
    		if(!isLoclePortUsing(i)){
    			return i;
    		}
    	}
    	return -1;
    }
    
    public static void main(String[] args){
    	System.out.println("avalubleport:"+getAvailablePort(8088));
    	
    }
    
}  
