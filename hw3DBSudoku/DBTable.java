import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DBTable {
	private Collection<DBRecord> records;
	private int numberOfSelectedRecords;
	
	public DBTable() {
		numberOfSelectedRecords = 0;
		//records = new ArrayList<DBRecord>();
		records = new ChunkList<DBRecord>();
	}
	
	public void addRecordsFromFile(String fileName) throws FileNotFoundException{
		Scanner scanner = new Scanner(new File(fileName));
		while (scanner.hasNextLine()) {
			DBRecord record = new DBRecord(scanner.nextLine());
			records.add(record);
		}
		scanner.close();
		
	}
	
	public void selectRecords(String criteria, DBRecord.selectMode mode) {
		DBRecord criteriaRecord = new DBRecord(criteria);
		for (DBRecord record: records) {
			if (record.matches(criteriaRecord, mode)) {
				if (!record.isSelected()) {
					record.setSelected(true);
					numberOfSelectedRecords++;
				}
			}
		}
	}
	
	public void unSelectRecords() {
		for (DBRecord record: records) {
			record.setSelected(false);
		}
		numberOfSelectedRecords = 0;
	}
	
	public void deleteRecords() {
		records.clear();
		numberOfSelectedRecords = 0;
	}
	
	public void deleteRecords(boolean selected) {
		Iterator<DBRecord> it = records.iterator();
		while (it.hasNext()) {
			DBRecord record = it.next();
			if (record.isSelected() == selected) {
				it.remove();
				if (selected) numberOfSelectedRecords--;
			}
		}
	}
	
	public int getNumberOfRecords() {
		return records.size();
	}
	
	public int getNumberOfSelectedRecords() {
		return numberOfSelectedRecords;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<DBRecord> it = records.iterator();
		while (it.hasNext()) {
			sb.append(it.next().toString());
			if (!it.hasNext()) break;
			sb.append("\n");
		}
		
		return sb.toString();
	}

}
