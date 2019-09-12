import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;

public class CloudData {

	Vector [][][] advection; // in-plane regular grid of wind vectors, that evolve over time
	float [][][] convection; // vertical air movement strength, that evolves over time
	int [][][] classification; // cloud type per grid point, evolving over time
	int dimx, dimy, dimt; // data dimensions
	List <Vector> localAverages = new ArrayList<Vector>();

	// overall number of elements in the timeline grids
	int dim(){
		return dimt*dimx*dimy;
	}
	
	// Average local vectors
	Vector combineLocalAverages() {
		double xSum = 0;
		double ySum = 0;
		for(int i = 0; i < localAverages.size(); i++) {
			xSum += (localAverages.get(i).x * (localAverages.get(i).endId - localAverages.get(i).startId + 1));
			ySum += (localAverages.get(i).y * (localAverages.get(i).endId - localAverages.get(i).startId + 1));
			System.out.printf("xsum: %.2f, x: %.2f\n", xSum, localAverages.get(i).x);
		}
		xSum /= dim();
		ySum /= dim();
		return new Vector((float)xSum, (float)ySum);
	}
	
	// convert linear position into 3D location in simulation grid
	void locate(int pos, int [] ind)
	{
		ind[0] = (int) pos / (dimx*dimy); // t
		ind[1] = (pos % (dimx*dimy)) / dimy; // x
		ind[2] = pos % (dimy); // y
	}
	
	// Printing out the entire dataset
	void printData() {
		for(int t = 0; t < dimt; t++) {
			System.out.printf("t = %d\n", t);
			for(int x = 0; x < dimy; x++) {
				for(int y = 0; y < dimx; y++) {
					System.out.printf("[%.2f, %.2f, %.2f] ", advection[t][x][y].x, advection[t][x][y].y, convection[t][x][y]);
				}
				System.out.println("");
			}
		}
	}
	
	// Calculate Prevailing wind for all elements
	Vector calculatePrevailingWind() {
		float xSum = 0;
		float ySum = 0;
		for(int i = 0; i < dim(); i++) {
			int [] ind = new int [3];
			locate(i, ind);
			int t = ind[0];
			int x = ind[1];
			int y = ind[2];
			xSum += advection[t][x][y].get("x");
			ySum += advection[t][x][y].get("y");
		}
		xSum = xSum/dim();
		ySum = ySum/dim();
		return new Vector(xSum, ySum);
	}
	
	// calculate Prevailing wind for elements iStart to iEnd
	void calculatePrevailingWind(int iStart, int iEnd) {
		float xSum = 0;
		float ySum = 0;
		for(int i = iStart; i < iEnd+1; i++) {
			int [] ind = new int [3];
			locate(i, ind);
			int t = ind[0];
			int x = ind[1];
			int y = ind[2];
			xSum += advection[t][x][y].get("x");
			ySum += advection[t][x][y].get("y");
		}
		xSum = xSum/(iEnd-iStart+1);
		ySum = ySum/(iEnd-iStart+1);
		localAverages.add(new Vector(xSum, ySum, iStart, iEnd));
	}
	
	// Classify clouds based on type
	void classifyClouds() {
		for(int i = 0; i < dim(); i++) {
			int [] ind = new int [3];
			locate(i, ind);
			int t = ind[0];
			int x = ind[1];
			int y = ind[2];
			int [] bounds = calculateBounds(x, y); //0 is xstart, 1 is xend,2 is ystart, 3 is yend
			float xSum = 0;
			float ySum = 0;
			for(int a = bounds[0]; a < bounds[1]+1; a++) {
				for(int b = bounds[2]; b < bounds[3]+1; b++) {
					xSum += advection[t][a][b].x;
					ySum += advection[t][a][b].y;
				}
			}
			int numElements = (bounds[1]-bounds[0]+1)*(bounds[3]-bounds[2]+1)-1;
			xSum /= numElements;
			ySum /= numElements;
			float mag = (float) Math.sqrt(xSum*xSum + ySum*ySum);
			if(Math.abs(convection[t][x][y]) > mag) {
				classification[t][x][y] = 0;
			}
			else if(Math.abs(convection[t][x][y]) <= mag
					&& mag > 0.2) {
				classification[t][x][y] = 1;
			}
			else {
				classification[t][x][y] = 2;
			}
		}
	}
	
