package cn.edu.bjut.sse.image.face.classifier.util;

import libsvm.svm_node;

public class SVMUtils {
	
	public static svm_node[] features(double[] features) {
		svm_node[] p = new svm_node[60];
		for (int i = 0 ; i < features.length; i++) {
			double feature = features[i];
			svm_node p0 = new svm_node();
			p0.index = i;
			p0.value = feature;
			p[i] = p0;
		}
		
		return p;
	}

}
