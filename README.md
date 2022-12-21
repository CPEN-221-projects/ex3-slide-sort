## Slide Sort

You are given an $m \times n$ grid (the grid has $m$ rows and $n$ columns). Let $c = \min \\{m,n\\}$. The grid has some (or all) of the integers in $[1, c]$ (this is the set $\\{1, 2, \dots, c\\}$) occuring exactly once at different positions in the grid and all the other entries are $0$. Location $(i, j)$ in the grid (the $i$-th row, $j$-th column) is position number $(n \times i) + j$. The grid is sorted if and only if, for every two non-zero entries $x$ and $y$ such that $x < y$, the position at which $x$ appears is smaller than the position at which $y$ appears.

>   Implement all the methods indicated with a `TODO` in the package `slidesort`.

### Task 1

1. Verify that the given grid is valid (it contains values in $[1,c]$ that do not repeat and all other entries are $0$).
2. Verify that the given grid is sorted.

### Task 2

You are allowed to move a non-zero number along a row or column as long as there is no other non-zero number that blocks the move. When a non-zero number is moved, its starting position becomes a $0$ and the final position (which would have been a $0$) is replaced by the number that was moved.

For instance, consider the following row that has ten columns:

```text
0 0 0 3 0 0 2 0 0 0
```

The `2` cannot be moved to the left past the `3`, so it can be moved at most two positions to the left. On the right, it can be moved at most three positions.

A `Position` is an $(x, y)$ coordinate on the grid. A `Move` is a type that represents a move with a `startingPosition`, a `displacement` and an indicator of whether it is a move along a row or along a column.

1. Given a sequence of `Move`s, verify that the sequence is indeed a valid sequence (a move starts with a non-zero entry, does not move past another non-zero entry, and does not attempt to move the number off the grid).
2. Given a sequence of `Move`s, apply those moves to the grid if they are all feasible.

### Task 3

Your final task is to return a sequence of `Move`s that will sort a given grid.

## Grading

| Tasks | Grade |
| ----- | :---: |
| Completing all three tasks | A |
| Completing Tasks 1 and 2 | B |
| Completing only Task 1 | C |
| Not completing any task | F |

Any task must be **completed fully** to get credit. Make sure you write additional tests. The provided tests are only to give you a sense that you are moving in the correct direction.
