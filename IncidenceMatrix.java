import java.io.PrintWriter;

/**
 * Incidence matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class IncidenceMatrix extends AbstractGraph
{

    private String vertices[][];
    private String incidenceMatrix[][];

	/**
	 * Contructs empty graph.
	 */
    public IncidenceMatrix() {

        incidenceMatrix = new String[0][0];
        vertices = new String[0][2];
    } // end of IncidenceMatrix()

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

        if(secondVert == null){
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
        else{
            int srcPos = getPosition(firstVert);
            int tarPos = getPosition(secondVert);

            //check if source vertex and target vertex of the edge exist
            if(srcPos != -1 && tarPos != -1 && srcPos != tarPos) 
            {
                if(incidenceMatrix.length > 0) 
                {
                    for(int x = 0; x < incidenceMatrix[0].length; x++) 
                    {
                        //check if the edge exists
                        if(incidenceMatrix[srcPos][x].equals("1") 
                        && incidenceMatrix[tarPos][x].equals("1")) 
                        {
                            exists = true;
                        }
                    }
                }
            }
        }
        return exists;
    } // end of checkIfExists2DArray()

    private void addRowForNewVertex()
    {
        //declare the variable to be used
        String tempMatrix[][] = new String[0][0];

        //check if there are edges (column) exist in the incidence matrix
        if(incidenceMatrix.length > 0) 
        {
            //for incidence matrix, the row are the vertices,
            //while the columns are the edges
            //a temporary matrix is used to update the matrix
            tempMatrix = new String[vertices.length][incidenceMatrix[0].length];
            {
                //copy all the existing rows and columns from the incidence matrix to tempMatrix
                for(int row = 0; row < incidenceMatrix.length; row++) 
                {
                    for(int col = 0; col < incidenceMatrix[0].length; col++) 
                    {
                        tempMatrix[row][col] = incidenceMatrix[row][col];
                    }
                }

                //when a vertex is added, add a new row to every column (edges) 
                for(int col = 0; col < incidenceMatrix[0].length; col++) 
                {
                    //set to 0 since the newly added vertex does NOT have an edge yet
                    tempMatrix[vertices.length - 1][col] = "0";
                }
            }
            incidenceMatrix = tempMatrix;
        }
    } //end of addRowForNewVertex()

    public void addVertex(String vertLabel) {
        
        //increase the size of the vertices array by 1
        String newVertices[][] = new String[vertices.length + 1][2];

        //add the new vertex if it does NOT exist yet
        if(!(checkIfExists2DArray(vertices, vertLabel, null))) 
        {
            //copy all existing values from vertices to newVertices
            for(int x = 0; x < vertices.length; x++) 
            {
                newVertices[x] = vertices[x];
            }

            //add the new vertex
            newVertices[vertices.length][0] = vertLabel;
            newVertices[vertices.length][1] = "S";

            //update the reference of the array
            vertices = newVertices; 

            //update the incidence matrix
            addRowForNewVertex();
        }
    } // end of addVertex()

    public void addEdge(String srcLabel, String tarLabel) {
        
        //declare the variables to be used
        int colLength = 1;
        //get the index (pos) of the source vertex (srcLabel)
        int srcPos = getPosition(srcLabel);
        //get the index (pos) of the target vertex (tarLabel)
        int tarPos = getPosition(tarLabel);

        //if there are edges exist
        if(incidenceMatrix.length > 0) 
        {
            //let the column length be the number of existing edges
            colLength = incidenceMatrix[0].length + 1;
        }

        //if the index (position) is -1, then the vertex does NOT exist
        //check if BOTH vertices (srcLabel and tarLabel) exist
        //a vertex CANNOT have an edge to itself (srcPos != tarPos)
        if(srcPos != -1 && tarPos != -1 && srcPos != tarPos) 
        {
            //add the new edge if it does NOT exist yet
            if(!(checkIfExists2DArray(incidenceMatrix, srcLabel, tarLabel))) 
            {
                //add a COLUMN when adding an edge to the incidence matrix
                String tempMatrix[][] = new String[vertices.length][colLength];

                if(incidenceMatrix.length > 0) 
                {
                    //copy all the existing values from incidence matrix to tempMatrix
                    for(int row = 0; row < incidenceMatrix.length; row++)
                    {
                        for(int col = 0; col < colLength - 1; col++)  
                        {
                            tempMatrix[row][col] = incidenceMatrix[row][col];
                        }
                    }
                }
                
                //create a column with 0's for the newly added edge
                for(int row = 0; row < tempMatrix.length; row++) 
                {
                    tempMatrix[row][colLength - 1] = "0";
                }

                //set the source vertex and target vertex to 1
                tempMatrix[tarPos][colLength - 1] = "1";
                tempMatrix[srcPos][colLength - 1] = "1";

                //update the reference of the array
                incidenceMatrix = tempMatrix;
            }
        }
        else
        {
            System.err.println("One of the vertices does NOT EXIST");
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
        if(checkIfExists2DArray(incidenceMatrix, srcLabel, tarLabel))
        {   
            //when deleting an edge from the incidence matrix, 
            //remove the column of the edge
            //the length of the column will be reduced to 1
            String[][] tempMatrix = new String[vertices.length][incidenceMatrix[0].length - 1];

            //get the index (pos) of the source vertex (srcLabel)
            int srcPos = getPosition(srcLabel);

            //get the index (pos) of the target vertex (tarLabel)
            int tarPos = getPosition(tarLabel);

            int newCol = 0;
            for(int col = 0; col < incidenceMatrix[0].length; col++) 
            {
                //do NOT include the column of the deleted edge 
                //to the updated matrix (tempMatrix)
                if(!(incidenceMatrix[srcPos][col].equals("1") 
                && incidenceMatrix[tarPos][col].equals("1"))) 
                {
                    for(int row = 0; row < incidenceMatrix.length; row++) 
                    {
                        //store the REMANING edges (columns) in the temporary matrix
                        tempMatrix[row][newCol] = incidenceMatrix[row][col];
                    }
                    newCol++;
                }
            }
            
            //update the reference of the array
            incidenceMatrix = tempMatrix;

            //if all the columns are deleted (no edges left)
            if(newCol == 0) 
            {
                //then the size of the incidence matrix is ZERO
                incidenceMatrix = new String[0][0];
            }
        }
        else
        {
            System.err.println("Edge does NOT EXIST");
        }
    } // end of deleteEdge()

    private int getVertLabelEdgesCount(String vertLabel, int vertPos)
    {
        //when deleting a vertex in the incidence matrix
        //this method is used to count the number of edges (columns)
        //that are associated with the deleted vertex
        int totalDeletedEdges = 0;

        for(int col = 0; col < incidenceMatrix[0].length; col++) 
        {
            //let verPos be the index of the deleted vertex
            if(incidenceMatrix[vertPos][col].equals("1")) 
            {
                totalDeletedEdges++;
            }
        }
        //return the total number of edges to be deleted
        return totalDeletedEdges;
    }// end of getVertLabelEdgesCount()

    private void deleteEdgesOfDeletedVert(String vertLabel)
    {
        String tempMatrix[][] = null;
        int vertPos = getPosition(vertLabel);
        int totalDeletedEdges = getVertLabelEdgesCount(vertLabel, vertPos);

        if(incidenceMatrix[0].length - totalDeletedEdges >= 0) 
        {
            //if there will still be REMAINING edges 
            //after deleting all the edges associated with the deleted vertex
            //then update the incidence matrix
            if(incidenceMatrix[0].length - totalDeletedEdges > 0)
            {
                //a temporary array will store all the REMAINING vertices (rows) and edges (columns)
                //reduce the number of row by 1 (delete a vertex)
                //reduce the number of columns depending on the number of edges to be deleted
                tempMatrix = new String[vertices.length - 1][incidenceMatrix[0].length - totalDeletedEdges];
                int newRow = 0;
                int newCol = 0;

                for(int row = 0; row < incidenceMatrix.length; row++) 
                {
                    //do NOT include the row of the deleted vertex
                    if(!(row == vertPos)) 
                    {
                        for(int col = 0; col < incidenceMatrix[0].length; col++) 
                        {
                            //do NOT include the edges (columns) 
                            //that are associated with the deleted vertex
                            if(!(incidenceMatrix[vertPos][col].equals("1"))) 
                            {
                                //store all the REMAINING vertices and edges
                                tempMatrix[newRow][newCol] = incidenceMatrix[row][col];
                                newCol++;
                            } 
                        }
                        newRow++;
                        newCol = 0;
                    }
                }
            }
            //if all the edges are associated with the deleted vertex
            else if(incidenceMatrix[0].length - totalDeletedEdges == 0 && totalDeletedEdges != 0) 
            {
                // means we deleted everything
                tempMatrix = new String[0][0]; 
            }
            //update the incidence matrix
            //update the reference of the array
            incidenceMatrix = tempMatrix;
        }
    } //end of deletedEdgesOfDeletedVert()

    public void deleteVertex(String vertLabel) {

        // check if the vertex exists
        if(checkIfExists2DArray(vertices, vertLabel, null)) 
        {
            //allocate room ready to delete 1 element (vertex)
            String tempVertices[][] = new String[vertices.length - 1][2];

            
            int col = 0;
            for(int x = 0; x < vertices.length; x++) 
            {
                // add all elements except the vertex (vertLabel) we plan to remove
                if(!(vertices[x][0].equals(vertLabel))) 
                {
                    tempVertices[col] = vertices[x];
                    col++;
                }
            }
            
            //if there are edges exist in the incidence matrix
            if(incidenceMatrix.length > 0)
            {
                //delete all the edges associated with the vertex
                //delete the row of the vertex as well
                deleteEdgesOfDeletedVert(vertLabel);
            }

            //update the reference of the array
            vertices = tempVertices;
        }
        else
        {
            System.err.println("Vertex does NOT EXIST");
        }
    } // end of deleteVertex()

    private String getNeighbour(int vertPos, int col){

        String neighbour = null;

        for(int row = 0; row < vertices.length; row++)
        {
            //let vertPos be the index of the selected vertex
            if(vertPos != row && incidenceMatrix[row][col].equals("1"))
            {
                //get the neighbour of the selected vertex
                neighbour = vertices[row][0];
            }
        }

        //return the neighbour
        return neighbour;
    } //end of getNeighbour()

    public String[] kHopNeighbours(int k, String vertLabel) {
        
        //declare the variables to be used
        String[] verts = new String[incidenceMatrix.length];
        int oldCounter = 0;
        int currCounter = 0;
        int index = 0;

        //check if the vertex exists
        if(checkIfExists2DArray(vertices, vertLabel, null))
        {
            //loop depending on the number of hops (the value of k)
            for(int i = 0; i < k && vertices.length > 0; i++)
            {
                //if only one hop (k==1) 
                //or if it is the first hop
                if(i == 0) 
                {
                    //get all the one-hop neighbours of vertLabel
                    index = getPosition(vertLabel);
                    for(int col = 0; col < incidenceMatrix[0].length; col++) 
                    {
                        //let the index of the vertex be the row
                        //find the edges of the vertex (vertLabel)
                        //to determine the one-hop neighbours
                        if(incidenceMatrix[index][col].equals("1"))
                        {
                            String neighbour = getNeighbour(index, col);

                            //store the neighbour to the verts array
                            verts[currCounter] = neighbour;

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
                        for(int col = 0; col < incidenceMatrix[0].length; col++) 
                        {
                            //let the index of the vertex be the row
                            //find the edges of the vertex (verts[oldCounter])
                            //to determine the one-hop neighbours
                            if(incidenceMatrix[index][col].equals("1"))
                            {
                                String neighbour = getNeighbour(index, col);

                                //check if duplicates
                                if (!(checkIfExists1DArray(verts, neighbour)) 
                                && !(neighbour.equals(vertLabel)))
                                {
                                    //store the neighbour to the verts array
                                    verts[currCounter] = neighbour;

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
        for(int x = 0; x < currCounter; x++) {
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

        if(incidenceMatrix.length > 0)
        {
            for(int col = 0; col < incidenceMatrix[0].length; col++) 
            {
                for(int row = 0; row < vertices.length; row++)
                {
                    if(incidenceMatrix[row][col].equals("1"))
                    {
                        os.printf(vertices[row][0].toString() + " ");
                    }
                }
                os.printf("\n");

                for(int row = vertices.length - 1; row > -1; row--)
                {
                    if(incidenceMatrix[row][col].equals("1"))
                    {
                        os.printf(vertices[row][0].toString() + " ");
                    }
                }
                os.printf("\n");
            }
        }
    } // end of printEdges()

} // end of class IncidenceMatrix
