package cn.edu.bjut.sse.adaboost;

import java.util.ArrayList;

public class AdaboostMain {
	
	
	public static void main(String[] args) {
		/**
		 * 测试数据，产生两类随机数据一类位于圆内，另一类位于包含小圆的大圆内，成环状 小圆半径为1，大圆半径为2，公共圆心位于(2, 2)内
		 */
		final int SMALL_CIRCLE_NUM = 24;
		final int RING_NUM = 34;

		ArrayList<ArrayList<Double>> dataSet = new ArrayList<ArrayList<Double>>();

		// 产生小圆数据
		for (int i = 0; i < SMALL_CIRCLE_NUM; i++) {
			double x = 1 + Math.random() * 2; // 1到3的随机数
			double y = 1 + Math.random() * 2; // 1到3的随机数
			if ((x - 2) * (x - 2) + (y - 2) * (y - 2) - 1 <= 0) { // 说明位于圆内
				ArrayList<Double> smallCircle = new ArrayList<Double>();
				smallCircle.add(x);
				smallCircle.add(y);
				smallCircle.add(1d); // 列别1
				dataSet.add(smallCircle);
			}
		}

		// 产生外围环形数据
		for (int i = 0; i < RING_NUM; i++) {
			double x1 = Math.random() * 4;
			double y1 = Math.random() * 4;
			if ((x1 - 2) * (x1 - 2) + (y1 - 2) * (y1 - 2) - 4 < 0
					&& (x1 - 2) * (x1 - 2) + (y1 - 2) * (y1 - 2) - 1 > 0) { // 说明位于环形区域内
				ArrayList<Double> ring = new ArrayList<Double>();
				ring.add(-x1);
				ring.add(-y1);
				ring.add(-1d); // 列别2
				dataSet.add(ring);
			}
		}

		AdaboostAlgorithm algo = new AdaboostAlgorithm();
		AdboostResult result = algo.adaboostClassify(dataSet);

		// 产生测试数据
		for (int i = 0; i < 10; i++) {

			ArrayList<Double> testData = new ArrayList<Double>();

			double x1 = Math.random() * 4;
			double y1 = Math.random() * 4;
			if ((x1 - 2) * (x1 - 2) + (y1 - 2) * (y1 - 2) - 4 < 0
					&& (x1 - 2) * (x1 - 2) + (y1 - 2) * (y1 - 2) - 1 > 0) {
				testData.add(x1);
				testData.add(y1);
			}

			// double x = 1 + Math.random() * 2; // 1到3的随机数
			// double y = 1 + Math.random() * 2; // 1到3的随机数
			// if((x - 2) * (x - 2) + (y - 2) * (y - 2) - 1 <= 0) { //说明位于圆内
			// testData.add(x);
			// testData.add(y);
			// }

			algo.computeResult(testData, result);
			System.out.println(algo.computeResult(testData, result));
		}

	}
}
