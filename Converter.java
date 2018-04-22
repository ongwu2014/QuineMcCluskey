public class Converter {

	public int DecimalToBinary (int decimal) {
		
		
		return 0;
	}
	
	public void BinaryToDecimal (int binary) {
		
	}
	
	public int countOccurrence (String string, char ch) {
		int occurrence = 0;
		
		int i = 0;
		
		while (i < string.length()) {
			if (string.charAt(i) == ch) {
				occurrence++;
			}
			i++;
		}
		
		return occurrence;
	}
	
	public int Differentiator (String a, String b) {
		int difference = 0;
		
		int i = 0;
		while (i < a.length()) {
			if (!(a.charAt(i) == (b.charAt(i)))) {
				difference++;
			}
			i++;
		}
		
		return difference;
	}
	
	public String exchanger (String a) {
		
		String [] alphabets = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
		
		String string = "";
		
		String [] arr = a.split("(?!^)");
		
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals("-"))
				continue;
			
			if (arr[i].equals("1"))
				string += alphabets[i];
			
			if (arr[i].equals("0"))
				string += alphabets[i] + "\'";
		}
		
		return string;
	}
	
	public String merger (String a, String b) {
		StringBuilder string = new StringBuilder();
		
		int i = 0;
		while (i < a.length()) {
			if (a.charAt(i) == b.charAt(i)) {
				string.append(a.charAt(i));
			} else {
				string.append('-');
			}
			i++;
		}
		return string.toString();
	}
}
