package micycle.polygonmorphing.featurePointDetection;

import micycle.polygonmorphing.shapes.FeaturePoint;

public class QuickSort {
	public static void sort(FeaturePoint[] p) {
		QuickSort.quicksort(p, 0, p.length - 1);
	}

	private static void quicksort(FeaturePoint[] p, int lower, int upper) {
		int i = lower;
		int j = upper;
		FeaturePoint x = p[(lower + upper) / 2];
		while (true) {
			if (p[i].compareTo(x) < 0) {
				++i;
				continue;
			}
			while (p[j].compareTo(x) > 0) {
				--j;
			}
			if (i <= j) {
				FeaturePoint tmp = p[i];
				p[i] = p[j];
				p[j] = tmp;
				++i;
				--j;
			}
			if (i > j)
				break;
		}
		if (lower < j) {
			QuickSort.quicksort(p, lower, j);
		}
		if (i < upper) {
			QuickSort.quicksort(p, i, upper);
		}
	}
}
