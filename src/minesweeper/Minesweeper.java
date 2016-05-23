package minesweeper;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class Minesweeper implements Runnable {
	final int x0, y0, wid, hei;
	final double w;

	public static void main(String[] args) throws AWTException {
		Robot r = new Robot();
		Minesweeper ms = new Minesweeper(r);
		for (int h = 0; h < ms.hei; h++) {
			for (int w = 0; w < ms.wid; w++) {
				System.out.print(ms.get(w, h));
				// ms.get(w, h);
			}
			System.out.println();
		}
		// int px = 0, py = 0;
		// Match m = Match.BLANK;
		// r.mouseMove(ms.left(px) + m.getXOff(ms.w), ms.top(py) + m.getYOff(ms.w));
		// System.out.println(r.getPixelColor(ms.left(px) + m.getXOff(ms.w), ms.top(py) + m.getYOff(ms.w)));
		// System.out.println(r.getPixelColor(ms.left(px) + m.getXOff(ms.w), ms.top(py) + m.getYOff(ms.w)).getRGB());
		// System.out.println(m.getRGB());
		// System.out.println(new Color(m.getRGB()));
		// System.out.println(r.getPixelColor(ms.left(px) + m.getXOff(ms.w), ms.top(py) + m.getYOff(ms.w)).getRGB() == m
		// .getRGB());
		// System.out.println();
		// System.out.println();
		// System.out.println(m.matches(r.getPixelColor(ms.left(px) + m.getXOff(ms.w), ms.top(py) + m.getYOff(ms.w))));
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
	}

	Robot r;

	void click(int x, int y) {
		r.mouseMove(xmid(x), ymid(y));
		r.mousePress(InputEvent.BUTTON1_MASK);
		r.mouseRelease(InputEvent.BUTTON1_MASK);
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
		for (Match m : Match.values()) {
			if (m.matches(r.getPixelColor(left(x) + m.getXOff(w), top(y) + m.getYOff(w))))
				return m.getVal();
		}
		// BufferedImage bi = r.createScreenCapture(new Rectangle(left(x), top(y), (int) w, (int) w));
		// for (int i = 0; i < bi.getWidth(); i++) {
		// for (int j = 0; j < bi.getHeight(); j++) {
		// int c = bi.getRGB(i, j);
		// if (c == WHITE)
		// return -1;
		// if (c == BLUE)
		// return 1;
		// if (c == GREEN)
		// return 2;
		// if (c == RED)
		// return 3;
		// if (c == DBLUE)
		// return 4;
		// }
		// }
		return 0;
	}
	static final int WHITE = -1052689, BLUE = -16776961, GREEN = -16745728, RED = -260602, DBLUE = -16777094;

	enum Match {
		BLANK(WHITE, 0.5, 0.07, 9), //
		ONE(BLUE, 0.5, 0.7, 1), //
		TWO(GREEN, 0.3, 0.7, 2), //
		THREE(RED, 0.6, 0.5, 3), //
		FOUR(DBLUE, 0.6, 0.5, 4);

		private final int rgb, val;
		private final double xOff, yOff;

		Match(int rgb, double xOff, double yOff, int val) {
			this.rgb = rgb;
			this.xOff = xOff;
			this.yOff = yOff;
			this.val = val;
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
			int off = Math.abs(c.getRed() - tc.getRed()) //
					+ Math.abs(c.getBlue() - tc.getBlue())//
					+ Math.abs(c.getGreen() - tc.getGreen());
			// System.out.println();
			// System.out.println(off);
			// System.out.println();
			return off < 120;
		}
	}

	@Override
	public void run() {
		int a = 0;
		try {
			while (true) {
				Thread.sleep(100);
				System.out.println("a " + (a++));
			}
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
