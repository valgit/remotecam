package remotecam;

import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.awt.*;  

import javax.swing.JFrame;  

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;  
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class jimage extends Canvas implements ActionListener,CameraModelListener,CamSocketServerListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private File selectedFile;// = new File("src/images/image01.jpg");
	
	private CameraModel camera;
	private CamSocketServer server;
	
	private JButton snap;
	private JButton filesel;
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
		/*
		g2d.setColor(Color.blue);
		g2d.drawLine(30, 30, 80, 80);
		g2d.drawRect(20, 150, 100, 100);
		g2d.fillRect(20, 150, 100, 100);
		 */
		int fw = getWidth();
		int fh = getHeight();

		if (currentShot != null) {
			//System.out.println("currentShot");				
			int w = currentShot.getWidth(null);
			int h = currentShot.getHeight(null);

			g2d.drawImage(currentShot, 
					0,0,fw,fh, // dest
					0,0,w,h, // src
					null);
		} else {
			g2d.setColor(Color.black);
			g2d.fillRect(5, 5, fw-5, fh-5);

		}
		
		g2d.setPaint(Color.white);
		g2d.setStroke(stroke);
		//g2.draw(_currentSelectionRect);
		
		/*
			if (selectedFile != null) {
				//System.out.println("paint");
				BufferedImage img = ImageIO.read(selectedFile);
				g.drawImage(img, 10,10,this);  
			}

		 */
	}  

	public jimage(JFrame f) {
		setSize(700,500);

		f.getContentPane().add(this);
		
		filesel =new JButton("Select File");
		filesel.addActionListener(this);

		f.getContentPane().add(filesel);

		snap =new JButton("Snap");
		snap.addActionListener(this);

		f.getContentPane().add(snap);
		
		
		
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
		if(e.getSource() == filesel) {
		JFileChooser fileChooser = new JFileChooser();
		//FileFilter filter = new FileNameExtensionFilter("JPEG files", "jpg");
		//fileChooser.addChoosableFileFilter(filter);

		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			selectedFile = fileChooser.getSelectedFile();
			System.out.println(selectedFile.getName());
			repaint();
		}
		}
		if(e.getSource() == snap) {
			//System.out.println("take snap");
			if (camera != null)
				camera.takeShot();			
		}
	}


	private File getShotFile() {
		//take the current timeStamp
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		
		//and make a media file:
		//TODO:
		final String projectdir="d:\\work\\stopproj";
		/* project dir */
		mediaFile = new File(projectdir + File.separator + "DSC_" + timeStamp + ".jpg");
		return mediaFile;
	}
	
	@Override
	public void onShot(BufferedImage img) {
		// TODO Auto-generated method stub
		//System.out.println("onShot: new img");
		int width = img.getWidth();
		int height = img.getHeight();
		//System.out.println("onShot: size: "+width+" h: "+height);
		currentShot = img;
		repaint();
		
		File outputfile = getShotFile();
		try {
			ImageIO.write(currentShot, "jpg", outputfile);
			System.out.println("onShot: save in" + outputfile.getAbsolutePath());
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		
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
