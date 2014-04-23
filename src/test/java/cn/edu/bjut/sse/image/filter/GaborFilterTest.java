package cn.edu.bjut.sse.image.filter;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.formats.tiff.TiffImageParser;
import org.junit.Test;

import cn.edu.bjut.sse.image.features.GaborFeatureExtractor;
import cn.edu.bjut.sse.image.filter.GaborFilter;
import cn.edu.bjut.sse.image.filter.GarborWaveletFilters;
import cn.edu.bjut.sse.image.filter.GrayFilter;
import cn.edu.bjut.sse.image.filter.ResizeFilter;

/**
 * The test class for GaborFilter class
 */
public class GaborFilterTest {
	
	private Logger logger = Logger.getLogger(GaborFilterTest.class);
	
	@Test
	public void generateGarborWaveletsImage() throws IOException, ImageReadException {
//		BufferedImage image = ImageIO.read(new File("/Users/zhanghao/Desktop/lena.bmp"));
		
		File file = new File("/Users/zhanghao/Desktop/face/FERET/female/004-01.tif");
		
		TiffImageParser parser = new TiffImageParser();
		List<BufferedImage> imgs = parser.getAllBufferedImages(file);
		BufferedImage image = imgs.get(0);
		
		BufferedImageOp filter = null;
		
		filter = new GrayFilter();
		filter.filter(image, image);
		
		filter = new ResizeFilter();
	    image = filter.filter(image, image);
	    
//		filter = new HistogramEFilter();
//	    filter.filter(image, image);
	    
	    filter = new GarborWaveletFilters();
	    image = filter.filter(image, image);
	    
	    File outFile = new File("/Users/zhanghao/Desktop/004-01-gabor.bmp");
	    ImageIO.write(image, "bmp", outFile);
		
	}
	
	@Test
	public void generateGarborWaveletsImageAndExtractFeatures() throws IOException {
		BufferedImage image = ImageIO.read(new File("/Users/zhanghao/Desktop/lena.bmp"));
		
		BufferedImageOp filter = null;
		
		filter = new GrayFilter();
		filter.filter(image, image);
		
		filter = new ResizeFilter();
	    image = filter.filter(image, image);
	    
	    filter = new GarborWaveletFilters();
	    image = filter.filter(image, image);
		
		GaborFeatureExtractor extractor = new GaborFeatureExtractor();
		double[] features = extractor.getFeature(image);
		logger.info(features.length);

	}
	
	@Test
	public void extractFeatures() throws IOException {
		BufferedImage image = ImageIO.read(new File("/Users/zhanghao/Desktop/lena-gabor.bmp"));
		GaborFeatureExtractor extractor = new GaborFeatureExtractor();
		double[] features = extractor.getFeature(image);
		logger.info(features.length);
	}
	
   /**
    * A Gabor filter test
    *
    * @throws IOException - IOException
    */
	@Test
	public void testImage() throws IOException {

		// Specifying the files
		File outFile = new File("/Users/zhanghao/Desktop/lena-gabor.bmp");
		BufferedImage bufferedImage = ImageIO.read(new File(
				"/Users/zhanghao/Desktop/lena.bmp"));

		int u = 4; // 0...4
		int v = 7; // 0...7

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
		BufferedImage destImage = filter.filter(bufferedImage, null);
		ImageIO.write(destImage, "bmp", outFile);
	}
   
	@Test
	public void testImageDifferent() throws IOException {
		BufferedImage image1 = ImageIO.read(new File(
				"/Users/zhanghao/Desktop/lena-gabor-u4-v7.bmp"));
		BufferedImage image2 = ImageIO.read(new File(
				"/Users/zhanghao/Desktop/lena-gabor-u2-v4.bmp"));
		
		int width = image1.getWidth();
		int height = image1.getHeight();
		
		for (int i = 0 ; i < width ; i++) {
			for (int j = 0 ; j < height; j++) {
				int p1 = image1.getRGB(i, j);
				
				int p2 = image2.getRGB(i, j);
				
				if (p1 != p2) {
					System.out.println(i + "," + j);
				}
				
			}
		}
	}
}