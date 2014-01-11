package cn.edu.bjut.sse.adaboost;

import java.util.ArrayList;

public class AdaboostAlgorithm {
	private static final int T = 30; // 迭代次数
	PerceptronApproach pa = new PerceptronApproach(); // 弱分类器

	/**
	 * 
	 * @Title: adaboostClassify
	 * @Description: 通过训练集计算出组合分类器
	 * @return AdboostResult
	 * @throws
	 */
	public AdboostResult adaboostClassify(ArrayList<ArrayList<Double>> dataSet) {
		AdboostResult res = new AdboostResult();

		int dataDimension;
		if (null != dataSet && dataSet.size() > 0) {
			dataDimension = dataSet.get(0).size();
		} else {
			return null;
		}

		// 为每条数据的权重赋初值
		ArrayList<Double> dataWeightSet = new ArrayList<Double>();
		for (int i = 0; i < dataSet.size(); i++) {
			dataWeightSet.add(1.0 / (double) dataSet.size());
		}

		// 存储每个弱分类器的权重
		ArrayList<Double> classifierWeightSet = new ArrayList<Double>();

		// 存储每个弱分类器
		ArrayList<ArrayList<Double>> weakClassifierSet = new ArrayList<ArrayList<Double>>();

		for (int i = 0; i < T; i++) {
			// 计算弱分类器
			ArrayList<Double> sensorWeightVector = pa.getWeightVector(dataSet,
					dataWeightSet);
			weakClassifierSet.add(sensorWeightVector);

			// 计算弱分类器误差
			double error = 0; // 分类数
			int rightClassifyNum = 0;
			ArrayList<Double> cllassifyResult = new ArrayList<Double>();
			for (int j = 0; j < dataSet.size(); j++) {
				double result = 0;
				for (int k = 0; k < dataDimension - 1; k++) {
					result += dataSet.get(j).get(k) * sensorWeightVector.get(k);

				}
				result += sensorWeightVector.get(dataDimension - 1);
				if (result < 0) { // 说明预测错误
					error += dataWeightSet.get(j);
					cllassifyResult.add(-1d);
				} else {
					cllassifyResult.add(1d);
					rightClassifyNum++;
				}
			}
			System.out.println("总数：" + dataSet.size() + "正确预测数"
					+ rightClassifyNum);
			if (dataSet.size() == rightClassifyNum) {
				classifierWeightSet.clear();
				weakClassifierSet.clear();
				classifierWeightSet.add(1.0);
				weakClassifierSet.add(sensorWeightVector);
				break;
			}

			// 更新数据集中每条数据的权重并归一化
			double dataWeightSum = 0;
			for (int j = 0; j < dataSet.size(); j++) {
				dataWeightSet.set(
						j,
						dataWeightSet.get(j)
								* Math.pow(
										Math.E,
										(-1) * 0.5
												* Math.log((1 - error) / error)
												* cllassifyResult.get(j))); // 按照http://wenku.baidu.com/view/49478920aaea998fcc220e98.html，更新的权重少除一个常数
				dataWeightSum += dataWeightSet.get(j);
			}
			for (int j = 0; j < dataSet.size(); j++) {
				dataWeightSet.set(j, dataWeightSet.get(j) / dataWeightSum);
			}

			// 计算次弱分类器的权重
			double currentWeight = (0.5 * Math.log((1 - error) / error));
			classifierWeightSet.add(currentWeight);
			System.out.println("classifier weight: " + currentWeight);
		}

		res.setClassifierWeightSet(classifierWeightSet);
		res.setWeakClassifierSet(weakClassifierSet);
		return res;
	}

	/**
	 * 
	 * @Title: computeResult
	 * @Description: 计算输入数据的类别
	 * @return double
	 * @throws
	 */
	public int computeResult(ArrayList<Double> data, AdboostResult classifier) {
		double result = 0;
		int dataSize = data.size();
		ArrayList<ArrayList<Double>> weakClassifierSet = classifier
				.getWeakClassifierSet();
		ArrayList<Double> classifierWeightSet = classifier
				.getClassifierWeightSet();
		for (int i = 0; i < weakClassifierSet.size(); i++) {
			for (int j = 0; j < dataSize; j++) {
				result += weakClassifierSet.get(i).get(j) * data.get(j)
						* classifierWeightSet.get(i);
			}
			result += weakClassifierSet.get(i).get(dataSize);
		}
		if (result > 0) {
			return 1;
		} else {
			return -1;
		}

	}
}
