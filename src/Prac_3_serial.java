public class Prac_3_serial {
	
	
	public static void main(String[] args) {
		CloudData cd = new CloudData();
		cd.readData(args[0]);
		System.gc();
		
		long startTime = System.currentTimeMillis();
		
		Vector v = cd.calculatePrevailingWind();
		cd.classifyClouds();
		
		long stopTime = System.currentTimeMillis();
		Long elapsedTime = stopTime - startTime;
		System.out.println(elapsedTime);
	    double elapsedTimed = (elapsedTime.doubleValue())/1000;
	    System.out.printf("elapsed time: %.5fs\n", elapsedTimed);
	    
	    cd.writeData(args[1], v);
	}

}