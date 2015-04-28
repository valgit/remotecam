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

public class jimage extends Canvas implements ActionListener,obsCamera {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private File selectedFile;// = new File("src/images/image01.jpg");
	//TODO:
	private remotecam camera;
	private CamSocketServer server;
	private JButton snap;
	private JButton filesel;
	
	public void paint(Graphics g) {  

		Toolkit t=Toolkit.getDefaultToolkit();  
		//Image i=t.getImage("p3.gif");  
		g.setColor(Color.blue);
		g.drawLine(30, 30, 80, 80);
		g.drawRect(20, 150, 100, 100);
		g.fillRect(20, 150, 100, 100);

		try {
			if (selectedFile != null) {
				//System.out.println("paint");
				BufferedImage img = ImageIO.read(selectedFile);
				g.drawImage(img, 10,10,this);  
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}  

	public jimage(JFrame f) {
		//setSize(300,300);

		filesel =new JButton("Select File");
		filesel.addActionListener(this);

		f.getContentPane().add(filesel);

		snap =new JButton("Snap");
		snap.addActionListener(this);

		f.getContentPane().add(snap);
	}

	void createAndShowGUI() {
		//Create and set up the window.
		System.out.println("createAndShowGUI");
	/*
		@SuppressWarnings("unused")
		IPCamera server;
		server = new IPCamera(8080,this);
		*/
		// websocket server ?
		server = new CamSocketServer(new InetSocketAddress("10.24.244.99",5000),5000);
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
			System.out.println("take snap");
			//BufferedImage shot = camera.takeShot();
			//TODO: camera.takeShot();
			server.takeShot();
		}
	}


	@Override
	public void newImg(BufferedImage img) {
		// TODO Auto-generated method stub
		System.out.println("Client send new img");
		int width = img.getWidth();
		int height = img.getHeight();
		System.out.println("size: "+width+" h: "+height);
	}

	@Override
	public void addCamera(remotecam cam) {
		camera = cam;
	}

}
