#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<search.h>

/* Build the Recursive MATrix. 
 * a, b, c and graph size 2^S and number of edges E are inputs; 
 * outputs adjacency matrix.
 * Graph is square.
 *
 *       -------------
 *       |     |     |
 *       |  a  |  b  |
 *       |     |     |
 *       |-----|-----|
 *       |     |     |
 *       |  c  |  d  |
 *       |     |     |
 *       -------------
 *
 */

/* Typically, the Riedi effect is over the range [-0.1, 0.1]. The
 * RIEDI_EFFECT parameter is multiplied to this range.
 */
#define RIEDI_EFFECT	1.0  // Set to zero when checking for estimated dia.

// Do we need to get the marginal rows/cols?
#define SHOW_MARGINALS	0

// Do we want to generate a weighted graph?
#define DO_WEIGHTED	1

char *progname;
FILE *fp_marginal_rows, *fp_marginal_cols, *fp_edges_biggest_component, *fp_edges;

struct vertex{
	double x,y;
	int weight;
	struct vertex *next, *prev, *up, *down;
	short int visited;
};

struct vertex TmpVertex;
void *rootrows, *rootcols;
//double FirstY=-1.0;

// Forward function defs
int mycompare_x(const void *pa, const void *pb);
int mycompare_y(const void *pa, const void *pb);
void action_rows(const void *nodep, const VISIT which, const int depth);
void action_cols(const void *nodep, const VISIT which, const int depth);
void action_weighted_rows(const void *nodep, const VISIT which, const int depth);


// Yiping's version of the Riedi effect.
double getrand(){
  return (float)rand() / (float)RAND_MAX * RIEDI_EFFECT + (1 - RIEDI_EFFECT * 0.5);
}

void normalize(double *a,double  *b,double  *c, double *d){
  double sum = *a + *b + *c + *d;
  *a /= sum;
  *b /= sum;
  *c /= sum;
  *d /= sum;
}

//changeabcd first scales each one of a, b, c and d with a random factor
//(when RIEDI_EFFECT=1, the scaling factor is random in [0.5, 1.5].)
//then the values of a, b, c and d are modified(normalized) so that their
//sum is 1
void changeabcd(double *a,double  *b,double  *c, double *d){
  *a *= getrand();
  *b *= getrand();
  *c *= getrand();
  *d *= getrand();
  normalize(a, b, c, d);
}


