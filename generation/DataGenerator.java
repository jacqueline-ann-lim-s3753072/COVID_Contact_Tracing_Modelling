package generation;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class DataGenerator
{
	/** Start of integer range to generate values from. */
	protected int mStartOfRange;
	/** End of integer range to generate values from. */
	protected int mEndOfRange;
	/** Random generator to use. */
	Random mRandGen;

	protected int vertTotalNum;

	protected List<String[]> edges;


	public DataGenerator() {
		// use current time as seed
		mRandGen = new Random(System.currentTimeMillis());

		edges = new LinkedList<String[]>();

	} // end of DataGenerator()

	//Getters and Setters
	public int getVertTotalNum()
	{
		return vertTotalNum;
	}

	public void setVertTotalNum(int vertTotalNum)
	{
		this.vertTotalNum = vertTotalNum;
	}

	public void setmStartOfRange(int mStartOfRange)
	{
		this.mStartOfRange = mStartOfRange;
	}

	public void setmEndOfRange(int mEndOfRange)
	{
		this.mEndOfRange = mEndOfRange;
	}

	public String[] generateVertexTestData(int sampleSize) throws IllegalArgumentException {
	    int populationSize = mEndOfRange - mStartOfRange + 1;

	    String[] samples = new String[sampleSize];
	    // fill it with initial values in the range
	    for (int i = 0; i < sampleSize; i++) {
            samples[i] = Integer.toString(i + mStartOfRange);
	    }

	    // without duplicates
	    for (int j = sampleSize; j < populationSize; j++) {
	    	int t = mRandGen.nextInt(j+1);
	    	if (t < sampleSize) {
	    		samples[t] = Integer.toString(j + mStartOfRange);
	    	}
	    }

	   return samples;
	} // end of sampleWithOutReplacement()

	//deleteEdges
	public String[] getExistingEdge()
	{
		Collections.shuffle(edges);
		String[] edge = ((LinkedList<String[]>) edges).pop();
		return edge;
	}

	public List<String[]> getAllExistingEdges()
	{
		return edges;
	}

	private boolean checkIfExists(String[] edge)
	{
		boolean check = false;

		for(int i = 0; i < edges.size(); i++)
		{
			String[] existingEdge = edges.get(i);

			if(existingEdge[0].equals(edge[0]) && existingEdge[1].equals(edge[1]) || existingEdge[1].equals(edge[0]) && existingEdge[0].equals(edge[1]))
			{
				check = true;
				break;
			}

		}

		return check;
	}

	public String[] getNewEdge()
	{
		int srcLabel = mRandGen.nextInt(10001);
		int tarLabel = mRandGen.nextInt(10001);
		String[] newEdge = null;

		if(srcLabel != tarLabel && srcLabel != 0 && tarLabel != 0)
		{
			String[] edge = new String[]{String.valueOf(srcLabel), String.valueOf(tarLabel)};
			String[] reverseEdge = new String[]{String.valueOf(tarLabel), String.valueOf(srcLabel)};

			if(!(checkIfExists(edge)))
			{
				newEdge = new String[]{String.valueOf(srcLabel), String.valueOf(tarLabel)};
				edges.add(newEdge);
			}
		}

		return newEdge;
	}

	/**
	 * Error message.
	 */
	public static void usage() {
		System.err.println("-f <initialGraph>");
        System.err.println("commands are <AV> <DV> <AE> <DE> <KN (int)>");
		System.exit(1);
	} // end of usage()

	public void addToList(String[] a) {
		edges.add(a);
	}

	public static void main(String[] args) {

		DataGenerator gen = new DataGenerator();

		// parse command line options
		OptionParser parser = new OptionParser("f:o:");
		OptionSet options = parser.parse(args);

		String inputFilename = null;
		// -f <inputFilename> specifies the file that contains edge list information to construct the initial graph with.
		if (options.hasArgument("f")) {
			inputFilename = (String) options.valueOf("f");
		}

		// if file specified, then load file (assumed in Pajek .net format)
		if (inputFilename != null) {

			try {
				BufferedReader reader = new BufferedReader(new FileReader(inputFilename));

		    	String line;
		    	String delimiterRegex = "[ \t]+";
		    	String[] tokens;
		    	String srcLabel, tarLabel;
				
				// read in initial vertex line *Vertex 
				int vertTotalNum = -1;
				if ((line = reader.readLine()) != null) {
					tokens = line.trim().split(delimiterRegex);
					vertTotalNum = Integer.parseInt(tokens[1]);
					gen.setVertTotalNum(vertTotalNum);
				}

				boolean bVertexPhrase = true;
		    	while ((line = reader.readLine()) != null) {
					// check if switch to edge phrase, which means line is *Edges
					if (line.compareTo("*Edges") == 0) {
						bVertexPhrase = false;
						continue;
					}
					// read in vertices
					if (bVertexPhrase) {
			    		tokens = line.trim().split(delimiterRegex);
					}
					// otherwise in edge reading phrase
					else {
						tokens = line.trim().split(delimiterRegex);
						srcLabel = tokens[0];
			    		tarLabel = tokens[1];
						String[] edge = new String[]{srcLabel, tarLabel};
						gen.addToList(edge);
					}
		    	}
				reader.close();
			}
			catch (FileNotFoundException ex) {
				System.err.println("File " + args[1] + " not found.");
			}
			catch(IOException ex) {
				System.err.println("Cannot open file " + args[1]);
			}
		}		

		try {
			BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
			String line = inReader.readLine();
			String[] tokens = line.split(" ");

			int sampleSize = Integer.parseInt(tokens[0]);
			String command = tokens[1];
			
			int khopVal = -1;
			if(command.equals("KN"))
			{
				khopVal = Integer.parseInt(tokens[2]);
				if(khopVal < 0) {
					throw new IllegalArgumentException("<KN (int)> int must be greater than or equal to zero."); 
				}
			}

			String[] samples = null;
			switch (command) {
				case "AV":
					gen.setmStartOfRange(gen.getVertTotalNum() + 1);
					gen.setmEndOfRange(gen.getVertTotalNum()*2);
					samples = gen.generateVertexTestData(sampleSize);
                    for(int i = 0; i < samples.length; i++) {
						System.out.println("AV " + samples[i]);
                    }
					break;
                case "DV":
					gen.setmStartOfRange(1);
					gen.setmEndOfRange(gen.getVertTotalNum());
                    samples = gen.generateVertexTestData(sampleSize);
                    for(int i = 0; i < samples.length; i++) {
						System.out.println("DV " + samples[i]);
                    }
                    break;
                case "AE":
					int count = 0;
                    while(count != sampleSize) 
					{
						String[] newEdge = gen.getNewEdge();
						if(newEdge != null)
						{
							System.out.println("AE " + newEdge[0].toString() + " " + newEdge[1].toString());
							count++;
						}
                    }
                    break;
                case "DE":
                    //samples = gen.sampleWithOutReplacement(sampleSize);
                    for(int i = 0; i < sampleSize; i++) {
					   String[] edge = gen.getExistingEdge();
                       System.out.println("DE " + edge[0].toString() + " " + edge[1].toString());
                    }
                    break;
                case "KN":
                    if(khopVal != -1) {
						gen.setmStartOfRange(1);
						gen.setmEndOfRange(gen.getVertTotalNum());
                        samples = gen.generateVertexTestData(sampleSize);
                        for(int i = 0; i < samples.length; i++) {
                            System.out.print("KN " + khopVal + " " + samples[i] + "\n");
                        }
                    }
                    break;
				default:
					System.err.println(command + " is an unknown command.");
					usage();
			}
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			usage();
		}

	} // end of main()
} // end of class DataGenerator