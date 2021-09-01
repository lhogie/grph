package grph;

import grph.oo.ObjectGrph;

class Test {

	@org.junit.jupiter.api.Test
	void test() {

	}

	public static void testRemove(ObjectGrph<String, String> g) {
		// TEST !! TODO WARNING

		for (String edge : g.getEdges()) {
			String ccS = g.getDirectedSimpleEdgeTail(edge);
			if (ccS == null) {
				System.out.println("PROBLEM !");
			}
		}
	}

	public static void main(String[] args) {
		ObjectGrph<String, String> g = new ObjectGrph();
		g.addVertex("jeremy");
		g.addVertex("julien");
		g.addVertex("luc");
		g.addUndirectedSimpleEdge("julien", "kicks", "luc");
		g.addUndirectedSimpleEdge("luc", "knows", "jvm");
		g.removeVertex("jeremy");
		g.addVertex("toto");
		testRemove(g);
		System.out.println(g.getIncidentEdges("luc"));
		testRemove(g);
		g.removeEdge("knows");
		testRemove(g);
		System.out.println(g.getIncidentEdges("luc"));
		testRemove(g);
	}

}
