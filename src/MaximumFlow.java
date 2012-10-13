import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class provides to calculate the maximum flow in directed graphs using
 * the Ford-Fulkerson Algorithm.
 * 
 * @author Ruben Beyer
 */
public class MaximumFlow {

	/**
	 * Main method just for testing
	 * 
	 * @param args
	 *            will be ignored
	 */
	public static void main(String[] args) {
		// Graph 1, maximum flow is 6
		int source = 1;
		int sink = 4;
		DirectedGraph g = new DirectedGraph();
		g.addEdge(1, 2, 4);
		g.addEdge(1, 3, 2);
		g.addEdge(2, 4, 1);
		g.addEdge(2, 3, 3);
		g.addEdge(3, 4, 6);
		HashMap<Edge, Integer> flow = getMaxFlow(g, source, sink);
		System.out.println(getFlowSize(flow, g, source));

		// Graph 2, maximum flow is 31
		g = new DirectedGraph();
		source = 0;
		sink = 7;
		g.addEdge(0, 1, 38);
		g.addEdge(0, 2, 1);
		g.addEdge(0, 6, 2);
		g.addEdge(1, 2, 8);
		g.addEdge(1, 4, 13);
		g.addEdge(1, 3, 10);
		g.addEdge(2, 3, 26);
		g.addEdge(3, 6, 24);
		g.addEdge(3, 5, 8);
		g.addEdge(3, 7, 1);
		g.addEdge(4, 2, 2);
		g.addEdge(4, 7, 7);
		g.addEdge(4, 5, 1);
		g.addEdge(5, 7, 7);
		g.addEdge(6, 7, 27);
		flow = getMaxFlow(g, source, sink);
		System.out.println(getFlowSize(flow, g, source));

		// Graph 3, maximum flow is 16
		g = new DirectedGraph();
		source = 1;
		sink = 6;
		g.addEdge(1, 3, 0);
		g.addEdge(1, 2, 6);
		g.addEdge(1, 4, 5);
		g.addEdge(1, 5, 5);
		g.addEdge(2, 4, 3);
		g.addEdge(2, 5, 1);
		g.addEdge(2, 6, 3);
		g.addEdge(3, 2, 3);
		g.addEdge(3, 4, Integer.MAX_VALUE);
		g.addEdge(3, 5, 2);
		g.addEdge(3, 6, 9);
		g.addEdge(4, 3, Integer.MAX_VALUE);
		g.addEdge(4, 6, 0);
		g.addEdge(5, 4, 3);
		g.addEdge(5, 6, 4);
		flow = getMaxFlow(g, source, sink);
		System.out.println(getFlowSize(flow, g, source));

		// Graph 4, some random shit
		g = new DirectedGraph();
		source = 1;
		sink = 100;
		for (int i = 0; i < 100000; i++) {
			g.addEdge((int) (Math.random() * sink + 1),
					(int) (Math.random() * sink + 1),
					(int) (Math.random() * 500));
		}
		flow = getMaxFlow(g, source, sink);
		System.out.println(getFlowSize(flow, g, source));
	}

	/**
	 * This method actually calculates the maximum flow by using the
	 * Ford-Fulkerson Algorithm.
	 * 
	 * @param g
	 *            The directed graph
	 * @param source
	 *            The object identifying the source node of the flow
	 * @param sink
	 *            The object identifying the sink node of the flow
	 * @return A HashMap for the edges, giving every edge in the graph a value
	 *         which shows the part of the edge's capacity that is used by the
	 *         flow
	 */
	static HashMap<Edge, Integer> getMaxFlow(DirectedGraph g, Object source,
			Object sink) {
		// The path from source to sink that is found in each iteration
		LinkedList<Edge> path;
		// The flow, i.e. the capacity of each edge that is actually used
		HashMap<Edge, Integer> flow = new HashMap<Edge, Integer>();
		// Create initial empty flow.
		for (Edge e : g.getEdges()) {
			flow.put(e, 0);
		}

		// The Algorithm itself
		while ((path = bfs(g, source, sink, flow)) != null) {
			// Activating this output will illustrate how the algorithm works
			// System.out.println(path);
			// Find out the flow that can be sent on the found path.
			int minCapacity = Integer.MAX_VALUE;
			Object lastNode = source;
			for (Edge edge : path) {
				int c;
				// Although the edges are directed they can be used in both
				// directions if the capacity is partially used, so this if
				// statement is necessary to find out the edge's actual
				// direction.
				if (edge.getStart() == lastNode) {
					c = edge.getCapacity() - flow.get(edge);
					lastNode = edge.getTarget();
				} else {
					c = flow.get(edge);
					lastNode = edge.getStart();
				}
				if (c < minCapacity) {
					minCapacity = c;
				}
			}

			// Change flow of all edges of the path by the value calculated
			// above.
			lastNode = source;
			for (Edge edge : path) {
				// If statement like above
				if (edge.getStart() == lastNode) {
					flow.put(edge, flow.get(edge) + minCapacity);
					lastNode = edge.getTarget();
				} else {
					flow.put(edge, flow.get(edge) - minCapacity);
					lastNode = edge.getStart();
				}
			}
		}
		return flow;
	}

