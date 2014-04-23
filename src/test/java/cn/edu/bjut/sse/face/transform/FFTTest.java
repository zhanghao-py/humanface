package cn.edu.bjut.sse.face.transform;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.log4j.Logger;
import org.junit.Test;

public class FFTTest {

	private Logger logger = Logger.getLogger(FFTTest.class);
	
	@Test
	public void test1() throws IOException {
		double[] f = {1f, 2f, 4f, 5f};
		
		FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
		Complex[] fu = fft.transform(f, TransformType.FORWARD);
		Complex[] fx = fft.transform(fu, TransformType.INVERSE);
		
		logger.info(fx);
	}
	
	@Test
	public void test() throws IOException {
		
		File file = new File("/Users/zhanghao/Desktop/lena.bmp");
		BufferedImage img = ImageIO.read(file);
		
		int width = img.getWidth();
		int height = img.getHeight();
		
		FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

		Complex[] d = getPixelsComplex(img);
		
		for (int i = 0; i < width ; i++) {
			Complex[] fx = getOrignalRowPixel(d, width, height, i);
			Complex[] fu = fft.transform(fx, TransformType.FORWARD);
			setOrignalRowPixel(d, width, height, i, fu);
		}
		
		for (int j = 0; j < height ; j++) {
			Complex[] fx = getOrignalColPixel(d, width, height, j);
			Complex[] fu = fft.transform(fx, TransformType.FORWARD);
			setOrignalColPixel(d, width, height, j, fu);
		}
		
		butterworthLowFrequestcy(d, width, height, 10);

		for (int j = 0; j < height ; j++) {
			Complex[] fx = getOrignalColPixel(d, width, height, j);
			Complex[] fu = fft.transform(fx, TransformType.INVERSE);
			setOrignalColPixel(d, width, height, j, fu);
		}
		
		for (int i = 0; i < width ; i++) {
			Complex[] fx = getOrignalRowPixel(d, width, height, i);
			Complex[] fu = fft.transform(fx, TransformType.INVERSE);
			setOrignalRowPixel(d, width, height, i, fu);
		}
		
		int[] pixels = toPix2(d, width, height);
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		image.setRGB(0, 0, width, height, pixels, 0, width);
		File outFile = new File("/Users/zhanghao/Desktop/lena-1.bmp");
		ImageIO.write(image, "jpg", outFile);
		
	}

	private void butterworthLowFrequestcy(Complex[] d, int width, int height, double d0) {
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				
				double dd = i * i + j * j;
				Complex butterworth = new Complex(1/(1+0.414213562*dd/(d0 * d0)), 0);
				
				int index = i * width + j;
				d[index] = d[index].multiply(butterworth);
			
			}
		}
		
		return;
		
	}

	private void setOrignalColPixel(Complex[] d, int width, int height, int col,
			Complex[] fu) {
		
		for (int row = 0; row < height; row++) {
			int index = row * width + col;
			d[index] = fu[row];
		}
		
		return;
	}

	private Complex[] getOrignalColPixel(Complex[] d, int width, int height,
			int col) {
		Complex[] outPixels = new Complex[height];
		
		for (int row = 0; row < height; row++) {
			int index = row * width + col;
			outPixels[row] = d[index];
		}
		
		return outPixels;
	}

	private void setOrignalRowPixel(Complex[] d, int width, int height, int row,
			Complex[] fu) {
		
		for (int col = 0; col < width; col++) {
			int index = row * width + col;
			d[index] = fu[col];
		}
		
		return;
	}

	private Complex[] getPixelsComplex(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		
		Complex[] outPixels = new Complex[width * height];
		
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				int index = row * width + col;
				
				int inPixels = img.getRGB(row, col);
				int tr = (inPixels >> 16) & 0xff;
				
//				tr = tr * (int) Math.pow(3, col + row);
				outPixels[index] = new Complex(tr, 0);
			}
		}
		
		return outPixels;
		
	}

	private Complex[] getOrignalRowPixel(Complex[] d, int width, int height, int row) {
		
		Complex[] outPixels = new Complex[width];
		
		for (int col = 0; col < width; col++) {
			int index = row * width + col;
			outPixels[col] = d[index];
		}
		
		return outPixels;
	}
	
	private int[] toPix2(Complex[] fftData, int iw, int ih) {
		int[] pix = new int[iw * ih];

		for (int j = 0; j < ih; j++) {
			for (int i = 0; i < iw; i++) {
				
				int index = i + j * iw;
				
				/*
				double tem = fftData[index].getReal() * fftData[index].getReal()
						+ fftData[index].getImaginary() * fftData[index].getImaginary();
				int r = (int) (Math.sqrt(tem) / 100);
				*/
				
				int r = (int) fftData[index].getReal();
				
				if (r > 255) {
					r = 255;
				}
				
				pix[index] = 255 << 24 | r << 16 | r << 8 | r;
				
//				pix[index] = 255 << 24 | r << 16 | r << 8 | r;
//				pix[index] = 255 << 24 | r << 16 | r << 8 | r;
			}
		}
		return pix;
	}
	
	private int[] toPix(Complex[] fftData, int iw, int ih) {
		int[] pix = new int[iw * ih];

		for (int j = 0; j < ih; j++) {
			for (int i = 0; i < iw; i++) {
				
				int u = i, v = j;
				int index = i + j * iw;
				
				double tem = fftData[index].getReal() * fftData[index].getReal()
						+ fftData[index].getImaginary() * fftData[index].getImaginary();
				int r = (int) (Math.sqrt(tem) / 100);
				
				if (r > 255) {
					r = 255;
				}
				
				// 移到中心
				if (i < iw / 2) {
					u = i + iw / 2;
				} else {
					u = i - iw / 2;
				}
					
				if (j < ih / 2) {
					v = j + ih / 2;
				} else {
					v = j - ih / 2;
				}

				pix[u + v * iw] = 255 << 24 | r << 16 | r << 8 | r;
//				pix[index] = 255 << 24 | r << 16 | r << 8 | r;
			}
		}
		return pix;
	}
	
}
