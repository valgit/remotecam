package remotecam;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.java_websocket.WebSocket;

public class CameraModel {
	private WebSocket conn;
	private String model;
	
	// should be a set ?
	private CameraModelListener listener;
	
	public CameraModel(WebSocket conn) {
		super();
		this.conn = conn;
	}
	
	public void parseMessage(String msg) {
		System.out.println("parseMessage: " + msg);
		if (msg.equals("hello")) {
			model = msg.split(":",2)[1];
		}
		if (msg.equals("Shot")) {
			System.out.println("new shot");
		}
	}
	
	public void parseMessage( ByteBuffer message ) {
		System.out.println("parseMessage: bytes" );
		
		try {
			InputStream in = new ByteArrayInputStream(message.array());
			BufferedImage img = ImageIO.read(in);
			
			// debug !
			int width = img.getWidth();
			int height = img.getHeight();
			System.out.println("size: "+width+" h: "+height);
			
			//TODO: _Context.newImg(img);
			//onShot(img);
			
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
		
	public void takeShot() {
		System.out.println("take shot");
		conn.send("takeShot");
	}
	
	public void attach(CameraModelListener listener) {
		this.listener = listener;
	}
	
	public void detach(CameraModelListener listener) {
		this.listener = null;
	}
}
