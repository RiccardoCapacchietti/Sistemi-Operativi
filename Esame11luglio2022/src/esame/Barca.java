package esame;

import java.util.Random;

public class Barca extends Thread{
	private int tipo;
	private Monitor m;
	private Random r;
	private int IN;
	private int OUT;
	
		
	public Barca(Monitor M,Random rnd, int t) {
		this.m=M;
		this.r=rnd;
		this.tipo=t;
		this.IN=Monitor.IN;
		this.OUT=Monitor.OUT;
		
	}
	public void run() {
		try {
			m.entraBarca(getName(),tipo,IN,r.nextInt(0,2));//random per il parcheggio 
			sleep(r.nextInt(5000,15000));
			m.esceBarca(getName(),tipo,IN,r.nextInt(0,2));
			sleep(r.nextInt(10000,20000));
			m.entraBarca(getName(),tipo,OUT,r.nextInt(0,2));
			sleep(r.nextInt(5000,15000));
			m.esceBarca(getName(),tipo,OUT,r.nextInt(0,2));

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
