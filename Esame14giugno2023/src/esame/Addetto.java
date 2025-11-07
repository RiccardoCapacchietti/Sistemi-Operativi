package esame;

import java.util.Random;

public class Addetto extends Thread {
	private Monitor m;
	private Random r;
	public Addetto(Monitor M,Random rnd) {
		this.m=M;
		this.r=rnd;
	}
	public void run() {
		try {
			while(true) {
			sleep(r.nextInt(2000,20000));
			m.chiudiPalazzo(getName());
			sleep(r.nextInt(6000,20000));
			m.apriPalazzo(getName());
			sleep(r.nextInt(10000,20000));

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
