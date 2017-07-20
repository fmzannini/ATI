package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import model.file.ImageFileManager;
import model.image.ImageGray;
import utils.HammingDistance;

public class HammingCompareTest {

	private static final String PATH="./results/";
	private static final String[][][] FILES={
			{
				{"17_1_1-abs.pgm", "17_1_1-abs_sqr.pgm", "17_1_1-complex.pgm", "17_1_1-real.pgm"},
			},
			{
				{"33_1_1-abs.pgm", "33_1_1-abs_sqr.pgm", "33_1_1-complex.pgm", "33_1_1-real.pgm"},
				{"33_1_2-abs.pgm", "33_1_2-abs_sqr.pgm", "33_1_2-complex.pgm", "33_1_2-real.pgm"},
				{"33_2_3-abs.pgm", "33_2_3-abs_sqr.pgm", "33_2_3-complex.pgm", "33_2_3-real.pgm"},
			},
			{
				{"93_1_2-abs.pgm", "93_1_2-abs_sqr.pgm", "93_1_2-complex.pgm", "93_1_2-real.pgm"},
				{"93_2_2-abs.pgm", "93_2_2-abs_sqr.pgm", "93_2_2-complex.pgm", "93_2_2-real.pgm"},
				{"93_2_4-abs.pgm", "93_2_4-abs_sqr.pgm", "93_2_4-complex.pgm", "93_2_4-real.pgm"}
			}
		};
	
	private static final String[] METHOD={"ABS","ABS_SQR","COMPLEX","REAL"};
	private static final String[] PERSON={"17","33","93"};
	
	public static void main(String[] args) throws IOException {
		ImageGray[][][] bank=loadBank();
	
		PrintStream ps=new PrintStream("log.txt");

		PrintStream ps2=new PrintStream("log2.txt");

		
		for(int method=0;method<FILES[0][0].length;method++){
			ps2.println();
			ps2.println();
			ps2.println("METHOD: "+METHOD[method]);
			ps2.println();
			for(int person1=0;person1<FILES.length;person1++){
				for(int photo1=0;photo1<FILES[person1].length;photo1++){
					for(int person2=0;person2<FILES.length;person2++){
						for(int photo2=0;photo2<FILES[person2].length;photo2++){
							if(person1==person2){
								ps2.print("=person:"+PERSON[person1]);
								if(photo1==photo2)
									ps2.print(" =photo:"+photo1);
								else
									ps2.print(" photo1:"+photo1+" photo2:"+photo2);
							}else
								ps2.println("person1:"+PERSON[person1]+" photo1:"+photo1+" person2:"+PERSON[person2]+" photo2:"+photo2);

							double hammingDistance=HammingDistance.compare(bank[person1][photo1][method], bank[person2][photo2][method]);
							ps.println("method:"+method+" person1:"+person1+" photo1:"+photo1+" person2:"+person2+" photo2:"+photo2+" hamming:"+hammingDistance);
							ps2.println(" hamming:"+hammingDistance);
						}
					}					
				}
			}
		}
		
		
	}
	
	
	private static ImageGray loadImage(String filepath) throws IOException{
		ImageFileManager ifm=new ImageFileManager(new File(filepath));
		return new ImageGray(ifm.readImagePGM(),false);
	}
	
	private static ImageGray[][][] loadBank() throws IOException{
		ImageGray[][][] ans=null;
		ans=new ImageGray[FILES.length][][];
		for(int i=0;i<FILES.length;i++){
			ans[i]=new ImageGray[FILES[i].length][];
			for(int j=0;j<FILES[i].length;j++){
				ans[i][j]=new ImageGray[FILES[i][j].length];
				for(int k=0;k<FILES[i][j].length;k++){
					ans[i][j][k]=loadImage(PATH+FILES[i][j][k]);
				}
			}
		}
		
		return ans;
	}
}
