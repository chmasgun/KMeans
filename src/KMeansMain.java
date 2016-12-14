import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class KMeansMain {
	private final static int SIZE=100;
	private final static int CLUSTERS=9;
	private final static int NUM_ITERATION=100;
	private final static Point[] store=new Point[SIZE];
	private final static Point[] clusterMeans=new Point[CLUSTERS];
	private final static Point[][] clusters=new Point[CLUSTERS][];
	private final static int[] clusterSize=new int[CLUSTERS];
	private final static String readFile="data3";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub



		try {
			double coorX,coorY;
			double minX = 100000,minY=100000,maxX=-100000,maxY=-100000;
			String group,strX,strY;
			
			BufferedReader rd=new BufferedReader(new FileReader(readFile+".txt"));
			group=rd.readLine();
			StringTokenizer token=new StringTokenizer(group," ");
			int index=0;
			while(token.hasMoreTokens()){
				strX=token.nextToken();
				strY=token.nextToken();
				coorX=Double.parseDouble(strX);
				coorY=Double.parseDouble(strY);
				minX=Math.min(minX, coorX);
				minY=Math.min(minY, coorY);
				maxX=Math.max(maxX, coorX);
				maxY=Math.max(maxY, coorY);
				store[index]=new Point(coorX,coorY);
				index++;
			}
			// DATA EXTRACTED ABOVE
			// NOW CLUSTERING
			//System.out.println(minX+"  "+minY+"  "+maxX+"  "+maxY+"  ");
			double rangeX=maxX-minX;
			double rangeY=maxY-minY;
			int columns=(int) Math.ceil(Math.sqrt(CLUSTERS));
			int rows= (int) Math.floor((double)CLUSTERS/columns);
			int leftover= CLUSTERS-columns*rows;
			if(leftover==0){
				leftover=columns;
				rows--;
			}
			//System.out.println(columns+" "+rows+" "+leftover);
			for(int i=0;i<columns;i++){
				for(int j=0;j<rows ;j++){
					clusterMeans[i*rows+j]=new Point(minX+(i+1)*rangeX/(columns+1),minY+(j+1)*rangeY/(rows+2),i*rows+j);
					

				}
			}
			for(int ind=0; ind<leftover;ind++){
				clusterMeans[rows*columns+ind]=new Point(minX+(ind+1)*rangeX/(leftover+1),minY+(rows+1)*rangeY/(rows+2),rows*columns+ind);

			}
			// CLUSTERS INITIALIZED UP HERE. NOW UPDATE
			//
			//
			//
			for(int a=0;a<clusterMeans.length;a++){
				System.out.println(clusterMeans[a]);
			}
			

			int iterCnt=0;
			while(iterCnt<NUM_ITERATION){
				assignClusters(clusterMeans,store,clusterSize,clusters);
				updateClusterMeans(clusterMeans,clusters);
				
				iterCnt++;
			}
			printClusterSizes();
			
			printClusterMeans();
		//	printAllClusters();
			writeClusters();


			rd.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	private static void printData(){
		for(int i=0;i<SIZE;i++){
			System.out.println(i+":  "+store[i]);
		}
	}
	private static void printAllClusters(){
		for(int clNo=0; clNo<clusters.length;clNo++){
			System.out.println((clNo+1)+"th cluster : ");
			for(int elt=0;elt<clusters[clNo].length;elt++){
				System.out.println(elt+":  "+clusters[clNo][elt]);
			}
		}
	}
	private static void printClusterMeans(){
		for(int i=0;i<CLUSTERS;i++){
			System.out.println(i+":  "+clusterMeans[i]);
		}
	}
	
	private static void printClusterSizes(){
		System.out.print("(");
		for(int i=0;i<clusters.length;i++){
			System.out.print(clusters[i].length+" ");
		}
		System.out.println(")");
	}
	


	private static void assignClusters(Point[] clusterMeans, Point[] data, int[] clSize, Point[][] clusters){
		for(int cld=0; cld<clSize.length ;cld++){
			// initialize cloud sizes array to zeros
			clSize[cld]=0;
		}
		for(int i=0;i<data.length;i++){
			double minD=1000000;
			for(int c=0; c<clusterMeans.length;c++){
				if(distance(clusterMeans[c],data[i])<minD){
					minD=distance(clusterMeans[c],data[i]);
					data[i].setCluster(c);
				}
			}
			clSize[data[i].getCluster()]++;
		}
		for(int cluster=0; cluster<clSize.length;cluster++){
			clusters[cluster]=new Point[clSize[cluster]];
			int cnt=0;
			for(int i=0;i<data.length;i++){
				if(cluster==data[i].getCluster()){
					clusters[cluster][cnt]=data[i];
					cnt++;
				}
			}
		}
	}

	private static void updateClusterMeans(Point[] clusterMeans, Point[][] clusters){
		for(int cluster=0; cluster<clusters.length;cluster++){
			double sumX=0;
			double sumY=0;
			for(int i=0;i<clusters[cluster].length;i++){
				sumX+=clusters[cluster][i].getX();
				sumY+=clusters[cluster][i].getY();
			}
			if(clusters[cluster].length!=0){
			clusterMeans[cluster]=new Point((sumX/clusters[cluster].length),(sumY/clusters[cluster].length) , cluster);
			}
		}
	}

	private static double distance(Point p1,Point p2){
		return Math.sqrt(Math.pow(p2.getY()-p1.getY(), 2)+Math.pow(p2.getX()-p1.getX(), 2));
	}
	
	
	private static void writeClusters() throws IOException{
		String writeFile=readFile;
		for(int i=0; i<clusters.length;i++){
			writeFile=readFile+"-"+i;
			BufferedWriter bw=new BufferedWriter(new FileWriter(writeFile+".txt"));
			for(int index=0;index<clusters[i].length;index++){
				bw.write(clusters[i][index].getX()+" "+clusters[i][index].getY()+" ");
			}
			bw.close();
		}
	}
}
