package cn.edu.bjut.sse.image.filter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import cn.edu.bjut.sse.image.filter.ResizeFilter;

public class ResizeFilterTest {
	
	   @Test
	   public void testImage() throws IOException {
	      
	      BufferedImage bufferedImage = ImageIO.read(new File("/Users/zhanghao/Desktop/lena.bmp"));
	      File outFile = new File("/Users/zhanghao/Desktop/lena-gabor.bmp");
	      
	      ResizeFilter filter = new ResizeFilter();
	      BufferedImage destImage = filter.filter(bufferedImage, bufferedImage);
	      ImageIO.write(destImage, "bmp", outFile);
	   }

}
