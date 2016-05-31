package minesweeper;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class Player implements Runnable {
	public static void main(String[] args) throws AWTException, InterruptedException {
		Robot r = new Robot();
		// r.mouseMove(370, 180);
		// r.mousePress(InputEvent.BUTTON1_MASK);
		// r.mouseRelease(InputEvent.BUTTON1_MASK);
		r.mouseMove(370, 380);
		r.mousePress(InputEvent.BUTTON1_MASK);
		r.mouseRelease(InputEvent.BUTTON1_MASK);
		Thread.sleep(50);
		Minesweeper ms = Minesweeper.chezpoor(r);
		Thread game = new Thread(ms);
		game.start();
		Thread.sleep(10000);
		game.interrupt();
		game.stop();
		// r.mouseMove(left(25) + Match.BLUE.xOff, top(7) + Match.BLUE.yOff);
		// System.out.println(r.getPixelColor(xmid(24) - 9, ymid(7)).getRGB());
		// for (int i = 0; i < HEI; i++) {
		// for (int j = 0; j < WID; j++) {
		// r.mouseMove(left(j), top(i));
		// Thread.sleep(100);
		// // System.out.print(get(j, i));
		//
		// }
		// System.out.println();
		// }
	}

	@Override
	public void run() {

		try {
			while (true) {
				Thread.sleep(5);

			}
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
}
