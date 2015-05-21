package remotecam;

import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

import java.awt.*;  

import javax.swing.JFrame;  

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;  
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class jimage extends Canvas implements ActionListener,CameraModelListener,CamSocketServerListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//TODO:
	private frameWin project;
	
	private CameraModel camera;
	private CamSocketServer server;
	
	private JButton snap;
	
	private BufferedImage currentShot; 
	final static BasicStroke stroke = new BasicStroke(2.0f);
	
	public void paint(Graphics g) {  
		Graphics2D g2d = (Graphics2D) g;		
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		//System.out.println("paint - in ");
		//Toolkit t=Toolkit.getDefaultToolkit();  
		//Image i=t.getImage("p3.gif");
		
		int fw = getWidth();
		int fh = getHeight();

		//double ratio = (double)fw/fh;
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, fw, fh);
		
		if (currentShot != null) {
			//System.out.println("currentShot");							
			int h = currentShot.getHeight(null);
			int w = currentShot.getWidth(null);
			
			//double imgratio =(double)w/h;
			//System.out.println("Frame ratio : " + ratio + " Shot ratio : " + imgratio);
			double echx = (double)w/fw;
			double echy = (double)h/fh;
			double ech = 1.0;
			
			if (echx < 1.0)
				ech = echx;
			if ((echy<1) && (echy> echx))
				ech = echy;
			//System.out.println("Frame echx : " + echx + " echy : " + echy + " choose :" + ech);
			/*
			g2d.drawImage(currentShot, 
					0,0,(int)(w/ech),(int)(h/ech), // dest
					0,0,w,h, // src
					null);
					*/
			g2d.drawImage(currentShot, 
					0,0,w,h, // dest
					0,0,w,h, // src
					null);
		} 
		
		// draw HD frame
		int hHD = 9* fw / 16;
		int delta = (fh - hHD)/2;
				
		g2d.setPaint(Color.magenta);
		g2d.setStroke(stroke);
		
		Rectangle2D HDRect = new Rectangle(0,delta,fw,hHD);
		        
        g2d.draw(HDRect);
        /*
		g2d.setColor(Color.blue);
		g2d.drawLine(30, 30, 80, 80);
		g2d.drawRect(20, 150, 100, 100);
		g2d.fillRect(20, 150, 100, 100);
		 */
	}  

	public jimage(frameWin f) {
		setSize(700,500);

		f.getContentPane().add(this);
				
		snap =new JButton("Snap");
		snap.addActionListener(this);

		f.getContentPane().add(snap);
				
		project = f;
		currentShot = null;
	}

	void createAndShowGUI() {
		//Create and set up the window.
		System.out.println("createAndShowGUI");
	
		// websocket server ?
		
		server = new CamSocketServer(new InetSocketAddress("10.24.244.99",5000),5000,this);
	    server.start();
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == snap) {
			//System.out.println("take snap");
			if (camera != null)
				camera.takeShot();			
		}
	}


	
	
	@Override
	public void onShot(byte[] img) {		
		/*
		int width = img.getWidth();
		int height = img.getHeight();
		//System.out.println("onShot: size: "+width+" h: "+height);
		currentShot = img;
		repaint();
		 */
		
		if (project != null)
			project.saveShot(img);

		// convert to image for display
		InputStream in = new ByteArrayInputStream(img);
		try {
			currentShot = ImageIO.read(in);
		} catch (IOException e) {			
			e.printStackTrace();
		}		
		repaint();
		
	}

		@Override
	public void onPreview(BufferedImage img) {
		// TODO Auto-generated method stub
		//System.out.println("onPreview: new img");
		currentShot = img;
		repaint();
	}

	@Override
	public void addCamera(CameraModel camera) {
		this.camera = camera;
		camera.attach(this);
	}

	@Override
	public void delCamera(CameraModel camera) {
		camera.detach(this);
		this.camera = null;		
	}

}
