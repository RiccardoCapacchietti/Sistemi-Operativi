package esame;

import java.util.Random;

public class Usciere extends Thread {
	private Monitor m;
	private Random rnd;
	public Usciere(Monitor M, Random r) {
		this.m=M;
		this.rnd=r;
	}
	public void run() {
		try {
			while(true) {
			m.entraUsciere(getName());
			sleep(rnd.nextInt(2000,5000));
			m.esceUsciere(getName());
			sleep(30000);//dopo che esce l'usciere lo addormento per parecchio, non ci deve essere sempre una riunione
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
