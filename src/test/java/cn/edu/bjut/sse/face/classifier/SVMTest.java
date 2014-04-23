package cn.edu.bjut.sse.face.classifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
		int lines = 100;
		
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
		param.gamma = 0.0769231;
		param.eps = 0.00001;
		param.C = 1;

		// 训练SVM分类模型
		System.out.println(svm.svm_check_parameter(problem, param)); // 如果参数没有问题，则svm.svm_check_parameter()函数返回null,否则返回error描述。
		svm_model model = svm.svm_train(problem, param); // svm.svm_train()训练出SVM分类模型
		
		int correctNum = 0;
		
		for (int i = 0; i < lines; i++) {
			svm_node[] pc = datas[i];
			double predict = svm.svm_predict(model, pc);
			double acutal = lables[i];
			System.out.println("predict:" + predict
					+ ", acutal:" + acutal);
			
			if (predict == acutal) {
				correctNum++;
			}
		}
		
		System.out.println("accuracy:" + (double) correctNum/lines);
		
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
	
	@Test
	public void testLineSVM() {
        //定义训练集点a{10.0, 10.0} 和 点b{-10.0, -10.0}，对应lable为{1.0, -1.0}
        svm_node pa0 = new svm_node();
        pa0.index = 0;
        pa0.value = 10.0;
        svm_node pa1 = new svm_node();
        pa1.index = -1;
        pa1.value = 10.0;
        svm_node pb0 = new svm_node();
        pb0.index = 0;
        pb0.value = -10.0;
        svm_node pb1 = new svm_node();
        pb1.index = 0;
        pb1.value = -10.0;
        svm_node[] pa = {pa0, pa1}; //点a
        svm_node[] pb = {pb0, pb1}; //点b
        svm_node[][] datas = {pa, pb}; //训练集的向量表
        double[] lables = {1.0, -1.0}; //a,b 对应的lable
        
        //定义svm_problem对象
        svm_problem problem = new svm_problem();
        problem.l = 2; //向量个数
        problem.x = datas; //训练集向量表
        problem.y = lables; //对应的lable数组
        
        //定义svm_parameter对象
        svm_parameter param = new svm_parameter();
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.LINEAR;
        param.cache_size = 100;
        param.eps = 0.00001;
        param.C = 1;
        
        //训练SVM分类模型
        System.out.println(svm.svm_check_parameter(problem, param)); //如果参数没有问题，则svm.svm_check_parameter()函数返回null,否则返回error描述。
        svm_model model = svm.svm_train(problem, param); //svm.svm_train()训练出SVM分类模型
        
        
        for (int i = 0; i < problem.l; i++) {
			svm_node[] pc = datas[i];
			System.out.println("predict:" + svm.svm_predict(model, pc)
					+ ", acutal:" + lables[i]);
		}
        
        /*
        //定义测试数据点c
        svm_node pc0 = new svm_node();
        pc0.index = 0;
        pc0.value = -0.1;
        svm_node pc1 = new svm_node();
        pc1.index = -1;
        pc1.value = 0.0;
        svm_node[] pc = {pc0, pc1};
        
        //预测测试数据的lable
        System.out.println(svm.svm_predict(model, pc));
        */
    }

}
