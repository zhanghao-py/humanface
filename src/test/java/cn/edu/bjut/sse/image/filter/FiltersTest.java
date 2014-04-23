package cn.edu.bjut.sse.image.filter;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.junit.Test;

import cn.edu.bjut.sse.image.filter.GrayFilter;
import cn.edu.bjut.sse.image.filter.HistogramEFilter;

public class FiltersTest {

	private Logger logger = Logger.getLogger(getClass());
	
	@Test
	public void test() throws IOException {
		
		// [Input]
		FileInputStream fis = new FileInputStream("/Users/zhanghao/Desktop/lena_C.bmp");
		BufferedImage src = ImageIO.read(fis);
		BufferedImage dest = null;
		
		// [Gray & HistogramE]
		GrayFilter grayFilter = new GrayFilter();
		grayFilter.filter(src, src);
		
		HistogramEFilter histogramEFilter = new HistogramEFilter();
		histogramEFilter.filter(src, src);
		
		// [Output]
		FileOutputStream fos = new FileOutputStream("/Users/zhanghao/Desktop/lena_C_out.bmp");
        ImageIO.write(src, "BMP", fos);
		
//		GaussianFilter filter = new GaussianFilter();
//		filter.setRadius(10);
//		filter.filter(img, img);
		
//		LaplaceFilter filter = new LaplaceFilter();
//		filter.filter(img, img);
		
	}
}
