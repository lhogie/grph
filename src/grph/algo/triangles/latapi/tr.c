
/* Matthieu Latapy */
/* March 2006 */
/* http://www-rp.lip6.fr/~latapy */
/* matthieu.latapy@lip6.fr */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#include <unistd.h>
#include <sys/times.h>

/* The two last '#include' above are used for time measurement
 * but may lead to portability problems; you may remove them,
 * as well as all the lines containing 'times', 'clock_t' or
 * 'time_info', to avoid this.
 * One may also remove the 'inline' statements below.
*/ 

#define MAX_LINE_LENGTH 1000

typedef struct graph{
  int n;
  int m;
  int **links;
  int *degrees;
  int *capacities;
} graph;

/* Utility functions */

void report_error(char *s){
  fprintf(stderr,"%s\n",s);
  exit(-1);
}

int is_in_array(int *a, int e, int left, int right){
 int mid;
 while (right>left) {
  mid = (left+right)/2;
  if (e < a[mid])
   right = mid-1;
  else if (e > a[mid])
   left = mid+1;
  else return(1);
  }
 return(0);
 }

/* returns the largest i such that g->links[v][i] < v */
int closest_in_array(graph *g, int v){
 int right = g->degrees[v]-1;
 int *a = g->links[v];

 /* optimization for extreme cases */
 if (right<0) return(-1);
 if (a[0]>=v)
  return(-1);
 if (a[right]<v)
  return(right);
 if (a[right]==v)
  return(right-1);

 {
 int left = 0, mid;
 while (right>left) {
  mid = (left+right)/2;
  if (v < a[mid])
   right = mid-1;
  else if (v > a[mid])
   left = mid+1;
  else return(mid-1);
  }
 if (v>a[right])
  return(right);
 else
  return(right-1);
 }

 }


/* Graph management */

void free_graph(graph *g){
 if (g!=NULL) {
  if (g->links!=NULL) {
   if (g->links[0]!=NULL)
    free(g->links[0]);
   free(g->links);
   }
  if (g->capacities!=NULL)
   free(g->capacities);
  if (g->degrees!=NULL)
   free(g->degrees);
  free(g);
  }
 }

graph *graph_from_file(FILE *f){
 char line[MAX_LINE_LENGTH];
 int i, u, v;
 graph *g;

 if( (g=(graph *)malloc(sizeof(graph))) == NULL )
  report_error("graph_from_file: malloc() error");
 
 /* read n */
 if( fgets(line,MAX_LINE_LENGTH,f) == NULL )
  report_error("graph_from_file; read error (fgets)");
 if( sscanf(line, "%d\n", &(g->n)) != 1 )
  report_error("graph_from_file; read error (sscanf)");

 /* read the degree sequence */
 if( (g->capacities=(int *)malloc(g->n*sizeof(int))) == NULL )
  report_error("graph_from_file: malloc() error");
 if( (g->degrees=(int *)calloc(g->n,sizeof(int))) == NULL )
  report_error("graph_from_file: calloc() error");
 for(i=0;i<g->n;i++){
  if( fgets(line,MAX_LINE_LENGTH,f) == NULL )
   report_error("graph_from_file; read error (fgets)");
  if( sscanf(line, "%d %d\n", &v, &(g->capacities[i])) != 2 )
   report_error("graph_from_file; read error (sscanf)");
  if( v != i ){
   fprintf(stderr,"Line just read : %s\n i = %d; v = %d\n",line,i,v);
   report_error("graph_from_file: error while reading degrees");
   }
  }

 /* compute the number of links */
 g->m=0;
 for(i=0;i<g->n;i++)
  g->m += g->capacities[i];
 g->m /= 2;

 /* create contiguous space for links */
 if (g->n==0){
  g->links = NULL; g->degrees = NULL; g->capacities = NULL;
  }
 else {
  if( (g->links=(int **)malloc(g->n*sizeof(int*))) == NULL )
   report_error("graph_from_file: malloc() error");
  if( (g->links[0]=(int *)malloc(2*g->m*sizeof(int))) == NULL )
   report_error("graph_from_file: malloc() error");
  for(i=1;i<g->n;i++)
   g->links[i] = g->links[i-1] + g->capacities[i-1];
  }

 /* read the links */
 for(i=0;i<g->m;i++) {
  if( fgets(line,MAX_LINE_LENGTH,f) == NULL )
   report_error("graph_from_file; read error (fgets)");
  if( sscanf(line, "%d %d\n", &u, &v) != 2 )
   report_error("graph_from_file; read error (sscanf)");
  if ( (u>=g->n) || (v>=g->n) || (u<0) || (v<0) ) {
   fprintf(stderr,"Line just read: %s",line);
   report_error("graph_from_file: bad node number");
  }
  if ( (g->degrees[u]>=g->capacities[u]) ||
       (g->degrees[v]>=g->capacities[v]) )
   report_error("graph_from_file: too many links for a node");
  g->links[u][g->degrees[u]] = v;
  g->degrees[u]++;
  g->links[v][g->degrees[v]] = u;
  g->degrees[v]++;
  }
 for(i=0;i<g->n;i++)
  if (g->degrees[i]!=g->capacities[i])
   report_error("graph_from_file: capacities <> degrees");
 if( fgets(line,MAX_LINE_LENGTH,f) != NULL )
  report_error("graph_from_file; too many lines");

 return(g);
}

