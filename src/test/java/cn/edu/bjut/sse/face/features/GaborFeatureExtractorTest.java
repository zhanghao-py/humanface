package cn.edu.bjut.sse.face.features;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.imageanalysis.Gabor;
import net.semanticmetadata.lire.imageanalysis.LocalBinaryPatterns;

import org.apache.log4j.Logger;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.formats.tiff.TiffImageParser;
import org.junit.Test;

import cn.edu.bjut.sse.image.features.GaborFeatureExtractor;
import cn.edu.bjut.sse.image.filter.GarborWaveletFilters;
import cn.edu.bjut.sse.image.filter.GrayFilter;
import cn.edu.bjut.sse.image.filter.ResizeFilter;

public class GaborFeatureExtractorTest {
	
	private Logger logger = Logger.getLogger(GaborFeatureExtractorTest.class);
	
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
	public void generateImageGaborFeatures2File() throws IOException, ImageReadException {
		File directory = new File("/Users/zhanghao/Desktop/face/FERET/female/");
		File output = new File("/Users/zhanghao/Desktop/face/FERET/FERET_female_gabor.txt");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		
		if (!directory.isDirectory()) {
			return;
		}
		
		TiffImageParser parser = new TiffImageParser();
		
		// Collection of Filters
		GrayFilter grayFilter = new GrayFilter();
		ResizeFilter resizeFilter = new ResizeFilter();
		GarborWaveletFilters gaborFilter = new GarborWaveletFilters();
		GaborFeatureExtractor extractor = new GaborFeatureExtractor();
		
		File[] files = directory.listFiles();
		
		for (File file : files) {
			
			List<BufferedImage> imgs = parser.getAllBufferedImages(file);
			
			if (imgs==null || imgs.size() <= 0) {
				continue;
			}
			
			BufferedImage image = imgs.get(0);
//			BufferedImage image = ImageIO.read(file);
			
			// do filter
			grayFilter.filter(image, image);
		    image = resizeFilter.filter(image, image);
		    image = gaborFilter.filter(image, image);
			double[] features = extractor.getFeature(image);
			
			bw.write("-1 ");
			for (int i = 0; i < features.length; i++) {
				bw.write((i+1) + ":" + String.valueOf(features[i]) + " ");
//				bw.write(String.valueOf(features[i]) + ",");
			}
			
			bw.newLine();
		}
		
		bw.close();
		
	}
	
	@Test
	public void testGabor() throws IOException, ImageReadException {
		File file = new File("/Users/zhanghao/Desktop/face/FERET/female/004-01.tif");
		
		TiffImageParser parser = new TiffImageParser();
		List<BufferedImage> imgs = parser.getAllBufferedImages(file);
		BufferedImage img = imgs.get(0);
		Gabor descriptor = new Gabor();
		descriptor.extract(img);
//		byte[] features = descriptor.getByteArrayRepresentation();
		double[] features = descriptor.getNormalizedFeature(img);
		logger.info(features);
	}
	
	@Test
	public void testLocalBinaryPatterns() throws IOException {
		
		File directory = new File("/Users/zhanghao/Desktop/face/yalefaces/");
		File[] files = directory.listFiles();
		
		for (File file : files) {
			BufferedImage img = ImageIO.read(file);
			
			File output= new File("/Users/zhanghao/Desktop/face/yalefaces_small/" + file.getName() + ".bmp");
			ImageIO.write(img, "bmp", output);
		}
		
//		File file = new File("/Users/zhanghao/Desktop/face/yalefaces/subject02.centerlight");
//		BufferedImage img = ImageIO.read(file);
//		LocalBinaryPatterns descriptor = new LocalBinaryPatterns();
//		descriptor.extract(img);
//		descriptor.ge
//		byte[] features = descriptor.getByteArrayRepresentation();
//		double[] features = descriptor.getDoubleHistogram();
		logger.info("");
	}

}
