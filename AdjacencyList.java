import java.io.PrintWriter;

/**
 * Adjacency list implementation for the AssociationGraph interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 */
public class AdjacencyList extends AbstractGraph
{

    /**
	 * Contructs empty graph.
	 */

    private String vertices[][];
    private LinkedList adjacencyList[];

    public AdjacencyList() {

        vertices = new String[0][2];
        adjacencyList = new LinkedList[0];
        
    } // end of AdjacencyList()

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
    } // end of getVertexState()

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
        return exists;
    } // end of checkIfExists2DArray()

    public void addVertex(String vertLabel) {

        //to increase the size of vertices array by 1  
        String tempVert[][] = new String[vertices.length + 1][2];

        //to increase the size of adjacency list (array of linkedlist) by 1
        LinkedList tempList[] = new LinkedList[adjacencyList.length + 1];

        //copy all existing values from vertices to tempVert
        for(int i = 0; i < vertices.length; i++) 
        {
            tempVert[i] = vertices[i];
        }

        //copy all existing values from adjacencyList to tempList
        for(int i = 0; i < adjacencyList.length; i++) 
        {
            tempList[i] = adjacencyList[i];
        }

        //add the new vertex if it does NOT exist yet
        if(!(checkIfExists2DArray(vertices, vertLabel, null))) 
        {
            //add the new vertex to the vertices array
            tempVert[vertices.length][0] = vertLabel;
            tempVert[vertices.length][1] = "S";

            //add a new linkedlist to the adjacency list for the new vertex
            tempList[adjacencyList.length] = new LinkedList();

            //update references of the arrays
            vertices = tempVert; 
            adjacencyList = tempList;
        }
    } // end of addVertex()

    public void addEdge(String srcLabel, String tarLabel) {

        //get the index (position) of source vertex in the vertices array
        int srcPos = getPosition(srcLabel);
        //get the index (position) of target vertex in the vertices array
        int tarPos = getPosition(tarLabel);

        //if the index (position) is -1, then the vertex does NOT exist
        //check if BOTH vertices (srcLabel and tarLabel) exist
        //a vertex CANNOT have an edge to itself (srcPos != tarPos)
        if(!(srcLabel.equals(tarLabel)) && srcPos != -1 && tarPos != -1)
        {
            // *SEE addNode() inside the private Linkedlist class at the end*
            //add the target vertex to the source vertex's linkedlist
            adjacencyList[srcPos].addNode(tarLabel);
            //add the source vertex to the target vertex's linkedlist
            adjacencyList[tarPos].addNode(srcLabel);
        }
        else
        {
            System.err.println("One of the vertices does NOT exist.");
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
        
        //get the index (position) of source vertex in the vertices array
        int srcPos = getPosition(srcLabel);
        //get the index (position) of target vertex in the vertices array
        int tarPos = getPosition(tarLabel);

        //if the index (position) is -1, then the vertex does NOT exist
        //check if BOTH vertices (srcLabel and tarLabel) exist
        //a vertex CANNOT have an edge to itself (srcPos != tarPos)
        if(!(srcLabel.equals(tarLabel)) && srcPos != -1 && tarPos != -1)
        {   
            //if the target vertex exist in the source vertex's linkedlist,
            //or vice versa, then this means that the edge exists
            //
            // *SEE removeNode() inside the private Linkedlist class at the end*
            //removeNode() will return true when the vertex (node) has been removed
            if(adjacencyList[srcPos].removeNode(tarLabel)) 
            {
                //remove the vertices (source vertex and target vertex)
                //in the adjacency list when deleting an edge
                adjacencyList[tarPos].removeNode(srcLabel);
            }
            else
            {
                System.err.println("Edge does NOT EXIST");
            }   
        }
        else 
        {
            System.err.println("Edge does NOT EXIST");
        }
    } // end of deleteEdge() 


    public void deleteVertex(String vertLabel) {

        //declare the variables to be used
        String tempVert[][] = new String[0][2];
        LinkedList tempList[] = new LinkedList[0];
        int counter = 0;

        //check if the vertex exists
        if(checkIfExists2DArray(vertices, vertLabel, null))
        {
            if(vertices.length > 1) {

                //decrease the size of vertices array by 1
                tempVert = new String[vertices.length - 1][2];

                //decrease the size of adjacency list by 1
                tempList = new LinkedList[adjacencyList.length - 1];

                //get the index of the vertex to be removed
                int vertPos = getPosition(vertLabel);

                for(int i = 0; i < adjacencyList.length; i++) 
                {
                    //do NOT include the vertex and its linkedlist
                    if(i != vertPos) 
                    {
                        //store all the remaining vertices
                        tempVert[counter] = vertices[i];

                        //store all the remaining linkedlist
                        tempList[counter] = adjacencyList[i];

                        //remove the deleted vertex from the others' linkedlists
                        tempList[counter].removeNode(vertLabel);
                        counter++;
                    }
                }
            }

            //update the references of the arrays
            vertices = tempVert;
            adjacencyList = tempList;
        }
        else {
            System.err.println("Vertex does NOT EXIST");
        }
    } // end of deleteVertex()

    
    public String[] kHopNeighbours(int k, String vertLabel) {
        
        //declare the variables to be used
        String[] verts = new String[0];
        String[] oneHopNeighbours;
        int oldCounter = 0;
        int currCounter = 0;
        int vertPos = -1;
        int length = 0;

        //check if the vertex exists
        if(checkIfExists2DArray(vertices, vertLabel, null)) 
        {
            //get the index (position) of the vertex (vertLabel)
            vertPos = getPosition(vertLabel);

            //this array will store all the neighbours
            verts = new String[adjacencyList[vertPos].getLength()];

            if(verts.length > 0) 
            {
                for(int i = 0; i < k; i++) 
                {
                    //System.out.println(i);
                    if(i == 0)
                    {
                        //get all the one-hop neighbours of vertLabel
                        //by getting the vertices in the vertex's linkedlist
                        oneHopNeighbours = adjacencyList[vertPos].print();
                        for(int x = 0; x < verts.length; x++) 
                        {
                            verts[x] = oneHopNeighbours[x];
                            length++;
                        }
                    }

                    else {
                        int loops = verts.length;
                        
                        while(oldCounter != loops) 
                        {
                            currCounter = 0;

                            //get index (position) of the vertex (vert[oldCounter])
                            vertPos = getPosition(verts[oldCounter]);
                            
                            if(vertPos != -1) {
                                
                                //check how many new elements needs adding to newVerts
                                int val = 0; 
                                
                                //get all the one-hop neighbours of the vertex (vert[oldCounter])
                                //by getting the vertices in the vertex's linkedlist
                                oneHopNeighbours = adjacencyList[vertPos].print();

                                //increase val for all valid neighbours
                                for(int x = 0; x < oneHopNeighbours.length; x++) 
                                {
                                    //check if duplicates
                                    if(!(checkIfExists1DArray(verts, oneHopNeighbours[x])) 
                                    && !(oneHopNeighbours[x].equals(vertLabel)))
                                    {
                                        val++;
                                    }
                                }

                                //create array of the current length + new elements length
                                String[] newVerts = new String[length + val];
                                
                                //copy all the existing elements from verts to newVerts
                                for(int x = 0; x < verts.length; x++) 
                                {
                                    if(verts[x] != null) 
                                    {
                                        newVerts[x] = verts[x];
                                        currCounter++;
                                    }
                                }

                                //newVerts will store all the neighbours of the vertex (vert[oldCounter])
                                for(int x = 0; x < oneHopNeighbours.length; x++) 
                                {
                                    //check if duplicates
                                    if(!(checkIfExists1DArray(verts, oneHopNeighbours[x])) 
                                    && !(oneHopNeighbours[x].equals(vertLabel)))
                                    {
                                        newVerts[currCounter] = oneHopNeighbours[x];
                                        currCounter++;
                                        length++;
                                    }
                                }
                                verts = newVerts; 
                            }
                            oldCounter++;
                            
                        }
                    }
                }
            }
            
        }
        else
        {
            System.err.println("Vertex does NOT EXIST");
        }        
        
        //store all the neighbours
        String[] completeVerts = new String[length];
        currCounter = 0;

        //copy all the elements from verts (which stores all the neighbours for EACH hop)
        for(int x = 0; x < completeVerts.length; x++) 
        {
            if(verts[x] != null) 
            {
                completeVerts[currCounter] = verts[x];
                currCounter++;
            }
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

        for(int i = 0; i < adjacencyList.length; i++)
        {
            String[] values = adjacencyList[i].print();
            for(int x = 0; x < values.length; x++) 
            {
                os.println(vertices[i][0] + " " + values[x]);
            }
        }
    } // end of printEdges()

    /**
     * Node class for LinkedList Implementation
     */
    private class Node
    {
        protected String nodeValue;
        protected Node nextNode;

        public Node(String value) {
            nodeValue = value;
            nextNode = null;
        }

        public String getValue() {
            return nodeValue;
        }

        public Node getNext() {
            return nextNode;
        }

        public void setNext(Node next) {
            nextNode = next;
        }
    } // end of inner class Node

    /**
     * LinkedList class for Adjacency List Implementation
     */
    private class LinkedList
    {
        private Node headNode;
        private int nodeLength;

        LinkedList() 
        {
            headNode = null;
            nodeLength = 0;
        }   

        public boolean addNode(String vert) 
        {
            Node newNode = new Node(vert);
            boolean check = false;
            Node currNode = null;
            
            if (headNode == null) 
            {
                headNode = newNode;
            }
            else 
            {
                currNode = headNode;
                for(int i = 0; i < nodeLength; i++) 
                {
                    if(currNode.getValue().equals(vert))
                    {
                        check = true;
                    }
                    else 
                    {
                        currNode = currNode.getNext();
                    }
                }
                if(check == false) 
                {
                    newNode.setNext(headNode);
                    headNode = newNode;
                }
                
            }
            if(check == false) 
            {
                nodeLength++;
            }
            return check;
        }

        public boolean removeNode(String value) 
        {
            Node currNode = headNode;
            Node prevNode = null;
            boolean check = false;
            
            for(int i = 0; i < nodeLength && currNode != null; i++)
            {   
                if(currNode.getValue().equals(value))
                {
                    if(prevNode != null)
                    {
                        prevNode.setNext(currNode.getNext());
                    }
                    else
                    {
                        headNode = currNode.getNext();
                    }          
                    currNode = null;
                    nodeLength--;
                    check = true;
                }
                else 
                {
                    prevNode = currNode;
                    currNode = currNode.getNext();
                }   
            }
            return check;
        }

        public String[] print() 
        {
            Node currNode = headNode;
            String[] returnString = new String[nodeLength];
            int counter = 0;

            while (currNode != null) {
                returnString[counter] = currNode.getValue();
                currNode = currNode.getNext();
                counter++;
            }

            return returnString;
        }

        public int getLength() 
        {
            return nodeLength;
        }

    } // end of inner class LinkedList

} // end of class AdjacencyList
