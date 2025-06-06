public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private final int addSize = 2;
    private int nextFirst;
    private int nextLast;
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }
    private T[] resize(int newSize) {
        T[] res = (T[]) new Object[newSize];
        int i = (nextFirst + 1) % items.length;
        int num = size;
        int j = newSize / (addSize * addSize);
        while (num > 0) {
            res[j] = items[i];
            i = (i + 1) % items.length;
            j += 1;
            num -= 1;
        }
        nextFirst = ((newSize / (addSize * addSize) - 1) + res.length) % res.length;
        nextLast = j;
        return res;
    }
    public void addFirst(T item) {
        if (size == items.length) {
            items = resize(size * addSize);
        }
        items[nextFirst] = item;
        nextFirst = ((nextFirst - 1) + items.length) % items.length;
        size += 1;
    }
    public void addLast(T item) {
        if (size == items.length) {
            items = resize(size * addSize);
        }
        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;
        size += 1;
    }
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        nextFirst = (nextFirst + 1) % items.length;
        size -= 1;
        T tmp = items[nextFirst];
        if (size <= items.length / (addSize * addSize) && items.length > 1) {
            items = resize(items.length / addSize);
        }
        return tmp;
    }
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        nextLast = (nextLast - 1 + items.length) % items.length;
        size -= 1;
        T tmp = items[nextLast];
        if (size <= items.length / (addSize * addSize) && items.length > 1) {
            items = resize(items.length / addSize);
        }
        return tmp;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public int size() {
        return size;
    }
    public T get(int index) {
        return items[(nextFirst + 1 + index) % items.length];
    }
    public void printDeque() {
        int i = nextFirst + 1;
        int num = size;
        while (num > 0) {
            System.out.print(items[i] + " ");
            num -= 1;
            i += 1;
        }
    }
}
