package slidesort;

/*** DO NOT CHANGE THIS CLASS ***/

public class Position {
    public final int i;
    public final int j;

    public Position(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Position) {
            Position that = (Position) other;
            return (this.i == that.i && this.j == that.j);
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "new Position(" + i + ", " + j + ")";
    }
}
