package br.com.ufop.utils;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import br.com.ufop.classes.Vertex;
import br.com.ufop.constants.K;

public class Instance {
	
	private String name;
	private int dimension;
	private int capacity;
	private Vertex depot;
	private ArrayList<Vertex> customers;
	private double[][] graph;

	public  void sortCustomers(){
		Collections.sort(this.customers, new Comparator<Vertex>() {
			public int compare(Vertex vertex, Vertex t1) {
				return Double.compare(vertex.getDistanceToDepot(), t1.getDistanceToDepot());
			}
		});
	}
	
	public Instance(String instance) throws IOException {
		List<String> lines = Utils.readInstance(K.INSTANCES_PATH + instance);
		
		String[] splited_name = lines.get(0).split(":");
		this.name = splited_name[splited_name.length - 1].trim();
		
		String[] splited_dimension = lines.get(3).split(":");
		this.dimension = Integer.parseInt(splited_dimension[splited_dimension.length - 1].trim());
		
		String[] splited_capacity = lines.get(5).split(":");
		this.capacity = Integer.parseInt(splited_capacity[splited_capacity.length - 1].trim());
		
		int i = 8;
		
		HashMap<Integer, List<Double>> vertexes = new HashMap<Integer, List<Double>>();
		
		for(; i < 8 + dimension; i++) {
			String[] splited_vertex = lines.get(i).split(" ");
			
			vertexes.put(Integer.parseInt(splited_vertex[0]), Arrays.asList(Double.parseDouble(splited_vertex[1].trim()), Double.parseDouble(splited_vertex[2].trim())));
		}
		
		i++;	// Pula a linha DEMAND_SECTION
		
		int j = i;
		
		HashMap<Integer, Integer> demands = new HashMap<Integer, Integer>();
		
		for(; j < i + dimension; j++) {
			String[] splited_demand = lines.get(j).split(" ");
			
			demands.put(Integer.parseInt(splited_demand[0]), Integer.parseInt(splited_demand[1]));
		}
		
		j++;	// Pula a linha DEPOT_SECTION
		
		int dp = Integer.parseInt(lines.get(j).trim());
		
		this.graph = new double[dimension][dimension];
		
		this.depot = new Vertex(vertexes.get(dp).get(0), vertexes.get(dp).get(1), dp, demands.get(dp));
		
//		vertexes.remove(dp);	// Remove o depósito da lista de vértices
		
		this.customers = new ArrayList<Vertex>();
		
		for(Entry<Integer, List<Double>> entry : vertexes.entrySet()) {
			if(entry.getKey() == dp) {
				continue;
			}
			
			this.customers.add(new Vertex(entry.getValue().get(0), entry.getValue().get(1), entry.getKey(), demands.get(entry.getKey())));
		}
		
		for(i = 1; i <= this.graph.length; i++) {
			for(j = 1; j <= this.graph.length; j++) {
				if(i == j) {
					this.graph[i - 1][j - 1] = 0;
				}
				
				this.graph[i - 1][j - 1] = Math.sqrt(Math.pow(vertexes.get(i).get(0) - vertexes.get(j).get(0), 2) + Math.pow(vertexes.get(i).get(1) - vertexes.get(j).get(1), 2));
			}
		}
		
		lines.clear();
		vertexes.clear();
		demands.clear();
	}

	public int getDimension() {
		return dimension;
	}

	public int getCapacity() {
		return capacity;
	}

	public Vertex getDepot() {
		return depot;
	}

	public ArrayList<Vertex> getCustomers() {
		return customers;
	}

	public double[][] getGraph() {
		return graph;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
