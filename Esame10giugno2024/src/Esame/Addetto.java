package Esame;

import java.util.Random;

public class Addetto extends Thread{
	private Monitor m;
	Random r;

	public Addetto(Monitor M,Random rnd) {
		this.m=M;
		this.r=rnd;
		
		
	}
	public void run() {
		try {
			while(true) {
				m.entraAddetto(getName());
			sleep(r.nextInt(1,3000));
			m.esceAddetto(getName());
			sleep(r.nextInt(3000,10000));
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
