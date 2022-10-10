
package project;

import java.util.Stack;


public class Puzzle {
	private final int[][] tiles;

	public Puzzle(int[][] tiles) {
		this.tiles = this.copy(tiles);
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(tiles.length + "\n");
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				str.append(" "+tiles[i][j]);
			}
			str.append("\n");
		}
		return str.toString();

	}

	public int dimension() {
		return this.tiles.length;
	}


	// sum of Manhattan distances between tiles and goal
	// The Manhattan distance between a board and the goal board is the sum
	// of the Manhattan distances (sum of the vertical and horizontal distance)
	// from the tiles to their goal positions.
	public int h() {
		
		int totalDistance = 0;
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				totalDistance += distanceOfTile(tiles[i][j], j , i);
			}
		}
		return totalDistance;
	}
	
	// This a helper method to calculate the manhattan distance in this.h()
	private int distanceOfTile(int number, int x, int y) {
		
		if (number == 0) {
			// don't count the empty tile
			// In the description pdf, the empty tile's distance was not added to the total distance. 
			return 0;
			// But if I had to add it as well, I would do it this way.
//			return Math.abs(this.dimension() - 1 - x) + Math.abs(this.dimension() - 1 - y);
		}
		
		// I made a short analysis and found out that it's possible to formulate what the x and y components of target tile will be.
		// It can clearly be seen and confirmed if one draws it on paper.
		int targetX = (number - 1)%this.dimension();
		int targetY = (number - 1)/this.dimension();
		return Math.abs(targetX - x) + Math.abs(targetY - y);
	}

	public boolean isCompleted() {
		// When completed, manhattan distance should be zero
		if (this.h() == 0) {
			return true;
		}
		return false;
	}


	// Returns any kind of collection that implements iterable.
	// For this implementation, I choose stack.
	public Iterable<Puzzle> getAdjacents() {
		Stack<Puzzle> adjacents = new Stack<>();
		// There are maximum four adjacents for each state.
		// They're reached by moving zero tile which can move either:
		// left, right, up and down.
		
		int[][] transformations = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
		
		// Let's first find zero tile in tiles.
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				if (tiles[i][j] == 0) {
					// We foudn zero tile. i and j are x and y components of this tile.
					
					// Now apply the tranformations
					for (int[] t : transformations) {
						// these are the new positions
						int cI = i + t[0];
						int cJ = j + t[1];
						// check if they're valid
						if ( 0 <= cI && cI < this.dimension() && 0 <= cJ && cJ < this.dimension()) {
							// create a new puzzle with the initaial tiles
							Puzzle newPuzzle = new Puzzle(tiles);
							
							//but change the position of the empty tile according to the transformation
							newPuzzle.tiles[i][j] = this.tiles[cI][cJ];
							newPuzzle.tiles[cI][cJ] = 0;
							
							adjacents.add(newPuzzle);
						}
					}
				}
			}
		}
		
		return adjacents;
		
	}

	private int[][] copy(int[][] source) {
		// create a new array and copy assign every valur to it.
		int[][] copy = new int[source.length][source[0].length];
		for (int i = 0; i < source.length; i++) {
			for (int j = 0; j < source[i].length; j++) {
				copy[i][j] = source[i][j];
			}
		}
		
		return copy;
	}
	
	@Override
	public boolean equals(Object o) {
		// Two puzzles are equal they're at the same dimension and every tile is at the same position
		
		Puzzle otherPuzzle = (Puzzle) o;
		if (otherPuzzle.dimension() != this.dimension()) {
			return false;
		}
		
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				if (this.tiles[i][j] != otherPuzzle.tiles[i][j]) {
					// there's a piece that's in a different position
					return false;
				}
			}
		}
		
		return true;
	}


	// You can use this main method to see your Puzzle structure.
	// Actual solving operations will be conducted in Solver.main method
//	public static void main(String[] args) {
//		int[][] array = { { 1, 8, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
//		Puzzle board = new Puzzle(array);
//		System.out.println(board);
//		System.out.println(board.dimension());
//		System.out.println(board.h());
//		System.out.println(board.isCompleted());
//		Iterable<Puzzle> itr = board.getAdjacents();
//		for (Puzzle neighbor : itr) {
//			System.out.println(neighbor);
//			System.out.println(neighbor.equals(board));
//		}
//	}
}

