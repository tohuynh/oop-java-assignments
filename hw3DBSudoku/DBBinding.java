
public class DBBinding {
	private String key;
	private String value;
	
	
	public DBBinding(String textFormat) {
		String[] binding = textFormat.split(":");
		key = binding[0].trim();
		value = binding[1].trim();
	}
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof DBBinding)) return false;
		DBBinding other = (DBBinding)obj;
		
		if (!key.equals(other.key)) return false;
		if (value.equalsIgnoreCase(other.value)) return true;
		
		//obj is the the real binding
		//this is the criteria binding
		return (other.value.toLowerCase().contains(value.toLowerCase()));
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		sb.append(":");
		sb.append(value);
		return sb.toString();
	}

}
