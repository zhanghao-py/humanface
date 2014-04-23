package cn.edu.bjut.sse.image.features;

import java.awt.image.BufferedImage;

import cn.edu.bjut.sse.image.filter.GarborWaveletFilters;
import cn.edu.bjut.sse.image.filter.ResizeFilter;

public class GaborFeatureExtractor {
	
	public double[] getFeatureByPoint(BufferedImage image, int x, int y) {
		
		double[] feature = new double[GarborWaveletFilters.U_MAX * GarborWaveletFilters.V_MAX];
		
		for (int u = 0; u < GarborWaveletFilters.U_MAX; u++) {
			for (int v = 0; v < GarborWaveletFilters.V_MAX; v++) {
				int pixel = image.getRGB(x + v * ResizeFilter.DEFAULT_DEST_WIDTH, y + u * ResizeFilter.DEFAULT_DEST_HEIGHT);
				int index = u * GarborWaveletFilters.U_MAX + v;
//				int ta = (pixel >> 24) & 0xff;
//				int tr = (pixel >> 16) & 0xff;
//				int tg = (pixel >> 8) & 0xff;
				int tb = pixel & 0xff;
				
//				int rgb = (255 << 24) | (255 << 16) | (255 << 8) | 255;
//				image.setRGB(x + v * ResizeFilter.DEFAULT_DEST_WIDTH, y + u * ResizeFilter.DEFAULT_DEST_HEIGHT, rgb);
				
				feature[index] = tb;
			}
		}
		
		return feature;
	}
	
	public double[] getFeature(BufferedImage image) {
		
		int xStep = 8;
		int yStep = 8;
		
		// 取样点个数
		// TODO: need to floor
		int xSample = ResizeFilter.DEFAULT_DEST_WIDTH / xStep;
		int ySample = ResizeFilter.DEFAULT_DEST_HEIGHT / yStep;
		
		// 每个点(x, y)对应的特征维数
		int featureSize = GarborWaveletFilters.U_MAX * GarborWaveletFilters.V_MAX;
		// 共有xSample * ySample个取样点，每个点有featureSize维向量
		double[] features = new double[xSample * ySample * featureSize];
		
		for (int ys = 0; ys < ySample; ys++) {
			for (int xs = 0; xs < xSample; xs++) {
				
				int x = xs * xStep;
				int y = ys * yStep;
				
				double[] feature = getFeatureByPoint(image, x, y);
				System.arraycopy(feature, 0, features, (ys * yStep + xs) * featureSize, feature.length);
			}
		}
		
		return features;
	}

}
