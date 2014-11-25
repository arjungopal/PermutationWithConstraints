import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/*Author Arjun Gopal Net-ID : AXG145630
 * Implementation of algorithm proposed  by Y. L. Varol and D. Rotem, “An Algorithm to generate all topological sorting arrangements”
 *  Reference : http://comjnl.oxfordjournals.org/content/24/1/83.full.pdf.
 */
public class AXG145630PermutationWithRestrictions {

	static int n = 0;
	static int v = 0;
	static int[][] incedenceArray;
	static int restrictions = 0;
	static long start_time;

	public static void main(String[] args) throws IOException {

		// Read the input. Create a valid topological sort and populate the
		// array.
		// A topological sort is an ordering of the elements which follows a
		// given conditions.
		// For example if the numbers are 1,2,3,4 and there is a restriction
		// that 2 should always comes before 1 then 2,1,3,4 is a valid
		// topological sort. 3,4,2,1 is another one.
		//
		int[] validTopSort = createTopologicalSort();
		int counter = generateAllValidPermutations(validTopSort);
		long end_time = System.currentTimeMillis();
		long execution_time = end_time - start_time;
		System.out.println(counter + " " + execution_time);

	}

	/*
	 * Method to generate all valid permutations/top sorts.
	 * 
	 * @param validTopSort : Initial valid top sort
	 */
	private static int generateAllValidPermutations(int[] validTopSort) {

		int t = n + 1;
		// create a tXt matrix which marks the constraints. if i,j is a
		// restriction then i,j and j,i location of the matrix will be 1/true.
		// for all other locations it will be 0. Also all the items in the last
		// row will be 1/true
		int[][] incidenceMatrix = new int[t][t];

		for (int i = 0; i < t; i++) {
			incidenceMatrix[i][t - 1] = 1;
		}
		// marking the constraints in the matrix with data from incedenceArray
		for (int i = 0; i < restrictions; i++) {
			int val1 = incedenceArray[i][0];
			int val2 = incedenceArray[i][1];

			incidenceMatrix[val1][val2] = 1;
			incidenceMatrix[val2][val1] = 1;
		}
		// creating array arr_p of size t and populating values from 0 to t
		// creating array arr_loc of size t and populating values from 0 to t
		int[] arr_p = new int[t];
		int[] arr_loc = new int[t];
		for (int i = 0; i < t; i++) {
			arr_p[i] = i;
			arr_loc[i] = i;
		}
		int counter = 1;
		// initialize all the variables
		int i = 0, var_k = 0, var_k1 = 0, obj_k, obj_k1;

		if (v == 1) {

			visit(validTopSort, arr_p, n);

		}
		// Algorithm is explained in detail in the project report. variables are
		// named with the same names as in the original algorithm
		while (i < t - 1) {
			var_k = arr_loc[i];
			var_k1 = var_k + 1;
			obj_k = arr_p[var_k];
			obj_k1 = arr_p[var_k1];
			// if incidenceMatrix[i][obj_k1] == 1 then this can't be swapped
			if (incidenceMatrix[i][obj_k1] == 1) {
				// reversing the array from location k
				for (int l = var_k; l >= i + 1; l--)	
					arr_p[l] = arr_p[l - 1];
				arr_p[i] = obj_k;
				arr_loc[i] = i;
				i++;
			} else {
				// swap arr_p[var_k] and arr_p[var_k1]. add var_k1 to arr_loc[i]
				arr_p[var_k] = obj_k1;
				arr_p[var_k1] = obj_k;
				arr_loc[i] = var_k1;
				// Visit the permutation and increment the counter

				// if verbose input==1 then print the permutation
				if (v == 1) {
					visit(validTopSort, arr_p, n);
				}

				counter++;
				i = 0;

			}
		}
		return counter;
	}

	/*
	 * This Method visits each permutations.
	 * @Param validTopSort : contains valid permutation of the index numbers.
	 * @Param p : contains the initial original topsort.
	 * @Param n2: number of elements to be permuted.
	 */
	private static void visit(int[] validTopSort, int[] p, int n2) {
		for (int w = 0; w < n; w++) {
			System.out.print(validTopSort[p[w]] + " ");
		}
		System.out.println();
	}

	/*--------------------------------------------------------------*/

