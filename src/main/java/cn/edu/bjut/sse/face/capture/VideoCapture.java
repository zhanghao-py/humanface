package cn.edu.bjut.sse.face.capture;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;

import libsvm.svm;
import libsvm.svm_model;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import cn.edu.bjut.sse.face.haar.HaarDetector;
import cn.edu.bjut.sse.image.filter.GrayFilter;
import cn.edu.bjut.sse.image.filter.HistogramEFilter;

import com.github.sarxos.webcam.Webcam;

public class VideoCapture {

	public static void main(String[] args) throws IOException {

		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(new Dimension(480, 400));
		webcam.open();

		VideoPanel panel = new VideoPanel();
		
		JFrame window = new JFrame();
		window.add(panel);
		window.setTitle("VideoCapture"); 
		window.setSize(webcam.getViewSize());
		window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
//        GrayFilter grayFilter = new GrayFilter();
//        HistogramEFilter histogramEFilter = new HistogramEFilter();
//		Resource re = new ClassPathResource("HCSB.txt");
//		InputStreamReader isr = new InputStreamReader(re.getInputStream());
//		BufferedReader br = new BufferedReader(isr);
//		svm_model model = svm.svm_load_model(br);
		
        HaarDetector detector = new HaarDetector(null);
        
        while(true) {
        	
        	if (!webcam.isOpen()) {
        		break;
        	}
        	
        	BufferedImage image = webcam.getImage();
    		
        	detector.filter(image, image);
//    		grayFilter.filter(image, image);
//    		histogramEFilter.filter(image, image);

        	
        	panel.setSource(image);
        	panel.repaint();
        }
		
	}

}
