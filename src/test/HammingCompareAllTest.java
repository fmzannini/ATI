package test;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;

import model.file.ImageFileManager;
import model.image.ImageGray;
import model.iris.IrisDescriptorGenerator.CalculateType;
import utils.HammingDistance;

public class HammingCompareAllTest {

	private static final String PATH="./pruebas/";
	
	
	public static void main(String[] args) throws IOException {
		test("0-0555_0-0305");		
		test("0-22_0-41");
		test("18_9-9");		
		test("70_9");
	}
	
	public static void test(String encodeParams) throws IOException {

		for(CalculateType method:CalculateType.values()){
			File dir=new File(PATH+"encode_"+encodeParams+"/"+method.name());
			File[] files=dir.listFiles();
			Arrays.sort(files, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					return o1.compareTo(o2);
				}
			});
			PrintStream ps=new PrintStream(PATH+"encode_"+encodeParams+"/"+"hd_"+encodeParams+"_"+method.name()+".csv");
			
			ImageGray[] encodes=new ImageGray[files.length];
			String[] encodesNames=new String[files.length];
			
			ps.print("+"+",");
			for(int i=0;i<files.length;i++){
				ImageFileManager ifm=new ImageFileManager(files[i]);
				encodes[i]=new ImageGray(ifm.readImagePGM(),false);
				encodesNames[i]=files[i].getName();
				
				ps.print(encodesNames[i]+(i==files.length-1?"":","));
			}
			ps.println();
			for(int k=0;k<encodes.length;k++){
				ps.print(encodesNames[k]+",");
				for(int m=0;m<encodes.length;m++){
					double hammingDistance=HammingDistance.compare(encodes[k], encodes[m]);
					ps.print(hammingDistance+(m==files.length-1?"":","));
				}
				ps.println();
			}
		}

	}
}
