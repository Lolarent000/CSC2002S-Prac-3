import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Prac_3_parallel extends RecursiveAction{

	
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
	
	

	@Override
	protected void compute() {
		// TODO Auto-generated method stub
		
	}
}
