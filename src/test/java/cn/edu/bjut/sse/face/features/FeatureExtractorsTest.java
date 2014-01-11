package cn.edu.bjut.sse.face.features;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.imageanalysis.Gabor;
import net.semanticmetadata.lire.imageanalysis.LocalBinaryPatterns;
import net.semanticmetadata.lire.imageanalysis.SurfFeature;

import org.apache.log4j.Logger;
import org.junit.Test;

public class FeatureExtractorsTest {
	
	private Logger logger = Logger.getLogger(FeatureExtractorsTest.class);
	
//	@Test
//	public void testSURF() throws IOException {
//		
//		File file = new File("/Users/zhanghao/Desktop/lena_C_out.bmp");
//		BufferedImage img = ImageIO.read(file);
//		SurfFeature descriptor = new SurfFeature();
//		
//		descriptor.run(image);
//		
//		
//	}
	
	@Test
	public void testGabor() throws IOException {
		File file = new File("/Users/zhanghao/Desktop/lena_C_out.bmp");
		BufferedImage img = ImageIO.read(file);
		Gabor descriptor = new Gabor();
		descriptor.extract(img);
//		byte[] features = descriptor.getByteArrayRepresentation();
		double[] features = descriptor.getNormalizedFeature(img);
		logger.info(features);
	}
	
	@Test
	public void testLocalBinaryPatterns() throws IOException {
		File file = new File("/Users/zhanghao/Desktop/lena_C_out.bmp");
		BufferedImage img = ImageIO.read(file);
		LocalBinaryPatterns descriptor = new LocalBinaryPatterns();
		descriptor.extract(img);
//		descriptor.ge
//		byte[] features = descriptor.getByteArrayRepresentation();
		double[] features = descriptor.getDoubleHistogram();
		logger.info(features);
	}

}
