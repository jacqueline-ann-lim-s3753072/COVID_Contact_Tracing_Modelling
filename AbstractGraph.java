import java.io.PrintWriter;

/**
 * Abstract class to allow you to implement common functionality or definitions.
 * All three graph implement classes extend this.
 *
 * Note, you should make sure to test after changes.  Note it is optional to
 * use this file.
 *
 * @author Jeffrey Chan, 2021.
 */
public abstract class AbstractGraph implements ContactsGraph
{
    /**
     * get the index (position) of the vertex
     */
    public abstract int getPosition(String vertLabel);

    /**
     * get the state (either 'S', 'I', or 'R') of the vertex
     * @param vertLabel
     * @return
     */
    public abstract String getVertexState(String vertLabel);

    /**
     * check if the element exists in the one-dimension array
     */
    public abstract boolean checkIfExists1DArray(String[] verts, String vertLabel);


    /**
     * check if the element exists in the two-dimension array
     */
    public abstract boolean checkIfExists2DArray(String array[][], String firstVert, String secondVert);

} // end of abstract class AbstractGraph
