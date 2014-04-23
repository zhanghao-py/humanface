package cn.edu.bjut.sse.image.features;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class EigenFacesFeatureExtractor {
	
	private final int K = 10;
	
	public List<RealVector> getFeatureFace(List<BufferedImage> images) {
		
		List<RealVector> gammas = new LinkedList<RealVector>();
		for (BufferedImage image : images) {
			RealVector gamma = image2Vector(image);
			gammas.add(gamma);
		}
		
		List<RealVector> omegas = getFeatureFace(gammas, K);
		return omegas;
	}
	
	public List<RealVector> getFeature(List<BufferedImage> images) {
		
		List<RealVector> gammas = new LinkedList<RealVector>();
		for (BufferedImage image : images) {
			RealVector gamma = image2Vector(image);
			gammas.add(gamma);
		}
		
		List<RealVector> omegas = getFeature(gammas, K);
		return omegas;
	}
	
	private RealVector image2Vector(BufferedImage image) {
		
		int width = image.getWidth();
		int height = image.getHeight();
		
		RealVector gamma = new ArrayRealVector(width * height);
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int pixel = image.getRGB(i, j);
//				int ta = (pixel >> 24) & 0xff;
//				int tr = (pixel >> 16) & 0xff;
//				int tg = (pixel >> 8) & 0xff;
				int tb = pixel & 0xff;
				
				int index = j * width + i;
				gamma.setEntry(index, tb);
			}
		}
		
		return gamma;
	}
	
	// MeanFace投影到特征向量后的特征 -> 特征提取
	private List<RealVector> getFeature(List<RealVector> gammas, int K) {
		RealVector psi = computeAverageFaceVector(gammas);
		
		List<RealVector> phis = new LinkedList<RealVector>();
		for (RealVector gamma : gammas) {
			RealVector phi = getMeanFaceVector(gamma, psi);
			phis.add(phi);
		}
		
		RealMatrix A = getMeanFaceMatrix(phis);
		
		List<RealVector> eigenvectors = getKLargestEigenvectors(A, K);
		
		List<RealVector> omegas = new LinkedList<RealVector>();
		for (RealVector phi : phis) {
			RealVector omega = getProjectionToEigenvectors(eigenvectors, phi);
			omegas.add(omega);
		}
		
		return omegas;
	}
	
	// MeanFace投影到特征向量后的特征脸 -> 重构特征脸
	private List<RealVector> getFeatureFace(List<RealVector> gammas, int K) {
		RealVector psi = computeAverageFaceVector(gammas);
		
		List<RealVector> phis = new LinkedList<RealVector>();
		for (RealVector gamma : gammas) {
			RealVector phi = getMeanFaceVector(gamma, psi);
			phis.add(phi);
		}
		
		RealMatrix A = getMeanFaceMatrix(phis);
		
		List<RealVector> eigenvectors = getKLargestEigenvectors(A, K);
		
		List<RealVector> phiHats = new LinkedList<RealVector>();
		for (RealVector phi : phis) {
			RealVector phiHat = getFaceHat(eigenvectors, phi);
			phiHats.add(phiHat);
		}
		
		return phiHats;
	}
	
	private RealVector getProjectionToEigenvectors(List<RealVector> eigenvectors, RealVector phi) {
		int K = eigenvectors.size();
		RealVector omega = new ArrayRealVector(K);
		
		for (int i = 0; i < eigenvectors.size(); i++) {
			RealVector u = eigenvectors.get(i);
			double w = u.dotProduct(phi);
			omega.addToEntry(i, w);
		}
		
		return omega;
	}
	
	private RealVector getFaceHat(List<RealVector> eigenvectors, RealVector phi) {
		int dimension = eigenvectors.iterator().next().getDimension();
		RealVector phiHat = new ArrayRealVector(dimension);
		
		for (int i = 0; i < eigenvectors.size(); i++) {
			RealVector u = eigenvectors.get(i);
			double w = u.dotProduct(phi);
			phiHat = phiHat.add(u.mapMultiply(w));
		}
		
		return phiHat;
	}
	
	// vector 是列向量
	private RealVector computeAverageFaceVector(List<RealVector> gammas) {
		int M = gammas.size();
		int dimension = gammas.iterator().next().getDimension();
		RealVector psi = new ArrayRealVector(dimension);
		
		for (RealVector gamma : gammas) {
			psi = psi.add(gamma);
		}
		
		psi = psi.mapDivide(M);
		
		return psi;
	}
	
	// phi 是列向量
	private RealVector getMeanFaceVector(RealVector gamma, RealVector psi) {
		RealVector phi = gamma.subtract(psi);
		return phi;
	}

	private RealMatrix getMeanFaceMatrix(List<RealVector> phis) {
		RealVector fristPhi = phis.iterator().next();
		int N = fristPhi.getDimension();
		int M = phis.size();
		
		RealMatrix A = new BlockRealMatrix(N, M);
		
		for (int col = 0; col < M; col++) {
			RealVector phi = phis.get(col);
			A.setColumnVector(col, phi);
		}
		return A;
	}

	/** 
	 * it should compute A * A.transpose(), but it is not practical!
	 * consider the matrix A.transpose() * A
	 * A * A.transpose() and A.transpose() * A have the same eigenvalues and their eigenvectors.
	 * corresponding to the K largest eigenvalues
	 * */
	private List<RealVector> getKLargestEigenvectors(RealMatrix A, int K) {
		
		RealMatrix matrix = A.transpose().multiply(A);
		
		EigenDecomposition decomposition = new EigenDecomposition(matrix);
		double[] eigenvalues = decomposition.getRealEigenvalues();
		
		//获取前k个最大的特征值对应的特征向量的下标
		int[] indexs = getKLargestEigenvalues2Index(eigenvalues, K);
		List<RealVector> kLargestEigenvectors = new ArrayList<RealVector>(K);
		for (int i : indexs) {
			RealVector v = decomposition.getEigenvector(i);
			RealVector u = A.operate(v);
			kLargestEigenvectors.add(u);
		}
		
		return kLargestEigenvectors;
	}
	
	private int[] getKLargestEigenvalues2Index(double[] eigenvalues, int K) {
		
		
		int[] indexs = new int[K];
		int size = eigenvalues.length;
		if (K < size) { // K << size, obtain topK
			for (int i = 0; i < K; i++) {
				indexs[i] = i;
			}
		} else { // K >= size
			for (int i = 0; i < size; i++) {
				indexs[i] = i;
			}
			
			for (int i = size; i < K; i++) {
				indexs[i] = 0;
			}
		}
		
		/*
		// 索引(小->大) 队列
		List<Integer> queue = new LinkedList<Integer>();
		
		for (int i = 0; i < eigenvalues.length; i++) {
			double eigenvalue = eigenvalues[i];
			
			if (queue.size() <= K) {  // add element in order if the queue.size() <= K
				
				if (queue.isEmpty()) {
					queue.add(i);
					continue;
				}
				
				int maxEigenvalueIndex = queue.get(0);
				
				if (eigenvalues[maxEigenvalueIndex] < eigenvalue) {
					queue.add(0, i);
				} else {
					queue.add(i);
				}
				
				
			} else {  // replace the element if the queue.size() > K
			
				int maxEigenvalueIndex = queue.get(0);
				if (eigenvalues[maxEigenvalueIndex] < eigenvalue) {
					queue.remove(queue.size() - 1);
					queue.add(0, i);
				}
			}
			
		}
		
		Integer[] indexs = (Integer[]) queue.toArray();
		*/
		
//		List<Double[]> list = Arrays.asList(eigenvalues);
//		Collections.sort(list);
//		ArrayUtils.
		
		return indexs;
	}

}
