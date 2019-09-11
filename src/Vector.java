
public class Vector {
	float x;
	float y;
	
	public Vector() {
		x = 0;
		y = 0;
	}
	
	public Vector(float xIn, float yIn) {
		x = xIn;
		y = yIn;
	}
	
	public float len() {
		double squares = (double) x*x + y*y;
		return (float) Math.sqrt(squares);
	}
	
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
	
	public void set(String i, float val) {
		if(i.equals("x")) {
			x = val;
		}
		else if (i.equals("y")){
			y = val;
		}
	}
}
