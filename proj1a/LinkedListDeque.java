public class LinkedListDeque<T> {
    private class Node {
        T item;
        Node prev;
        Node next;
        public Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }
    private int size;
    private Node sentinel;
    public LinkedListDeque() {
        size = 0;
        sentinel = new Node(null, sentinel, sentinel);
    }
    public void addFirst(T item) {
        if (size == 0) {
            sentinel.next = new Node(item, sentinel, sentinel);
            sentinel.prev = sentinel.next;
            size++;
            return;
        }
        size += 1;
        Node p = sentinel;
        Node p1 = sentinel.next;
        p.next = new Node(item, p, sentinel.next);
        p1.prev = p.next;
    }
    public void addLast(T item) {
        if (size == 0) {
            addFirst(item);
            return;
        }
        size += 1;
        Node p = sentinel.prev;
        p.next = new Node(item, p, sentinel);
        sentinel.prev = p.next;
        p.next.prev = p;
        p.next.next = sentinel;
    }
    public T removeFirst() {
        size -= 1;
        Node p = sentinel;
        Node p1 = sentinel.next;
        T tmp = p1.item;
        p.next = p1.next;
        p1.next.prev = p;
        p1.next = null;
        p1.prev = null;
        return tmp;
    }
    public T removeLast() {
        size -= 1;
        Node p = sentinel;
        Node p1 = sentinel.prev;
        T tmp = p1.item;
        p.prev = p.prev.prev;
        p.prev.next = sentinel;
        p1.next = null;
        p1.prev = null;
        return tmp;
    }
    public T get(int index) {
        Node p = sentinel.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.item;
    }
    public int size() {
        return size;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public void printDeque() {
        Node p = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(p.item + " ");
            p = p.next;
        }
    }
    private T helpGet(int index, Node p) {
        if (index == 0) {
            return p.item;
        }
        return helpGet(index - 1, p.next);
    }
    public T getRecursive(int index) {
        return helpGet(index, sentinel.next);
    }
}