/* in-place quicksort from Fabien Viger */
// Median of three int
inline int med3(int a, int b, int c) {
  if(a<b) {
    if(c<b) return (a<c) ? c : a;
    else return b;
  }
  else {
    if(c<a) return (b<c) ? c : b;
    else return a;
  }
}
// Sort integer arrays in ASCENDING order
inline void isort(int *v, int t) {
  int i;
  if(t<2) return;
  for(i=1; i<t; i++) {
    register int *w = v+i;
    int tmp = *w;
    while(w!=v && *(w-1)>tmp) { *w = *(w-1); w--; }
    *w = tmp;
  }
}
// Sort integer arrays in ASCENDING order
void quicksort(int *v, int t) {
  if(t<15) isort(v,t);
  else {
    int p = med3(v[t>>1], v[(t>>2)+2], v[t-(t>>1)-2]);
    int i=0;
    int j=t-1;
    while(i<j) {
      while(i<=j && v[i]<p) i++;
      while(i<=j && v[j]>p) j--;
      if(i<j) {
        int tmp=v[i];
        v[i++]=v[j];
        v[j--]=tmp;
      }
    }
    if(i==j && v[i]<p) i++;
    quicksort(v,i);
    quicksort(v+i,t-i);
  }
}

void sort_graph(graph *g){
 int i;
 for(i=0;i<g->n;i++)
  quicksort(g->links[i],g->degrees[i]);
}

/* TRIANGLES */

/* computes the triangles in which a given node is involved, in O(d(v)^2) time and O(1) space */
void old_vertex(graph *g, unsigned long int *t, int v){
 int i, u1, u2, j;
 for (i=g->degrees[v]-1;i>=0;i--){
  u1 = g->links[v][i];
  if (g->degrees[u1]>1)
  for (j=i-1;j>=0;j--){
   u2 = g->links[v][j];
   if (g->degrees[u2]>1)
   if (is_in_array(g->links[u1],u2,0,g->degrees[u1]-1)){
    t[u1]++;
    t[u2]++;
    t[v]++;
    }
   }
  }
 }

/* computes the triangles in which a given link is involved, in O(d(v)+d(u))*/
void tr_link(graph *g, unsigned long int *t, int u, int v){
 int iu = 0, iv = 0, w;
 while ((iu<g->degrees[u]) && (iv<g->degrees[v])) {
  if (g->links[u][iu] < g->links[v][iv])
   iu++;
  else if (g->links[u][iu] > g->links[v][iv])
   iv++;
  else { /* neighbor in common */
   w = g->links[u][iu];
   t[w]++;
   iu++;
   iv++;
   } 
  }
 }

