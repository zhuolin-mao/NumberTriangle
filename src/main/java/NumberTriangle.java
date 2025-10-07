import java.io.*;

/**
 * This is the provided NumberTriangle class to be used in this coding task.
 *
 * Note: This is like a tree, but some nodes in the structure have two parents.
 *
 * The structure is shown below. Observe that the parents of e are b and c, whereas
 * d and f each only have one parent. Each row is complete and will never be missing
 * a node. So each row has one more NumberTriangle object than the row above it.
 *
 *                  a
 *                b   c
 *              d   e   f
 *            h   i   j   k
 *
 * Also note that this data structure is minimally defined and is only intended to
 * be constructed using the loadTriangle method, which you will implement
 * in this file. We have not included any code to enforce the structure noted above,
 * and you don't have to write any either.
 *
 *
 * See NumberTriangleTest.java for a few basic test cases.
 *
 * Extra: If you decide to solve the Project Euler problems (see main),
 *        feel free to add extra methods to this class. Just make sure that your
 *        code still compiles and runs so that we can run the tests on your code.
 *
 */
public class NumberTriangle {

    private int root;

    private NumberTriangle left;
    private NumberTriangle right;

    public NumberTriangle(int root) {
        this.root = root;
    }

    public void setLeft(NumberTriangle left) {
        this.left = left;
    }


    public void setRight(NumberTriangle right) {
        this.right = right;
    }

    public int getRoot() {
        return root;
    }


    /**
     * [not for credit]
     * Set the root of this NumberTriangle to be the max path sum
     * of this NumberTriangle, as defined in Project Euler problem 18.
     * After this method is called, this NumberTriangle should be a leaf.
     *
     * Hint: think recursively and use the idea of partial tracing from first year :)
     *
     * Note: a NumberTriangle contains at least one value.
     */
    public void maxSumPath() {
        // for fun [not for credit]:
    }


    public boolean isLeaf() {
        return right == null && left == null;
    }


    /**
     * Follow path through this NumberTriangle structure ('l' = left; 'r' = right) and
     * return the root value at the end of the path. An empty string will return
     * the root of the NumberTriangle.
     *
     * You can decide if you want to use a recursive or an iterative approach in your solution.
     *
     * You can assume that:
     *      the length of path is less than the height of this NumberTriangle structure.
     *      each character in the string is either 'l' or 'r'
     *
     * @param path the path to follow through this NumberTriangle
     * @return the root value at the location indicated by path
     *
     */
    public int retrieve(String path) {
        // Start from this node; an empty path returns this.root.
        NumberTriangle curr = this;

        for (int i = 0; i < path.length(); i++) {
            char ch = path.charAt(i);
            if (ch == 'l') {
                if (curr.left == null) {
                    throw new IllegalStateException("Path goes left past a leaf at index " + i);
                }
                curr = curr.left;
            } else if (ch == 'r') {
                if (curr.right == null) {
                    throw new IllegalStateException("Path goes right past a leaf at index " + i);
                }
                curr = curr.right;
            } else {
                // Defensive: if input ever contains characters other than 'l'/'r'
                throw new IllegalArgumentException("Invalid path character '" + ch + "' at index " + i);
            }
        }
        return curr.root;
    }

    /** Read in the NumberTriangle structure from a file.
     *
     * You may assume that it is a valid format with a height of at least 1,
     * so there is at least one line with a number on it to start the file.
     *
     * See resources/input_tree.txt for an example NumberTriangle format.
     *
     * @param fname the file to load the NumberTriangle structure from
     * @return the topmost NumberTriangle object in the NumberTriangle structure read from the specified file
     * @throws IOException may naturally occur if an issue reading the file occurs
     */
    public static NumberTriangle loadTriangle(String fname) throws IOException {
        // open the file and get a BufferedReader object whose methods
        // are more convenient to work with when reading the file contents.
        InputStream inputStream = NumberTriangle.class.getClassLoader().getResourceAsStream(fname);
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: " + fname);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        // We will build the triangle row-by-row.
        // prevRow holds the NumberTriangle nodes of the previous row.
        java.util.List<NumberTriangle> prevRow = null;

        // This will be the top-most node to return at the end.
        NumberTriangle top = null;

        String line = br.readLine();
        while (line != null) {
            // Trim and skip empty lines defensively.
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {

                // Split by one-or-more whitespaces; each token should be an integer.
                String[] parts = trimmed.split("\\s+");

                // Build current row nodes.
                java.util.List<NumberTriangle> currRow = new java.util.ArrayList<>(parts.length);
                for (String p : parts) {
                    int val = Integer.parseInt(p);
                    currRow.add(new NumberTriangle(val));
                }

                // Link with the previous row (if any):
                // For each node i in prevRow:
                //   prevRow[i].left  -> currRow[i]
                //   prevRow[i].right -> currRow[i + 1]
                if (prevRow != null) {
                    if (currRow.size() != prevRow.size() + 1) {
                        // Enforce the "complete rows" property described in the README.
                        throw new IllegalArgumentException(
                                "Invalid triangle format: row length " + currRow.size()
                                        + " does not equal previous row length + 1 (" + (prevRow.size() + 1) + ")."
                        );
                    }
                    for (int i = 0; i < prevRow.size(); i++) {
                        prevRow.get(i).setLeft(currRow.get(i));
                        prevRow.get(i).setRight(currRow.get(i + 1));
                    }
                } else {
                    // The very first non-empty line defines the top node.
                    if (currRow.size() != 1) {
                        throw new IllegalArgumentException(
                                "Invalid triangle format: the first row must contain exactly one number."
                        );
                    }
                    top = currRow.get(0);
                }

                // Move current row to prevRow for the next iteration.
                prevRow = currRow;
            }

            // Read next line.
            line = br.readLine();
        }

        br.close();

        if (top == null) {
            throw new IllegalArgumentException("Empty triangle: no data lines were found in " + fname);
        }
        return top;
    }

    public static void main(String[] args) throws IOException {

        NumberTriangle mt = NumberTriangle.loadTriangle("input_tree.txt");

        // [not for credit]
        // you can implement NumberTriangle's maxPathSum method if you want to try to solve
        // Problem 18 from project Euler [not for credit]
        mt.maxSumPath();
        System.out.println(mt.getRoot());
    }
}
