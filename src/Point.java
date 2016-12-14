
public class Point {
	private double x,y;
	private int cluster;
	
	public Point(double x, double y){
		this.x=x;
		this.y=y;
	}
	
	public Point(double x, double y,int cluster){
		this.x=x;
		this.y=y;
		this.cluster=cluster;
	}
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public int getCluster(){
		return cluster;
	}
	public void setCluster(int n){
		cluster=n;
	}
	public String toString(){
		return x+" "+y+" in cluster "+cluster;
	}
}