main(int argc, char **argv){
int i, j, S, S2, maxS, E;
struct vertex *M, *tmp_vertex, *tmp2_vertex, *new_vertex, *prev_vertex;
float a, b, c, d, rnd;
int length_along_path;
double x,y,len,len2,numnodes,numnodes2;
int repeats, size_connected;
char *path_taken;
ENTRY hash_entry, *tmp_entry;
struct vertex **tree_result;
double *riedi_add_a, *riedi_add_b, *riedi_add_c, dummy;

int counts_a,counts_b,counts_c;

// srand(10);  //-> Used this value when checking for estimated diameter

	progname = argv[0];
	
	if(argc!=6 && argc!=7)usage();

		// The probabilities
	a = atof(argv[1]);
	b = atof(argv[2]);
	c = atof(argv[3]);
	d = 1 - a - b - c;
	if(a<0 || a> 1 || b < 0 || b > 1 || c < 0 || c > 1 || c < 0 || c > 1)
		usage();

		// log_{2}(Side of the matrix).
	S = atoi(argv[4]);
	if(S < 0)usage();
		
	if(argc==7){
		S2 = atoi(argv[5]);
		if(S2 < 0)usage();
		argv++;
	}else S2 = S;

		
		// E is the number of edges; that is, the number of elements
		// to fill into the matrix
	E = atoi(argv[5]);
	
	if(E < 0 || (log(E)/log(2) > S+S2 && !DO_WEIGHTED))usage();

		// path_taken stores the path that a data element takes 
		// to its final position in the matrix
	path_taken = (char*)malloc(S * sizeof(char));
	
	rootrows = rootcols = NULL;

	maxS = (S>S2)?S:S2;
	riedi_add_a = (double *)malloc(maxS*sizeof(double));
	riedi_add_b = (double *)malloc(maxS*sizeof(double));
	riedi_add_c = (double *)malloc(maxS*sizeof(double));
	for(i=0;i<maxS;i++){
		riedi_add_a[i] = a;
		riedi_add_b[i] = b;
		riedi_add_c[i] = c;
		dummy = d;
		changeabcd(riedi_add_a+i, riedi_add_b+i, riedi_add_c+i, &dummy);
		//printf("%3.3f %3.3f %3.3f\n",riedi_add_a[i],riedi_add_b[i],riedi_add_c[i]);
	}

		// The matrix
	M = NULL;
	
	if(!(fp_edges=fopen("edges","w"))){
		fprintf(stderr,"Cannot create the edges file!\n");
		exit(1);
	}

	repeats = 0;
	numnodes = pow(2,S);
	numnodes2 = pow(2,S2);

	hcreate(2*E);

	counts_a = counts_b = counts_c = 0;
	for(i=0;i<E;i++){
		length_along_path = 0;
		x = y = 0;
		len = numnodes;
		len2 = numnodes2;
		while(len > 1 || len2 > 1){
			rnd = (float)rand()/(float)RAND_MAX;
			if(rnd <= riedi_add_a[length_along_path]){
				len = (len>1.5)?len/2:len;
				len2 = (len2>1.5)?len2/2:len2;
				path_taken[length_along_path++] = 'a';
				counts_a++;
			}else if(rnd <= riedi_add_a[length_along_path]+riedi_add_b[length_along_path]){
				if(len2>1.5){
					x += len2/2;
					len2 /= 2;
				}
				len = (len>1.5)?len/2:len;
				path_taken[length_along_path++] = 'b';
				counts_b++;
			}else if(rnd <= riedi_add_a[length_along_path]+riedi_add_b[length_along_path]+riedi_add_c[length_along_path]){
				if(len>1.5){
					y += len/2;
					len /= 2;
				}
				len2 = (len2>1.5)?len2/2:len2;
				path_taken[length_along_path++] = 'c';
				counts_c++;
			}else{
				if(len>1.5){
					y += len/2;
					len /= 2;
				}
				if(len2>1.5){
					x += len2/2;
					len2 /= 2;
				}
				path_taken[length_along_path++] = 'd';
			}
		}

	
		hash_entry.key = (char*)malloc(3*sizeof(double)); // Some extra.
		sprintf(hash_entry.key,"%g %g",x,y);
		
		if(tmp_entry=hsearch(hash_entry, FIND)){
			if(DO_WEIGHTED){
				new_vertex = (struct vertex *)malloc(sizeof(struct vertex));
				new_vertex->x = x;
				new_vertex->y = y;
				tree_result=tsearch((void *)new_vertex, &rootrows, mycompare_y);
				tmp_vertex = *tree_result;
				while(tmp_vertex->x != x)
					tmp_vertex = tmp_vertex->next;
				tmp_vertex->weight++;
				free(new_vertex);
			}else
				i--;

			repeats++;
			continue;  // Entry exists
		}


		hsearch(hash_entry, ENTER);  // Add to hash table

		//fprintf(fp_edges,"%g %g\n",x,y);
		if(!DO_WEIGHTED) fprintf(fp_edges,"%g %g\n",y,x);
		

		if(SHOW_MARGINALS || DO_WEIGHTED){
			new_vertex = (struct vertex *)malloc(sizeof(struct vertex));
			new_vertex->x = x;
			new_vertex->y = y;
			new_vertex->next = NULL;
			new_vertex->down = NULL;
			new_vertex->visited = 0;
			new_vertex->weight = 1;

			tree_result=tsearch((void *)new_vertex, &rootrows, mycompare_y);
			tmp_vertex = *tree_result;
			if(tmp_vertex != new_vertex){
				new_vertex->next = tmp_vertex->next;
				tmp_vertex->next = new_vertex;
			}

			if(SHOW_MARGINALS){
				tree_result=tsearch((void *)new_vertex, &rootcols, mycompare_x);
				tmp_vertex = *tree_result;
				if(tmp_vertex != new_vertex){
					new_vertex->down = tmp_vertex->down;
					tmp_vertex->down = new_vertex;
				}
			}
		}
	}

	if(DO_WEIGHTED)	// We haven't printed out the edges yet
		twalk(rootrows,action_weighted_rows);


	printf("Repeats = %d\n",repeats);

	printf("\n");

	// Find one of the marginals by summing over columns
	if(SHOW_MARGINALS){
		if(!(fp_edges_biggest_component=fopen("edges_biggest_component","w"))){
			fprintf(stderr,"Cannot create the edges_biggest_component file!\n");
			exit(1);
		}
		if(!(fp_marginal_rows=fopen("marginal_rows","w"))){
			fprintf(stderr,"Cannot create the marginal_rows file!\n");
			exit(1);
		}

		if(!(fp_marginal_cols=fopen("marginal_cols","w"))){
			fprintf(stderr,"Cannot create the marginal_cols file!\n");
			exit(1);
		}

		//fprintf(fp_edges,"%d\n",E);

		twalk(rootrows, action_rows);

		twalk(rootcols, action_cols);

		// Find the size of the largest connected component.
		TmpVertex.y = TmpVertex.x = 0;
		while(1){
			if(tree_result=tfind(&TmpVertex,&rootrows,mycompare_y))
				break;
			TmpVertex.y++;
		}
		
		size_connected = get_connected(*tree_result,fp_edges_biggest_component);

		printf("Size of the largest connected component = %d\n",size_connected);

		fclose(fp_marginal_rows);
		fclose(fp_marginal_cols);
		fclose(fp_edges_biggest_component);
	}

	fclose(fp_edges);
}


