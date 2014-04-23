package cn.edu.bjut.sse.image.filter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jhlabs.image.AbstractBufferedImageOp;

public class GarborWaveletFilters extends AbstractBufferedImageOp {
	
	public static int U_MAX = 5;
	public static int V_MAX = 8;
	
	private List<BufferedImage> images = new ArrayList<BufferedImage>();
	
	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {

		for (int u = 0; u < U_MAX; u++) {
			for (int v = 0; v < V_MAX; v++) {
				BufferedImage image = gaborImage(src, u, v);
				images.add(image);
			}
		}
		
		// 拼接成新图片
        BufferedImage changedImage = new BufferedImage(ResizeFilter.DEFAULT_DEST_WIDTH * V_MAX, ResizeFilter.DEFAULT_DEST_HEIGHT * U_MAX, BufferedImage.TYPE_INT_BGR);
        Graphics2D g = changedImage.createGraphics();
        
        Iterator<BufferedImage> iter = images.iterator();
        
		for (int u = 0; u < U_MAX; u++) {
			for (int v = 0; v < V_MAX; v++) {
				BufferedImage image = iter.next();
		        g.drawImage(image, v * ResizeFilter.DEFAULT_DEST_WIDTH, u * ResizeFilter.DEFAULT_DEST_HEIGHT, null); // 绘制缩小后的图   
			}
		}
        
		g.dispose();
        
		return changedImage;
	}
	
	private BufferedImage gaborImage(BufferedImage srcImage, int u, int v) {
		// lambda
		double waveLength = 2 * Math.pow(2, u) / Math.PI;
		// theta
		double orientation = Math.PI * v / 8;
		// phi
		double phaseOffset = 0;
		// gamma
		double aspectRatio = 1;
		// b
		double bandwidth = Math.PI / 8;

		int windowsWidth = 3;
		int windowsHeight = 3;

		GaborFilter filter = new GaborFilter(waveLength,
				new double[] { orientation }, phaseOffset, aspectRatio,
				bandwidth, windowsWidth, windowsHeight);
		BufferedImage destImage = filter.filter(srcImage, null);
		return destImage;
	}

}
