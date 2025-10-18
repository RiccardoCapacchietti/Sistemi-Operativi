package esame;

import java.util.Random;

public class Dipendente extends Thread{
	private Monitor m;
	private Random rnd;
	public Dipendente(Monitor M, Random r) {
		this.m=M;
		this.rnd=r;
	}
	public void run() {
		try {
			while(true) {
			m.entraDipendente(getName());
			sleep(rnd.nextInt(2000,5000));
			m.esceDipendente(getName());
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
