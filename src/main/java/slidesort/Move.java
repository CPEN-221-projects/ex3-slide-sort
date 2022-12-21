package slidesort;

/*** DO NOT CHANGE THIS CLASS ***/

public class Move {
    public final Position startingPosition;
    public final boolean rowMove;
    public final int displacement;

    public Move(Position startingPosition, boolean rowMove, int displacement) {
        this.startingPosition = startingPosition;
        this.rowMove = rowMove;
        this.displacement = displacement;
    }

    public String toString() {
        return "new Move(" + startingPosition.toString()
            + ", " + rowMove + ", " + displacement + "),";
    }
}
