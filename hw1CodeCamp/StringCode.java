import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		int currentRun = 0;
		int maxRun = 0;
		
		if (str.length() > 0 ) {
			currentRun++;
			maxRun++;
		}
		
		for (int i = 1; i < str.length(); i++) {
			if (str.charAt(i) == str.charAt(i-1)) {
				currentRun++;
			} else {
				if (currentRun > maxRun) maxRun = currentRun;
				currentRun = 1;
			}
			
		}
		
		return maxRun;
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (Character.isDigit(str.charAt(i))) {
				if (i < str.length() - 1) {
					char[] repeats = new char[Character.getNumericValue(str.charAt(i))];
					Arrays.fill(repeats, str.charAt(i+1));
					sb.append(repeats);
				}
			} else {
				sb.append(str.charAt(i));
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		Set<String> aSubStrings = new HashSet<String>();
		
		for (int i = 0; i <= a.length() - len; i++) {
			aSubStrings.add(a.substring(i, i + len));
		}
		
		for (int i = 0; i <= b.length() - len; i++) {
			if (aSubStrings.contains(b.substring(i, i + len))) {
				return true;
			}
		}
		
		return false;
	}
}
