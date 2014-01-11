package cn.edu.bjut.sse.image.face.classifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import libsvm.svm_node;

public class FileDataLoader {
	
	public void loadData() throws IOException {
		File f = new File("/Users/zhanghao/Desktop/testSet_svm.txt");
		FileReader fr = new FileReader(f);
		
		BufferedReader br = new BufferedReader(fr);
		
		// 获得文件行数
		int lines = 100;
		
		svm_node[][] datas = new svm_node[lines][]; // 训练集的向量表
		Double[] lables = new Double[lines]; // a,b 对应的lable
		
		String line = "";
		int lineIndex = 0;
		
		while ( (line = br.readLine()) != null ) {
			String[] token = line.split("\t");
//			logger.info(token[0] + "," + token[1] + "," + token[2] + "," + lineIndex);
			
			svm_node p0 = new svm_node();
//			pa0.index = 0;
			p0.value = Double.valueOf(token[0]);
			svm_node p1 = new svm_node();
//			pa1.index = -1;
			p1.value = Double.valueOf(token[1]);
			svm_node[] p = { p0, p1 };
			
			datas[lineIndex] = p;
			lables[lineIndex] = Double.valueOf(token[2]);
			
			lineIndex++;
		}
		
		fr.close();
	}

}
