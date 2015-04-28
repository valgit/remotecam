package remotecam;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class frameWin extends JFrame {
	private jimage liveview;
	
	public frameWin(){
        
        setTitle("Camera View");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setLayout( new FlowLayout() );
        
        liveview =new jimage(this);  
        liveview.createAndShowGUI();
	
		getContentPane().add(liveview);  	        

		//Display the window.
		pack();
		setVisible(true);
        
        JButton buttonExit = new JButton(" Exit ");
        buttonExit.addActionListener(new ActionListener(){
 
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
         
        add(buttonExit);
         
    }
}


		