	// Take Input and create topological sort

	/*
	 * This method reads the input from standard input and then creates a
	 * toplogical sort considering the restrictions The input that is read from
	 * the console includes int n : the number of items that is to be permuted
	 * int r : number of restrictions int v : the verbose input and then r pairs
	 * of restrictions (int,int)
	 * 
	 * the algorithm is as follows. reference
	 * http://en.wikipedia.org/wiki/Topological_sorting described by Kahn(1962)
	 */
	// L : Empty list that will contain the sorted elements
	// S : Set of all nodes with no incoming edges
	// while S is non-empty do
	// remove a node n from S
	// add n to tail of L
	// for each node m with an edge e from n to m do
	// remove edge e from the graph
	// if m has no other incoming edges then
	// insert m into S
	// if graph has edges then
	// return error (graph has at least one cycle)
	// else
	// return L (a topologically sorted order)

	public static int[] createTopologicalSort() throws IOException {
		int count = 0;
		LinkedList<Node> Vertices = new LinkedList();
		Scanner sc = new Scanner(System.in);
		// read the input
		n = sc.nextInt();
		restrictions = sc.nextInt();
		v = sc.nextInt();
		if (!(v == 0 || v == 1)) {
			throw new IOException("Invalid Input");
		}
		// create a nodeArray to store the restrictions.
		int[][] nodeArray = new int[restrictions][2];
		// Initialize a one dimensional array which can store a valid
		// topological sort
		int[] result = new int[n];
		int constraintCount = 0;
		// create all the nodes from the input n and store it in a linked list
		for (int i = 0; i < n; i++) {
			Node node = new Node(i + 1);
			Vertices.add(node);
		}
		// Now read all the restrictions and update the values in the
		// corresponding nodes.
		for (int k = 0; k < restrictions; k++) {

			int n1 = sc.nextInt();
			int n2 = sc.nextInt();
			Node node1 = Vertices.get(n1 - 1);
			Node node2 = Vertices.get(n2 - 1);
			nodeArray[constraintCount][0] = n1;
			nodeArray[constraintCount++][1] = n2;

			node1.connectionList.add(node2);
			// If there is an incoming edge, then degree has to be incremented.
			node2.degree += 1;
		}
		// starting the timer to check the execution time.
		start_time = System.currentTimeMillis();
		Iterator it_1 = Vertices.iterator();
		// Iterate through the nodes in the linked list
		while (it_1.hasNext()) {
			// get the current node
			Node node = (Node) it_1.next();
			// If the degree is 0, then there is no dependency on this node.
			if (node.degree == 0) {
				// Initialize i2 to the connection list of the current node
				Iterator it_2 = node.connectionList.iterator();
				while (it_2.hasNext()) {
					Node outNode = (Node) it_2.next();
					// we are going decrease dependency(degree) of this node by
					// one , as we are going to visit its parent node.
					outNode.degree = outNode.degree - 1;
				}
				Vertices.remove(node);
				// Initialize i1 again
				it_1 = Vertices.iterator();
				// Visit this node .. adding it to the result array
				result[count++] = node.data;
			}
		}
		if (count < n) {
			// There is a cycle in the restriction list. We can exit.
			throw new IOException(
					"There is an invalid input of the form a b and b a. Cycle in the restriction list. Program exiting.");
		}
		// Now we have to create a restriction array which is mapped to the
		// index locations.More explanations given in the report
		incedenceArray = new int[restrictions][2];
		for (int x = 0; x < restrictions; x++) {
			for (int c = 0; c < result.length; c++) {
				if (result[c] == nodeArray[x][0])
					incedenceArray[x][0] = c;
				if (result[c] == nodeArray[x][1])
					incedenceArray[x][1] = c;
			}
		}
		return result;
	}
}

/*--------------------------------------------------------------*/
// Node class to store the element and its connections.
class Node {
	int data;// the element value
	int degree;// stores the number of incoming edges to the node.
	LinkedList<Node> connectionList; // stores all the nodes to which there is a
										// connection from this node.Outgoing
										// edges.

	public Node(int data) {
		this.data = data;
		this.degree = 0;
		this.connectionList = new LinkedList<Node>();
	}

}
/*--------------------------------------------------------------*/