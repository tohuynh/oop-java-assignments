// TabooTest.java
// Taboo class tests -- nothing provided.

import java.util.*;

import junit.framework.TestCase;

public class TabooTest extends TestCase {
	
	public void testNoFollow1() {
		List<String> rules = Arrays.asList("a", "c", "a", "b");
		Set<String> noFollows = new HashSet<String>();
		noFollows.addAll(Arrays.asList("c","b"));
		
		Taboo<String> t = new Taboo<String>(rules);
		assertEquals(noFollows, t.noFollow("a"));
		assertEquals(Collections.emptySet(), t.noFollow("x"));
	}
	
	public void testNoFollow2() {
		List<Integer> rules = Arrays.asList(1, 2, 1, 3, 1, 2);
		Set<Integer> noFollows = new HashSet<Integer>();
		noFollows.addAll(Arrays.asList(2,3));
		
		Taboo<Integer> t = new Taboo<Integer>(rules);
		assertEquals(noFollows, t.noFollow(1));
		assertEquals(Collections.emptySet(), t.noFollow(4));
	}
	
	public void testNoFollow3() {
		List<String> rules = Arrays.asList("a", "b", null, "c", "d");
		Set<String> noFollows = new HashSet<String>();
		noFollows.addAll(Arrays.asList("d"));
		
		Taboo<String> t = new Taboo<String>(rules);

		//System.out.println(t.noFollow(null));
		assertEquals(noFollows, t.noFollow("c"));
		assertEquals(Collections.emptySet(), t.noFollow("b"));
		assertEquals(Collections.emptySet(), t.noFollow(null));
	}
	
	public void testReduce1() {
		List<String> rules = Arrays.asList("a", "c", "a", "b");
		Taboo<String> t = new Taboo<String>(rules);
		
		List<String> before = new ArrayList<String>(Arrays.asList("a", "c", "b", "x", "c", "a"));
		List<String> after = new ArrayList<String>(Arrays.asList("a", "x", "c"));
		t.reduce(before);
		assertEquals(after, before);
		assertTrue(Arrays.deepEquals(after.toArray(), before.toArray()));
		
	}
	
	public void testReduce2() {
		List<Integer> rules = Arrays.asList(1, 2, 1, 4);
		Taboo<Integer> t = new Taboo<Integer>(rules);
		
		List<Integer> before = new ArrayList<Integer>(Arrays.asList(1, 4, 5, 2, 1));
		List<Integer> after = new ArrayList<Integer>(Arrays.asList(1, 5, 2));
		t.reduce(before);
		assertEquals(after, before);
		assertTrue(Arrays.deepEquals(after.toArray(), before.toArray()));
		
	}
	
	public void testReduce3() {
		List<Integer> rules = Arrays.asList(1, 1);
		Taboo<Integer> t = new Taboo<Integer>(rules);
		
		List<Integer> before = new ArrayList<Integer>(Arrays.asList(1, 1, 1));
		List<Integer> after = new ArrayList<Integer>(Arrays.asList(1));
		t.reduce(before);
		assertEquals(after, before);
		assertTrue(Arrays.deepEquals(after.toArray(), before.toArray()));
		
	}
	
	public void testReduce4() {
		List<Integer> rules = Arrays.asList(1);
		Taboo<Integer> t = new Taboo<Integer>(rules);
		
		List<Integer> before = new ArrayList<Integer>(Arrays.asList(1, 1, 1, 1));
		List<Integer> after = new ArrayList<Integer>(Arrays.asList(1, 1, 1, 1));
		t.reduce(before);
		assertEquals(after, before);
		assertTrue(Arrays.deepEquals(after.toArray(), before.toArray()));
		
	}
	
	public void testReduce5() {
		List<Integer> rules = Arrays.asList();
		Taboo<Integer> t = new Taboo<Integer>(rules);
		
		List<Integer> before = new ArrayList<Integer>(Arrays.asList(1, 1, 1));
		List<Integer> after = new ArrayList<Integer>(Arrays.asList(1, 1 , 1));
		t.reduce(before);
		assertEquals(after, before);
		assertTrue(Arrays.deepEquals(after.toArray(), before.toArray()));
		
	}
	
	public void testReduce6() {
		List<Integer> rules = Arrays.asList(1, 2, null, 3, 4);
		Taboo<Integer> t = new Taboo<Integer>(rules);
		
		List<Integer> before = new ArrayList<Integer>(Arrays.asList(1, 2, 5, 2 ,3, 4));
		List<Integer> after = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 3));
		t.reduce(before);
		assertEquals(after, before);
		assertTrue(Arrays.deepEquals(after.toArray(), before.toArray()));
		
	}
	
	public void testReduce7() {
		List<Integer> rules = Arrays.asList();
		Taboo<Integer> t = new Taboo<Integer>(rules);
		
		List<Integer> before = new ArrayList<Integer>();
		List<Integer> after = new ArrayList<Integer>();
		t.reduce(before);
		assertEquals(after, before);
		assertTrue(Arrays.deepEquals(after.toArray(), before.toArray()));
		
	}
	
	
}
