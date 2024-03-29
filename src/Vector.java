
public class Vector {
	float x;
	float y;
	int startId;
	int endId;
	
	public Vector() {
		x = 0;
		y = 0;
	}
	
	public Vector(float xIn, float yIn) {
		x = xIn;
		y = yIn;
	}
	
	public Vector(float xIn, float yIn, int startIdIn, int endIdIn) {
		x = xIn;
		y = yIn;
		startId = startIdIn;
		endId = endIdIn;
	}
	
	//calculates the length of the vector
	public float len() {
		double squares = (double) x*x + y*y;
		return (float) Math.sqrt(squares);
	}
	
	// returns x or y component
	public float get(String i) {
		if(i.equals("x")) {
			return x;
		}
		else if (i.equals("y")){
			return y;
		}
		else {
			return 0;
		}
	}
	
	// sets x or y component
	public void set(String i, float val) {
		if(i.equals("x")) {
			x = val;
		}
		else if (i.equals("y")){
			y = val;
		}
	}
}
