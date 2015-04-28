package remotecam;

import javax.swing.SwingUtilities;

public class testCamera {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(runJFrameLater);
	}

	static Runnable runJFrameLater = new Runnable() {

		@Override
		public void run() {
			frameWin jFrameWindow = new frameWin();
			jFrameWindow.setVisible(true);
		}

	};

}


