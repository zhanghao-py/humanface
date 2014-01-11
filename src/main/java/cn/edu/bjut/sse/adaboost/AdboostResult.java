package cn.edu.bjut.sse.adaboost;

import java.util.ArrayList;

public class AdboostResult {
	private ArrayList<ArrayList<Double>> weakClassifierSet;
	private ArrayList<Double> classifierWeightSet;

	public ArrayList<ArrayList<Double>> getWeakClassifierSet() {
		return weakClassifierSet;
	}

	public void setWeakClassifierSet(
			ArrayList<ArrayList<Double>> weakClassifierSet) {
		this.weakClassifierSet = weakClassifierSet;
	}

	public ArrayList<Double> getClassifierWeightSet() {
		return classifierWeightSet;
	}

	public void setClassifierWeightSet(ArrayList<Double> classifierWeightSet) {
		this.classifierWeightSet = classifierWeightSet;
	}
}