void tr_link_nohigh(graph *g, unsigned long int *t, int u, int v, int nb_high){
 int iu = 0, iv = 0, w;
 while ((iu<g->degrees[u]) && (iv<g->degrees[v])) {
  if (g->links[u][iu] < g->links[v][iv])
   iu++;
  else if (g->links[u][iu] > g->links[v][iv])
   iv++;
  else { /* neighbor in common */
   w = g->links[u][iu];
   if (w>=nb_high)
    t[w]++;
   iu++;
   iv++;
   } 
  }
 }

/* coumputes the triangles in which a given node is involved, in O(m) time and O(n) space */
void new_vertex(graph *g, unsigned long int *t, int v){
 int i, j, u, w;
 char *adj;

 if( (adj=(char *)calloc(g->n,sizeof(char))) == NULL )
  report_error("new_vertex: calloc() error");
 for (i=g->degrees[v]-1;i>=0 && g->links[v][i]>v;i--)
  adj[g->links[v][i]] = 1;

 for (i=g->degrees[v]-1;i>=0;i--){
  u = g->links[v][i];
  for (j=closest_in_array(g,u);j>=0;j--){
   w = g->links[u][j];
   if (adj[w]){
    t[u]++;
    t[v]++;
    t[w]++;
    }
   }
  }

 free(adj);
 }

/* Computation of *all* the triangles in a given graph */

/* edge-iterator */
void old_triangles(graph *g, unsigned long int *t){
 int i, v, u;
 for (v=g->n-1;v>=0;v--)
 for (i=closest_in_array(g,v);i>=0;i--) {
  u = g->links[v][i];
  if (g->degrees[u]>1)
   tr_link(g,t,u,v);
  }
 }

/* forward */
void forward_triangles(graph *g, unsigned long int *tr){
 int i, is, it, s, t;
 int *deg;
 int **A;

 /* space allocation */
 if( (A=(int **)malloc(g->n*sizeof(int*))) == NULL )
  report_error("forward_triangles: malloc() error");
 if( (A[0]=(int *)malloc(2*g->m*sizeof(int))) == NULL )
  report_error("forward_triangles: malloc() error");
 for(i=1;i<g->n;i++)
  A[i] = A[i-1] + g->capacities[i-1];

 if( (deg=(int *)calloc(g->n,sizeof(int))) == NULL )
  report_error("forward_triangles: malloc() error");

 /* the algorithm */
 for (s=0;s<g->n;s++)
 for (i=0;i<g->degrees[s];i++) {
  t = g->links[s][i];

  if (s<t) {
   is=0; it=0;

   while ((is<deg[s]) && (it<deg[t])) {
    if (A[s][is]<A[t][it])
     is++;
    else if (A[s][is]>A[t][it])
     it++;
    else {
     /* elt in the intersection */
     tr[s]++;
     tr[t]++;
     tr[A[s][is]]++;
     is++;
     it++;
     }
    }

   A[t][deg[t]++] = s;
   }

  }

 free(A[0]);
 free(A);
 free(deg);
 }

/* compact-forward */
void compact_forward_triangles(graph *g, unsigned long int *tr){
 int v, u, i, iu, iv;
 for (v=0;v<g->n;v++)
 for (i=1+closest_in_array(g,v);i<g->degrees[v];i++){
  u = g->links[v][i];
  if (u>v){
   iu = 0;
   iv = 0;
   while ((iu<g->degrees[u]) && (iv<g->degrees[v]) && (g->links[u][iu]<v) && (g->links[v][iv]<v)){
    if (g->links[u][iu]<g->links[v][iv])
     iu++;
    else if (g->links[u][iu]>g->links[v][iv])
     iv ++;
    else {
     tr[u]++;
     tr[v]++;
     tr[g->links[u][iu]]++;
     iu++;
     iv++;
     }
    }
   }
  }
 }

