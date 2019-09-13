import java.util.concurrent.ForkJoinPool;

public class Prac_3_parallel{

	int [] workload = new int[2];
	CloudData cd;
	
	public static void main(String[] args) {
		CloudData cd = new CloudData();
		cd.readData(args[0]);
//		cd.readData("largesample_input.txt");
		System.gc();
		
		long startTime = System.currentTimeMillis();
		
		ForkJoinPool pool = ForkJoinPool.commonPool();
		Parallel_Manager pm = new Parallel_Manager(0, cd.dim()-1, cd);
		pool.invoke(pm);
		cd = pm.getCloudData();
		
		Vector v = cd.combineLocalAverages();
		
		long stopTime = System.currentTimeMillis();
		Long elapsedTime = stopTime - startTime;
	    double elapsedTimed = (elapsedTime.doubleValue())/1000;
	    System.out.printf("elapsed time: %.5fs\n", elapsedTimed);
	    
	    cd.writeData(args[1], v);
//		cd.writeData("output.txt", v);
	}
}
