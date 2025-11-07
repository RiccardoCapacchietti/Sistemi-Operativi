package Esame;

import java.util.Random;

public class Addetto extends Thread {
	Monitor m;
	Random r;
	public Addetto(Monitor M,Random rnd) {
		this.m=M;
		this.r=rnd;
	}
	public void run() {
		try {
			
			while(true) {
			m.iniziaSostituzione(getName());
		sleep(r.nextInt(2000,3000));
		m.terminaSostituzione(getName());
		sleep(1000);
			}
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}