	// Classify clouds based on type from iStart to iEnd
		void classifyClouds(int iStart, int iEnd) {
			for(int i = iStart; i < iEnd+1; i++) {
				int [] ind = new int [3];
				locate(i, ind);
				int t = ind[0];
				int x = ind[1];
				int y = ind[2];
				int [] bounds = calculateBounds(x, y);  //0 is xstart, 1 is xend,2 is ystart, 3 is yend
				float xSum = 0;
				float ySum = 0;
				for(int a = bounds[0]; a < bounds[1]+1; a++) {
					for(int b = bounds[2]; b < bounds[3]+1; b++) {
						xSum += advection[t][a][b].x;
						ySum += advection[t][a][b].y;
					}
				}
				int numElements = (bounds[1]-bounds[0]+1)*(bounds[3]-bounds[2]+1)-1;
				xSum /= numElements;
				ySum /= numElements;
				float mag = (float) Math.sqrt(xSum*xSum + ySum*ySum);
				if(Math.abs(convection[t][x][y]) > mag) {
					classification[t][x][y] = 0;
				}
				else if(Math.abs(convection[t][x][y]) <= mag
						&& mag > 0.2) {
					classification[t][x][y] = 1;
				}
				else {
					classification[t][x][y] = 2;
				}
			}
		}
	
	// Printing out the entire classification dataset
		void printClassification() {
			for(int t = 0; t < dimt; t++) {
				System.out.println("");
				for(int x = 0; x < dimy; x++) {
					for(int y = 0; y < dimx; y++) {
						System.out.printf("%d ", classification[t][x][y]);
					}
				}
			}
		}
	
	//calculate bounds for cloud type {returns in format xStart, xEnd, yStart, yEnd}
	int[] calculateBounds(int x, int y){
		int [] bounds = new int[4];
		
		if(x == 0) bounds[0] = 0;
		else bounds[0] = x-1;
		
		if(x == dimx-1) bounds[1] = dimx-1;
		else bounds[1] = x+1;
		
		if(y == 0)bounds[2] = 0;
		else bounds[2] = y-1;
		
		if(y == dimy-1) bounds[3] = dimy-1;
		else bounds[3] = y+1;
			
		return bounds;
	}
	
	// read cloud simulation data from file
	void readData(String fileName){ 
		try{ 
			Scanner sc = new Scanner(new File(fileName), "UTF-8");
			
			// input grid dimensions and simulation duration in timesteps
			dimt = sc.nextInt();
			dimx = sc.nextInt(); 
			dimy = sc.nextInt();
			
			// initialize and load advection (wind direction and strength) and convection
			advection = new Vector[dimt][dimx][dimy];
			convection = new float[dimt][dimx][dimy];
			for(int t = 0; t < dimt; t++)
				for(int x = 0; x < dimx; x++)
					for(int y = 0; y < dimy; y++){
						advection[t][x][y] = new Vector();
						advection[t][x][y].set("x", sc.nextFloat());
						advection[t][x][y].set("y", sc.nextFloat());
						convection[t][x][y] = sc.nextFloat();
					}
			
			classification = new int[dimt][dimx][dimy];
			sc.close(); 
		} 
		catch (IOException e){ 
			System.out.println("Unable to open input file "+fileName);
			e.printStackTrace();
		}
		catch (java.util.InputMismatchException e){ 
			System.out.println("Malformed input file "+fileName);
			e.printStackTrace();
		}
	}
	
	// write classification output to file
	void writeData(String fileName, Vector wind){
		 try{ 
			 FileWriter fileWriter = new FileWriter(fileName);
			 PrintWriter printWriter = new PrintWriter(fileWriter);
			 printWriter.printf("%d %d %d\n", dimt, dimx, dimy);
			 printWriter.printf("%f %f\n", wind.x, wind.y);
			 
			 for(int t = 0; t < dimt; t++){
				 for(int x = 0; x < dimx; x++){
					for(int y = 0; y < dimy; y++){
						printWriter.printf("%d ", classification[t][x][y]);
					}
				 }
				 printWriter.printf("\n");
		     }
				 
			 printWriter.close();
		 }
		 catch (IOException e){
			 System.out.println("Unable to open output file "+fileName);
				e.printStackTrace();
		 }
	}
}