	/**
	 * This method gives the actual flow value by adding all flow values of the
	 * out leading edges of the source.
	 * 
	 * @param flow
	 *            A HashMap of the form like getMaxFlow produces them
	 * @param g
	 *            The directed Graph
	 * @param source
	 *            The object identifying the source node of the flow
	 * @return The value of the given flow
	 */
	static int getFlowSize(HashMap<Edge, Integer> flow, DirectedGraph g,
			Object source) {
		int maximumFlow = 0;
		Node sourceNode = g.getNode(source);
		for (int i = 0; i < sourceNode.getOutLeadingOrder(); i++) {
			maximumFlow += flow.get(sourceNode.getEdge(i));
		}
		return maximumFlow;
	}

	/**
	 * Simple breadth first search in the directed graph
	 * 
	 * @param g
	 *            The directed Graph
	 * @param start
	 *            The object that identifying the start node of the search
	 * @param target
	 *            The object that identifying the target node of the search
	 * @param flow
	 *            A HashMap of the form like getMaxFlow produces them. If an
	 *            edge has a value > 0 in it, it will also be used in the
	 *            opposite direction. Also edges that have a value equal to its
	 *            capacity will be ignored.
	 * @return A list of all edges of the found path in the order in which they
	 *         are used, null if there is no path. If the start node equals the
	 *         target node, an empty list is returned.
	 */
	static LinkedList<Edge> bfs(DirectedGraph g, Object start, Object target,
			HashMap<Edge, Integer> flow) {
		// The edge by which a node was reached.
		HashMap<Object, Edge> parent = new HashMap<Object, Edge>();
		// All outer nodes of the current search iteration.
		LinkedList<Object> fringe = new LinkedList<Object>();
		// We need to put the start node into those two.
		parent.put(start, null);
		fringe.add(start);
		// The actual algorithm
		all: while (!fringe.isEmpty()) {
			// This variable is needed to prevent the JVM from having a
			// concurrent modification
			LinkedList<Object> newFringe = new LinkedList<Object>();
			// Iterate through all nodes in the fringe.
			for (Object nodeID : fringe) {
				Node node = g.getNode(nodeID);
				// Iterate through all the edges of the node.
				for (int i = 0; i < node.getOutLeadingOrder(); i++) {
					Edge e = node.getEdge(i);
					// Only add the node if the flow can be changed in an out
					// leading direction. Also break, if the target is reached.
					if (e.getStart() == nodeID
							&& !parent.containsKey(e.getTarget())
							&& flow.get(e) < e.getCapacity()) {
						parent.put(e.getTarget(), e);
						if (e.getTarget() == target) {
							break all;
						}
						newFringe.add(e.getTarget());
					} else if (e.getTarget() == nodeID
							&& !parent.containsKey(e.getStart())
							&& flow.get(e) > 0) {
						parent.put(e.getStart(), e);
						if (e.getStart() == target) {
							break all;
						}
						newFringe.add(e.getStart());
					}
				}
			}
			// Replace the fringe by the new one.
			fringe = newFringe;
		}

		// Return null, if no path was found.
		if (fringe.isEmpty()) {
			return null;
		}
		// If a path was found, reconstruct it.
		Object node = target;
		LinkedList<Edge> path = new LinkedList<Edge>();
		while (node != start) {
			Edge e = parent.get(node);
			path.addFirst(e);
			if (e.getStart() == node) {
				node = e.getTarget();
			} else {
				node = e.getStart();
			}
		}

		// Return the path.
		return path;
	}
}
