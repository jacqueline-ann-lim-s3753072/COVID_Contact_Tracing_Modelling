import java.io.PrintWriter;
import java.util.Random;

/**
 * SIR model.
 *
 * @author Jeffrey Chan, 2021.
 */
public class SIRModel
{

    /**
     * Default constructor, modify as needed.
     */
    public SIRModel() {

    } // end of SIRModel()


    /**
     * Run the SIR epidemic model to completion, i.e., until no more changes to the states of the vertices for a whole iteration.
     *
     * @param graph Input contracts graph.
     * @param seedVertices Set of seed, infected vertices.
     * @param infectionProb Probability of infection.
     * @param recoverProb Probability that a vertex can become recovered.
     * @param sirModelOutWriter PrintWriter to output the necessary information per iteration (see specs for details).
     * @throws InterruptedException
     */
    public void runSimulation(ContactsGraph graph, String[] seedVertices,
        float infectionProb, float recoverProb, PrintWriter sirModelOutWriter)
    {  
        // set all seedVertices to infected in the graph
        for(int x = 0; x < seedVertices.length; x++) 
        {
            if(((AbstractGraph)graph).getVertexState(seedVertices[x]).equals("S")) 
            {
                graph.toggleVertexState(seedVertices[x]);
            } // assume all seedVertices are Infected and not already set to "R"
        }

        boolean check = true;
        String deleted[] = new String[0];
        int count = 0;
        int line = 0;

        while(check == true) 
        {
            String newInfected[] = new String[0];
            String newRecovered[] = new String[0];
            String prev[] = seedVertices;

            for(int x = 0; x < seedVertices.length; x++) 
            {
                String[] neighbours = graph.kHopNeighbours(1, prev[x]);
                newInfected = infectVertex(neighbours, newInfected, deleted, seedVertices, infectionProb, graph);
            }

            // update Infected states
            for(int x = 0; x < newInfected.length; x++) 
            {
                graph.toggleVertexState(newInfected[x]);
                seedVertices = addVertex(newInfected[x], seedVertices);
            }

            for(int x = 0; x < prev.length; x++) 
            {
                newRecovered = recoverVertex(newRecovered, prev[x], recoverProb);
            }

            for(int x = 0; x < newRecovered.length; x++) 
            {
                graph.toggleVertexState(newRecovered[x]);
                seedVertices = removeVertex(newRecovered[x], seedVertices);
                deleted = addVertex(newRecovered[x], deleted);
            }

            if(prev.length > 0 || newInfected.length > 0) 
            {    
                line++;
                printOneIteration(line, newInfected, newRecovered, sirModelOutWriter);
                
                if(newInfected.length > 0 && newRecovered.length > 0) 
                {
                    count = 0; // reset counter
                }
            }
            else 
            {
                check = false;
            }

            if(prev.length == seedVertices.length) 
            {
                count++;
            }

            if(count == 10) 
            {
                check = false;
            }
        }

        // IMPLEMENT ME!
    } // end of runSimulation()

    private String[] infectVertex(String[] neighbours, String[] newInfected, String[] deleted, String[] seedVertices, float infectionProb, ContactsGraph g) {
        
        Random rand = new Random();
        String temp[] = new String[neighbours.length];
        int counter = 0;


        for(int x = 0; x < neighbours.length; x++) 
        {
            if(((AbstractGraph)g).getVertexState(neighbours[x]).equals("S")) 
            {
                if(!((AbstractGraph)g).checkIfExists1DArray(deleted, neighbours[x]) 
                && !((AbstractGraph)g).checkIfExists1DArray(seedVertices, neighbours[x])
                && !((AbstractGraph)g).checkIfExists1DArray(newInfected, neighbours[x]))
                {
                    if(rand.nextFloat() <= infectionProb) 
                    {
                        temp[counter] = neighbours[x];
                        counter++;
                    }
                }
            }
        }

        String returnString[] = new String[counter + newInfected.length];
        for(int x = 0; x < newInfected.length; x++) 
        {
            returnString[x] = newInfected[x];
        }
        for(int x = 0; x < counter; x++) 
        {
            returnString[x + newInfected.length] = temp[x];
        }

        return returnString;

    }

    private String[] recoverVertex(String[] newRecovered, String vertex, float recoverProb) {
        Random rand = new Random();

        if(rand.nextFloat() <= recoverProb) 
        {
            newRecovered = addVertex(vertex, newRecovered);
        }

        return newRecovered;
    }

    private String[] addVertex(String vertLabel, String[] vertices) {
        String[] temp = new String[vertices.length + 1];
        
        for(int i = 0; i < vertices.length; i++)
        {
            temp[i] = vertices[i];
        }
        
        temp[vertices.length] = vertLabel;

        return temp;
    }

    private String[] removeVertex(String vertLabel, String[] vertices) {
        String[] temp = new String[0];
        if(vertices.length > 1) 
        {
            temp = new String[vertices.length - 1];
        }
        int counter = 0;
        
        for(int i = 0; i < vertices.length && temp.length > 0; i++)
        {
            if(!(vertices[i].equals(vertLabel))) 
            {
                temp[counter] = vertices[i];
                counter++;
            }
        }

        return temp;
    }

    private void printOneIteration(int line, String[] newInfected, String[] newRecovered, PrintWriter sirModelOutWriter)
    {
        
        sirModelOutWriter.printf(line + ": [");
        for(int x = 0; x < newInfected.length; x++) 
        {
            sirModelOutWriter.printf(newInfected[x].toString());
        }
        sirModelOutWriter.printf("] [");

        for(int x = 0; x < newRecovered.length; x++) 
        {
            sirModelOutWriter.printf(newRecovered[x].toString());
        }
        sirModelOutWriter.printf("]\n");
    }

} // end of class SIRModel