int mycompare_x(const void *pa, const void *pb){
	if (((struct vertex *)pa)->x - ((struct vertex *)pb)->x < -0.5)
		return -1;
	if (((struct vertex *)pa)->x - ((struct vertex *)pb)->x > 0.5)
		return 1;
	return 0;
}

int mycompare_y(const void *pa, const void *pb){
	if (((struct vertex *)pa)->y - ((struct vertex *)pb)->y < -0.5)
		return -1;
	if (((struct vertex *)pa)->y - ((struct vertex *)pb)->y > 0.5)
		return 1;
	return 0;
}

void action_weighted_rows(const void *nodep, const VISIT which, const int depth){
struct vertex *tmp_vertex;

	switch(which){
		case preorder: case endorder:
			break;
		case postorder: case leaf:
			tmp_vertex = *(struct vertex **)nodep;

			//if(which == leaf && FirstY < 0)
			//	FirstY = tmp_vertex->y;
			
			for(;tmp_vertex;tmp_vertex=tmp_vertex->next)
				fprintf(fp_edges,"%g %g %d\n",tmp_vertex->y,tmp_vertex->x,tmp_vertex->weight);
			break;
	}
}
void action_rows(const void *nodep, const VISIT which, const int depth){
struct vertex *tmp_vertex;
int marginal;

	switch(which){
		case preorder: case endorder:
			break;
		case postorder: case leaf:
			tmp_vertex = *(struct vertex **)nodep;

			//if(which == leaf && FirstY < 0)
			//	FirstY = tmp_vertex->y;
			
			//for(marginal=0;tmp_vertex;marginal++,tmp_vertex=tmp_vertex->next);
			for(marginal=0;tmp_vertex;marginal+=tmp_vertex->weight,tmp_vertex=tmp_vertex->next);
			fprintf(fp_marginal_rows,"%d\n",marginal);
			break;
	}
}

void action_cols(const void *nodep, const VISIT which, const int depth){
struct vertex *tmp_vertex;
int marginal;

	switch(which){
		case preorder: case endorder:
			break;
		case postorder: case leaf:
			tmp_vertex = *(struct vertex **)nodep;
			//for(marginal=0;tmp_vertex;marginal++,tmp_vertex=tmp_vertex->down);
			for(marginal=0;tmp_vertex;marginal+=tmp_vertex->weight,tmp_vertex=tmp_vertex->down);
			fprintf(fp_marginal_cols,"%d\n",marginal);
			break;
	}
}



int get_connected(struct vertex *start, FILE *fp_edges_biggest_component){
int count = 0;
struct vertex *traverse_col, *tmp_vertex, **tree_result;
double current_x;

	start->visited = 1;
	
	for(traverse_col=start;traverse_col!=NULL;traverse_col=traverse_col->next){
		count++;

		current_x=traverse_col->x;

			// Print out the edge. The edge is: y->x
			// So, marginal on right hand is outdegree, and
			// marginal on bottom is indegree.

		fprintf(fp_edges_biggest_component,"%d %d\n",(int)(traverse_col->y),(int)(traverse_col->x));
		TmpVertex.y = current_x;
		tree_result = tfind(&TmpVertex,&rootrows,mycompare_y);
		if(!tree_result)
			continue;
			
		tmp_vertex = *tree_result;
		if(tmp_vertex->visited)
			continue;
		count += get_connected(tmp_vertex,fp_edges_biggest_component);
	}

	return count;
}

usage(){
	fprintf(stderr,"Usage: %s <a> <b> <c> <S> {<S2>} <E>\n",progname);
	fprintf(stderr,"a, b and c must be between 0 and 1, and sum to less than 1.\n");
	fprintf(stderr,"2^S = height of matrix.\n");
	fprintf(stderr,"2^S2 = length of matrix. If unspecified, it is\n");
	fprintf(stderr,"taken to be equal to S.\n");
	fprintf(stderr,"E = number of edges in matrix.\n");
	exit(1);
}

