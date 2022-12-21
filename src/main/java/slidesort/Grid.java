package slidesort;

import java.util.*;

public class Grid {
    private int[][] _grid;

    /**
     * Create a new grid
     * @param seedArray is not null
     *                  and seedArray.length > 0
     *                  and seedArray[0].length > 0
     */
    public Grid(int[][] seedArray) {
        int rows = seedArray.length;
        int cols = seedArray[0].length;
        _grid = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                _grid[i][j] = seedArray[i][j];
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Grid) {
            Grid g2 = (Grid) other;
            if (this._grid.length != g2._grid.length) {
                return false;
            }
            if (this._grid[0].length != g2._grid[0].length) {
                return false;
            }
            int rows = _grid.length;
            int cols = _grid[0].length;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (this._grid[i][j] != g2._grid[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Check if this grid is a valid grid.
     * A grid is valid if, for c = min(rows, cols),
     * the grid contains zero or more values in [1, c]
     * exactly once and all other entries are 0s.
     *
     * @return true if this is a valid grid and false otherwise
     */
    public boolean isValid() {

        int c = Math.min(_grid.length, _grid[0].length);
        int[] countArray = new int[c + 1]; // extra spot for 0s

        for (int i = 0; i < _grid.length; i++) {
            for (int j = 0; j < _grid[0].length; j++) {
                if ( _grid[i][j] > c || (countArray[_grid[i][j]] != 0 && _grid[i][j] != 0)) {
                    return false;
                }
                countArray[_grid[i][j]]++;
            }
        }

        return true;
    }

    /**
     * Check if this grid is sorted.
     * A grid is sorted iff it is valid and,
     *  for all pairs of entries (x, y)
     *  such that x > 0 and y > 0,
     *  if x < y then the position(x) < position(y).
     * If x is at location (i, j) in the grid
     * then position(x) = i * (number of cols) + j.
     *
     * @return true if the grid is sorted and false otherwise.
     */
    public boolean isSorted() {

        int lastNonZeroNumber = _grid[0][0];

        for (int i = 0; i < _grid.length; i++) {
            for (int j = 0; j < _grid[0].length; j++) {
                if (_grid[i][j] != 0) {
                    if (_grid[i][j] < lastNonZeroNumber) {
                        return false;
                    }
                    lastNonZeroNumber = _grid[i][j];
                }
            }
        }
        return true;
    }

    /**
     * Check if a list of moves is feasible.
     * A move is feasible if it starts with a non-zero entry,
     * does not move that number off the grid,
     * and it does not involve jumping over another non-zero number.
     *
     * @param   moveList is not null.
     * @return  true if the list of moves are all feasible
     *          and false otherwise.
     *          By definition an empty list is always feasible.
     */
    public boolean validMoves(List<Move> moveList) {

        List<Position> nowEmpty = new ArrayList<>();
        List<Position> nowOccupied = new ArrayList<>();

        for (Move move: moveList) {

            // check if the number at the starting position is nonzero
            if (!validStart(move, nowEmpty, nowOccupied)) {
                return false;
            }

            // check if the moves go out of bounds
            if (!moveWithinBounds(move)) {
                return false;
            }

            //check if move path is clear of nonzero entries
            if (!pathClear(move, nowEmpty, nowOccupied)) {
                return false;
            }

            Position initialPosition;
            Position finalPosition;

            if (move.rowMove) {
                initialPosition = move.startingPosition;
                finalPosition = new Position(move.startingPosition.i, move.startingPosition.j + move.displacement);
            } else {
                initialPosition = move.startingPosition;
                finalPosition = new Position(move.startingPosition.i + move.displacement, move.startingPosition.j);
            }

            updateLists(initialPosition, finalPosition, nowEmpty, nowOccupied);
        }
        return true;
    }

    /**
     * Check if the starting position of a move is valid.
     * @param move is the move for the starting position to be checked
     * @param nowEmpty list of positions that are now empty
     * @param nowOccupied list of positions that are now occupied
     * @return whether the move's beginning point is valid or not
     */
    private boolean validStart(Move move, List<Position> nowEmpty, List<Position> nowOccupied){

        int startValue = _grid[move.startingPosition.i][move.startingPosition.j];
        Position startPosition = move.startingPosition;

        return (startValue == 0 && nowOccupied.contains(startPosition)) ||
                (startValue != 0 && !nowEmpty.contains(startPosition));
    }

    /**
     * Check if a move is within bounds of the grid.
     * @param move move to check
     * @return true if the move is within bounds, false if not
     */
    private boolean moveWithinBounds(Move move) {
        int maxLength;
        int changingCoordinate;

        if (move.rowMove) {
            maxLength = _grid[0].length;
            changingCoordinate = move.startingPosition.j;
        } else {
            maxLength = _grid.length;
            changingCoordinate = move.startingPosition.i;
        }

        return changingCoordinate + move.displacement >= 0 && changingCoordinate + move.displacement < maxLength;
    }

    /**
     * Checks if move path is clear of nonzero entries.
     * @param move move to check
     * @param nowEmpty list of spots now empty
     * @param nowOccupied list of spots now occupied
     * @return true if the path is clear, false if not
     */
    private boolean pathClear(Move move, List<Position> nowEmpty, List<Position> nowOccupied) {

        int changeI;
        int changeJ;

        if (move.rowMove) {
            changeJ = 1;
            changeI = 0;
        } else {
            changeJ = 0;
            changeI = 1;
        }

        //move in negative direction if displacement is negative
        if (move.displacement < 0) {
            changeI *= -1;
            changeJ *= -1;
        }

        // check if any of the spots you're moving into are nonzero
        for (int k = 1; k <= Math.abs(move.displacement); k++) {
            // start loop from 1 since the starting position itself is not 0
            // have absolute value of displacement so that you actually loop through even if you have a negative value
            int curICoord = move.startingPosition.i + changeI * k;
            int curJCoord = move.startingPosition.j + changeJ * k;

            int curValue = _grid[curICoord][curJCoord];
            Position curPosition = new Position(curICoord, curJCoord);

            boolean emptySpot = (curValue == 0 && !(nowOccupied.contains(curPosition))) ||
                    (curValue != 0 && nowEmpty.contains(curPosition));

            if (!emptySpot) {
                return false;
            }
        }

        return true;
    }

    /**
     * Update nowEmpty and nowOccupied lists when a potential move is calculated
     * @param initialPosition initial position
     * @param finalPosition final position
     */
    private void updateLists(Position initialPosition, Position finalPosition,
                             List<Position> nowEmpty, List<Position> nowOccupied) {
        nowEmpty.add(initialPosition);
        if (nowEmpty.contains(finalPosition)) {
            nowEmpty.remove(nowEmpty.indexOf(finalPosition));
        }

        nowOccupied.add(finalPosition);
        if (nowOccupied.contains(initialPosition)) {
            nowOccupied.remove(nowOccupied.indexOf(initialPosition));
        }
    }

    /**
     * Apply the moves in moveList to this grid
     * @param moveList is a valid list of moves
     */
    public void applyMoves(List<Move> moveList) {
        if (validMoves(moveList)) {
            for (Move move: moveList) {
                if (move.displacement != 0) {
                    if (move.rowMove) {
                        _grid[move.startingPosition.i][move.startingPosition.j + move.displacement] =
                                _grid[move.startingPosition.i][move.startingPosition.j];
                    } else {
                        _grid[move.startingPosition.i + move.displacement][move.startingPosition.j] =
                                _grid[move.startingPosition.i][move.startingPosition.j];
                    }
                    _grid[move.startingPosition.i][move.startingPosition.j] = 0;
                }
            }
        }
    }

    /**
     * Return a list of moves that, when applied, would convert this grid
     * to be sorted
     * @return a list of moves that would sort this grid
     */
    public List<Move> getSortingMoves() {

        /** Idea: move each element one by one into top row smallest to largest, acting in the order 1 -> c
         * ex. move 1 into top left corner, then 2 to the right of one, so on until c is at the last position
         * To get to desired location, make one move for each element to the correct column position, then one giant flump upwards
         * If a number is in the way, call a moveOutOfWay function to move blocks in the direction perpendicular to the desired move
         * have moveOutOfWay be recursive in case there is a block blocking the block
         * continuously add moves to the grand list
         */

        Map<Integer, Position> nonZeroEntries = new HashMap<>();

        List<Position> nowEmpty = new ArrayList<>();
        List<Position> nowOccupied = new ArrayList<>();
        List<Move> sortingMoves = new ArrayList<>();

        getNonZeroEntries(nonZeroEntries);

        for (Integer value: nonZeroEntries.keySet()) {

            Position position = nonZeroEntries.get(value);

            int xDisplacement = -(position.j - value + 1);

            Move xMove = new Move(position, true, xDisplacement);
            List<Move> xMoveAsList = new ArrayList<>();
            xMoveAsList.add(xMove);

            List<Object> testMove = (List<Object>) validMovesWithList(xMoveAsList, nowEmpty, nowOccupied, true);

            if (testMove.get(0).equals(false)) {
                // if you can't do it right away, clear the path first
                List<Object> clearPathMoves = getOutOfMyWay(xMove, nowEmpty, nowOccupied, nonZeroEntries);
                nowEmpty = (List<Position>) clearPathMoves.get(1);
                nowOccupied = (List<Position>) clearPathMoves.get(2);
                nonZeroEntries = (Map<Integer, Position>) clearPathMoves.get(3);

                // then try again
                List<Object> redoMove = (List<Object>) validMovesWithList(xMoveAsList, nowEmpty, nowOccupied, true);
                nowEmpty = (List<Position>) redoMove.get(1);
                nowOccupied = (List<Position>) redoMove.get(2);

                sortingMoves.addAll((List<Move>) clearPathMoves.get(0));
            } else {
                nowEmpty = (List<Position>) testMove.get(1);
                nowOccupied = (List<Position>) testMove.get(2);
            }

            sortingMoves.add(xMove);
            nonZeroEntries.put(value, new Position(nonZeroEntries.get(value).i,
                    nonZeroEntries.get(value).j + xDisplacement));

            //after all this code, the empty and occupied lists have already been updated by other methods
        }

        for (Integer value: nonZeroEntries.keySet()) {
            Position position = nonZeroEntries.get(value);

            int yDisplacement = -position.i;
            Move yMove = new Move (position, false, yDisplacement);
            sortingMoves.add(yMove);
        }

        return sortingMoves;
    }

    public Object validMovesWithList(List<Move> moveList, List<Position> nowEmpty,
                                     List<Position> nowOccupied, boolean updateLists) {

        for (Move move: moveList) {

            // check if the number at the starting position is nonzero
            if (!validStart(move, nowEmpty, nowOccupied)) {
                List<Object> returnValue = Arrays.asList(false, nowEmpty, nowOccupied);
                return returnValue;
            }

            // check if the moves go out of bounds
            if (!moveWithinBounds(move)) {
                List<Object> returnValue = Arrays.asList(false, nowEmpty, nowOccupied);
                return returnValue;
            }

            //check if move path is clear of nonzero entries
            if (!pathClear(move, nowEmpty, nowOccupied)) {
                List<Object> returnValue = Arrays.asList(false, nowEmpty, nowOccupied);
                return returnValue;
            }

            Position initialPosition;
            Position finalPosition;

            if (move.rowMove) {
                initialPosition = move.startingPosition;
                finalPosition = new Position(move.startingPosition.i, move.startingPosition.j + move.displacement);
            } else {
                initialPosition = move.startingPosition;
                finalPosition = new Position(move.startingPosition.i + move.displacement, move.startingPosition.j);
            }

            if (updateLists) {
                updateLists(initialPosition, finalPosition, nowEmpty, nowOccupied);
            }
        }

        List<Object> returnValue = Arrays.asList(true, nowEmpty, nowOccupied);

        return returnValue;
    }

    private List<Object> getOutOfMyWay(Move targetMove, List<Position> nowEmpty, List<Position> nowOccupied,
                                       Map<Integer, Position> nonZeroEntries) {
        List<Move> getOutOfMyWayList = new ArrayList<>();
        Position startingPoint = targetMove.startingPosition;

        int moveDownUp;
        int moveRightLeft;

        //other items must be moved perpendicular to target move
        if (targetMove.rowMove) {
            moveDownUp = 1;
            moveRightLeft = 0;
        } else {
            moveDownUp = 0;
            moveRightLeft = 1;
        }

        int step = 1;
        if (targetMove.displacement < 0) {
            step = -1;
        }

        for (int k = 1; k <= Math.abs(targetMove.displacement); k++) {

            Position currentPosition = new Position(startingPoint.i + moveRightLeft * k * step, startingPoint.j + moveDownUp * k * step);

            Move downRightMove;
            if (moveRightLeft != 0) {
                downRightMove = new Move(currentPosition, true, 1);
            } else {
                downRightMove = new Move(currentPosition, false, 1);
            }

            Move upLeftMove;
            if (moveRightLeft != 0) {
                upLeftMove = new Move(currentPosition, true, -1);
            } else {
                upLeftMove = new Move(currentPosition, false, -1);
            }

            if (!validStart(downRightMove, nowEmpty, nowOccupied)) {
                continue;
            }

            List<Move> downRightMoveAsList = new ArrayList<>();
            downRightMoveAsList.add(downRightMove);

            List<Object> checkDownRight = (List<Object>) validMovesWithList(downRightMoveAsList, nowEmpty, nowOccupied, false);

            List<Move> upLeftMoveAsList = new ArrayList<>();
            upLeftMoveAsList.add(upLeftMove);

            List<Object> checkUpLeft = (List<Object>) validMovesWithList(upLeftMoveAsList, nowEmpty, nowOccupied, false);

            //TODO: convert validMovesWithList to just take in 1 move
            //if you can move down or right, go do it
            if (checkDownRight.get(0).equals(true)) {
                boolean rowMove = moveRightLeft != 0;
                Move makeMove = new Move(currentPosition, rowMove, 1);
                Position finalPosition = new Position(currentPosition.i + moveDownUp,
                        currentPosition.j + moveRightLeft);
                getOutOfMyWayList.add(makeMove);
                changePositionBlind(nonZeroEntries, currentPosition, finalPosition);
                updateLists(currentPosition, finalPosition, nowEmpty, nowOccupied);
            } else if (checkUpLeft.get(0).equals(true)) {
                //if you can move up or left, go do it
                boolean rowMove = moveRightLeft != 0;
                Move makeMove = new Move(currentPosition, rowMove, -1);
                Position finalPosition = new Position(currentPosition.i - moveDownUp,
                        currentPosition.j - moveRightLeft);
                getOutOfMyWayList.add(makeMove);
                changePositionBlind(nonZeroEntries, currentPosition, finalPosition);
                updateLists(currentPosition, finalPosition, nowEmpty, nowOccupied);
            } else if (moveWithinBounds(downRightMove)) {
                //if the move is within bounds, force yourself through and recursively move things out of the way
                boolean rowMove = moveRightLeft != 0;

                Move intendedDirection = new Move(currentPosition, rowMove, 1);

                List<Object> recursiveMoveList = getOutOfMyWay(intendedDirection, nowEmpty, nowOccupied, nonZeroEntries);

                getOutOfMyWayList.addAll((List<Move>) recursiveMoveList.get(0));
                getOutOfMyWayList.add(intendedDirection);

                Position finalPosition = new Position(currentPosition.i + moveDownUp,
                        currentPosition.j + moveRightLeft);
                changePositionBlind(nonZeroEntries, currentPosition, finalPosition);
                updateLists(currentPosition, finalPosition, nowEmpty, nowOccupied);
            } else {
                //impossible to move down/right, and a simple move up/left is invalid, so force your way up/left
                boolean rowMove = moveRightLeft != 0;

                Move intendedDirection = new Move(currentPosition, rowMove, -1);

                List<Object> recursiveMoveList = getOutOfMyWay(intendedDirection, nowEmpty, nowOccupied, nonZeroEntries);

                getOutOfMyWayList.addAll((List<Move>) recursiveMoveList.get(0));
                getOutOfMyWayList.add(intendedDirection);

                Position finalPosition = new Position(currentPosition.i - moveDownUp,
                        currentPosition.j - moveRightLeft);
                changePositionBlind(nonZeroEntries, currentPosition, finalPosition);
                updateLists(currentPosition, finalPosition, nowEmpty, nowOccupied);
            }
        }

        List<Object> returnValue = Arrays.asList(getOutOfMyWayList, nowEmpty, nowOccupied, nonZeroEntries);

        return returnValue;
    }

    /**
     * Finds all nonZeroEntries in the grid and maps them to their positions.
     * @param nonZeroEntries Set object to be populated
     */
    private void getNonZeroEntries(Map<Integer, Position> nonZeroEntries) {
        for (int i = 0; i < _grid.length; i++) {
            for (int j = 0; j < _grid[0].length; j++) {
                if (_grid[i][j] != 0) {
                    nonZeroEntries.put(_grid[i][j], new Position(i,j));
                }
            }
        }
    }

    private void changePositionBlind(Map<Integer, Position> nonZeroEntries,
                                     Position initialPosition, Position finalPosition) {
        for (Integer value: nonZeroEntries.keySet()) {
            if (Objects.equals(nonZeroEntries.get(value), initialPosition)) {
                nonZeroEntries.put(value, finalPosition);
            }
        }
    }
}
