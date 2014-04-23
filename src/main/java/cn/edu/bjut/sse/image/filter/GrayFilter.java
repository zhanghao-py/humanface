package cn.edu.bjut.sse.image.filter;

import java.awt.image.BufferedImage;

import com.jhlabs.image.AbstractBufferedImageOp;

public class GrayFilter extends AbstractBufferedImageOp {

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {

		int width = src.getWidth();
		int height = src.getHeight();

		if (dest == null)
			dest = createCompatibleDestImage(src, null);

		int[] inPixels = new int[width * height];
		int[] outPixels = new int[width * height];
		getRGB(src, 0, 0, width, height, inPixels);

		int index = 0;
		
		// calculate means of pixel
		/*
		double redSum = 0, greenSum = 0, blueSum = 0;
		double total = height * width;
		for (int row = 0; row < height; row++) {
			int ta = 0, tr = 0, tg = 0, tb = 0;
			for (int col = 0; col < width; col++) {
				index = row * width + col;
				ta = (inPixels[index] >> 24) & 0xff;
				tr = (inPixels[index] >> 16) & 0xff;
				tg = (inPixels[index] >> 8) & 0xff;
				tb = inPixels[index] & 0xff;
				redSum += tr;
				greenSum += tg;
				blueSum += tb;
			}
		}
		int means = (int) (redSum / total);
		System.out.println(" threshold average value = " + means);
		*/
		
		// dithering
		for (int row = 0; row < height; row++) {
			int ta = 0, tr = 0, tg = 0, tb = 0;
			for (int col = 0; col < width; col++) {
				index = row * width + col;
				ta = (inPixels[index] >> 24) & 0xff;
				tr = (inPixels[index] >> 16) & 0xff;
				tg = (inPixels[index] >> 8) & 0xff;
				tb = inPixels[index] & 0xff;
//				if (tr >= means) {
//					tr = tg = tb = 255;
//				} else {
//					tr = tg = tb = 0;
//				}
				
				tr = tg = tb = (int) (0.299 * tr + 0.587 * tg + 0.114 * tb);
				
				outPixels[index] = (ta << 24) | (tr << 16) | (tg << 8) | tb;

			}
		}
		setRGB(dest, 0, 0, width, height, outPixels);
		return dest;

	}

}
