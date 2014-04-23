package cn.edu.bjut.sse.face.classifier;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import net.semanticmetadata.lire.imageanalysis.Gabor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ORLSVMTest {
	
	private Logger logger = Logger.getLogger(ORLSVMTest.class);
	
	class SVMNode {
		public svm_node[][] datas;
		public double[] lables;
		public int l;
	}
	
	public SVMNode loadTrainData() throws IOException {
		File f1 = new File("/Users/zhanghao/Documents/workspace/HumanFaceClassification/src/main/resources/orl_male.txt");
		BufferedReader br1 = new BufferedReader(new FileReader(f1));
		
		File f2 = new File("/Users/zhanghao/Documents/workspace/HumanFaceClassification/src/main/resources/orl_female.txt");
		BufferedReader br2 = new BufferedReader(new FileReader(f2));
		
		// 获得文件行数
		int lines = 400;
		
		svm_node[][] datas = new svm_node[lines][]; // 训练集的向量表
		double[] lables = new double[lines]; // a,b 对应的lable
		
		String line = "";
		int lineIndex = 0;
		
		//male
		while ( (line = br1.readLine()) != null ) {
			
			if (StringUtils.isBlank(line)) {
				continue;
			}
			
			String[] tokens = line.split(",");
			
			svm_node[] p = new svm_node[60];
			for (int i = 0 ; i < tokens.length; i++) {
				String token = tokens[i];
				svm_node p0 = new svm_node();
				p0.value = Double.valueOf(token);
				p0.index = i+1;
				p[i] = p0;
			}
			
			datas[lineIndex] = p;
			lables[lineIndex] = 1.0;
			
			lineIndex++;
		}
		
		//female
		while ( (line = br2.readLine()) != null ) {
			
			if (StringUtils.isBlank(line)) {
				continue;
			}
			
			String[] tokens = line.split(",");
			
			svm_node[] p = new svm_node[60];
			for (int i = 0 ; i < tokens.length; i++) {
				String token = tokens[i];
				svm_node p0 = new svm_node();
				p0.value = Double.valueOf(token);
				p0.index = i+1;
				p[i] = p0;
			}
			
			datas[lineIndex] = p;
			lables[lineIndex] = -1.0;
			
			lineIndex++;
		}
		
		br1.close();
		br2.close();
		
		SVMNode node = new SVMNode();
		node.datas = datas;
		node.lables = lables;
		node.l = lines;
		
		return node;
	}
	
	public svm_node[] loadTestData() throws IOException {
		File file = new File("/Users/zhanghao/Desktop/face/ORL/ORL_male_gabor.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		
		StringTokenizer tokenizer = new StringTokenizer(line, " ");
		svm_node[] p = new svm_node[2560];
		
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			
			if (token.equals("1")) {
				continue;
			}
			
			String[] tokenSplits = token.split(":");
			
			svm_node p0 = new svm_node();
			p0.index = Integer.valueOf(tokenSplits[0]);
			p0.value = Double.valueOf(tokenSplits[1]);
			p[p0.index-1] = p0;
			
		}
		
		return p;
	}
	
	@Test
	public void testORL() throws IOException {
		ORLSVMTest main = new ORLSVMTest();
		svm_node[] testNode = main.loadTestData();
//		
//		// 定义svm_problem对象
//		svm_problem problem = new svm_problem();
//		problem.l = trainNodes.l; // 向量个数
//		problem.x = trainNodes.datas; // 训练集向量表
//		problem.y = trainNodes.lables; // 对应的lable数组
//
//		// 定义svm_parameter对象
//		svm_parameter param = new svm_parameter();
//		param.svm_type = svm_parameter.C_SVC;
//		param.kernel_type = svm_parameter.RBF;
//		param.cache_size = 100;
//		param.eps = 0.00001;
//		param.gamma = 0.5;
//		param.C = 8192;
//		
//		// 训练SVM分类模型
//		System.out.println(svm.svm_check_parameter(problem, param)); // 如果参数没有问题，则svm.svm_check_parameter()函数返回null,否则返回error描述。
//		svm_model model = svm.svm_train(problem, param); // svm.svm_train()训练出SVM分类模型
		
		Resource re = new ClassPathResource("ORL.txt.model");
		InputStreamReader isr = new InputStreamReader(re.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		svm_model model = svm.svm_load_model(br);
		
		double predict = svm.svm_predict(model, testNode);
		
		System.out.println("predict:" + predict
				+ ", acutal: 1");

//		svm.svm_save_model("/Users/zhanghao/Desktop/model.txt", model);
		
//		int correctNum = 0;
//		for (int i = 0; i < trainNodes.l; i++) {
//			svm_node[] pc = trainNodes.datas[i];
//			
//			double predict = svm.svm_predict(model, testNode);
//			double acutal = testNode.lables[i];
//			
//			System.out.println("predict:" + predict
//					+ ", acutal:" + acutal);
//			
//			if (predict == acutal) {
//				correctNum++;
//			}
//		}
		
//		System.out.println("accuracy:" + (double) correctNum/trainNodes.l);
		
		// test
//		svm_node[] pc0 = trainNodes.datas[360];
//		svm_node[] pc = main.loadTestData();
//		double predict = svm.svm_predict(model, pc);
//		System.out.println(pc0);
	}

}
