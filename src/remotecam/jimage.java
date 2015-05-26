package remotecam;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

import java.awt.*;  

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.io.ByteArrayInputStream;
import java.io.IOException;  
import java.io.InputStream;
import java.net.InetSocketAddress;

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
	
	private BufferedImage previewFrame; 
	private BufferedImage lastShot;
	
	final static BasicStroke stroke = new BasicStroke(2.0f);
	private float alpha = 0.5f;
	private AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);

	private Dimension scaled;
	
	private Dimension preserveRatio(Dimension img) {
		Dimension frame = getSize();
		
		double resizedHeight;
		double resizedWidth;
		
		double aspect = img.getWidth() / img.getHeight();
		 
		if(frame.getHeight() < frame.getWidth()) {
		     resizedHeight = frame.getHeight();
		     resizedWidth =  (resizedHeight * aspect);
		}
		 
		else { // screen width is smaller than height (mobile, etc)
		     resizedWidth = frame.getWidth();
		     resizedHeight =  (resizedWidth / aspect);      
		}
		Dimension resized = new Dimension();
		resized.setSize(resizedWidth, resizedHeight);
		return resized;
	}
	
	public void paint(Graphics g) {  
		Graphics2D g2d = (Graphics2D) g;		
		
		Composite originalComposite = g2d.getComposite();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, getWidth(), getHeight() );
						
		if (lastShot != null) {
			int h = lastShot.getHeight(null);
			int w = lastShot.getWidth(null);
			g2d.drawImage(lastShot, 
					0,0,w,h, // dest
					0,0,w,h, // src
					null);
		}
		if (previewFrame != null) {
			g2d.setComposite(ac);
			
			
			//System.out.println("currentShot");							
			int h = previewFrame.getHeight(null);
			int w = previewFrame.getWidth(null);
			
			/*
			Dimension dim_img = new Dimension(w,h);
			
			Dimension scaled = preserveRatio(dim_img);
				
					*/
			
			//System.out.println("Frame echx : " + echx + " echy : " + echy + " choose :" + ech);
			/*
			g2d.drawImage(currentShot, 
					0,0,(int)(w/ech),(int)(h/ech), // dest
					0,0,w,h, // src
					null);
					*/
			g2d.drawImage(previewFrame, 
					0,0,scaled.width,scaled.height, // dest
					0,0,w,h, // src
					null);
		} 
		
		// draw HD frame
		int hHD = 9* getWidth() / 16;
		int delta = (getHeight() - hHD)/2;
				
		g2d.setPaint(Color.magenta);
		g2d.setStroke(stroke);
		
		Rectangle2D HDRect = new Rectangle(0,delta,getWidth(),hHD);
		        
        g2d.draw(HDRect);
        /*
		g2d.setColor(Color.blue);
		g2d.drawLine(30, 30, 80, 80);
		g2d.drawRect(20, 150, 100, 100);
		g2d.fillRect(20, 150, 100, 100);
		 */
        
        g2d.setComposite(originalComposite);
	}  

	public jimage(frameWin f) {
		setSize(700,500);

		f.getContentPane().add(this);
				
		snap =new JButton("Snap");
		snap.addActionListener(this);

		f.getContentPane().add(snap);
				
		project = f;
		lastShot = null;
		previewFrame = null;
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
			lastShot = ImageIO.read(in);
		} catch (IOException e) {			
			e.printStackTrace();
		}		
		repaint();
		
	}

		@Override
	public void onPreview(BufferedImage img) {
		// TODO Auto-generated method stub
		//System.out.println("onPreview: new img");
		previewFrame = img;
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

	@Override
	public void onPreviewSize(Dimension psize) {	
		System.out.println("onPreviewSize, preview is : " + psize);
					
		scaled = preserveRatio(psize);		
	}

}
