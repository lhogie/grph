package grph.algo.msp;

public class EdgeProperty implements Comparable<EdgeProperty>{

	private final int id;
	private final int weight;

	public EdgeProperty(int i, int w) {
		id = i;
		weight = w;
	}

	public int getId() {
		return id;
	}

	public int getWeight() {
		return weight;
	}

	public String toString() {
		return "id : " + id + ", weight : " + weight + "\n";
	}

	@Override
	public int compareTo(EdgeProperty o) {
		return Integer.compare(weight, o.weight);
		
	}

}
