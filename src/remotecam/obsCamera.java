package remotecam;

import java.awt.image.BufferedImage;

public interface obsCamera {
	public void newImg(BufferedImage img);
	void addCamera(remotecam cam);

}
