package test;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import model.file.ImageFileManager;
import model.image.ImageGray;
import model.iris.IrisDescriptorGenerator;
import utils.HammingDistance;

public class LogGaborTest {
	private static final String PATH="./pruebas/patterns/";
	public static void main(String[] args) throws IOException {

		new LogGaborTest().test();
	}
	
	public void test() throws IOException{
		ImageGray pattern1=loadImage(PATH+"33_1_1_pattern.pgm");
		ImageGray pattern2=loadImage(PATH+"33_1_2_pattern.pgm");
		ImageGray pattern3=loadImage(PATH+"93_1_2_pattern.pgm");
		ImageGray pattern4=loadImage(PATH+"93_2_2_pattern.pgm");

		
		List<ImagePair> list=new ArrayList<ImagePair>();
		list.add(new ImagePair(pattern1, pattern2,"33a-33b",true));
		list.add(new ImagePair(pattern3, pattern4,"93a-93b",true));
		list.add(new ImagePair(pattern1, pattern3,"33a-93a",false));
		list.add(new ImagePair(pattern2, pattern4,"33b-93b",false));
		
/*		for(double f0=0;f0<1;f0+=0.01){
			System.out.println("f0:"+f0);
			for(double sigma=0;sigma<1;sigma+=0.01)
				test(list,f0, sigma);
		}
	*/	
		List<Double[]> pars=new ArrayList<>();
		pars.add(new Double[]{(double) 65,5.0});
		pars.add(new Double[]{70.0,9.0});
		pars.add(new Double[]{0.22,0.41});
		pars.add(new Double[]{0.0555,0.0305});
		pars.add(new Double[]{0.0555,0.5});

		for(Double[] param:pars){
			test(list,param[0],param[1]);
		}
		PrintStream ps=new PrintStream("goodParams.csv");
		ps.println("f0,sigma, diff-HD");
		for(Double[] params:goodParams){
			ps.println(""+params[0]+","+params[1]+","+params[2]);
		}
	}

	List<Double[]> goodParams=new ArrayList<Double[]>();
	
	private Comparator<ImagePair> comparator=new Comparator<ImagePair>() {
		@Override
		public int compare(ImagePair o1, ImagePair o2) {
			return (int) Math.signum(o1.hd1_2-o2.hd1_2);
		}
	};
	private void test(List<ImagePair> list, double f0, double sigma){
		System.out.println();
		for(ImagePair ip:list){
			ip.encode(f0, sigma);
	//		System.out.println("hd:"+ip.hd1_2 +" eq:"+ip.equals+" "+ip.name);
		}
		list.sort(comparator);
		if((!list.get(0).equals && !list.get(1).equals) 
				)//|| (list.get(0).equals && list.get(1).equals) )
			goodParams.add(new Double[]{f0,sigma,list.get(2).hd1_2-list.get(1).hd1_2});
		//System.out.println(list.get(0).name+","+list.get(1).name+","+list.get(2).name+","+list.get(3).name);
	}
	private ImageGray loadImage(String filepath) throws IOException{
		ImageFileManager ifm=new ImageFileManager(new File(filepath));
		return new ImageGray(ifm.readImagePGM(),false);
	}

	public class ImagePair{
		ImageGray pattern1;
		ImageGray pattern2;
		
		ImageGray encode1;
		ImageGray encode2;
		
		double hd1_2;
		
		String name;
		boolean equals;
		
		public ImagePair(ImageGray pattern1, ImageGray pattern2,String name,boolean equals) {
			super();
			this.pattern1 = pattern1;
			this.pattern2 = pattern2;
			this.name=name;
			this.equals=equals;
		}


		public void encode(double f0, double sigma){
			IrisDescriptorGenerator idg=new IrisDescriptorGenerator(null, null, f0, sigma);
			this.encode1=idg.encode(pattern1);
			this.encode2=idg.encode(pattern2);
			
			this.hd1_2=HammingDistance.compare(encode1, encode2);
		}
		
	}
	
}
