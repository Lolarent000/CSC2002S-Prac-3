import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class Parallel_Manager extends RecursiveTask<CloudData>{

	private static final int THRESHOLD = 300;
	private int start;
	private int end;
	private CloudData cd;
	
	public Parallel_Manager(int start, int end, CloudData cd) {
		this.start = start;
		this.end = end;
		this.cd = cd;
	}

	@Override
	protected CloudData compute() {
		if (end - start > THRESHOLD) {
			ForkJoinTask.invokeAll(createSubtasks());
		}
		else {
			cd.calculatePrevailingWind(start, end);
			cd.classifyClouds(start, end);
			return cd;
		}
	}
	
	private Collection<Parallel_Manager> createSubtasks() {
        List<Parallel_Manager> dividedTasks = new ArrayList<>();
        int middle = start + (end - start - ((end - start - 1) % 2) - 1)/2;
        
        dividedTasks.add(new Parallel_Manager(start, middle, cd));
        dividedTasks.add(new Parallel_Manager((middle+1), end, cd));
        return dividedTasks;
    }

}
