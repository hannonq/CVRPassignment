package br.com.ufop.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import br.com.ufop.constants.K;
import br.com.ufop.genetic.Chromosome;
import br.com.ufop.genetic.GeneticAlgorithm;
import br.com.ufop.utils.Instance;
import br.com.ufop.utils.Statistics;
import br.com.ufop.utils.Utils;

public class Main {
	private static Instance instance;
	
	private static HashMap<String, List<Chromosome>> results = new HashMap<String, List<Chromosome>>();
	private static HashMap<String, List<Long>> times = new HashMap<String, List<Long>>();
	
	public static void main(String[] args) throws IOException {
		List<String> instances = Utils.getInstances(new File(K.INSTANCES_PATH));

		for(int cont = 0; cont < K.TESTS_AMOUNT; cont++) {
			for(int l = 0; l < instances.size(); l++) {
				String inst = instances.get(l);
				
				System.out.println("Checking: " + (l + 1) + " of " + instances.size() + " (" + (cont + 1) + "/" + K.TESTS_AMOUNT + ") - " + inst);
				
				Long ini = System.currentTimeMillis();
				
				instance = new Instance(inst);
				
				double bestCost = Integer.MAX_VALUE;
				Chromosome bestSolution = new Chromosome();
				
				GeneticAlgorithm genetic = GeneticAlgorithm.getInstance(instance);
				
				for(int i = 0; i < K.GENERATIONS_AMOUNT; i++) {
					for(Chromosome chromossome : genetic.getPopulation()) {
						double cost = genetic.calculateFitness(chromossome);
						
						chromossome.setCost(cost);
						
						if(cost < bestCost) {
							bestCost = cost;
							bestSolution = chromossome;
						}
					}
					
//					genetic.refineSolution(bestSolution);
					
					for(int j = 0; j < K.POPULATION_AMOUNT / 2; j++) {
						genetic.reproduction();
					}
					
					genetic.mutation();
					
					genetic.changePopulation();
				}
				
				setTime(inst, System.currentTimeMillis() - ini);
				
				genetic.clear();
				
				genetic.localSearch(bestSolution);
				
				setResults(inst, bestSolution);
				
				/*System.out.println(instance.getName() + " - Cost: " + bestSolution.getCost());
		
				System.out.print("Path: ");
				for(Vertex vertex : bestSolution.getPath()) {
					System.out.print(vertex.getNumber() + " ");
				}
				System.out.println();*/
			}
		}
		
		processResults();
	}

	private static void setTime(String instance, long time) {
		if(times.containsKey(instance)) {
			times.get(instance).add(time);
		} else {
			times.put(instance, new ArrayList<Long>());
			times.get(instance).add(time);
		}
	}

	public static void setResults(String instance, Chromosome result) {
		if(results.containsKey(instance)) {
			results.get(instance).add(result);
		} else {
			results.put(instance, new ArrayList<Chromosome>());
			results.get(instance).add(result);
		}
	}
	
	private static void processResults() {
		List<String> result = new ArrayList<String>();
		
		try {
			Utils.writeFile("results.log", "", false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		for(Entry<String, List<Chromosome>> entry : results.entrySet()) {
			double[] data = new double[entry.getValue().size()];
			double sum = 0;
			double best = Integer.MAX_VALUE;
			
			int i = 0;
			for(Chromosome chromosome : entry.getValue()) {
				data[i++] = chromosome.getCost();
				sum += chromosome.getCost();
				
				if(chromosome.getCost() < best) {
					best = chromosome.getCost();
				}
			}
			
			result.add(entry.getKey() + "\t" + (sum / entry.getValue().size()) + "\t" + best + "\t" + new Statistics(data).getStdDev());
		}
		
		int i = 0;
		for(Entry<String, List<Long>> entry : times.entrySet()) {
			double sum = 0;
			
			for(Long time : entry.getValue()) {
				sum += time;
			}
			
			try {
				Utils.writeFile("results.log", result.get(i++) + "\t" + (sum / (entry.getValue().size() * 1000)) + "\n", true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("\n------- RESULTADOS GERADOS -------");
	}
}
