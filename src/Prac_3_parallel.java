import java.util.concurrent.ForkJoinPool;

public class Prac_3_parallel{

	int [] workload = new int[2];
	CloudData cd;
	
	public static void main(String[] args) {
		ForkJoinPool commonPool = ForkJoinPool.commonPool();
		
		CloudData cd = new CloudData();
		cd.readData(args[0]);
		//cd.readData("simplesample_input.txt");
		//cd.printData();
		Vector v = cd.calculatePrevailingWind();
		System.out.printf("Wind = (%.6f,%.6f)\n", v.x, v.y);
		cd.classifyClouds();
		cd.printClassification();
		cd.writeData(args[1], v);
	}
}
