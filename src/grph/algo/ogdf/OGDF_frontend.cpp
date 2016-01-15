#include <ogdf/basic/Graph_d.h>
#include <ogdf/planarity/BoyerMyrvold.h>
using namespace ogdf;

int main(int numberOfArguments, char** args)
{
	if (numberOfArguments < 2)
	{
		cerr << "at least 2 arguments are required" << endl;
		exit(1);
	}

	Graph g;

	int nbOfNodes;
	cin >> nbOfNodes;
	node nodeArray[nbOfNodes]; 

	for (int i = 0; i < nbOfNodes; ++i)
	{
		int n;
		cin >> n;
		nodeArray[n] = g.newNode(n);
	}

	int nbOfEdges;
	cin >> nbOfEdges;
	edge edgeArray[nbOfEdges]; 

	for (int i = 0; i < nbOfEdges; ++i)
	{
		int v1;
		cin >> v1;
		int v2;
		cin >> v2;
		g.newEdge(nodeArray[v1], nodeArray[v2]);
	}

	for (int i = 1; i < numberOfArguments; ++i)
	{
		char* algorithm = args[i];
		
		if (strcmp(args[1], "planarity") == 0)
		{
			BoyerMyrvold BMv;
			bool result = BMv.isPlanar(g);	
			cout << (result ? "true" : "false");	
		}
		else if (strcmp(args[1], "diameter") == 0)
		{
			//ldsfjsklfjsj
			//cout << result;	
		}
		else
		{
			cerr << "unknown algorithm " << args[2] << endl;
		}
	}
}