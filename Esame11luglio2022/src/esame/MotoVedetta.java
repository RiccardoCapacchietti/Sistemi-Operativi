package esame;

import java.util.Random;

public class MotoVedetta extends Thread {
	Monitor m;
	Random r;
	private int IN;
	private int OUT;
	public MotoVedetta(Monitor M, Random rnd) {
		this.m=M;
		this.r=rnd;
		this.IN=Monitor.IN;
		this.OUT=Monitor.OUT;
	}
	public void run() {
		try {
			while(true) {
				m.entraMv(getName(),OUT);
				sleep(r.nextInt(5000, 10000));
				m.esceMv(getName(),OUT);
				sleep(r.nextInt(10000, 20000));
				m.entraMv(getName(),IN);
				sleep(r.nextInt(5000, 10000));
				m.esceMv(getName(),IN);
				sleep(r.nextInt(30000, 50000));



			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
