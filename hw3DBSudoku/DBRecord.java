
import java.util.*;

public class DBRecord {
	
	public static enum selectMode {AND, OR};
	
	private Collection<DBBinding> bindings;
	private boolean selected;

	public DBRecord(String textFormat) {
		selected = false;
		//bindings = new ArrayList<DBBinding>();
		bindings = new ChunkList<DBBinding>();
		
		StringTokenizer tokenizer = new StringTokenizer(textFormat, ",");
		while(tokenizer.hasMoreTokens()) {
			DBBinding binding = new DBBinding(tokenizer.nextToken());
			bindings.add(binding);
		}
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public int getNumberOfBindings() {
		return bindings.size();
	}
	
	public Collection<DBBinding> getBindings() {
		return bindings;
	}
	
	public boolean matches(DBRecord criteria, selectMode mode) {
		if (criteria.getNumberOfBindings() == 0 ) return (mode == selectMode.AND ? true : false);
		switch(mode) {
			case AND:  return matchesAllBindings(criteria);
			case OR: return matchesAtleastOneBinding(criteria);
			default :throw new RuntimeException("Invalid select mode.");
		}
	}
	
	private boolean matchesAtleastOneBinding(DBRecord criteria) {
		for (DBBinding bindingCriterion: criteria.getBindings()) {
			if (bindings.contains(bindingCriterion)) return true;
		}
		return false;
	}

	private boolean matchesAllBindings(DBRecord criteria) {
		return bindings.containsAll(criteria.getBindings());
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (selected) sb.append("*");
		Iterator<DBBinding> it = bindings.iterator();
		while (it.hasNext()) {
			sb.append(it.next().toString());
			if (!it.hasNext()) break;
			sb.append(", ");
		}
		return sb.toString();
		
	}
}
