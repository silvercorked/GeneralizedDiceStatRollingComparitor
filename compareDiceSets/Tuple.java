package compareDiceSets;

public class Tuple<K, V> {
    private K first;
    private V second;

    public Tuple() {
        this.setFirst(null);
        this.setSecond(null);
    }
    public Tuple(K first, V second) {
        this.setFirst(first);
        this.setSecond(second);
    }

    public void setFirst(K first) {
        this.first = first;
    }
    public void setSecond(V second) {
        this.second = second;
    }
    public K getFirst() {
        return this.first;
    }
    public V getSecond() {
        return this.second;
    } 
}