package esame;

import java.util.Random;

public class Cittadino extends Thread{
	private Monitor m;
	private Random r;
	public Cittadino(Monitor M,Random rnd) {
		this.m=M;
		this.r=rnd;
	}
	public void run() {
		try {
			sleep(r.nextInt(2000,20000));

			m.prelevaPacco(getName(),r.nextInt(0,2));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
