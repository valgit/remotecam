package remotecam;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class frameWin extends JFrame implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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


		