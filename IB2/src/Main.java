import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
	static int columns = 0;

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		String line = ""; 
		String[] data = null;
		String first = "";
		ArrayList<ArrayList<Float> > theList = new ArrayList<ArrayList<Float> >();
		ArrayList<String> classList = new ArrayList<>();
		boolean flag = true;
		
		System.out.print("Write the path of the csv file: ");
		String path = keyboard.nextLine();
		keyboard.close();
		System.out.println("Please wait...(It may take up to 2 minutes)");
		
		//Read from csv
		try   {  
			BufferedReader br = new BufferedReader(new FileReader(path));  
			while ((line = br.readLine()) != null)  { 
				if(flag) {
					first = line;
					flag = false;
				}else {
					data = line.split(",");
					classList.add(data[data.length-1]);
					
					for(int i =0;i<data.length-1;i++) {
						theList.add(new ArrayList<Float>());
						theList.get(i).add(Float.parseFloat(data[i]));
					}
				}
			}  
			br.close();
		}   
		catch (IOException e)   {  
			e.printStackTrace();  
		}  
		
		columns = data.length;
		
		ArrayList<ArrayList<Float> > es = new ArrayList<ArrayList<Float>>();
		ArrayList<String> tempClassList = new ArrayList<String>();
        
		for(int v=0;v<columns-1;v++) {
			es.add(new ArrayList<Float>());
			
		}
		for(int x=0;x<columns-1;x++) {
			es.get(x).add(theList.get(x).get(0));
			theList.get(x).remove(0);
		}
		tempClassList.add(classList.get(0));
		classList.remove(0);
		
		
		//Calculate distances, remove from ts and move to es
		while(!theList.get(0).isEmpty()) {

			ArrayList<ArrayList<Float> > paok = findDistances(es,theList,0);
	
			Collections.sort(paok, new Comparator<ArrayList<Float>>() {
	        	@Override
	        	public int compare(ArrayList<Float> one, ArrayList<Float> two) {
	        			return one.get(0).compareTo(two.get(0));
	        	}
	        });
			
			if(!(tempClassList.get(Math.round(paok.get(0).get(1))).equals(classList.get(0)))) {
				for(int x=0;x<columns-1;x++) {
					es.get(x).add(theList.get(x).get(0));
					tempClassList.add(classList.get(0));
				}
			}
			
			classList.remove(0);
			for(int z=0;z<columns-1;z++)
				theList.get(z).remove(0);
		}

		//Save to csv file
		 try (PrintWriter writer = new PrintWriter("reduced-file.csv")) {
		      StringBuilder sb = new StringBuilder();

		      sb.append(first);
		      sb.append('\n');
		      
		      for(long y=0;y<es.get(0).size();y++) {
		    	  for(int i=0;i<columns-1;i++) {
		    		  sb.append(es.get(i).get((int) y));
		    		  sb.append(',');
		    	  }
		    	  sb.append(tempClassList.get((int) y));
		    	  sb.append('\n');
		      }
		      writer.write(sb.toString());
		      System.out.println("The reduced file is in the program's folder");

	   } catch (FileNotFoundException e) {
		      System.out.println(e.getMessage());
	   }
	}
	
	//Calculate distances
	public static ArrayList<ArrayList<Float> > findDistances(ArrayList<ArrayList<Float> > es,ArrayList<ArrayList<Float> > theList,int position) {
		 ArrayList<ArrayList<Float> > tempList = new ArrayList<ArrayList<Float> >();
	        float distance = 0;
	        float sum = 0;
	        
	        for(long i=0;i<es.get(0).size();i++) {
	        	sum = 0;
	        	for(int y=0;y<columns-1;y++) {
	        		sum = (float) (sum + Math.pow((es.get(y).get((int) i))-(theList.get(y).get(position)), 2));
	        	}
	        	distance = (float) Math.sqrt(sum);
	        	tempList.add(new ArrayList<Float>());
	        	tempList.get((int) i).add(distance);
	        	tempList.get((int) i).add((float) i);
	        }
		return tempList;
	}
}
