package cn.edu.bjut.sse.adaboost;

import java.util.ArrayList;

public class PerceptronApproach {

	private static final int T = 100; // 最大迭代次数

	/**
	 * @param dataSet ：数据集
	 * @param weight ：每条数据的权重
	 * @return
	 */
	public ArrayList<Double> getWeightVector(
			ArrayList<ArrayList<Double>> dataSet, ArrayList<Double> dataWeight) {
		int dataLength = 0;
		if (null == dataSet) {
			return null;
		} else {
			dataLength = dataSet.get(0).size();
		}

		// 初始化感知器的权重向量
		ArrayList<Double> sensorWeightVector = new ArrayList<Double>();
		for (int i = 0; i < dataLength; i++) {
			sensorWeightVector.add(1d);
		}

		// 初始化感知器的增量
		// int increment = 1;

		int sign = 0; // 迭代终止的条件: 权值向量的的值连续dataSet.size()次大于0
		for (int i = 0; i < T && sign < dataSet.size(); i++) { // 最大迭代次数
			for (int z = 0; z < dataSet.size(); z++) {
				double result = 0;
				for (int j = 0; j < dataLength; j++) {
					result += dataSet.get(z).get(j) * sensorWeightVector.get(j);
				}
				if (result > 0) {
					sign++;
					if (sign >= dataSet.size())
						break;
				} else {
					sign = 0;
					for (int k = 0; k < dataLength; k++) { // 更新权值向量
						sensorWeightVector.set(k, sensorWeightVector.get(k)
								+ dataSet.get(z).get(k) * dataWeight.get(z));
					}
				}
			}
		}

		return sensorWeightVector;
	}
}
