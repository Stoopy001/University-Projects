//This is my example Solution
import org.jetbrains.annotations.NotNull;

import java.lang.Math;
import java.util.*; 


class Example {

		public static double sumFit(ArrayList<boolean[]> list){
			double sum = 0;
			for (int i = 0 ; i < list.size() ; i++){
				sum += calcFit2(list.get(i));
			}
			return sum;
		}
		public static double sumFit1(ArrayList<double[]> list){
			double sum = 0;
			for (int i = 0; i<list.size();i++){
				sum += Assess.getTest1(list.get(i));
			}
			return sum;
		}
		public static boolean[] mutate2(boolean[] list, double mutationRate){
			int index = (int) (Math.random() * list.length);
			boolean isMutate = Math.random() < mutationRate;
			if (isMutate){
				list[index] = !list[index];
			}
			return list;
		}
		public static ArrayList<boolean[]> crossOver(boolean[] x, boolean[] y){
			int crossOverPoint = (int) (Math.random() * 81);
			boolean[] child1 = new boolean[x.length];
			boolean[] child2 = new boolean[x.length];
			ArrayList<boolean[]> lToReturn = new ArrayList<>();
			System.arraycopy(x, 0, child1, 0, crossOverPoint);
			System.arraycopy(y, crossOverPoint, child1, crossOverPoint, x.length - crossOverPoint);
			System.arraycopy(y, 0, child2, 0, crossOverPoint);
			System.arraycopy(x, crossOverPoint, child2, crossOverPoint, x.length - crossOverPoint);
			lToReturn.add(child1);
			lToReturn.add(child2);
			return lToReturn;
		}
		public static ArrayList<double[]> crossOver1(double[] arr1, double[] arr2){
			int crossOverPoint = (int) (Math.random() * arr1.length);
			double[] child1 = new double[arr1.length];
			double[] child2 = new double[arr1.length];
			ArrayList<double[]> returnList = new ArrayList<>();
			System.arraycopy(arr1, 0, child1, 0, crossOverPoint);
			System.arraycopy(arr2, crossOverPoint, child1, crossOverPoint, arr2.length - crossOverPoint);
			System.arraycopy(arr2, 0, child2, 0, crossOverPoint);
			System.arraycopy(arr1, crossOverPoint, child2, crossOverPoint, arr2.length - crossOverPoint);
			returnList.add(child1);
			returnList.add(child2);
			return returnList;
		}

		public static boolean[] createPop(int popSize) {
			boolean[] sol2 = new boolean[100];
			int index;
			Arrays.fill(sol2, Boolean.FALSE);
			double[] currentFit;
			for (int j = 0; j < popSize; j++) {
				currentFit = Assess.getTest2(sol2);
				index = (int) (Math.random() * popSize);
				if (currentFit[0] < 400) {
					sol2[index] = (Math.random() < 0.5);
				}


			}
			return sol2;
		}

		public static double calcFit2(boolean[] arr){
			double[] temp = Assess.getTest2(arr);

			if (temp[0] >= 500.0){
				if ((temp[1] - 2*(temp[0] - 500) <= 0)){
					return 0.0;
				}else{
					return (temp[1] - 2*(temp[0] - 500));
				}
			}
			return temp[1];
		}
		public static boolean[] getHighestFitness(ArrayList<boolean[]> list){
			int index = 0;
			double highest = calcFit2(list.get(index));
			double currFit;
			for (int i = 1 ; i<list.size();i++){
				currFit = calcFit2(list.get(i));
				if (currFit > highest){
					highest = currFit;
					index = i;
				}
			}
			return list.get(index);
		}

		public static ArrayList<double[]> generateSolution(int solSize, int popSize){
			ArrayList<double[]> ret = new ArrayList<>();

			double[] sol1 = new double[solSize];
			for (int i = 0 ; i < popSize; i++) {
				for (int j = 0; j < solSize; j++) {
					sol1[j] = Math.random() * Math.round(5.12 * (Math.random() - Math.random()));
				}
				ret.add(sol1);
			}
			return ret;
		}

		public static double[] selectHighestFitness(ArrayList<double[]> list){
			double currentFitness;
			int indexFound = 0;
			double highest = Assess.getTest1(list.get(indexFound));
			for (int i = 1; i<list.size();i++){
				currentFitness = Assess.getTest1(list.get(i));
				if (currentFitness <= highest){
					indexFound = i;
					highest = currentFitness;
				}
			}
			return list.get(indexFound);
		}
		public static double[] mutate(double mutationRate, double[] tmp, double ratio) {
			boolean isMutate = Math.random() < mutationRate;
			int solIndex = (int) (Math.random() * tmp.length);
			double change = (Math.random() * Math.round(5.12 * (Math.random() - Math.random()))) * Math.random() * ratio;
			if (isMutate) {
				if (((tmp[solIndex] + change < 5) && (tmp[solIndex] - change > -5))) {
					if (Math.random() < 0.5) {
						tmp[solIndex] += change;
					} else {
						tmp[solIndex] -= change;
					}
				}
			}
			return tmp;
		}

