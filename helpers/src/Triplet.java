/**
 * Created by burak on 11/3/2016.
 */
public class Triplet<T> {
    private T value1;
    private T value2;
    private T value3;

    public Triplet(T value1, T value2, T value3) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    public T getValue1() {
        return value1;
    }

    public T getValue2() {
        return value2;
    }

    public T getValue3() {
        return value3;
    }

    public void setValue1(T value1) {
        this.value1 = value1;
    }

    public void setValue2(T value2) {
        this.value2 = value2;
    }

    public void setValue3(T value3) {
        this.value3 = value3;
    }

}
