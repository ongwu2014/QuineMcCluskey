import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class QuineMcCluskey {

	private static String[][] pIT;
	private static int rowSize = 0;
	private static int colSize = 0;
	private static Converter converter;

	private static ArrayList<String> primaryEPI;
	private static ArrayList<String> primaryEPISet;
	private static ArrayList<Integer> secondaryEPI;
	private static ArrayList<String> secondaryEPISet;
	private static ArrayList<ArrayList<String>> bucket;
	private static ArrayList<ArrayList<String>> bucketInt;

	public static void main(String[] args) {
		int variablesNumber = 0;
		String[] minterms = null;
		int[] mintermsAsInt = null;
		//String[] dontCare = null;
		//int[] dontCareAsInt = null;
		boolean control = true;

		Scanner input = new Scanner(System.in);
		converter = new Converter();
		String[][] initSequence;
		ArrayList<String> sequencer = new ArrayList<>();

		bucket = new ArrayList<>();
		bucketInt = new ArrayList<>();

		

		ArrayList<ArrayList<String>> columnTwo = new ArrayList<>();
		ArrayList<ArrayList<String>> columnTwoInt = new ArrayList<>();

		ArrayList<ArrayList<String>> primes = new ArrayList<>();
		ArrayList<ArrayList<String>> primesInt = new ArrayList<>();

		// Begin data collection
		// request the variables
		System.out.print("How many variables are in the min-terms: ");
		try {
			variablesNumber = Integer.parseInt(input.nextLine());
		} catch (Exception e) {
			while (variablesNumber <= 0) {
				System.out
						.println("ERROR: The number of variables should be an integer greater than 0.\n");
				System.out.print("How many variables are in the min-terms: ");
				try {
					variablesNumber = Integer.parseInt(input.nextLine());
					if (variablesNumber <= 0) {
						continue;
					}
					break;
				} catch (Exception f) {
					continue;
				}
			}
		}

		System.out.println();

		// request and collect the minterms
		while (control) {
			System.out
					.println("Enter the ON-set minterms (including dontcares) of F (seperated by commas) below");
			System.out
					.println("For example: 0, 2, 7, 4, 8, 10, 11, 12, 13, 15");

			try {
				minterms = input.nextLine().split(", ");
				mintermsAsInt = new int[minterms.length];
				for (int i = 0; i < minterms.length; i++) {
					mintermsAsInt[i] = Integer.parseInt(minterms[i]);
					if (mintermsAsInt[i] >= Math.pow(2, variablesNumber)
							|| mintermsAsInt[i] < 0) {
						throw new UnsupportedOperationException();
					}

					// TODO: perform reduction by duplicate elimination here
					if (mintermsAsInt.length > Math.pow(2, variablesNumber)) {
						throw new InterruptedException();
					}
				}
			} catch (UnsupportedOperationException e) {
				System.out
						.println("ERROR: Either one of more of the minterms is greater than 2^V or an integer less than 0.\n");
				continue;
			} catch (NumberFormatException e) {
				System.out
						.println("ERROR: Either one of more of the minterms is not an integer.\n");
				continue;
			} catch (InterruptedException e) {
				System.out
						.println("ERROR: Either one of more of the minterms is not an integer.\n");
				continue;
			}
			control = false;
		}
		
		Arrays.sort(mintermsAsInt);
		
		initSequence = new String[mintermsAsInt.length][mintermsAsInt.length];
		for (int i = 0; i < Math.pow(2, variablesNumber); i++) {
			sequencer.add("");
		}
		for (int i = 0; i < minterms.length; i++) {
			initSequence[i][0] = minterms[i];
			initSequence[i][1] = String.format("%" + variablesNumber + "s",
					Integer.toBinaryString(Integer.parseInt(minterms[i])))
					.replace(' ', '0');
			sequencer.add(Integer.parseInt(minterms[i]), initSequence[i][1]);
		}

		// create a bucket
		for (int i = 0; i <= variablesNumber; i++) {
			bucket.add(new ArrayList<>());
			bucketInt.add(new ArrayList<>());
		}

		for (int i = 0; i < minterms.length; i++) {
			bucket.get(converter.countOccurrence(initSequence[i][1], '1')).add(
					initSequence[i][1]);
			bucketInt.get(converter.countOccurrence(initSequence[i][1], '1'))
					.add(initSequence[i][0]);
		}

		for (int i = 0; i < bucket.size(); i++) {
			if (bucket.get(i).size() <= 0) {
				bucket.remove(i);
				bucketInt.remove(i);
				i--;
			}
		}

		// begin reduction
		// TODO:this goes in a loop;;;
		int mark = 0;
		while (mark >= 0) {
			 mark = 0;
			ArrayList<ArrayList<String>> column = new ArrayList<>();
			ArrayList<ArrayList<String>> columnInt = new ArrayList<>();
			
			for (int i = 0; i < bucket.size() - 1; i++) {
				column.add(new ArrayList<>());
				columnInt.add(new ArrayList<>());
			}
			
			for (int i = 0; i < bucket.size() - 1; i++) {
				for (int j = 0; j < bucket.get(i).size(); j++) {
					for (int k = 0; k < bucket.get(i + 1).size(); k++) {
						int diff = converter.Differentiator(bucket.get(i).get(j),
								bucket.get(i + 1).get(k));
						if (diff == 1) {
							columnInt.get(i).add(
									bucketInt.get(i).get(j) + ","
											+ bucketInt.get(i + 1).get(k));
							column.get(i).add(
									converter.merger(bucket.get(i).get(j), bucket
											.get(i + 1).get(k)));
							mark++;
						}
					}
				}
			}
			
			if (mark == 0) {
				for (int i = 0; i < bucket.size(); i++) {
					columnTwo.add(new ArrayList<>());
					columnTwoInt.add(new ArrayList<>());
				}
				
				columnTwo = bucket;
				columnTwoInt = bucketInt;
				
				mark = -1;
			} else {
				bucket = column;
				bucketInt = columnInt;
			}
			
		}

		

	

		// Eliminate Duplicate

		bucket = new ArrayList<>();
		bucketInt = new ArrayList<>();

		for (int i = 0; i < columnTwo.size(); i++) {
			bucket.add(new ArrayList<>());
			bucketInt.add(new ArrayList<>());
		}

		for (int i = 0; i < columnTwo.size(); i++) {
			for (int j = 0; j < columnTwo.get(i).size(); j++) {
				if (!bucket.get(i).contains(columnTwo.get(i).get(j))) {
					bucket.get(i).add(columnTwo.get(i).get(j));
					bucketInt.get(i).add(columnTwoInt.get(i).get(j));
				}
			}
		}

		int primeSize = 0;
		for (int i = 0; i < bucket.size(); i++) {
			primeSize += bucket.get(i).size();
		}

		pIT = new String[minterms.length + 1][primeSize + 1];
		int[] placeHolder = new int[(int) Math.pow(2, variablesNumber)];

		for (int i = 0; i < pIT.length; i++) {
			for (int j = 0; j < pIT[i].length; j++) {
				pIT[i][j] = "0";
			}
		}

		for (int i = 0; i < minterms.length; i++) {
			placeHolder[mintermsAsInt[i]] = i + 1;
		}

		for (int i = 1; i <= minterms.length; i++) {
			pIT[i][0] = Integer.toString(mintermsAsInt[i - 1]);
		}

		int m = 1;
		for (int i = 1; i <= bucket.size(); i++) {
			for (int j = 0; j < bucketInt.get(i - 1).size(); j++) {
				pIT[0][m] = bucketInt.get(i - 1).get(j);
				m++;
			}
		}

		for (int i = 1; i < pIT[0].length; i++) {
			String[] strArray = pIT[0][i].split(",");
			int[] intArray = new int[strArray.length];
			for (int j = 0; j < strArray.length; j++) {
				intArray[j] = Integer.parseInt(strArray[j]);
			}

			for (int j = 0; j < strArray.length; j++) {
				pIT[placeHolder[intArray[j]]][i] = "X";
			}

		}

		/*
		 * for (String[] x : pIT) { for (String y : x) { System.out.print(y +
		 * "				"); } System.out.println(); }
		 */
		System.out.println();
		System.out.println();
		
		rowSize = pIT.length;
		colSize = pIT[0].length;
		
		for (int i = 0; i < rowSize; i++) {

			for (int j = 0; j < colSize; j++) {
				System.out.print(pIT[i][j] + "				");
			}
			System.out.println();
		}

		// TODO: Remove Dont cares
		

		// STEP 3: Reduce Prime Implicant Table
		// Iteration #1

		primaryEPI = new ArrayList<>();
		for (int i = 1; i < rowSize; i++) {
			int count = 0;
			int whereFound = 0;
			for (int j = 1; j < colSize; j++) {
				if (pIT[i][j].equals("X")) {
					count++;
				}
				if (count == 1) {
					whereFound = j;
				}
			}
			if (count == 1) {
				control = true;
				for (int k = 0; k < primaryEPI.size(); k++) {
					int newFound = 0;
					int isPEPI = Integer.parseInt(primaryEPI.get(k));
					
					for (int m1 = 1; m1 < colSize; m1++) {
						if (pIT[placeHolder[isPEPI]][m1].equals("X")) {
							newFound = m1;
						}
					}
					if (newFound == whereFound) {
						control = false;
					}
				}
				if (control)
					primaryEPI.add(pIT[i][0]);
			}
		}

		primaryEPISet = new ArrayList<>();
		for (int i = 0; i < primaryEPI.size(); i++) {

			for (int j = 1; j < pIT[0].length; j++) {
				if (pIT[placeHolder[Integer.parseInt(primaryEPI.get(i))]][j]
						.equals("X")) {
					primaryEPISet.add(pIT[0][j]);
					break;
				}

			}

		}

		

		// if primary EPI covers row, print them as solution and stop.
		
		

		if (primaryEPISet.size() == rowSize - 1) {
			ArrayList<String> finalSolution = new ArrayList<>();

			for (int i = 0; i < primaryEPISet.size(); i++) {
				for (int j = 0; j < bucketInt.size(); j++) {
					ArrayList<String> thenThis = bucketInt.get(j);
					for (int k = 0; k < thenThis.size(); k++) {

						if (thenThis.get(k).equals(primaryEPISet.get(i))) {
							finalSolution.add(bucket.get(j).get(k));
						}
					}
				}

			}

			// print solutions
			System.out.println();
			String toPrint = "F = ";
			for (int j = 0; j < finalSolution.size(); j++) {
				toPrint += converter.exchanger(finalSolution.get(j)) + " + ";
			}

			System.out.println(toPrint.substring(0, toPrint.length() - 3));
			
		} else {

			for (int i = 0; i < primaryEPI.size(); i++) {
				for (int j = 1; j < pIT[i].length; j++) {
					boolean controller = false;
					String[] temp = pIT[0][j].split(",");
					for (int v = 0; v < temp.length; v++) {
						if (temp[0].equals(primaryEPI.get(i))) {
							controller = true;
							break;
						}
					}

					if (controller) {
						// begin strip

						for (int k = 1; k < rowSize; k++) {
							if (pIT[k][j].equals("X")) {
								for (int l = k; l < rowSize - 1; l++) {
									for (int n = 0; n < pIT[0].length; n++) {
										pIT[l][n] = pIT[l + 1][n];
									}
								}
								rowSize--;
								k--;
							}
						}

						for (int k = 0; k <= colSize; k++) {
							for (int l = j; l < colSize - 1; l++) {
								pIT[k][l] = pIT[k][l + 1];
							}
						}
						colSize--;
					}
				}
			}

			/*
			 * for (int i = 0; i < rowSize; i++) {
			 * 
			 * for (int j = 0; j < colSize; j++) { System.out.print(pIT [i][j]+
			 * "				"); } System.out.println(); }
			 */

			// row dominance
			int index = 1;

			while (index > -1) {
				index = rowDominance(index);
			}

			for (int i = 0; i < rowSize; i++) {

				for (int j = 0; j < colSize; j++) {
					System.out.print(pIT[i][j] + "				");
				}
				System.out.println();
			}

			// column dominace

			index = 1;

			while (index > -1) {
				index = colDominance(index);
			}

			for (int i = 0; i < rowSize; i++) {

				for (int j = 0; j < colSize; j++) {
					System.out.print(pIT[i][j] + "				");
				}
				System.out.println();
			}

			// Iteration number 2

			// check and remove secondary EPI
			secondaryEPI = new ArrayList<>();

			for (int i = 1; i < rowSize; i++) {
				int count = 0;
				for (int j = 1; j < colSize; j++) {
					if (pIT[i][j].equals("X")) {
						count++;
					}
				}
				if (count == 1) {
					secondaryEPI.add(i);
				}
			}
			secondaryEPISet = new ArrayList<>();
			for (int i = 0; i < secondaryEPI.size(); i++) {
				secondaryEPISet.add(pIT[0][secondaryEPI.get(i)]);
			}

			if (secondaryEPI.size() == rowSize - 1) {
				finalSolution();
			} else {
				// remove secondary EPI

				secondaryEPI();

				// 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13

				// Petrick's Method

				petricksMethod();
			}

		}
		input.close();

	}

	private static int colDominance(int index) {
		if (index >= colSize) {
			return -1;
		}

		ArrayList<Integer> posOfX = new ArrayList<>();
		for (int i = 1; i < rowSize; i++) {
			if (pIT[i][index].equals("X")) {
				posOfX.add(i);
			}
		}

		for (int i = 1; i < colSize; i++) {
			int domCounter = 0;
			if (i == index) {
				continue;
			}
			for (int j = 0; j < posOfX.size(); j++) {
				if (pIT[posOfX.get(j)][i].equals("X")) {
					domCounter++;
				}
			}

			if (domCounter == posOfX.size()) {
				// remove
				for (int l = i; l < colSize - 1; l++) {
					for (int n = 0; n < rowSize; n++) {
						pIT[n][l] = pIT[n][l + 1];
					}
				}
				colSize--;
				return index;
			}
		}
		return index + 1;
	}

	public static String extractNumber(final String str) {

		if (str == null || str.isEmpty())
			return "";

		StringBuilder sb = new StringBuilder();
		boolean found = false;
		for (char c : str.toCharArray()) {
			if (Character.isDigit(c)) {
				sb.append(c);
				found = true;
			} else if (found) {
				// If we already found a digit before and this char is not a
				// digit, stop looping
				break;
			}
		}

		return sb.toString();
	}

	private static void finalSolution() {
		ArrayList<String> finalSolution = new ArrayList<>();

		for (int i = 0; i < primaryEPISet.size(); i++) {
			for (int j = 0; j < bucketInt.size(); j++) {
				ArrayList<String> thenThis = bucketInt.get(j);
				for (int k = 0; k < thenThis.size(); k++) {

					if (thenThis.get(k).equals(primaryEPISet.get(i))) {
						finalSolution.add(bucket.get(j).get(k));
					}
				}
			}

		}

		for (int i = 1; i < colSize; i++) {
			String look = pIT[0][i];
			boolean onlooker = false;

			for (int j = 0; j < bucketInt.size(); j++) {
				ArrayList<String> thenThis = bucketInt.get(j);
				for (int k = 0; k < thenThis.size(); k++) {
					if (thenThis.get(k).equals(look)) {
						finalSolution.add(bucket.get(j).get(k));
						onlooker = true;
						break;
					}
				}
				if (onlooker)
					break;
			}
		}

		// print solutions
		System.out.println();
		String toPrint = "F = ";
		for (int j = 0; j < finalSolution.size(); j++) {
			toPrint += converter.exchanger(finalSolution.get(j)) + " + ";
		}

		System.out.println(toPrint.substring(0, toPrint.length() - 3));

	}

	private static void petricksMethod() {
		ArrayList<String> petricks = new ArrayList<>();

		for (int i = 1; i < rowSize; i++) {
			int mark = 0;
			int nextMark = 0;
			for (int j = 1; j < colSize; j++) {
				if (pIT[i][j].equals("X")) {
					if (mark == 0) {
						mark = j;
					} else {
						nextMark = j;
						break;
					}
				}
			}
			petricks.add("P" + mark + " + P" + nextMark);
		}

		String toPrint = "P = ";
		for (int i = 0; i < petricks.size(); i++) {
			toPrint += "(" + petricks.get(i) + ")";

		}
		System.out.println(toPrint);

		ArrayList<Integer> used = new ArrayList<>();
		ArrayList<String> pet = new ArrayList<>();

		for (int i = 1; i < petricks.size(); i++) {
			if (!used.contains(i)) {
				String[] arr = null;
				String toAdd = "P" + i + " + ";
				used.add(i);
				for (int j = 0; j < petricks.size(); j++) {
					if (petricks.get(j).contains("P" + i)) {
						arr = petricks.get(j).split(" ");

						if (arr[arr.length - 1].equals("P" + i)) {
							toAdd += arr[0];
							used.add(Integer.parseInt(extractNumber(arr[0])));
						} else {
							toAdd += arr[arr.length - 1];
							used.add(Integer
									.parseInt(extractNumber(arr[arr.length - 1])));
						}

					}

				}

				pet.add(toAdd);
			} else {
				continue;
			}
		}
		toPrint = "P = ";
		for (int j = 0; j < pet.size(); j++) {
			toPrint += "(" + pet.get(j) + ")";
		}
		System.out.println(toPrint);

		// start petricking
		ArrayList<String> newPet = new ArrayList<>();
		boolean controlThis = true;
		while (controlThis) {

			for (int i = 0; i < pet.size(); i += 2) {
				if (i == pet.size() - 1) {
					newPet.add(pet.get(i));
					break;
				}
				newPet = new ArrayList<>();
				String toAdd = "";
				String[] arr1 = pet.get(i).split(" ");
				String[] arr2 = pet.get(i + 1).split(" ");
				for (int j = 0; j < arr1.length; j++) {
					if (arr1[j].equals("+"))
						continue;
					for (int k = 0; k < arr2.length; k++) {
						if (arr2[k].equals("+"))
							continue;
						toAdd += arr1[j];
						String[] splitter = arr2[k].split("(?<=\\G.{2})");

						for (int l = 0; l < splitter.length; l++)
							if (!arr1[j].contains(splitter[l]))
								toAdd += splitter[l];

						toAdd += " + ";
					}
				}
				newPet.add(toAdd.substring(0, toAdd.length() - 3));

			}

			toPrint = "P = ";
			for (int j = 0; j < newPet.size(); j++) {
				toPrint += "(" + newPet.get(j) + ")";
			}
			pet = newPet;
			System.out.println(toPrint);
			if (pet.size() <= 1) {
				break;
			}
		}

		// eliminations
		
		String forThis = pet.get(0).replaceAll("\\p{Z}", "");
		String[] individuals = forThis.split("\\+");

		int[] forThat = new int[individuals.length];
		int small = 200;
		for (int i = 0; i < forThat.length; i++) {
			forThat[i] = converter.countOccurrence(individuals[i], 'P');
			if (forThat[i] < small) {
				small = forThat[i];
			}
		}

		ArrayList<Integer> withSmall = new ArrayList<>();
		for (int i = 0; i < forThat.length; i++) {
			if (forThat[i] == small)
				withSmall.add(i);
		}
		ArrayList<Integer> toRemove = new ArrayList<>();
		for (int i = 0; i < withSmall.size(); i++) {
			// TODO: dont split wth two's
			String[] splitter = individuals[withSmall.get(i)]
					.split("(?<=\\G.{2})");

			for (int j = 0; j < individuals.length; j++) {
				int count = 0;
				for (int k = 0; k < splitter.length; k++) {
					if (j == withSmall.get(i))
						continue;
					if (individuals[j].contains(splitter[k])) {
						count++;
					}

				}
				if (count == small)
					toRemove.add(j);
			}
		}

		String[] finalArr = new String[individuals.length - toRemove.size()];
		int m = 0;
		for (int i = 0; i < individuals.length; i++) {
			if (!toRemove.contains(i)) {
				finalArr[m] = individuals[i];
				m++;
			}
		}
		toPrint = "P = ";
		for (int i = 0; i < finalArr.length; i++) {
			toPrint += finalArr[i] + " + ";
		}

		System.out.println(toPrint.substring(0, toPrint.length() - 3));
		System.out.println();

		ArrayList<String> finalSolution = new ArrayList<>();

		for (int i = 0; i < secondaryEPISet.size(); i++) {
			for (int j = 0; j < bucketInt.size(); j++) {
				ArrayList<String> thenThis = bucketInt.get(j);
				for (int k = 0; k < thenThis.size(); k++) {

					if (thenThis.get(k).equals(secondaryEPISet.get(i))) {
						finalSolution.add(bucket.get(j).get(k));
					}
				}
			}

		}

		for (int i = 0; i < withSmall.size(); i++) {
			ArrayList<String> finalSolution2 = new ArrayList<>();
			String[] splitter = individuals[withSmall.get(i)]
					.split("(?<=\\G.{2})");
			for (int j = 0; j < splitter.length; j++) {
				int position = Integer.parseInt(extractNumber(splitter[j]));
				String test = pIT[0][position];

				for (int k = 0; k < bucketInt.size(); k++) {
					ArrayList<String> thenThis = bucketInt.get(k);
					for (int l = 0; l < thenThis.size(); l++) {

						if (thenThis.get(l).equals(test)) {
							finalSolution2.add(bucket.get(k).get(l));
						}
					}
				}
			}

			// print solutions
			toPrint = "F = ";
			for (int j = 0; j < finalSolution.size(); j++) {
				toPrint += converter.exchanger(finalSolution.get(j)) + " + ";
			}
			for (int j = 0; j < finalSolution2.size(); j++) {
				toPrint += converter.exchanger(finalSolution2.get(j)) + " + ";
			}

			System.out.println(toPrint.substring(0, toPrint.length() - 3));
		}

	}

	private static int rowDominance(int index) {
		if (index >= rowSize) {
			return -1;
		}
		ArrayList<Integer> posOfX = new ArrayList<>();
		for (int i = 1; i < colSize; i++) {
			if (pIT[index][i].equals("X")) {
				posOfX.add(i);
			}
		}

		for (int i = 1; i < rowSize; i++) {
			int domCounter = 0;
			if (i == index) {
				continue;
			}
			for (int j = 0; j < posOfX.size(); j++) {
				if (pIT[i][posOfX.get(j)].equals("X")) {
					domCounter++;
				}
			}

			if (domCounter == posOfX.size()) {
				// remove
				for (int l = i; l < rowSize - 1; l++) {
					for (int n = 0; n < colSize; n++) {
						pIT[l][n] = pIT[l + 1][n];
					}
				}
				rowSize--;
				return index;
			}
		}
		return index + 1;
	}

	private static void secondaryEPI() {

		for (int i = 0; i < secondaryEPI.size(); i++) {
			ArrayList<Integer> posOfX = new ArrayList<>();
			for (int j = 1; j < rowSize; j++) {
				if (pIT[j][secondaryEPI.get(i)].equals("X")) {
					posOfX.add(j);
				}
			}

			for (int j = posOfX.size() - 1; j >= 0; j--) {
				int index = posOfX.get(j);
				for (int l = index; l < rowSize - 1; l++) {
					for (int n = 0; n < colSize; n++) {
						pIT[l][n] = pIT[l + 1][n];
					}
				}
				rowSize--;
			}

			for (int l = secondaryEPI.get(i); l < colSize - 1; l++) {
				for (int n = 0; n < rowSize; n++) {
					pIT[n][l] = pIT[n][l + 1];
				}
			}
			colSize--;

		}

		for (int i = 0; i < rowSize; i++) {

			for (int j = 0; j < colSize; j++) {
				System.out.print(pIT[i][j] + "				");
			}
			System.out.println();
		}
	}

}