/* new algorithm */
void new_triangles(graph *g, int nb_high, unsigned long int *tr){
 int i, v, u;

 /* high degree nodes */
 fprintf(stderr,"Processing high degree nodes...\n");
 for (v=0;v<nb_high;v++)
  new_vertex(g,tr,v);
 fprintf(stderr," Treated %d high degree nodes.\n",nb_high);

 /* remaining links */
 for (v=g->n-1;v>=nb_high;v--)
 for (i=closest_in_array(g,v);i>=0;i--) {
  u = g->links[v][i];
  if (u>=nb_high)
  tr_link_nohigh(g,tr,u,v,nb_high);
  }

 }


/* Clusterings */

float *local_clusterings(graph *g, unsigned long int *tr){
 int i;
 float *cc, deg;
 if( (cc = (float *)malloc(g->n*sizeof(float))) == NULL )
  report_error("local_clusterings: malloc() error");
 for (i=g->n-1;i>=0;i--) {
  deg = (float)g->degrees[i];
  if (deg<2)
   cc[i] = -1;
  else
   cc[i] = (float)(2.*((float)tr[i])/deg/(deg-1.));
 }
 return(cc);
}

float local_clustering(graph *g, unsigned long int *tr){
 int i, n=0;
 double cc=0., deg;
 for (i=g->n-1;i>=0;i--) {
  deg = (float)g->degrees[i];
  if (deg>1.){
   cc += 2.*((float)tr[i])/deg/(deg-1.);
   n++;
  }
 }
 return(cc/(float)n);
}

float global_clustering(graph *g, unsigned long int *tr){
 int i;
 unsigned long int nb_triangles=0, nb_triples=0, deg;
 for (i=g->n-1;i>=0;i--) {
  nb_triangles += tr[i];
  deg = g->degrees[i];
  nb_triples += (deg*(deg-1))/2;
 }
 if (nb_triangles%3 != 0)
  report_error("global_clustering: nb_triangles is not a multiple of 3");
 return((double)nb_triangles/(double)nb_triples);
}

/* Graph sorting and renumbering */

int *sort_nodes_by_degrees(graph *g){ /* in O(m) time and O(n) space */
 int *distrib, *resu;
 int **tmp, *tmpi;
 int v, i, j, x;

 if( (distrib=(int *)calloc(g->n,sizeof(int))) == NULL )
  report_error("sort_nodes_by_degrees: calloc() error");

 for (v=g->n-1;v>=0;v--)
  distrib[g->degrees[v]]++;

 if( (tmpi=(int *)calloc(g->n,sizeof(int))) == NULL )
  report_error("sort_nodes_by_degrees: calloc() error");
 if( (tmp=(int **)malloc(g->n*sizeof(int *))) == NULL )
  report_error("sort_nodes_by_degrees: malloc() error");
 if( (tmp[0]=(int *)malloc(g->n*sizeof(int))) == NULL )
  report_error("sort_nodes_by_degrees: malloc() error");
 for (i=1;i<g->n;i++)
  tmp[i] = tmp[i-1] + distrib[i-1];

 for (v=g->n-1;v>=0;v--) {
  tmp[g->degrees[v]][tmpi[g->degrees[v]]] = v;
  tmpi[g->degrees[v]]++;
  }

 if( (resu=(int *)malloc(g->n*sizeof(int))) == NULL )
  report_error("sort_nodes_by_degrees: malloc() error");

 x = 0;
 for (i=g->n-1;i>=0;i--)
 for (j=tmpi[i]-1;j>=0;j--)
  resu[x++] = tmp[i][j];

 free(tmpi);
 free(tmp[0]);
 free(tmp);
 free(distrib);
 return(resu);
 }

