/**
 * The edge class that is used for DirectedGraph
 * 
 * @author Ruben Beyer
 */
public class Edge {

	private final Object target;
	private final Object start;
	private final int capacity;

	Edge(Object start, Object target, int capacity) {
		this.capacity = capacity;
		this.target = target;
		this.start = start;
	}

	Object getTarget() {
		return target;
	}

	Object getStart() {
		return start;
	}

	int getCapacity() {
		return capacity;
	}

	@Override
	public String toString() {
		return this.start + "->" + this.target + "(" + this.capacity + ")";
	}
}
