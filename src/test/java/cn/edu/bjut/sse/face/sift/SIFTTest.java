package cn.edu.bjut.sse.face.sift;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.alibaba.simpleimage.analyze.sift.SIFT;
import com.alibaba.simpleimage.analyze.sift.render.RenderImage;
import com.alibaba.simpleimage.analyze.sift.scale.KDFeaturePoint;

public class SIFTTest {
	
	private Logger logger = Logger.getLogger(SIFTTest.class);
	
	@Test
	public void testSift() throws IOException {
		
		File file = new File("/Users/zhanghao/Desktop/lena.bmp");
		BufferedImage img = ImageIO.read(file);
        RenderImage ri = new RenderImage(img);
        
        SIFT sift = new SIFT();
        sift.detectFeatures(ri.toPixelFloatArray(null));
        List<KDFeaturePoint> al = sift.getGlobalKDFeaturePoints();
        
        Graphics2D g = img.createGraphics();
        g.setColor(Color.GREEN);
        
        Iterator<KDFeaturePoint> iter = al.iterator();
        while (iter.hasNext()) {
        	KDFeaturePoint point = iter.next();
        	g.drawLine((int)point.x, (int)point.y, (int)point.x, (int)point.y);
        }
        
        File outfile = new File("/Users/zhanghao/Desktop/lena-sift.bmp");
        ImageIO.write(img, "bmp", outfile);
        
        logger.info("feature size : " + al.size());
        
		
	}

}
