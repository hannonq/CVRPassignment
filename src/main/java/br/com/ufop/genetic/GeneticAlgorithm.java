package br.com.ufop.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import br.com.ufop.classes.Vertex;
import br.com.ufop.constants.K;
import br.com.ufop.utils.Instance;

public class GeneticAlgorithm {
	private List<Chromosome> population;
	private List<Chromosome> new_population;
	private Instance data;
	
	private static GeneticAlgorithm instance;
	
	public GeneticAlgorithm(Instance data) {
		setData(data);
		
		population = new ArrayList<Chromosome>();
		new_population = new ArrayList<Chromosome>();
		
		getInitialPopulation();
	}
	
	private void getInitialPopulation() {
		Random random = new Random();
		
		for(int i = 0; i < K.POPULATION_AMOUNT; i++) {
			Chromosome chromosome = new Chromosome();
			
			for(Vertex vertex : data.getCustomers()) {
				vertex.setProbability(random.nextDouble());
				
				chromosome.add(vertex);
			}
			
			population.add(chromosome);
		}
	}

	public static GeneticAlgorithm getInstance(Instance data) {
		if(instance != null) {
			return instance;
		}
		
		instance = new GeneticAlgorithm(data);
	
		return instance;
	}
	
	public double calculateFitness(Chromosome chrom) {
		Chromosome chromosome = new Chromosome();
		
		chromosome.setCost(chrom.getCost());
		chromosome.addAll(chrom);
		
		Collections.sort(chromosome);
		
		// Custo do caminho
		double cost = 0;
		
		// Quantidade restante no caminhão
		int amount = data.getCapacity();
		
		// Vértice visitado anteriormente
		Vertex previous = data.getDepot();

		List<Vertex> path = new ArrayList<Vertex>();
		path.add(previous);
		
		for(int i = 0; i < chromosome.size(); i++) {
			Vertex vertex = chromosome.get(i);
			
			if(vertex.getDemand() <= amount) {
				cost += data.getGraph()[previous.getNumber() - 1][vertex.getNumber() - 1];
				amount -= vertex.getDemand();
				
				previous = vertex;
			} else {
				previous = data.getDepot();
				amount = data.getCapacity();
				cost += data.getGraph()[vertex.getNumber() - 1][data.getDepot().getNumber() - 1];
				
				i--;
			}
			
			path.add(previous);
			
			if((i == chromosome.size() - 1) && (previous.getNumber() != data.getDepot().getNumber())) {
				path.add(data.getDepot());
				cost += data.getGraph()[previous.getNumber() - 1][data.getDepot().getNumber() - 1];
			}
		}
		
		chrom.setPath(path);
		
		return cost;
	}
	
	public List<Chromosome> getPopulation() {
		return population;
	}

	public void setPopulation(List<Chromosome> population) {
		this.population = population;
	}

	public Instance getData() {
		return data;
	}

	public void setData(Instance data) {
		this.data = data;
	}

	public void reproduction() {
		List<Chromosome> parents = selection();
		
		crossover(parents);
	}
	
	private void crossover(List<Chromosome> parents) {
		Random random = new Random();
		
		int index = random.nextInt(parents.get(0).size() - 1);
		
		List<Vertex> picked  = new ArrayList<Vertex>(parents.get(0).subList(index, parents.get(0).size()));

		for(int i = index; i < parents.get(0).size(); i++) {
			parents.get(0).set(i, parents.get(1).get(i));
			parents.get(1).set(i, picked.get(i - index));
		}
		
		picked.clear();
		
		new_population.add(parents.get(0));
		new_population.add(parents.get(1));
	}

	private List<Chromosome> selection() {
		List<Chromosome> parents = new ArrayList<Chromosome>();
		
		for(int i = 0; i < 2; i++) {
			parents.add(rouletteSelect());
		}
		
		return parents;
	}
	
	@SuppressWarnings("unused")
	private Chromosome binaryTournament() {
		Random random = new Random();
		
		int a = random.nextInt(population.size());
		int b = random.nextInt(population.size());
		
		while(b == a) {
			b = random.nextInt(population.size());
		}
		
		if(population.get(a).getCost() >= population.get(b).getCost()) {
			return population.get(a);
		}
		
		return population.get(b);
	}
	
	private Chromosome rouletteSelect() {
		Random random = new Random();
		
		double sum = 0;
		
		for(Chromosome chromossome : population) {
			sum += chromossome.getCost();
		}
		
		double value = random.nextDouble() * sum;
		
		for(Chromosome chromossome : population) {
			value -= chromossome.getCost();
			
			if(value <= 0) {
				return chromossome;
			}
		}
		
		return population.get(population.size() - 1);
	}
	
	public void refineSolution(Chromosome bestSolution) {
		Random random = new Random();
		
	    for(int i = 0; i < population.size(); i++) {
	    	if(random.nextDouble() < K.REPRODUCTION_MODIFYING_PERCENTAGE) {
	    		population.set(i, bestSolution);
	    	}
	    }
	}

	public List<Chromosome> getNew_population() {
		return new_population;
	}

	public void setNew_population(List<Chromosome> new_population) {
		this.new_population = new_population;
	}

	public void changePopulation() {
		Collections.copy(population, new_population);
		
		new_population.clear();
	}

	public void clear() {
		population.clear();
		new_population.clear();
		
		instance = null;
	}

	public void mutation() {
		Random random = new Random();
		
		for(Chromosome chromossome : new_population) {
			for(Vertex vertex : chromossome) {
				if(random.nextDouble() < K.MUTATION_RATE) {
					vertex.setProbability(random.nextDouble());
				}
			}
		}
	}
	
	public void localSearch(Chromosome chromosome) {
		boolean improvment;
		
		do {
			improvment = false;
					
			for(int i = 0; i < chromosome.size(); i++) {
				for(int j = i + 1; j < chromosome.size(); j++) {
					double cost = chromosome.getCost();
					List<Vertex> path = chromosome.getPath();
					
					double probability = chromosome.get(i).getProbability();
					
					chromosome.get(i).setProbability(chromosome.get(j).getProbability());
					chromosome.get(j).setProbability(probability);
					
					double fitness = calculateFitness(chromosome);
					
					if(fitness < chromosome.getCost()) {
						chromosome.setCost(fitness);
						
						improvment = true;
						
					} else {
						probability = chromosome.get(i).getProbability();
						
						chromosome.get(i).setProbability(chromosome.get(j).getProbability());
						chromosome.get(j).setProbability(probability);
						
						chromosome.setCost(cost);
						chromosome.setPath(path);
					}
				}
			}
		} while(improvment);
	}
}
