import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		int sameCount = 0;
		Map<T, Integer> aCount = countElement(a);
		Map<T, Integer> bCount = countElement(b);
		for (T element: aCount.keySet()) {
			if (bCount.containsKey(element) && aCount.get(element) == bCount.get(element)) {
				sameCount++;
			}
		}
		
		return sameCount;
	}
	
	private static <T> Map<T, Integer> countElement(Collection<T> collection) {
		Map<T, Integer> count = new HashMap<T, Integer>();
		for (T element: collection) {
			if (count.containsKey(element)) {
				count.put(element, count.get(element) + 1);
			} else {
				count.put(element, 1);
			}
		}
		
		return count;
	}
	
}
