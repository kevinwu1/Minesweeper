package minesweeper;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.HashSet;
import java.util.Set;

public class Minesweeper implements Runnable {
	final int x0, y0, wid, hei;
	final double w;
	Robot r;

	int[] board;
	Set<Integer> numCells;

	static void test() throws AWTException {
		Robot r = new Robot();
		Minesweeper ms = new Minesweeper(r);
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

	public Minesweeper(Robot r) {
		this(32, 206, 31, 16, r);
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
		// Match pioup = Match.SIX;
		// System.out.println(r.getPixelColor(left(x) + pioup.getXOff(w), top(y) + pioup.getYOff(w)));
		// r.mouseMove(left(x) + pioup.getXOff(w), top(y) + pioup.getYOff(w));
		// System.out.println(pioup.matches(r.getPixelColor(left(x) + pioup.getXOff(w), top(y) + pioup.getYOff(w))));
		for (Match m : Match.values()) {
			if (m.matches(r.getPixelColor(left(x) + m.getXOff(w), top(y) + m.getYOff(w))))
				return m.getVal();
		}
		return 0;
	}
	static final int WHITE = -1052689, BLUE = -16776961, GREEN = -16745728, RED = -260602, DBLUE = -16777094,
			BLACK = -16777216, DRED = -8116955, TEAL = -16746119;

	enum Match {
		MINE(BLACK, 0.5, 0.75, -2, 120), //
		BLANK(WHITE, 0.5, 0.07, -1, 120), //
		ONE(BLUE, 0.5, 0.7, 1, 120), //
		TWO(GREEN, 0.3, 0.7, 2, 120), //
		THREE(RED, 0.7, 0.38, 3, 120), //
		FOUR(DBLUE, 0.6, 0.5, 4, 120), //
		FIVE(DRED, 0.2, 0.5, 5, 120), //
		SIX(TEAL, 0.65, 0.5, 6, 120);

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
			// System.out.println(tc);
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

	void read() {
		for (int h = 0; h < hei; h++) {
			for (int w = 0; w < wid; w++) {
				board[h * 31 + w] = get(w, h);
			}
		}
	}

	void printBoard() {
		for (int i = 0; i < board.length; i++) {
			if (i % wid == 0)
				System.out.println();
			System.out.print(board[i] == -1 ? "-" : board[i] == -2 ? "*" : board[i]);
		}
	}

	void addAll() {
		for (int i = 0; i < board.length; i++) {
			if (!isComplete(i) && board[i] != -1)
				numCells.add(i);
		}
	}

	void tryComplete() {
		boolean stop = false;
		int count = 0;
		while (!stop) {
			stop = true;
			Set<Integer> rem = new HashSet<>();
			Set<Integer> add = new HashSet<>();
			for (int i : numCells) {
				if (completeCell(i, rem, add))
					stop = false;
			}
			numCells.removeAll(rem);
			numCells.addAll(add);
			rem.clear();
			add.clear();
			// read();
			addAll();
			for (int i : numCells) {
				if (completeCell(i, rem, add))
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
			System.out.println(count++);
		}
	}

	boolean completeCell(int i, Set<Integer> rem, Set<Integer> add) {
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
							click(o % wid, o / wid);
							try {
								Thread.sleep(2);
							}
							catch (InterruptedException e) {
								e.printStackTrace();
							}
							board[o] = get(o % wid, o / wid);
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

	public static void main(String[] args) throws AWTException {
		Minesweeper ms = new Minesweeper(new Robot());
		ms.move(18, 4);
		System.out.println(ms.get(18, 4));
	}

	@Override
	public void run() {
		// move(14, 12);
		// completeCell(i, rem, add)
		read();
		addAll();
		tryComplete();
		System.out.println("Done");
	}
}
