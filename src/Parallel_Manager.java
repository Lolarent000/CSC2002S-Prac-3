import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class Parallel_Manager extends RecursiveTask<CloudData>{

	private static final int THRESHOLD = 50000;
	private int start;
	private int end;
	private CloudData cd;
	
	public Parallel_Manager(int start, int end, CloudData cd) {
		this.start = start;
		this.end = end;
		this.cd = cd;
	}
	
	public CloudData getCloudData() {
		return cd;
	}

	@Override
	protected CloudData compute() {
		if (end - start > THRESHOLD) {
			int middle = start + (end - start - ((end - start - 1) % 2) - 1)/2;
			
			Parallel_Manager firstTask = new Parallel_Manager(start, middle, cd);
	        Parallel_Manager secondTask = new Parallel_Manager((middle+1), end, cd);

	        ForkJoinTask.invokeAll(firstTask, secondTask);
	        
	        return firstTask.cd;
		}
		else {
			cd.calculatePrevailingWind(start, end);
			cd.classifyClouds(start, end);
			return cd;
		}
	}

}
