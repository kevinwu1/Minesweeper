package minesweeper;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Minesweeper implements Runnable {
	public static Minesweeper chezpoor(Robot r) {// Expert level zoomed in twice
		return new Minesweeper(32, 206, 31, 16, r);
	}

	final int x0, y0, wid, hei;
	final double w;
	Robot r;

	int[] board;
	boolean[] needRead;
	Set<Integer> numCells;

	static void test() throws AWTException {
		Robot r = new Robot();
		Minesweeper ms = chezpoor(r);
		// for (int h = 0; h < ms.hei; h++) {
		// for (int w = 0; w < ms.wid; w++) {
		// System.out.print(ms.get(w, h));
		// // ms.get(w, h);
		// }
		// System.out.println();
		// }
		int px = 12, py = 3;
		Match m = Match.MINE;
		r.mouseMove(ms.left(px) + m.getXOff(ms.w), ms.top(py) + m.getYOff(ms.w));
		System.out.println(r.getPixelColor(ms.left(px) + m.getXOff(ms.w), ms.top(py) + m.getYOff(ms.w)));
		System.out.println(r.getPixelColor(ms.left(px) + m.getXOff(ms.w), ms.top(py) + m.getYOff(ms.w)).getRGB());
		System.out.println(m.getRGB());
		System.out.println(new Color(m.getRGB()));
		System.out.println(r.getPixelColor(ms.left(px) + m.getXOff(ms.w), ms.top(py) + m.getYOff(ms.w)).getRGB() == m
				.getRGB());
		System.out.println();
		System.out.println();
		System.out.println(m.matches(r.getPixelColor(ms.left(px) + m.getXOff(ms.w), ms.top(py) + m.getYOff(ms.w))));
	}

	public Minesweeper(int x0, int y0, int wid, int hei, Robot r) {
		super();
		this.x0 = x0;
		this.y0 = y0;
		this.wid = wid;
		this.hei = hei;
		w = (732.0 - 29) / wid;
		this.r = r;
		board = new int[wid * hei];
		needRead = new boolean[wid * hei];
		Arrays.fill(needRead, true);
		numCells = new HashSet<>();
	}

	void click(int x, int y) {
		r.mouseMove(xmid(x), ymid(y));
		r.mousePress(InputEvent.BUTTON1_MASK);
		r.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	void rClick(int x, int y) {
		r.mouseMove(xmid(x), ymid(y));
		r.mousePress(InputEvent.BUTTON3_MASK);
		r.mouseRelease(InputEvent.BUTTON3_MASK);
	}

	void move(int x, int y) {
		r.mouseMove(xmid(x), ymid(y));
	}

	int mid(int i, int i0) {
		return (int) (i0 + i * w + w / 2);
	}

	int xmid(int x) {
		return mid(x, x0);
	}

	int ymid(int y) {
		return mid(y, y0);
	}

	int left(int x) {
		return (int) (x0 + x * w);
	}

	int top(int y) {
		return (int) (y0 + y * w);
	}

	int scale(double d) {
		return (int) (w * d);
	}

	int get(int x, int y) {
		// Match pioup = Match.SEVEN;
		// System.out.println(r.getPixelColor(left(x) + pioup.getXOff(w), top(y) + pioup.getYOff(w)));
		// r.mouseMove(left(x) + pioup.getXOff(w), top(y) + pioup.getYOff(w));
		// System.out.println(pioup.matches(r.getPixelColor(left(x) + pioup.getXOff(w), top(y) + pioup.getYOff(w))));
		BufferedImage bi = r.createScreenCapture(new Rectangle(left(x), top(y), (int) w, (int) w));

		for (Match m : Match.values()) {
			if (m.matches(new Color(bi.getRGB(m.getXOff(w), m.getYOff(w)))))
				return m.getVal();
		}
		return 0;
	}
	static final int WHITE = -1052689, BLUE = -16776961, GREEN = -16745728, RED = -260602, DBLUE = -16777094,
			BLACK = -16777216, DRED = -8116955, TEAL = -16746119;

	enum Match {
		MINE(BLACK, 0.5, 0.7, -2, 120), //
		BLANK(WHITE, 0.5, 0.07, -1, 120), //
		ONE(BLUE, 0.5, 0.7, 1, 120), //
		TWO(GREEN, 0.3, 0.7, 2, 120), //
		THREE(RED, 0.7, 0.38, 3, 120), //
		FOUR(DBLUE, 0.6, 0.5, 4, 120), //
		FIVE(DRED, 0.25, 0.45, 5, 120), //
		SIX(TEAL, 0.65, 0.5, 6, 120); //
		// SEVEN(BLACK, 0.24, 0.2, 7, 120);

		private final int rgb, val;
		private final double xOff, yOff;
		private int thresh;

		Match(int rgb, double xOff, double yOff, int val, int thresh) {
			this.rgb = rgb;
			this.xOff = xOff;
			this.yOff = yOff;
			this.val = val;
			this.thresh = thresh;
		}

		int getRGB() {
			return rgb;
		}

		int getXOff(double w) {
			return (int) (w * xOff);
		}

		int getYOff(double w) {
			return (int) (w * yOff);
		}

		int getVal() {
			return val;
		}

		boolean matches(Color c) {
			Color tc = new Color(rgb);
			// System.out.println("database: " + tc);
			// System.out.println("read: " + c);
			// System.out.println(c.getRGB());
			int off = Math.abs(c.getRed() - tc.getRed()) //
					+ Math.abs(c.getBlue() - tc.getBlue())//
					+ Math.abs(c.getGreen() - tc.getGreen());
			// System.out.println();
			// System.out.println("OFF: " + off);
			// System.out.println();
			return off < thresh;
		}
	}

	boolean connected(int i, int j) {
		if (i < 0 || j < 0)
			return false;
		if (i >= wid * hei || j >= wid * hei)
			return false;
		return Math.abs((i % wid) - (j % wid)) <= 1 && Math.abs((i / wid) - (j / wid)) <= 1;
	}

	boolean isComplete(int i) {
		for (int v = -wid; v <= wid; v += wid) {
			for (int h = -1; h <= 1; h++) {
				if (connected(i + h + v, i)) {
					// System.out.println(i + h + v);
					if (board[i + h + v] == -1)
						return false;
				}
			}
		}
		return true;
	}

	void openCell(int i) {
		click(i % wid, i / wid);
	}

	void mineCell(int i) {
		rClick(i % wid, i / wid);
	}

	void read() {
		for (int h = 0; h < hei; h++) {
			for (int w = 0; w < wid; w++) {
				int o = h * 31 + w;
				if (needRead[o]) {
					board[o] = get(w, h);
					if (board[o] != -1)
						needRead[o] = false;
				}
			}
		}
	}

	void printBoard(int[] board) {
		for (int i = 0; i < board.length; i++) {
			if (i % wid == 0)
				System.out.println();
			System.out.print(board[i] == -1 ? "-" : board[i] == -2 ? "*" : board[i] == -3 ? "+" : board[i]);
		}
	}

	void addAll() {
		numCells.clear();
		for (int i = 0; i < board.length; i++) {
			if (!isComplete(i) && board[i] > 0)
				numCells.add(i);
		}
	}

	boolean tryComplete(boolean click, int[] board, Set<Integer> numCells) {
		boolean anyClicked = false;
		boolean stop = false;
		while (!stop) {
			stop = true;
			Set<Integer> rem = new HashSet<>();
			Set<Integer> add = new HashSet<>();
			for (int i : numCells) {
				if (completeCell(i, rem, add, click, board)) {
					stop = false;
					anyClicked = true;
				}
			}
			// printBoard(board);
			numCells.removeAll(rem);
			numCells.addAll(add);
			rem.clear();
			add.clear();
			// read();
			addAll();
			for (int i : numCells) {
				if (completeCell(i, rem, add, click, board))
					stop = false;
			}
			numCells.removeAll(rem);
			numCells.addAll(add);
			// try {
			// Thread.sleep(10);
			// }
			// catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// System.out.println(count++);
		}
		return anyClicked;
	}

	boolean completeCell(int i, Set<Integer> rem, Set<Integer> add, boolean click, int[] board) {
		// System.out.println("Checking : " + (i % wid) + " , " + i / wid);
		boolean ret = false;
		int openCells = 0;
		int mineCells = 0;
		for (int v = -wid; v <= wid; v += wid) {
			for (int h = -1; h <= 1; h++) {
				int o = i + h + v;
				if (connected(o, i)) {
					if (board[o] == -1)
						openCells++;
					if (board[o] == -2)
						mineCells++;
				}
			}
		}

		// System.out.println(i);
		// System.out.println(board[i]);
		// System.out.println(openCells);
		// System.out.println(mineCells);
		if (openCells == board[i] - mineCells) { // mine all
			for (int v = -wid; v <= wid; v += wid) {
				for (int h = -1; h <= 1; h++) {
					int o = i + h + v;
					if (connected(o, i)) {
						if (board[o] == -1) {
							board[o] = -2;
							if (click)
								rClick(o % wid, o / wid);
							ret = true;
						}
					}
				}
			}
			rem.add(i);
		}
		if (board[i] == mineCells) { // clear All
			for (int v = -wid; v <= wid; v += wid) {
				for (int h = -1; h <= 1; h++) {
					int o = i + h + v;
					if (connected(o, i)) {
						if (board[o] == -1) {
							if (click) {
								click(o % wid, o / wid);
								try {
									Thread.sleep(2);
								}
								catch (InterruptedException e) {
									e.printStackTrace();
								}
								board[o] = get(o % wid, o / wid);
							}
							else {
								board[o] = -3;
							}
							add.add(o);
							ret = true;
						}
					}
				}
			}
			rem.add(i);
		}

		return ret;
	}

	// copy new board
	// assume square
	// solve
	// checkContradiction

	boolean assumeMine() {
		Set<Integer> numCells2 = new HashSet<>();
		numCells2.addAll(numCells);
		for (int i : numCells2) {
			for (int v = -wid; v <= wid; v += wid) {
				for (int h = -1; h <= 1; h++) {
					int o = i + h + v;
					if (connected(o, i)) {
						if (board[o] == -1) {
							boolean isOk = assumeMine(o);
							if (!isOk) {
								// System.out.println("clicking : " + o);
								openCell(o);
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	boolean assumeOpen() {
		Set<Integer> numCells2 = new HashSet<>();
		numCells2.addAll(numCells);
		for (int i : numCells2) {
			for (int v = -wid; v <= wid; v += wid) {
				for (int h = -1; h <= 1; h++) {
					int o = i + h + v;
					if (connected(o, i)) {
						if (board[o] == -1) {
							boolean isOk = assumeOpen(o);
							if (!isOk) {
								// System.out.println("clicking : " + o);
								mineCell(o);
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	// return true if assumption is ok
	// return false if assumption is bad
	boolean assumeMine(int i) {
		// System.out.println(i % wid + " , " + i / wid);
		int[] nboard = Arrays.copyOf(board, board.length);
		nboard[i] = -2;
		Set<Integer> numCells2 = new HashSet<>();
		numCells2.addAll(numCells);
		tryComplete(false, nboard, numCells2);
		for (int q = 0; q < nboard.length; q++) {
			if (nboard[q] > 0) {
				boolean b = contradiction(nboard, q);
				if (b)
					return false;
			}
		}
		return true;
	}

	boolean assumeOpen(int i) {
		// System.out.println(i % wid + " , " + i / wid);
		int[] nboard = Arrays.copyOf(board, board.length);
		nboard[i] = -3;
		Set<Integer> numCells2 = new HashSet<>();
		numCells2.addAll(numCells);
		tryComplete(false, nboard, numCells2);
		for (int q = 0; q < nboard.length; q++) {
			if (nboard[q] > 0) {
				boolean b = contradiction(nboard, q);
				if (b)
					return false;
			}
		}
		return true;
	}

	boolean contradiction(int[] board, int i) {
		int openCells = 0;
		int mineCells = 0;
		int value = board[i];
		for (int v = -wid; v <= wid; v += wid) {
			for (int h = -1; h <= 1; h++) {
				int o = i + h + v;
				if (connected(o, i)) {
					if (board[o] == -1)
						openCells++;
					if (board[o] == -2)
						mineCells++;
				}
			}
		}
		if (value - mineCells > openCells)
			return true; // not enough open squares to fill mines
		if (mineCells > value)
			return true; // more mines than cell says
		return false;
	}

	//
	// boolean contradictionExists(int[] board, int i) {
	//
	// }

	public static void main(String[] args) throws AWTException {
		Minesweeper ms = chezpoor(new Robot());
		int x = 1;
		int y = 8;
		ms.move(x, y);
		ms.click(x, y);
		System.out.println(ms.get(x, y));
		// ms.read();
		// ms.addAll();
		// ms.assumeMine();
		// ms.printBoard();
	}

	@Override
	public void run() {
		// move(14, 12);
		// completeCell(i, rem, add)
		read();
		addAll();
		boolean a1 = true;
		boolean a2 = true;
		boolean a3 = true;
		for (int i = 0; i < 10; i++) {
			while (a1 || a2 || a3) {
				a1 = a2 = a3 = false;
				// printBoard(board);
				a1 = tryComplete(true, board, numCells);
				// System.out.println("a1: " + a1);
				if (!a1) {
					a2 = assumeMine();
					if (!a2)
						a3 = assumeOpen();
				}
				try {
					Thread.sleep(10);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				read();
				addAll();
				// System.out.println(numCells.size());
			}
			try {
				Thread.sleep(20);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int j = 0; j < wid * hei; j++) {
				if (board[j] == -1) {
					// System.out.println("guess: " + (j % wid + "," + j / wid));
					click(j % wid, j / wid);
					a1 = a2 = a3 = true;
					try {
						Thread.sleep(50);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
					read();
					addAll();
					break;
				}
			}
			// System.out.println("e");
		}

		System.out.println("Done");
	}
}
