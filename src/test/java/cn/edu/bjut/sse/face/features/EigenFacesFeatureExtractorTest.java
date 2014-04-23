package cn.edu.bjut.sse.face.features;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.math3.linear.RealVector;
import org.apache.sanselan.ImageReadException;
import org.junit.Test;

import cn.edu.bjut.sse.image.features.EigenFacesFeatureExtractor;

public class EigenFacesFeatureExtractorTest {
	
	@Test
	public void generateEigenFaceFeatures2File() throws IOException, ImageReadException {
		File directory = new File("/Users/zhanghao/Desktop/face/yalefaces_small/15/");
		File output = new File("/Users/zhanghao/Desktop/face/yalefaces_small/eigen_15.txt");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		
		if (!directory.isDirectory()) {
			bw.close();
			return;
		}
		
		EigenFacesFeatureExtractor extractor = new EigenFacesFeatureExtractor();
		
		File[] files = directory.listFiles();
		List<BufferedImage> images = files2BufferedImages(files);
		
		List<RealVector> omegas = extractor.getFeature(images);

		// Write2File
		Iterator<RealVector> iter = omegas.iterator();
		while (iter.hasNext()) {
			RealVector omega = iter.next();
			
			bw.write("15 ");
			for (int i = 0; i < omega.getDimension(); i++) {
				double feature = omega.getEntry(i);
				bw.write((i+1) + ":" + String.valueOf(feature) + " ");
			}
			bw.newLine();
		}
		
		bw.close();
		
	}
	
	@Test
	public void reconstructEigenFace() throws IOException, ImageReadException {
		File directory = new File("/Users/zhanghao/Desktop/face/yalefaces_small/01/");
		
		if (!directory.isDirectory()) {
			return;
		}
		
		EigenFacesFeatureExtractor extractor = new EigenFacesFeatureExtractor();
		
		File[] files = directory.listFiles();
		List<BufferedImage> images = files2BufferedImages(files);
		
		List<RealVector> phiHats = extractor.getFeatureFace(images);
		
        BufferedImage image = new BufferedImage(11 * 320, 240, BufferedImage.TYPE_INT_BGR);
        Graphics2D g = image.createGraphics();

		// Write2ImageFile
		for (int i = 0; i < phiHats.size(); i++) {
			RealVector phiHat = phiHats.get(i);
			BufferedImage face = eigenFacesFeature2Image(phiHat);
			g.drawImage(face, i * face.getWidth(), 0, null); // 绘制缩小后的图   
		}
		g.dispose();
		
		File output = new File("/Users/zhanghao/Desktop/face/yalefaces_small/output/" + Math.random() + ".bmp");
		ImageIO.write(image, "bmp", output);
		
	}
	
	private BufferedImage eigenFacesFeature2Image(RealVector phiHat) {
		
		int width = 320;
		int height = 243;
		
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int index = j * width + i;
				
				int ta = 0, tr = 0, tg = 0, tb = 0;
				tr = tg = tb = (int) phiHat.getEntry(index);
				
				int rgb = (ta << 24) | (tr << 16) | (tg << 8) | tb;
				image.setRGB(i, j, rgb);
			}
		}
		
		return image;
	}

	private List<BufferedImage> files2BufferedImages(File[] files) throws ImageReadException, IOException {
		
		List<BufferedImage> images = new LinkedList<BufferedImage>();
		
		// Collection of Filters
//		GrayFilter grayFilter = new GrayFilter();
//		ResizeFilter resizeFilter = new ResizeFilter();
		
		for (File file : files) {
			
			if (file.getName().startsWith(".")) {
				continue;
			}
			
			BufferedImage image = ImageIO.read(file);
			
			// do filter
//			grayFilter.filter(image, image);
//		    image = resizeFilter.filter(image, image);
		    
		    images.add(image);
		}
		
		return images;
	}

}
