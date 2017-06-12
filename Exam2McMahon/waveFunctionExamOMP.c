#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <omp.h>


/* Max McMahon
   CSCI 373
   Exam 2 phase 2
   4/7/17
   waveFunctionOMP.c

 */
//Compile statement:  gcc -std=c99 -fopenmp -o test waveFunctionExamOMP.c -lm
//Edit size of grid by modifiying N on line 34
//Edit number of steps by modifying end on line 35
//Enable output by setting OUTPUTSWITCH on line 42 to 1, set to 0 to disable
//Enable timing by setting TIMINGSWITCH on line 44 to 1, set to 0 to disable
//Change number of threads by changing the value of NUMBEROFTHREADS on line 50
#define M_PI 3.14159


  int main()
  {

    double initialCondition(double x, double y);
    void timeOut(double timeElapsed, int threadN, int N, int steps);
    double* discretize(int N);
    char* fileGen(int step,int N);
    void output(int N, double**inAr, char* fileName, double max);
    
    //N and end values
    int N=2000;
    int end=5000;
    double start;
    double finish;
    double elapsed;
    

    //outputswitch, set to 0 disable output, 1 to enable
    const int OUTPUTSWITCH=0;
    //timingswitch, set to 0 to disable timing data, 1 to enable
    const int TIMINGSWITCH=1;

    if(TIMINGSWITCH==1)
      start=omp_get_wtime();

    //change number of threads here
    const int NUMBEROFTHREADS=12;
    omp_set_num_threads(NUMBEROFTHREADS);
    static int numThreads=1;

    
    double**arC=(double**)malloc(sizeof(double)*N);
    double**ar1=(double**)malloc(sizeof(double)*N);
    double**ar0=(double**)malloc(sizeof(double)*N);
    double**arT=(double**)malloc(sizeof(double)*N);

    //array gen   
 
   int i=0;
    for(i=0; i<N; i++)
      {
	arC[i]=(double*)malloc(N*sizeof(double));
	ar1[i]=(double*)malloc(N*sizeof(double));
	ar0[i]=(double*)malloc(N*sizeof(double));
	arT[i]=(double*)malloc(N*sizeof(double));
      }
    
    double* xyi=(double*)malloc(N*sizeof(double));
    xyi=discretize(N);

    //main calc function

      for(int i=0; i<end; i++)
	{
	  char* fileN;
	  if(OUTPUTSWITCH==1)
	    fileN=fileGen(i, N);
	  // printf("file:%s, created\n", fileN);
	  double absMax=0;
	#pragma omp parallel
	  {
	    numThreads=omp_get_num_threads();
	  #pragma omp for
	    for(int x=0; x<N; x++)
	      {

		for(int y=0; y<N; y++)
		  {
		    if(i==0 || i==1)
		      arC[x][y]=initialCondition(xyi[x],xyi[y]);
		    else if(xyi[x]==0 || xyi[y]==0 || xyi[x]==1 || xyi[y]==1)
		      arC[x][y]=0;
		    else
		      {
			arC[x][y]=0.01*(ar1[x-1][y]+ar1[x+1][y]+ar1[x][y-1]+ar1[x][y+1]-4*ar1[x][y])+2*ar1[x][y]-ar0[x][y];
		      }
		  
	   
		    if(fabs(arC[x][y])>absMax)
		      {
			absMax=fabs(arC[x][y]);
		      }
		    
		  }
	      }
	  }
	  if(OUTPUTSWITCH==1)
	    output(N, arC, fileN, absMax);

	  arT=ar0;
	  ar0=ar1;
	  ar1=arC;
	  arC=arT;
	  	  
	}
      if(TIMINGSWITCH==1)
	{
	  finish=omp_get_wtime();
	  elapsed=finish-start;
	 
	  timeOut(elapsed, numThreads, N, end); 
	  
	}
  }

double* discretize(int N)
{
  double* xi=(double*)malloc(N*sizeof(double));
  int i=0;
  for(i=0; i<N; i++)
    {
      xi[i]=i/((double)(N-1));
    }
  return xi;
}

double initialCondition(double x, double y) {
	double sigma=0.01;//tight point
	//double sigma=0.1;//wider point
	double mu=0.5;//center
	double max = (1.0/(2.0*M_PI*sigma*sigma))*exp(-0.5*( ((0.5-mu)/sigma)*((0.5-mu)/sigma) +  ((0.5-mu)/sigma)*((0.5-mu)/sigma)   ));
	double result = (1.0/(2.0*M_PI*sigma*sigma))*exp(-0.5*( ((x-mu)/sigma)*((x-mu)/sigma) +  ((y-mu)/sigma)*((y-mu)/sigma)   ))/max;
	return result;
}

char* fileGen(int step, int N)
{
  FILE *fp;
  static char fileName[15];
  sprintf(fileName, "output%04d.pgm",step);
  fp=fopen(fileName, "w+");
  fprintf(fp, "P2 \n%d %d \n255 \n",N, N);
  fclose(fp);
  return fileName;
}

void output( int N, double**inAr, char* fileName, double max)
{
  double scaleD;
  int scaleI;
  FILE *fp;
  double localMax=1;
  fp=fopen(fileName, "a");
  if(max>0)
    localMax=max;
  for(int y=0; y<N; y++)
    {
      for(int x=0; x<N; x++)
	{
	  scaleD=((inAr[x][y])*(127/localMax))+127;
	  scaleI=scaleD;
	  
	  fprintf(fp, "%d ",scaleI);
	}
      fprintf(fp, "\n");
    }
  fclose(fp);
}

void timeOut(double timeElapsed, int threadN, int N, int steps)
{
  FILE *tF;
  static int ext=0;
  tF=fopen("Exam2Timings.txt","r");
  if(tF)
    {
      ext=1;
      fclose(tF);
    }
   tF=fopen("Exam2Timings.txt","a");
   if(ext==0)
     fprintf(tF, "Threads:  N:  Steps:  Time: \n");

   fprintf(tF, "  %02d     %04d  %04d   %f\n", threadN, N, steps, timeElapsed);
   fclose(tF);
      
}




