package cn.edu.bjut.sse.face.classifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import org.apache.log4j.Logger;
import org.junit.Test;

public class SVMTest {
	
	private Logger logger = Logger.getLogger(SVMTest.class);
	
	@Test
	public void loadData() throws IOException {
		File f = new File("/Users/zhanghao/Desktop/testSet_svm.txt");
		FileReader fr = new FileReader(f);
		
		BufferedReader br = new BufferedReader(fr);
		
		// 获得文件行数
		int lines = 10;
		
		svm_node[][] datas = new svm_node[lines][]; // 训练集的向量表
		Double[] lables = new Double[lines]; // a,b 对应的lable
		
		String line = "";
		int lineIndex = 0;
		
		while ( (line = br.readLine()) != null ) {
			String[] token = line.split("\t");
			logger.info(token[0] + "," + token[1] + "," + token[2] + "," + lineIndex);
			
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
			
			if ( lineIndex+1 >= lines)
				break;
		}
		
		fr.close();
	}

	@Test
	public void libSVMTest() throws NumberFormatException, IOException {
		File f = new File("/Users/zhanghao/Desktop/testSet_svm.txt");
		FileReader fr = new FileReader(f);
		
		BufferedReader br = new BufferedReader(fr);
		
		// 获得文件行数
		int lines = 10;
		
		svm_node[][] datas = new svm_node[lines][]; // 训练集的向量表
		double[] lables = new double[lines]; // a,b 对应的lable
		
		String line = "";
		int lineIndex = 0;
		
		while ( (line = br.readLine()) != null ) {
			String[] token = line.split("\t");
			logger.info(token[0] + "," + token[1] + "," + token[2] + "," + lineIndex);
			
			svm_node p0 = new svm_node();
			p0.index = 0;
			p0.value = Double.valueOf(token[0]);
			svm_node p1 = new svm_node();
			p1.index = 1;
			p1.value = Double.valueOf(token[1]);
			svm_node[] p = { p0, p1 };
			
			datas[lineIndex] = p;
			lables[lineIndex] = Double.valueOf(token[2]);
			
			lineIndex++;
			
			if ( lineIndex >= lines)
				break;
		}
		
		fr.close();
		/*
		// 定义训练集点a{10.0, 10.0} 和 点b{-10.0, -10.0}，对应lable为{1.0, -1.0}
		svm_node pa0 = new svm_node();
		pa0.index = 0;
		pa0.value = 3.542485;
		svm_node pa1 = new svm_node();
		pa1.index = 1;
		pa1.value = 1.977398;
		
		svm_node pb0 = new svm_node();
		pb0.index = 0;
		pb0.value = 3.018896;
		svm_node pb1 = new svm_node();
		pb1.index = 1;
		pb1.value = 2.556416;
		
		svm_node pc0 = new svm_node();
		pc0.index = 0;
		pc0.value = 7.551510;
		svm_node pc1 = new svm_node();
		pc1.index = 1;
		pc1.value = -1.580030;
		
		svm_node pd0 = new svm_node();
		pd0.index = 0;
		pd0.value = 2.114999;
		svm_node pd1 = new svm_node();
		pd1.index = 1;
		pd1.value = -0.004466;
		
		svm_node pe0 = new svm_node();
		pe0.index = 0;
		pe0.value = 8.127113;
		svm_node pe1 = new svm_node();
		pe1.index = 1;
		pe1.value = 1.274372;
		
		svm_node pf0 = new svm_node();
		pf0.index = 0;
		pf0.value = 7.108772;
		svm_node pf1 = new svm_node();
		pf1.index = 1;
		pf1.value = -0.986906;
		
		svm_node pg0 = new svm_node();
		pg0.index = 0;
		pg0.value = 8.610639;
		svm_node pg1 = new svm_node();
		pg1.index = 1;
		pg1.value = 2.046708;
		
		svm_node ph0 = new svm_node();
		ph0.index = 0;
		ph0.value = 2.326297;
		svm_node ph1 = new svm_node();
		ph1.index = 1;
		ph1.value = 0.265213;
		
		svm_node pi0 = new svm_node();
		pi0.index = 0;
		pi0.value = 3.634009;
		svm_node pi1 = new svm_node();
		pi1.index = 1;
		pi1.value = 1.730537;
		
		svm_node pk0 = new svm_node();
		pk0.index = 0;
		pk0.value = 0.341367;
		svm_node pk1 = new svm_node();
		pk1.index = 1;
		pk1.value = -0.894998;
		
		svm_node[] pa = { pa0, pa1 }; // 点a
		svm_node[] pb = { pb0, pb1 }; // 点b
		svm_node[] pc = { pc0, pc1 }; // 点b
		svm_node[] pd = { pd0, pd1 }; // 点b
		svm_node[] pe = { pe0, pe1 }; // 点b
		svm_node[] pf = { pf0, pf1 }; // 点b
		svm_node[] pg = { pg0, pg1 }; // 点b
		svm_node[] ph = { ph0, ph1 }; // 点b
		svm_node[] pi = { pi0, pi1 }; // 点b
		svm_node[] pk = { pk0, pk1 }; // 点b
		svm_node[][] datas = { pa, pb, pc, pe }; // 训练集的向量表
		double[] lables = { -1.0, -1.0, 1.0, 1.0 }; // a,b 对应的lable
		*/
		
		// 定义svm_problem对象
		svm_problem problem = new svm_problem();
		problem.l = lines; // 向量个数
		problem.x = datas; // 训练集向量表
		problem.y = lables; // 对应的lable数组

		// 定义svm_parameter对象
		svm_parameter param = new svm_parameter();
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.cache_size = 100;
		param.eps = 0.00001;
		param.C = 1;

		// 训练SVM分类模型
		System.out.println(svm.svm_check_parameter(problem, param)); // 如果参数没有问题，则svm.svm_check_parameter()函数返回null,否则返回error描述。
		svm_model model = svm.svm_train(problem, param); // svm.svm_train()训练出SVM分类模型
		
		// 定义测试数据点c
//		svm_node pc0 = new svm_node();
//		pc0.index = 0;
//		pc0.value = 0.1;
//		svm_node pc1 = new svm_node();
//		pc1.index = -1;
//		pc1.value = 0.0;
//		svm_node[] pc = { pc0, pc1 };

		// 预测测试数据的lable
//		System.out.println(svm.svm_predict(model, pc));
		
	}

}
