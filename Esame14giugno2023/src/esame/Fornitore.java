package esame;

import java.util.Random;

public class Fornitore extends Thread {
	private Monitor m;
	private Random r;
	public Fornitore(Monitor M,Random rnd) {
		this.m=M;
		this.r=rnd;
	}
	public void run() {
		try {
			sleep(r.nextInt(2000,20000));
			m.depositaPacco(getName(),r.nextInt(0,2));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
 
}
