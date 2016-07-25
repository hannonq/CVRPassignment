package br.com.ufop.classes;

public class Vertex implements Comparable<Vertex>{
	private int number;
	private double coordX;
	private double coordY;
	private double demand;
	private boolean visited;
	private double distanceToDepot;
	
	public Vertex(Double coordX, Double coordY, int number, int demand) {
		this.coordX = coordX;
		this.coordY = coordY;
		this.number = number;
		this.setDemand(demand);
	}

	public int compareTo(Vertex other){
		return Double.compare(this.distanceToDepot, other.distanceToDepot);
	}


	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public double getDistanceToDepot() {
		return distanceToDepot;
	}

	public void setDistanceToDepot(double distanceToDepot) {
		this.distanceToDepot = distanceToDepot;
	}
	public double getCoordX() {
		return coordX;
	}
	
	public void setCoordX(int coordX) {
		this.coordX = coordX;
	}
	
	public double getCoordY() {
		return coordY;
	}
	
	public void setCoordY(int coordY) {
		this.coordY = coordY;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public double getDemand() {
		return demand;
	}

	public void setDemand(int demand) {
		this.demand = demand;
	}

	@Override
	public String toString() {
		return "Vertex [ Distance Depot= "+distanceToDepot+" number=" + number + ", coordX=" + coordX + ", coordY=" + coordY + ", demand=" + demand + "]";
	}
}
