package remotecam;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class frameWin extends JFrame implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private jimage liveview;
	private JButton filesel;
	private File selectedFile;// = new File("src/images/image01.jpg");
	//TODO:
	private File projectdir= new File("d:\\work\\stopproj");
			
	public frameWin(){
        
        setTitle("Camera View");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setLayout( new FlowLayout() );
        
        liveview =new jimage(this);  
        liveview.createAndShowGUI();
	
		getContentPane().add(liveview);  	        

		
		filesel =new JButton("Select File");
		filesel.addActionListener(new ActionListener(){
 
            @Override
            public void actionPerformed(ActionEvent ae) {
            	JFileChooser fileChooser = new JFileChooser();
            	fileChooser.setDialogTitle("project directory");
            	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            	fileChooser.setAcceptAllFileFilterUsed(false);
    			int returnValue = fileChooser.showOpenDialog(null);
    			if (returnValue == JFileChooser.APPROVE_OPTION) {
    				projectdir = fileChooser.getCurrentDirectory();
    			}
            }
        });

			
		add(filesel);
		
        JButton buttonExit = new JButton(" Exit ");
        buttonExit.addActionListener(new ActionListener(){
 
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
         
        add(buttonExit);
        
      //Display the window.
      		pack();
      		setVisible(true);
              
      		
    }

	private File getShotFile() {
		//take the current timeStamp
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		
		/* project dir */
		mediaFile = new File(projectdir + File.separator + "DSC_" + timeStamp + ".jpg");
		return mediaFile;
	}

	public void saveShot(byte[] shot) {
		System.out.println("saveShot");
		// save file
		File outputfile = getShotFile();
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(outputfile);
		} catch (FileNotFoundException e) {		
			e.printStackTrace();
		}
		try {
			stream.write(shot);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	

		System.out.println("saveShot: save in" + outputfile.getAbsolutePath());
	}
	
	@Override
	public void keyPressed(KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_SPACE ) {
			System.out.println("space");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {				
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}


		