void renumbering(graph *g, int *perm){
 int *tmpp, **tmppp;
 int i, j;

 for (i=g->n-1;i>=0;i--)
 for (j=g->degrees[i]-1;j>=0;j--)
  g->links[i][j] = perm[g->links[i][j]];

 if( (tmpp=(int *)malloc(g->n*sizeof(int))) == NULL )
  report_error("renumbering: malloc() error");
 if( (tmppp=(int **)malloc(g->n*sizeof(int *))) == NULL )
  report_error("renumbering: malloc() error");

 memcpy(tmppp,g->links,g->n*sizeof(int *));
 for (i=g->n-1;i>=0;i--)
  g->links[perm[i]] = tmppp[i];

 memcpy(tmpp,g->degrees,g->n*sizeof(int));
 for (i=g->n-1;i>=0;i--)
  g->degrees[perm[i]] = tmpp[i];

 memcpy(tmpp,g->capacities,g->n*sizeof(int));
 for (i=g->n-1;i>=0;i--)
  g->capacities[perm[i]] = tmpp[i];

 free(tmpp);
 free(tmppp);
 }

int *random_perm(int n){
 int *perm;
 int i, tmp, j;
 if( (perm=(int *)malloc(n*sizeof(int))) == NULL )
  report_error("random_perm: malloc() error");
 for (i=n-1;i>=0;i--)
  perm[i] = i;
 for (i=n-1;i>=0;i--){
  j = random()%n;
  tmp = perm[i];
  perm[i] = perm[j];
  perm[j] = tmp;
  }
 return(perm);
 }

int *inverse_perm(int *p, int n){
 int *perm;
 int i;
 if( (perm=(int *)malloc(n*sizeof(int))) == NULL )
  report_error("random_perm: malloc() error");
 for (i=n-1;i>=0;i--)
  perm[p[i]]=i;
 return(perm);
 }

void random_renumbering(graph *g){
 int *perm;
 perm = random_perm(g->n);
 renumbering(g,perm);
 free(perm);
 }

/* Output functions */

void output_result(clock_t begin, clock_t end, int cc, int count, int pl, graph *g, unsigned long int *tr){
 clock_t ticks;
 int i;
 unsigned long int nb;
 end -= begin;
 ticks = end;
 end /= sysconf(_SC_CLK_TCK);
 fprintf(stderr,"Done in %dh %dmn %ds (=%ld ticks)\n\n", (int)end/3600, ((int)end%3600)/60, ((int)end%3600)%60, (long)(ticks));
 if (pl){
  for (i=g->n-1;i>=0;i--)
   printf("%d %d\n",i,(int)tr[i]);
  printf("\n");
  fflush(stdout);
  }
 if (count) {
  nb=0;
  for (i=g->n-1;i>=0;i--)
   nb += tr[i];
  if (nb%3 != 0)
   report_error("error: total number of triangles is not a multiple of 3\n");
  printf("Found %ld triangles...\n",(nb/3));
  }
 if (cc) {
  printf("average cc: %f\n",local_clustering(g,tr));
  printf("global cc: %f\n",global_clustering(g,tr));
  fflush(stdout);
  }
 }

void usage(char *c){
 fprintf(stderr,"Usage: %s [-cc] [-c] [-p] [-e|f|cf|n y]\n",c);
 fprintf(stderr,"  -cc: clustering coefficient computations\n");
 fprintf(stderr,"  -c: count triangles\n");
 fprintf(stderr,"  -p: pseudo-listing\n");
 fprintf(stderr,"  -e: use edge-iterator algorithm\n");
 fprintf(stderr,"  -f: use forward algorithm\n");
 fprintf(stderr,"  -cf: use *compact* forward algorithm\n");
 fprintf(stderr,"  -n y: use the new algorithm, with parameter n_K=y\n");
 exit(-1);
 }


