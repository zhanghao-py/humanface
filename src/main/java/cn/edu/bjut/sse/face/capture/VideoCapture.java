package cn.edu.bjut.sse.face.capture;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;

import cn.edu.bjut.sse.image.basic.filter.GrayFilter;
import cn.edu.bjut.sse.image.basic.filter.HistogramEFilter;

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
        
        GrayFilter grayFilter = new GrayFilter();
        HistogramEFilter histogramEFilter = new HistogramEFilter();
        
        while(true) {
        	
        	if (!webcam.isOpen()) {
        		break;
        	}
        	
        	BufferedImage image = webcam.getImage();
    		
    		grayFilter.filter(image, image);
    		histogramEFilter.filter(image, image);
        	
        	panel.setSource(image);
        	panel.repaint();
        }
		
	}

}
