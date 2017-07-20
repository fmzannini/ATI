package utils;

import model.image.ImageGray;

public class HammingDistance {

	public static double compare(ImageGray img1, ImageGray img2) {
		int N = 0;
		int hammingSum = 0;

		for (int i = 0; i < img1.getWidth(); i++) {
			for (int j = 0; j < img1.getHeight(); j++) {
				if(i>=img2.getWidth() || j>=img2.getHeight())
					continue;
				if (img1.getPixel(i, j) == 0 && img2.getPixel(i, j) == 0) {
					hammingSum++;
					N++;
				} else if (img1.getPixel(i, j) == 0 && img2.getPixel(i, j) != 0) {
					N += Long.toBinaryString(Double.doubleToRawLongBits(img2.getPixel(i, j))).length();
				} else if (img1.getPixel(i, j) != 0 && img2.getPixel(i, j) == 0) {
					N += Long.toBinaryString(Double.doubleToRawLongBits(img1.getPixel(i, j))).length();
				} else {
					char[] s1 = Long.toBinaryString(Double.doubleToRawLongBits(img1.getPixel(i, j))).toCharArray();
				//	System.out.println(s1);
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
		return ((double)hammingSum) / (double)N;
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
	
	public static double compare2(ImageGray img1, ImageGray img2) {
		int N = 0;
		int hammingSum = 0;
		
		int width=Math.min(img1.getWidth(), img2.getWidth());
		int height=Math.min(img1.getHeight(), img2.getHeight());
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				double pixel1=img1.getPixel(i, j);
				double pixel2=img2.getPixel(i, j);
				String s1=Long.toBinaryString(Double.doubleToRawLongBits(pixel1));
				String s2=Long.toBinaryString(Double.doubleToRawLongBits(pixel2));
				s1=fix(s1);
				s2=fix(s2);
				byte[] b1=s1.getBytes();
				byte[] b2=s2.getBytes();
				for(int k=0;k<b1.length;k++){
					int bit1=(int)(b1[k]!='0'?1:0);
					int bit2=(int)(b2[k]!='0'?1:0);
					hammingSum+=bit1^bit2;
					N++;
				}
			}
		}
		return ((double)hammingSum)/N;
	}
	private static String fix(String s1){
		int diff=64-s1.length();
		String s="";
		for(int i=0;i<diff;i++)
			s+="0";
		return s+s1;
	}

}
