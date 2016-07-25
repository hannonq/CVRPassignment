package br.com.ufop.main;

import br.com.ufop.classes.Vertex;
import br.com.ufop.constants.K;
import br.com.ufop.utils.Instance;
import br.com.ufop.utils.Utils;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	private static Instance instance;
	
	public static void main(String[] args) throws IOException {
		List<String> instances = Utils.getInstances(new File(K.INSTANCES_PATH));

		
		for(String inst : instances) {
			instance = new Instance(inst);
			

		}
        for(Vertex costumer : instance.getCustomers()) {
            costumer.setDistanceToDepot(calculateRatio(instance.getDepot(), costumer));
        }

        instance.sortCustomers();

        System.out.println("\n\n\n");
        System.out.println(instance.getCustomers());
        calculateRoute();

	}

	private static void calculateRoute() {
		List<List<Vertex>> routes = new ArrayList<List<Vertex>>();

        List<Vertex> route = new ArrayList<>();
        route.add(instance.getCustomers().get(0));
        instance.getCustomers().get(0).setVisited(true);

        List<Vertex> aux = new ArrayList<>();
        for(Vertex costumer : instance.getCustomers()) {
            Vertex vAux = costumer;
            if(!costumer.isVisited()) {
                vAux.setDistanceToDepot(calculateRatio(instance.getCustomers().get(0), costumer));
                aux.add(vAux);
            }
        }

        System.out.println("\n\n\n");
        System.out.println(aux);

		int smallest;
	}

	private static double calculateRatio(Vertex origin, Vertex destination) {
		return instance.getGraph()[origin.getNumber() - 1][destination.getNumber() - 1] / destination.getDemand();
	}
}
