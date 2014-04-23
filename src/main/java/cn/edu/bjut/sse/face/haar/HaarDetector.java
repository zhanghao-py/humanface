package cn.edu.bjut.sse.face.haar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jjil.algorithm.RgbAvgGray;
import jjil.core.Error;
import jjil.core.Rect;
import jjil.core.RgbImage;
import jjil.j2se.RgbImageJ2se;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import net.semanticmetadata.lire.imageanalysis.Gabor;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import cn.edu.bjut.sse.image.face.classifier.util.SVMUtils;

import com.jhlabs.image.AbstractBufferedImageOp;

public class HaarDetector extends AbstractBufferedImageOp {
	
	private Logger logger = Logger.getLogger(HaarDetector.class);

	private int minScale = 1;
	private int maxScale = 40;
	private Gray8DetectHaarMultiScale detectHaar;
	
	private svm_model model;
	private Gabor gabor;
	
	public HaarDetector(svm_model model) {
//		this.model = model;
		this.gabor = new Gabor();
		init();
	}
	
	public HaarDetector() {
		init();
	}
	
	private void init() {
		try {
			Resource resouce = new ClassPathResource("HCSB.txt");
			InputStream is  = resouce.getInputStream();
			detectHaar = new Gray8DetectHaarMultiScale(is, minScale, maxScale);
		} catch (FileNotFoundException e) {
			logger.warn("", e);
		} catch (IOException e) {
			logger.warn("", e);
		} catch (Error e) {
			logger.warn("", e);
		}
	}
	
	/*
	public static void findFaces(BufferedImage bi, int minScale, int maxScale, File output) {
        try {
            // step #2 - convert BufferedImage to JJIL Image
            RgbImage im = RgbImageJ2se.toRgbImage(bi);
            // step #3 - convert image to greyscale 8-bits
            RgbAvgGray toGray = new RgbAvgGray();
            toGray.push(im);
            // step #4 - initialise face detector with correct Haar profile
//            InputStream is  = new FileInputStream("/Users/zhanghao/Documents/workspace/HumanFaceClassification/target/classes/HCSB.txt");
            
//            Gray8DetectHaarMultiScale detectHaar = new Gray8DetectHaarMultiScale(is, minScale, maxScale);
            // step #5 - apply face detector to grayscale image
			List<Rect> results = detectHaar.pushAndReturn(toGray.getFront());
			logger.info("Found " + results.size() + " faces");
			
			for (Rect result : results) {
				logger.info("TopLeft:" + result.getTopLeft() + ", " + result.getBottomRight());
				bi.getGraphics().drawRect(result.getLeft(), result.getTop(), result.getWidth(), result.getHeight());
			}
			
            // step #6 - retrieve resulting face detection mask
//            Image i = detectHaar.getFront();
            // finally convert back to RGB image to write out to .jpg file
//            Gray8Rgb g2rgb = new Gray8Rgb();
//            g2rgb.push(i);
            
//            RgbImage rgb = (RgbImage) g2rgb.getFront();
            
//            BufferedImage img = new BufferedImage(
//    				rgb.getWidth(), 
//    				rgb.getHeight(), 
//    				BufferedImage.TYPE_INT_RGB);
//    		DataBufferInt dbi = new DataBufferInt(
//    				rgb.getData(),
//    				rgb.getHeight() * rgb.getWidth());
//    		DataBufferInt dbi = new DataBufferInt(
//    				rgb.getHeight() * rgb.getWidth());
//    		Raster r = Raster.createRaster(
//    				img.getSampleModel(),
//    				dbi, 
//    				null);
//    		img.setData(r);
//            
    		ImageIO.write(bi, "JPG", output);
//            RgbImageJ2se conv = new RgbImageJ2se();
//            conv.toFile(rgb, output.getCanonicalPath());
            
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }
	
    public static void main(String[] args) throws Exception {
        BufferedImage bi = ImageIO.read(new File("/Users/zhanghao/Documents/workspace/HumanFaceClassification/target/classes/4.jpg"));
        findFaces(bi, 1, 40, new File("/Users/zhanghao/Documents/workspace/HumanFaceClassification/target/classes/result.jpg")); // change as needed
    }
    */

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        RgbImage im = RgbImageJ2se.toRgbImage(src);
        RgbAvgGray toGray = new RgbAvgGray();
        
        List<Rect> results = null;
        try {
			toGray.push(im);
			results = detectHaar.pushAndReturn(toGray.getFront());
		} catch (Error e) {
			logger.warn("", e);
		}
        
		for (Rect result : results) {
			
	        if (logger.isDebugEnabled()) {
	        	logger.debug("Found " + results.size() + " faces");
	        	logger.debug("TopLeft:" + result.getTopLeft() + ", " + result.getBottomRight());
	        }
	        
	        int x = result.getLeft();
	        int y = result.getTop();
	        int w = result.getWidth();
	        int h = result.getHeight();
	        
	        BufferedImage sub = src.getSubimage(x, y, w, h);
//	        double[] features = gabor.getNormalizedFeature(sub);
//			svm_node[] p = SVMUtils.features(features);
//			double predict = svm.svm_predict(model, p);
			
//			logger.info("predict is " + predict);
			
			Graphics g = src.getGraphics();
//			if (predict > 0) {
				g.setColor(Color.BLUE);
//			} else {
//				g.setColor(Color.RED);
//			}
			g.drawRect(x, y, w, h);
		}
		
		return dest;
	}
}
