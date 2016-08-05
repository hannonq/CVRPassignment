package br.com.ufop.main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import br.com.ufop.classes.Vertex;
import br.com.ufop.constants.K;
import br.com.ufop.genetic.Chromosome;
import br.com.ufop.genetic.GeneticAlgorithm;
import br.com.ufop.utils.Instance;
import br.com.ufop.utils.Utils;

public class Main {
	private static Instance instance;
	
	public static void main(String[] args) throws IOException {
		List<String> instances = Utils.getInstances(new File(K.INSTANCES_PATH));

		for(String inst : instances) {
//		for(int k = 0; k < 10; k++) {
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
				
				for(int j = 0; j < K.POPULATION_AMOUNT / 2; j++) {
					genetic.reproduction();
				}
				
				genetic.mutation();
				
				genetic.changePopulation();
			}
			
			genetic.localSearch(bestSolution);
			
			genetic.clear();
			
			System.out.println("Instance: " + instance.getName() + " - Cost: " + bestSolution.getCost());

			for(Vertex vertex : bestSolution.getPath()) {
				System.out.print(vertex.getNumber() + " ");
			}
			System.out.println();
		}
	}
}