		public static void main(String[] args) {
			//Do not delete/alter the next line
			long startT = System.currentTimeMillis();

			//Edit this according to your name and login
			String name = "Costi Lacatusu";
			String login = "cfl21";

			System.out.println("These are the instructions of how to use the problem library.  Please make sure you follow the instructions carefully.");


			System.out.println("For the first problem, you need to use Assess.getTest1(double[]).");

			//An example solution consists of an array  of doubles of size 20
			//Allowed values are between -5 and +5 (this is not actually checked, but no point in going beyond these values)
			//Lower fitness is better. The optimal fitness is 0 (i.e. no negative fitness values).
			int solSize = 20;
			//generate a sample solution like so:

			int populationSize = 50;
			double fitnessSum;
			double ratio = 1;

			ArrayList<double[]> solList = new ArrayList<>();
			ArrayList<double[]> newPopulation = new ArrayList<>();
			double crossOverRate1 = 0.7;
			double mutationRate = 0.4;
			int j = 0;
			solList.addAll(generateSolution(solSize, populationSize));
			double fit = Assess.getTest1(selectHighestFitness(solList));
			while (fit > 0) {
				while (fit > (ratio / 10000)) {
					newPopulation.add(selectHighestFitness(solList));
					fitnessSum = sumFit1(solList);
					while (newPopulation.size() < populationSize) {
						double picked = Math.random();
						double currFitness = Assess.getTest1(solList.get(j));
						if (picked > currFitness / fitnessSum) {
							newPopulation.add(mutate(mutationRate, solList.get(j), ratio));
							if (newPopulation.size() >= 2 && (Math.random() < crossOverRate1)) {
								ArrayList<double[]> crossList = crossOver1(selectHighestFitness(newPopulation), newPopulation.get((int) (Math.random() * newPopulation.size())));
								newPopulation.add(mutate(mutationRate, crossList.get(0), ratio));
								newPopulation.add(mutate(mutationRate, crossList.get(1), ratio));
							}
							if (j == populationSize - 1) {
								j = 0;

							} else {
								j++;
							}

						}
					}
					solList.clear();
					solList.addAll(newPopulation);
					newPopulation.clear();
					fit = Assess.getTest1(selectHighestFitness(solList));
					System.out.println(fit);
				}
				ratio = ratio / 1.5;
			}
			double[] sol1 = selectHighestFitness(solList);



			
			System.out.println("The fitness of your example Solution is: "+ fit);

				
			System.out.println(" ");	
			System.out.println(" ");	
			System.out.println("Now let us turn to the second problem:");
			System.out.println("A sample solution in this case is a boolean array of size 100.");
			System.out.println("I now create a random sample solution and get the weight and utility:");



			//Creating a sample solution for the second problem
			//The higher the fitness, the better, but be careful of  the weight constraint!
			boolean[] sol2 = new boolean[100];
			//Now checking the fitness of the candidate solution
			double sum1;
			int popSize = 50;
			double mutationRate2 = 0.5;
			double crossOverRate = 0.5;
			int x = 0;

			ArrayList<boolean[]> tmpPop = new ArrayList<>();
			ArrayList<boolean[]> mainPop = new ArrayList<>();

			for (int i = 0; i<popSize;i++){
				mainPop.add(createPop(100));
			}

			System.arraycopy(getHighestFitness(mainPop), 0, sol2, 0, 100 );
			double[] tmp =Assess.getTest2(sol2);
			double fit2 = calcFit2(getHighestFitness(mainPop));

			while (fit2 < 205){

				sum1 = sumFit(mainPop);
				while (tmpPop.size() < popSize) {
					double pick = Math.random();

					if (pick >= (calcFit2(mainPop.get(x))/sum1)){
						tmpPop.add(mutate2(mainPop.get(x), mutationRate2));
						if (Math.random() < crossOverRate && tmpPop.size() >= 2){
							tmpPop.addAll(crossOver(tmpPop.get(tmpPop.size() - 1), getHighestFitness(tmpPop)));
						}
						if (x == popSize - 1){
							x = 0;
						}else{
							x++;
						}
					}
				}

				mainPop.clear();
				mainPop.addAll(tmpPop);
				tmpPop.clear();
				tmp = Assess.getTest2(getHighestFitness(mainPop));
				fit2 = calcFit2(getHighestFitness(mainPop));
				System.out.println(fit2);
			}

			sol2 = getHighestFitness(mainPop);

			//The index 0 of tmp gives the weight. Index 1 gives the utility
			System.out.println("The weight is: " + tmp[0]);
			System.out.println("The utility is: " + tmp[1]);

			//Once completed, your code must submit the results you generated, including your name and login: 
			//Use and adapt  the function below:
			Assess.checkIn(name,login,sol1,sol2);
      
		         //Do not delete or alter the next line
		        long endT= System.currentTimeMillis();
			System.out.println("Total execution time was: " +  ((endT - startT)/1000.0) + " seconds");



	  }


}