/* MAIN */
int main(int argc, char **argv){
 graph *g;
 int i;
 unsigned long int *tr;
 int *sorted_nodes, *sorted_perm;
 int clustering, count, pseudo_listing, compact_forward, forward, edge_iterator, new, nb_high;
 clock_t begin=0, end=0;
 struct tms time_info;

 srandom(time(NULL));

 /* parse the command line */
 clustering = count = pseudo_listing = nb_high = 0;
 forward = compact_forward = edge_iterator = new = 0;
 for (i=1; i<argc; i++){
  if (strcmp(argv[i],"-cc")==0)
   clustering = 1;
  else if (strcmp(argv[i],"-c")==0)
   count = 1;
  else if (strcmp(argv[i],"-p")==0)
   pseudo_listing = 1;
  else if (strcmp(argv[i],"-e")==0)
   edge_iterator = 1;
  else if (strcmp(argv[i],"-f")==0)
   forward = 1;
  else if (strcmp(argv[i],"-cf")==0)
   compact_forward = 1;
  else if (strcmp(argv[i],"-n")==0) {
   new = 1;
   if (i==argc-1)
    report_error("invalid parameters (try -h)");
   nb_high = atoi(argv[++i]);
   if (nb_high==0)
    report_error("invalid parameters (try -h)");
   }
  else
   usage(argv[0]);
  }
 if (edge_iterator + forward + compact_forward + new != 1)
  usage(argv[0]);
 if (count+clustering+pseudo_listing == 0)
  usage(argv[0]);
  
 /* read the graph, randomly renumber the nodes, then according
  * to their degree, and sort the graph */
 /* we do this independently of the algorithme used afterwards
  * because it makes the code simpler in all the cases */
 fprintf(stderr,"Preprocessing the graph...\n");
 fprintf(stderr," reading...\n");
 g = graph_from_file(stdin);
 random_renumbering(g);
 fprintf(stderr," %d nodes, %d links.\n",g->n,g->m);
 fflush(stderr);
 fprintf(stderr," renumbering...\n");
 sorted_nodes = sort_nodes_by_degrees(g);
 sorted_perm = inverse_perm(sorted_nodes,g->n);
 renumbering(g,sorted_perm);
 free(sorted_perm);
 free(sorted_nodes);
 fprintf(stderr," sorting...\n");
 sort_graph(g);
 fprintf(stderr," done.\n");
 fflush(stderr);

 /* Triangle computation */

 if( (tr = (unsigned long int *)calloc(g->n,sizeof(unsigned long int))) == NULL )
  report_error("main: tr[] calloc() error");

 /* classical (edge-iterator) method */
 if (edge_iterator) {
  fprintf(stderr,"*edge-iterator* algorithm.\n");
  times(&time_info);
  begin = time_info.tms_utime;
  old_triangles(g,tr);
  times(&time_info);
  end = time_info.tms_utime;
  }

 /* 'forward' method, COMPACT version */
 else if (compact_forward) {
  fprintf(stderr,"*compact forward* algorithm.\n");
  times(&time_info);
  begin = time_info.tms_utime;
  compact_forward_triangles(g,tr);
  times(&time_info);
  end = time_info.tms_utime;
  }

 /* 'forward' method */
 else if (forward) {
  fprintf(stderr,"*forward* algorithm.\n");
  times(&time_info);
  begin = time_info.tms_utime;
  forward_triangles(g,tr);
  times(&time_info);
  end = time_info.tms_utime;
  }

 /* new method */
 else if (new) {
  fprintf(stderr,"*new* algorithm (%d high degree nodes, deg_max = %d).\n",nb_high,g->degrees[nb_high]);
  times(&time_info);
  begin = time_info.tms_utime;
  new_triangles(g,nb_high,tr);
  times(&time_info);
  end = time_info.tms_utime;
 }

 output_result(begin,end,clustering,count,pseudo_listing,g,tr);

// cannot be used because of renumbering and contiguous allocation
// free_graph(g);
 free(tr);
 return(0);
 }


