import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class is represents a directed graph for the maximum flow calculation
 * 
 * @author Ruben Beyer
 */
public class DirectedGraph {
	private HashMap<Object, Node> nodes = new HashMap<Object, Node>();
	private LinkedList<Edge> edges = new LinkedList<Edge>();

	/**
	 * Use this method to build the graph. It will add an edge to the graph and
	 * also its nodes, if necessary. The node identifiers can be any object. Two
	 * objects identify the same node, if they are equal according to their
	 * equals function.
	 * 
	 * @param startNodeID
	 *            Identifier object of the start node of the edge
	 * @param endNodeID
	 *            Identifier object of the end node of the edge
	 * @param capacity
	 *            Capacity of the edge
	 */
	void addEdge(Object startNodeID, Object endNodeID, int capacity) {
		Node startNode;
		Node endNode;
		if (!this.nodes.containsKey(startNodeID)) {
			startNode = new Node();
			this.nodes.put(startNodeID, startNode);
		} else {
			startNode = this.nodes.get(startNodeID);
		}
		if (!this.nodes.containsKey(endNodeID)) {
			endNode = new Node();
			this.nodes.put(endNodeID, endNode);
		} else {
			endNode = this.nodes.get(endNodeID);
		}
		Edge edge = new Edge(startNodeID, endNodeID, capacity);
		startNode.addEdge(edge);
		endNode.addEdge(edge);
		this.edges.add(edge);
	}

	Node getNode(Object nodeID) {
		return this.nodes.get(nodeID);
	}

	LinkedList<Edge> getEdges() {
		return this.edges;
	}
}
