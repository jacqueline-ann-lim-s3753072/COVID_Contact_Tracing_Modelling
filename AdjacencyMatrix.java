import java.io.PrintWriter;


/**
 * Adjacency matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class AdjacencyMatrix extends AbstractGraph
{

	/**
	 * Contructs empty graph.
	 */

    private String vertices[][];
    private String adjacencyMatrix[][];

    public AdjacencyMatrix() {

        adjacencyMatrix = new String[0][0];
        vertices = new String[0][2];
    } // end of AdjacencyMatrix()

    public int getPosition(String vertLabel) {

        //this method will return -1 if vertex does NOT exist
        int pos = -1;

        //get the index (pos) of the vertex (vertLabel) 
        //in the vertices array
        for(int x = 0; x < vertices.length; x++) 
        {
            if(vertices[x][0].equals(vertLabel)) 
            {
                pos = x;
                x = vertices.length;
            }
        }
        return pos;
    } // end of getPosition()

    public String getVertexState(String vertLabel){
        
        String state = null;

        for(int i = 0; i < vertices.length; i++) 
        {
            if(vertices[i][0].equals(vertLabel)) 
            {
                //get the current state of the vertex 
                state = vertices[i][1];
                i = vertices.length;
            }
        }
        return state;
    } //end of getVertexState()

    public boolean checkIfExists1DArray(String[] verts, String vertLabel) {
        
        boolean check = false;

        for(int x = 0; x < verts.length; x++) 
        {
            if(verts[x] != null) 
            {
                if(verts[x].equals(vertLabel)) 
                {
                    check = true;
                    x = verts.length;
                }
            }
        }
        return check;
    } // end of checkIfExists1DArray()

    public boolean checkIfExists2DArray(String array[][], String firstVert, String secondVert){
        boolean exists = false;

        if(secondVert == null)
        {
            for(int i = 0; i < array.length; i++) 
            {
                if(array[i] != null) 
                {
                    //check if the vertex (vertLabel) exists                
                    if(array[i][0].equals(firstVert)) 
                    {
                        exists = true; 
                    }
                }
            }
        }
        else
        {
            int srcPos = getPosition(firstVert);
            int tarPos = getPosition(secondVert);

            //check if source vertex and target vertex of the edge exist
            if(srcPos != -1 && tarPos != -1 && srcPos != tarPos) 
            {
                //check if the edge exists   
                if(adjacencyMatrix[srcPos][tarPos].equals("1"))
                {
                    exists = true;
                }   
            }
        }
        return exists;
    } // end of checkIfExists2DArray()

    private void addVertexToAdjMatrix()
    {
        //increase the size of adjacency matrix by 1
        String newMatrix[][] = new String[adjacencyMatrix.length + 1][adjacencyMatrix.length + 1];
        
        //copy all the existing elements from adjacency matrix to the new matrix
        for(int x = 0; x < adjacencyMatrix.length; x++) 
        {
            for(int y = 0; y < adjacencyMatrix.length; y++) 
            {
                newMatrix[x][y] = adjacencyMatrix[x][y];
            }
        }
        
        //add new row and column for the newly added vertex
        for(int x = 0; x <= adjacencyMatrix.length; x++) 
        {
            newMatrix[adjacencyMatrix.length][x] = "0";
            newMatrix[x][adjacencyMatrix.length] = "0";
        }

        //update the reference of the array
        adjacencyMatrix = newMatrix;
    } //end Of addVertexToAdjMatrix

    public void addVertex(String vertLabel) {

        //increase the size of the vertices array by 1
        String newVertices[][] = new String[vertices.length + 1][2];

        //add the new vertex if it does NOT exist yet
        if(!(checkIfExists2DArray(vertices, vertLabel, null))) 
        {
            //copy all existing values from vertices to newVertices
            for(int x = 0; x < vertices.length; x++) 
            {
                newVertices[x][0] = vertices[x][0];
                newVertices[x][1] = vertices[x][1];
            }

            //add the new vertex
            newVertices[vertices.length][0] = vertLabel;
            newVertices[vertices.length][1] = "S";

            //update the reference of the array
            vertices = newVertices;

            //update the adjacency matrix
            addVertexToAdjMatrix();
        }
    } // end of addVertex()

    private void edges(String srcLabel, String tarLabel, String setBit) {

        //get the index (position) of source vertex in the vertices array
        int srcPos = getPosition(srcLabel);

         //get the index (position) of target vertex in the vertices array
        int tarPos = getPosition(tarLabel);

        //if the index (position) is -1, then the vertex does NOT exist
        //check if BOTH vertices (srcLabel and tarLabel) exist
        //a vertex CANNOT have an edge to itself (srcPos != tarPos)
        if((srcPos != -1 && tarPos != -1) && srcPos != tarPos) 
        {
            //for adjacency matrix, we set 0 if NO edge, 
            //while set 1 if there is an edge
            adjacencyMatrix[srcPos][tarPos] = setBit;
            adjacencyMatrix[tarPos][srcPos] = setBit;
        }
        else
        {
            System.err.println("One of the vertices does NOT EXIST");
        }
    } // end of edges()

    public void addEdge(String srcLabel, String tarLabel) {

        //add the new edge if it does NOT exist yet
        if(!(checkIfExists2DArray(adjacencyMatrix, srcLabel, tarLabel)))
        {
            //add the new edge by setting 1
            edges(srcLabel, tarLabel, "1");
        }
    } // end of addEdge()

    public void toggleVertexState(String vertLabel) {

        // check if the vertex exists
        if(checkIfExists2DArray(vertices, vertLabel, null))
        {
            for(int i = 0; i < vertices.length; i++) 
            {
                if(vertices[i][0].equals(vertLabel)) 
                {
                    //if the current state of the vertex is SUSCEPTIBLE (S), 
                    //then change it to INFECTED (I)
                    if(vertices[i][1].equals("S")) 
                    {
                        vertices[i][1] = "I";
                    }
                    //if the current state of the vertex is INFECTED (I), 
                    //then change it to RECOVERED (R)
                    else if(vertices[i][1].equals("I")) 
                    {
                        vertices[i][1] = "R";
                    }
                }
            }
        }
        else
        {
            System.err.println("Vertex does NOT EXIST");
        }
    } // end of toggleVertexState()

    public void deleteEdge(String srcLabel, String tarLabel) {

        //check if the edge exists
        if(checkIfExists2DArray(adjacencyMatrix, srcLabel, tarLabel))
        {
            //delete edge by setting 0
            edges(srcLabel, tarLabel, "0");
        }
        else
        {
            System.err.println("Edge does NOT EXIST");
        }
    } // end of deleteEdge()

    private void deleteVertexFromAdjMatrix(String vertLabel)
    {
        //reduce the size of the adjacency matrix by 1
        String newMatrix[][] = new String[adjacencyMatrix.length - 1][adjacencyMatrix.length - 1];

        //get the index of the vertex (vertLabel)
        int index = getPosition(vertLabel);
        int countRow = 0;
        int countCol = 0;

        //when deleteing a vertex, delete the ROW and COLUMN of the vertex 
        //from the adjacency matrix
        for(int row = 0; row < adjacencyMatrix.length; row++) 
        {
            //not include the row of the deleted vertex (vertLabel)
            if(row != index)
            {
                for(int col = 0; col < adjacencyMatrix.length; col++) 
                {
                    //not include the column of the deleted vertex (vertLabel)
                    if(col != index)
                    {
                        //store the rows and columns of the REMAINING vertices (that are not deleted)
                        newMatrix[countRow][countCol] = adjacencyMatrix[row][col];
                        countCol++;
                    }
                }
                countRow++;
                countCol = 0;
            }
        }
        //update the reference of the array
        adjacencyMatrix = newMatrix;
    } // end of deleteVertexFromAdjMatrix()

    public void deleteVertex(String vertLabel) {

        //declare the variables to be used
        String tempVertex[][] = new String[0][2];
        int counter = 0;
        
        //check if the vertex exists
        if(checkIfExists2DArray(vertices, vertLabel, null))
        {
            //a temporary array that will store all the REMAINING vertices (that are NOT deleted)
            //reduce the size of the vertices array by 1
            tempVertex = new String[vertices.length - 1][2];

            //remove the row and column of the deleted vertex
            deleteVertexFromAdjMatrix(vertLabel);

            for(int i = 0; i < vertices.length; i++) 
            {
                //if the vertex is NOT the deleted vertex
                if(!(vertices[i][0].equals(vertLabel))) 
                {
                    //store in the temporary array
                    tempVertex[counter] = vertices[i];
                    counter++;
                }
            }

            //update the reference of the array
            vertices = tempVertex;
        }
        else
        {
            System.err.println("Vertex does NOT EXIST");
        }
    } // end of deleteVertex()

    public String[] kHopNeighbours(int k, String vertLabel) {

        //declare the variables to be used
        String[] verts = new String[adjacencyMatrix.length];
        int oldCounter = 0;
        int currCounter = 0;
        int index = 0;

        //check if the vertex exists
        if(checkIfExists2DArray(vertices, vertLabel, null))
        {
            //loop depending on the number of hops (the value of k)
            for(int i = 0; i < k; i++)
            {
                //if only one hop (k==1) 
                //or if it is the first hop
                if(i == 0) 
                {
                    //get all the one-hop neighbours of vertLabel
                    index = getPosition(vertLabel);
                    for(int col = 0; col < adjacencyMatrix.length; col++) 
                    {
                        //let the index of the vertex be the row
                        //find the edges of the vertex (vertLabel)
                        //to determine the one-hop neighbours
                        if(adjacencyMatrix[index][col].equals("1"))
                        {
                            //store the neighbour to the verts array
                            verts[currCounter] = vertices[col][0];

                            //count the number of one-hop neighbours
                            currCounter++;
                        }
                    }
                }
                //if k is more than one
                //after the first hop
                else 
                {
                    //get the total number of neighbours of the previous vertex
                    int totalNumNeighbours = currCounter;

                    //for instance, KN 2 A, in the first hop, we get B and C as the one-hop neighbours of A
                    //then for the second hop, we get all the one-hop neighbours of B and C. 
                    while(oldCounter != totalNumNeighbours) 
                    {
                        //get all the one-hop neighbour of the vertex (verts[oldCounter])
                        index = getPosition(verts[oldCounter]);
                        for(int col = 0; col < adjacencyMatrix.length; col++) 
                        {
                            //let the index of the vertex be the row
                            //find the edges of the vertex (verts[oldCounter])
                            //to determine the one-hop neighbours
                            if(adjacencyMatrix[index][col].equals("1"))
                            {
                                //check if duplicates
                                if(!(checkIfExists1DArray(verts, vertices[col][0])) 
                                && !(vertices[col][0].equals(vertLabel)))
                                {
                                    //store the neighbour to the verts array
                                    verts[currCounter] = vertices[col][0];

                                    //count the number of neighbours
                                    currCounter++;
                                }
                            }
                        }
                        oldCounter++;
                    }
                }
            }
        }
        else
        {
            System.err.println("Vertex does NOT EXIST");
        }
        
        //store all the neighbours
        String[] completeVerts = new String[currCounter];

        //copy all the elements from verts (which stores all the neighbours for EACH hop)
        for(int x = 0; x < currCounter; x++) 
        {
            completeVerts[x] = verts[x];
        }

        //return all the neighbours
        return completeVerts;
    } // end of kHopNeighbours()


    public void printVertices(PrintWriter os) {

        if(vertices.length > 0)
        {
            for(int i = 0; i < vertices.length; i++) 
            {
                os.printf("(" + vertices[i][0].toString() + ", " + vertices[i][1].toString() + ") ");
            }
            os.println(); 
        }
   
    } // end of printVertices()


    public void printEdges(PrintWriter os) {

        for(int x = 0; x < adjacencyMatrix.length; x++) 
        {
            for(int y = 0; y < adjacencyMatrix.length; y++) 
            {
                if(adjacencyMatrix[x][y].equals("1"))
                {
                    os.printf(vertices[x][0].toString() + " " + vertices[y][0].toString());
                    os.printf("\n");
                }
            }
        }  
    } // end of printEdges()

} // end of class AdjacencyMatrix
