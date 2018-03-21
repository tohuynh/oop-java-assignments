import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public class ChunkList<E> extends AbstractCollection<E> implements Collection<E>{
	private Chunk head = null;
	private Chunk tail = null;
	private int size = 0;
	@Override
	public Iterator<E> iterator() {
		return new ChunkIterator();
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public boolean add(E e) {
		if (isEmpty() || tail.isFull()) addNewChunk();
		return tail.add(e);
		
	}
	
	private void addNewChunk() {
		Chunk chunk = new Chunk();
		if (isEmpty()) {
			tail = chunk;
			head = chunk;
		} else {
			tail.next = chunk;
			tail = chunk;
		}
	}
	
	private class ChunkIterator implements Iterator<E> {
		private int nextIndex = 0;
        private Chunk next = head;
        private Chunk previous = null;

		public boolean hasNext() {
			if (next == null) return false;
			if (nextIndex < next.size()) return true;
			return (next.next != null);
		}

		public E next() {
			if(nextIndex >= next.size()) {
                previous = next;
                next = next.next;
                nextIndex = 0;
            }
			return next.data[nextIndex++];
		}
		
		public void remove() {
			next.remove(--nextIndex);
            if(next.isEmpty()) {
                if(head == next) {
                    head = next.next;
                } else {
                    previous.next = next.next;
                }
                Chunk temp = next.next;
                next = null;
                next = temp;
                temp = null;
                //next = next.next;
                nextIndex = 0;
                if(next == null) tail = previous;
            }
		}
		
	}
	
	private class Chunk {
		private int chunkSize;
		private static final int ARRAY_SIZE = 8;
		private E[] data;
		private Chunk next;
		
		private Chunk() {
			chunkSize = 0;
			data = (E[]) new Object[ARRAY_SIZE];
			next = null;
		}
		
		private boolean add(E e) {
			if(!isFull()) {
                size++;
                data[chunkSize++] = e;
                return true;
            }
			return false;
		}
		
		private void remove(int index) {
            data[index] = null;
            int i;
            for(i = index; i < chunkSize - 1; i++)
                data[i] = data[i + 1];
            data[i] = null;
            chunkSize--;
            size--;
}
		
		private boolean isFull() {
			return chunkSize == ARRAY_SIZE;
		}
		
		private boolean isEmpty() {
			return chunkSize == 0;
		}
		
		private int size() {
			return chunkSize;
		}
		
	}

}
