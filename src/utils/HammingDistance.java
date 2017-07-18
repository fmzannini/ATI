package utils;

import model.image.ImageGray;

public class HammingDistance {

	public static double compare(ImageGray img1, ImageGray img2) {
		int N = 0;
		int hammingSum = 0;
		for (int i = 0; i < img1.getWidth(); i++) {
			for (int j = 0; j < img1.getHeight(); j++) {
				if (img1.getPixel(i, j) == 0 && img2.getPixel(i, j) == 0) {
					hammingSum++;
					N++;
				} else if (img1.getPixel(i, j) == 0 && img2.getPixel(i, j) != 0) {
					N += Long.toBinaryString(Double.doubleToRawLongBits(img2.getPixel(i, j))).length();
				} else if (img1.getPixel(i, j) != 0 && img2.getPixel(i, j) == 0) {
					N += Long.toBinaryString(Double.doubleToRawLongBits(img1.getPixel(i, j))).length();
				} else {
					char[] s1 = Long.toBinaryString(Double.doubleToRawLongBits(img1.getPixel(i, j))).toCharArray();
					System.out.println(s1);
					char[] s2 = Long.toBinaryString(Double.doubleToRawLongBits(img2.getPixel(i, j))).toCharArray();

					if (s1.length > s2.length) {
						s2 = fixLengths(s1, s2);
					} else if (s1.length < s2.length) {
						s1 = fixLengths(s2, s1);
					}

					for (int c = 0; c < s1.length; c++) {
						if (s1[c] == s2[c]) {
							hammingSum++;
						}
						N++;
					}
				}
			}
		}
		return hammingSum / N;
	}

	//s1 is the string with greater length
	private static char[] fixLengths(char[] s1, char[] s2) {
		int diff = s1.length - s2.length;

		char[]  result = new char[diff + s2.length];
		for (int i = 0; i < diff; i++) {
			result[i] = 0;
		}
		for (int i = 0; i < s2.length; i++) {
			result[i + diff] = s2[i];
		}
		
		return result;
	}
}
