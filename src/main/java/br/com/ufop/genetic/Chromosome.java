package br.com.ufop.genetic;

import java.util.ArrayList;
import java.util.List;

import br.com.ufop.classes.Vertex;

public class Chromosome extends ArrayList<Vertex> {
	
	private double cost;
	private List<Vertex> path = new ArrayList<Vertex>();

	private static final long serialVersionUID = 1L;

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public int compareTo(Chromosome other){
		return Double.compare(other.getCost(), getCost());
	}

	public List<Vertex> getPath() {
		return path;
	}

	public void setPath(List<Vertex> path) {
		this.path = path;
	}
}
