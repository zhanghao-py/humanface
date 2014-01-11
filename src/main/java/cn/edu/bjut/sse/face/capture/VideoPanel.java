package cn.edu.bjut.sse.face.capture;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class VideoPanel extends JPanel {

	private static final long serialVersionUID = 1236881365109186106L;
	
	private BufferedImage source;
	
	
	@Override
	protected void paintComponent(Graphics g) {
		if (source != null) {
			g.drawImage(source, 0, 0, source.getWidth(), source.getHeight(), null);
		} else {
			super.paintComponent(g);
		}
		return;
	}
	
	public void setSource(BufferedImage source) {
		this.source = source;
	}

	public BufferedImage getSource() {
		return source;
	}
	
}
