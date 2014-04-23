package cn.edu.bjut.sse.image.filter;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.jhlabs.image.AbstractBufferedImageOp;

public class ResizeFilter extends AbstractBufferedImageOp {
	
	public static int DEFAULT_DEST_WIDTH = 64;
	public static int DEFAULT_DEST_HEIGHT = 64;

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
		dest = getResizeImage(src);
		return dest;
	}

	private final BufferedImage getResizeImage(BufferedImage originalImage) {

		// 根据缩放比例获得处理后的图片宽度。
		int changedImageWidth = DEFAULT_DEST_WIDTH;
		// 根据缩放比例获得处理后的图片高度。
		int changedImageHeight = DEFAULT_DEST_HEIGHT;
		
        Image image = originalImage.getScaledInstance(changedImageWidth, changedImageHeight, Image.SCALE_DEFAULT);  
        
        //缩放图像
        BufferedImage changedImage = new BufferedImage(changedImageWidth, changedImageHeight, BufferedImage.TYPE_INT_BGR);
        Graphics2D g = changedImage.createGraphics();   
        g.drawImage(image, 0, 0, null); // 绘制缩小后的图   
        g.dispose();
        
		return changedImage;
	}

}
