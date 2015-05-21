package remotecam;

import java.awt.image.BufferedImage;

public interface CameraModelListener {
	public void onShot(byte[] img);
	public void onPreview(BufferedImage img);
}
