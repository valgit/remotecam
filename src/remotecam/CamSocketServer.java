package remotecam;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.text.html.HTMLDocument.Iterator;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class CamSocketServer extends WebSocketServer implements remotecam {
	private Set<WebSocket> conns;
	//TODO: have a list of "client" implementing remotecam ?
	// offer list of client choice ?
	
	public CamSocketServer(InetSocketAddress address, int port) {
		//super(address, decoders);
		super(new InetSocketAddress(port));
		
		System.out.println("websocket created");
	    conns = new HashSet<>();
	}

	 /** 
	   * Method handler when a connection has been closed.
	   */
	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {		
		conns.remove(conn);
		System.out.println("Closed connection to " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
		
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		 System.out.println("ERROR from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
	}

	/** 
	   * Method handler when a message has been received from the client.
	   */
	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("Received: " + message);
		if (message.equals("Shot")) {
			System.out.println("new shot");
		}
	}

	@Override
	public void onMessage( WebSocket conn, ByteBuffer message ) {
		System.out.println("Received: bytes" );
		
		try {
			InputStream in = new ByteArrayInputStream(message.array());
			BufferedImage img = ImageIO.read(in);
			
			// debug !
			int width = img.getWidth();
			int height = img.getHeight();
			System.out.println("size: "+width+" h: "+height);
			
			//TODO: _Context.newImg(img);
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	/** 
	   * Method handler when a new connection has been opened. 
	   */
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		conns.add(conn);
	    System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
	    		
	}

	@Override
	public void takeShot() {
		System.out.println("take shot");
		if (!conns.isEmpty()) {		
			WebSocket conn = conns.iterator().next();
			conn.send("takeShot");
		}
	}

}
