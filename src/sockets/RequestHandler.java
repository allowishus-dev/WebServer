package sockets;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RequestHandler implements Runnable {
	private String clientSentence;
	private String[] getReturned;
	private String fileName;
	private String path = "/code/WebServer/files/";
	private Socket connectionSocket;
	private static int threadNumber;
	
	public RequestHandler(Socket connectionSocket, int threadNumber) {
		this.connectionSocket = connectionSocket;
		RequestHandler.threadNumber = threadNumber;
	}

	@Override
	public void run(){
		try {
			BufferedReader inFromClient;
	
			inFromClient = new BufferedReader(
				new InputStreamReader(connectionSocket.getInputStream()));
			
			System.out.println("T#" + threadNumber + " : Create input stream from client");
			
			DataOutputStream  outToClient = 
			     new DataOutputStream(connectionSocket.getOutputStream());
			System.out.println("T#" + threadNumber + " : Create output stream to client");
			
			System.out.println("T#" + threadNumber + " : Waiting for input from client ...");
			clientSentence = inFromClient.readLine();
			System.out.println("T#" + threadNumber + " : Reading input from client");
			
			if (clientSentence.startsWith("GET")) {
				System.out.println("T#" + threadNumber + " : Got GET!");
				getReturned = clientSentence.split(" ");
	//			for (String s : getReturned) {
	//				System.out.println(s);
	//			}
				
				System.out.println("T#" + threadNumber + " : Getting file path");
				fileName = path + getReturned[1];
				
				if (fileName.endsWith("/")) {
					System.out.println("T#" + threadNumber + " : Directory requested. Adding index to filepath");
					fileName += "index.html";
				}
				
				File requestedFile = new File(fileName);
				
				if (!requestedFile.isFile()) {
					// not found or not working
					System.out.println("T#" + threadNumber + " : Can't find it. Replaced with error 404");
					requestedFile = new File(path + "error404.html");
					
				}
				
				
				int lengthOfFile = (int) requestedFile.length();
				
				byte[] bytes = new byte[lengthOfFile];
				
				FileInputStream fStream = new FileInputStream(requestedFile);
				
				System.out.println("T#" + threadNumber + " : Reading from file input stream to byte array");
				fStream.read(bytes);
				
	//			for (byte b : bytes) {
	//				System.out.print(new Character((char) b));
	//			}
	//			System.out.println();
				
				System.out.println("T#" + threadNumber + " : Writing bytes to socket");
				
				outToClient.writeBytes("HTTP/1.1 200 ");
				
				checkContentTypes(fileName, outToClient);
				
				outToClient.writeBytes("Content-Length: " + lengthOfFile + "\r\n");
				outToClient.writeBytes("\r\n");
				outToClient.write(bytes);
				connectionSocket.close();
				
				System.out.println("T#" + threadNumber + " : Closing socket\n");
			}			
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void checkContentTypes(String fileName, DataOutputStream outToClient) throws IOException {
		if(fileName.endsWith(".jpg")) {
            outToClient.writeBytes("Content-Type: image/jpg \r\n");
            System.out.println("T#" + threadNumber + " : JPG image requested");
		}
		if(fileName.endsWith(".gif")) {
            outToClient.writeBytes("Content-Type: image/gif \r\n");
            System.out.println("T#" + threadNumber + " : GIF image requested");
		}
		else if(fileName.endsWith(".png")) {
            outToClient.writeBytes("Content-Type: image/png \r\n");
            System.out.println("T#" + threadNumber + " : PNG image requested");
		}
		else if(fileName.endsWith(".svg")) {
            outToClient.writeBytes("Content-Type: image/svg \r\n");
            System.out.println("T#" + threadNumber + " : SVG image requested");
		}
		else if(fileName.endsWith(".svg")) {
            outToClient.writeBytes("Content-Type: image/svg \r\n");
            System.out.println("T#" + threadNumber + " : SVG image requested");
		}
		else if(fileName.endsWith(".txt")) {
            outToClient.writeBytes("Content-Type: text/plain \r\n");
            System.out.println("T#" + threadNumber + " : Plain text requested");
		}
		else if(fileName.endsWith(".rtf")) {
            outToClient.writeBytes("Content-Type: text/rtf \r\n");
            System.out.println("T#" + threadNumber + " : RTF document requested");
		}
		else if(fileName.endsWith(".pdf")) {
            outToClient.writeBytes("Content-Type: application/pdf \r\n");
            System.out.println("T#" + threadNumber + " : PDF document requested");
		}
		else if(fileName.endsWith(".css")) {
            outToClient.writeBytes("Content-Type: text/css \r\n");
            System.out.println("T#" + threadNumber + " : CSS script requested");
		}
		else if(fileName.endsWith(".xml")) {
            outToClient.writeBytes("Content-Type: text/xml \r\n");
            System.out.println("T#" + threadNumber + " : XML script requested");
		}
	}
}
