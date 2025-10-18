package esame;

import java.util.Random;

public class Visitatore extends Thread {
	private Monitor m;
	private Random rnd;
	public Visitatore(Monitor M, Random r) {
		this.m=M;
		this.rnd=r;
	}
	public void run() {
		try {
			
			m.entraVisitatore(getName());
			sleep(rnd.nextInt(2000,5000));
			m.esceVisitatore(getName());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
