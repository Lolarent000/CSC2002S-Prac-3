import java.util.concurrent.ForkJoinPool;

public class Prac_3_parallel{

	int [] workload = new int[2];
	CloudData cd;
	
	public static void main(String[] args) {
		CloudData cd = new CloudData();
//		cd.readData(args[0]);
		cd.readData("largesample_input.txt");
		//cd.printData();
		
		ForkJoinPool pool = ForkJoinPool.commonPool();
		Parallel_Manager pm = new Parallel_Manager(0, cd.dim()-1, cd);
		pool.invoke(pm);
		cd = pm.getCloudData();
		
		Vector v = cd.combineLocalAverages();
		System.out.printf("Wind = (%.6f,%.6f)\n", v.x, v.y);
//		cd.printClassification();
//		cd.writeData(args[1], v);
		cd.writeData("test.txt", v);
	}
}
