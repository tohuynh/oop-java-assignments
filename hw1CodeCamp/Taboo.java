
/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
 */

import java.util.*;

public class Taboo<T> {
	//private List<T> rules;
	private Map<T, Set<T>> rulesMap;

	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		rulesMap = new HashMap<T, Set<T>>();
		for (int i = 0; i < rules.size() - 1; i++) {
			T elem = rules.get(i);
			if (elem != null && rules.get(i  + 1) != null) {
				if (!rulesMap.containsKey(elem)) {
					rulesMap.put(elem, new HashSet<T>());
				}
				rulesMap.get(elem).add(rules.get(i + 1));
			}
		}
	}

	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		return rulesMap.getOrDefault(elem, new HashSet<T>());
	}

	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		Iterator<T> it = list.iterator();
		T currentElem = null;
		while (it.hasNext()) {
			T elem = it.next();
			if (currentElem == null) {
				currentElem = elem;
			} else {
				if (rulesMap.getOrDefault(currentElem, new HashSet<T>()).contains(elem)) {
					it.remove();
				} else {
					currentElem = elem;
				}
			}
		}
		
	}
}
