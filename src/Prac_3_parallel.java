import java.util.concurrent.ForkJoinPool;

public class Prac_3_parallel{

	int [] workload = new int[2];
	CloudData cd;
	
	public static void main(String[] args) {
		CloudData cd = new CloudData();
		cd.readData(args[0]);
		System.gc();
		
		//Start timing
		long startTime = System.currentTimeMillis();
		
		//Start parallel tasks
		ForkJoinPool pool = ForkJoinPool.commonPool();
		Parallel_Manager pm = new Parallel_Manager(0, cd.dim()-1, cd);
		pool.invoke(pm);
		cd = pm.getCloudData();
		
		Vector v = cd.combineLocalAverages();
		
		//Stop timing
		long stopTime = System.currentTimeMillis();
		Long elapsedTime = stopTime - startTime;
	    double elapsedTimed = (elapsedTime.doubleValue())/1000;
	    System.out.printf("elapsed time: %.5fs\n", elapsedTimed);
	    
	    cd.writeData(args[1], v);
	}
}
