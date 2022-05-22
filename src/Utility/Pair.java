package Utility;

import java.util.Objects;

public class Pair {

    private int x;
    private int y;

    public Pair(){

    }

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return x == pair.x &&
                y == pair.y;
    }

    @Override
    public String toString() {

        int a = x + 1;
        int b = y + 1;
        return "x = " + a +
                ", y = " + b;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
