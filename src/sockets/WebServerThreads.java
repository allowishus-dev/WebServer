package sockets;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServerThreads {
	public static void main(String args[]) throws Exception { 
		
		ServerSocket welcomeSocket = new ServerSocket(8001);
		
		int threadNumber = 0;
		  
	    while(true) {
			  
			System.out.println("Waiting for external socket with request ...");
			Socket connectionSocket = welcomeSocket.accept(); 
			System.out.println("Found client");
			
			Runnable request = new RequestHandler(connectionSocket, threadNumber);
			
			new Thread(request).start();
			
			System.out.println("Threads: " + Thread.activeCount());
			threadNumber++;
		}
	}
}
