package Esame;

import java.util.Random;

public class Cliente extends Thread {
	private Monitor m;
	private Random r;
	private double t;
	public Cliente(Monitor M,Random rnd,double tipo) {
		if(tipo!=Monitor.PICCOLO && tipo!=Monitor.GRANDE) throw new IllegalArgumentException("parametro errato");
		this.m=M;
		this.r=rnd;
		this.t=tipo;
	}
	public void run() {
		int i=0;
		int random= r.nextInt(1,10);
		try {
			
			while(i<random) {
			m.iniziaErogazione(t,getName());
		sleep(r.nextInt(1,1500));
		m.terminaErogazione(t,getName());
		sleep(3000);
		i++;
			}
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
