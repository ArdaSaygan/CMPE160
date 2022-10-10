
package project;
   
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

public class Solver {
	private PriorityObject winner;
	private int noMovesWinner;
	private boolean solvable = true;

	// priority = moves + manhattan
	// if priority is low, it's good.
	// find a solution to the initial board
	public Solver(Puzzle root) {
		System.out.println("Starting the solver...");
		if (root == null)
			throw new IllegalArgumentException();
		solve(root);
		System.out.println("Solving is finished...");
	}


	// I implemented the solution that was presented in explanation pdf
	
	// pop elements from the priority queue
	//     put their adjacents, except their prev
	// if popped element is complete break; 
	private void solve(Puzzle root) {
		// puzzles will be contained in PriotiyQueue's and PriotiyQueue's will be stored in pq
		PriorityQueue<PriorityObject> pq = new PriorityQueue<>(PriorityObject.comparator());
		PriorityObject priObjRoot = new PriorityObject(root, 0, null);
		// Add the initial board to the queue
		pq.add(priObjRoot);
		
//		ArrayList<PriorityObject> used = new ArrayList<>();

		
		// I implemented a solution that was able to detect unsolvable solutions, but then I removed that feature by commenting some lines.
		while (solvable) {
			
			if (pq.isEmpty()) {
				solvable = false;
				break;
			}
			
			PriorityObject curr = pq.poll();
			// if the board you're looking is complete, then you've solved the puzzle, you can break;
			if (curr.board.isCompleted()) {
				this.winner = curr;
				break;
			}
			
			// If curr is not complete then add its adjacents by creating a PriorityObject.
			for (Puzzle adjacent : curr.board.getAdjacents()) {
				
				// Don't add the same tile again
				if (curr.prev != null && adjacent.equals(curr.prev.board)) {
					continue;
				}
				// new g equals q+1 because only one movement is done, movement of empty tile
				PriorityObject priObjAdjacent = new PriorityObject(adjacent, curr.g + 1, curr);
				
				boolean addThis = true;
//				for (PriorityObject usedPriObj : used) {
//					if (usedPriObj.board.equals(priObjRoot.board)) {
//						if (usedPriObj.g < priObjRoot.g) {
//							addThis = false;
//							break;
//						}
//					}
//				}
				
				if (addThis) {
					pq.add(priObjAdjacent);
//					used.add(priObjAdjacent);
				}
//				
//				System.out.println(used.size() + " + s: " + pq.size());
			}
			
		}
		    
	}

	public int getMoves() {
		this.getSolution();
		return noMovesWinner;
	}

	public Iterable<Puzzle> getSolution() {
		// there's no winner
		if (this.winner == null) {
			return null;
		}
		
		// If there's a winner, backtrack and add boards in every step to l
		// l will be solutions
		ArrayList<Puzzle> l = new ArrayList<>();
		PriorityObject curr = winner;
		while (curr != null) {
			l.add(curr.board);
			curr = curr.prev;
		}
		
		noMovesWinner = l.size() - 1;
		Collections.reverse(l);
		return l;
	}

	private class PriorityObject {
		private Puzzle board;
		private int f;
		private PriorityObject prev;
		private int g;
		
		// f = g + h
		// g is the cost
		// h is the heuristic
		public PriorityObject(Puzzle board, int g, PriorityObject prev) {
			this.board = board;
			this.g = g;
			this.prev = prev;
			this.f = g + board.h();
		}
		
		// This is used in priotiy queue, priority queue will poll the board with smallest f. To achieve this we need a comparator.
		public static Comparator<PriorityObject> comparator() {
			return new CustomComparator();
		}
		private static class CustomComparator implements Comparator<PriorityObject> {
			
			@Override
			public int compare(PriorityObject o1, PriorityObject o2) {
				// one with a grater f is far from the solution
				if (o1.f > o2.f) {
					return 1;
				}
				else if (o1.f < o2.f) {
					return -1;
				}
				return 0;
			}
			
		}
	}

	// test client
	public static void main(String[] args) throws IOException {

		File input = new File("input12.txt");
		// Read this file int by int to create 
		// the initial board (the Puzzle object) from the file
		Scanner sc = new Scanner(input);
		int N = sc.nextInt();
		int[][] initialTiles = new int[N][N];
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				initialTiles[i][j] = sc.nextInt();
			}
		}
		
		Puzzle initial = new Puzzle(initialTiles);
		
		// solve the puzzle here. Note that the constructor of the Solver class already calls the 
		// solve method. So just create a solver object with the Puzzle Object you created above 
		// is given as argument, as follows:
		
		Solver solver = new Solver(initial);  // where initial is the Puzzle object created from input file

		// You can use the following part as it is. It creates the output file and fills it accordingly.
		File output = new File("output.txt");
		output.createNewFile();
		PrintStream write = new PrintStream(output);
		write.println("Minimum number of moves = " + solver.getMoves());
		
		if (solver.getSolution() == null) {
			write.println("Board is unsolvable");
		}
		else {
		for (Puzzle board : solver.getSolution())
			write.println(board);
		}
		
	}
}

