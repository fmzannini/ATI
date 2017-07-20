package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.DirectoryChooserBuilder;
import model.file.ImageFileManager;
import model.image.ImageGray;
import model.iris.IrisDescriptorGenerator;
import model.iris.IrisDescriptorGenerator.CalculateType;

public class IrisEncodeTest {

	private static String PATH="./pruebas/";
	
	
	public static void main(String[] args) throws IOException {
		test("0-0555_0-0305");		
		test("0-22_0-41");
		test("18_9-9");		
		test("70_9");
	}
	
	public static void test(String params) throws IOException {
		String[] pars=params.replaceAll("-", ".").split("_");
		double f0=Double.parseDouble(pars[0]);
		double sigma=Double.parseDouble(pars[1]);
		
		IrisDescriptorGenerator idg=new IrisDescriptorGenerator(null, null, f0, sigma);
		
		File dir=new File(PATH+"patterns/");
		File[] files=dir.listFiles();

		for(CalculateType method:CalculateType.values()){
			idg.CALCULATE_TYPE=method;
			for(File file:files){
				ImageFileManager ifm=new ImageFileManager(file);
				ImageGray pattern=new ImageGray(ifm.readImagePGM(),false);
				ImageGray code=idg.encode(pattern);
				
				File dest=new File(PATH+"encode_"+params+"/"+method.name()+"/"+file.getName().replaceAll("pattern", method.name()));
				dest.mkdirs();
				ImageFileManager ifm2=new ImageFileManager(dest);
				ifm2.writeImagePGM(code.showImage());
			}
		}
		

	}
	
}
