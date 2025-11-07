package esame;

import java.util.Random;

public class Utente extends Thread{
	private Monitor m;
	private int tipo;
	private char Sp;
	private char Sa;
	private Random r;
	public Utente(Monitor M, int t,char partenza,char arrivo,Random rnd) {
		if(t!=Monitor.SINGOLO && t!=Monitor.COPPIA) throw new IllegalArgumentException("tipo Utente errato");
		this.m=M;
		this.tipo=t;
		this.Sp=partenza;
		this.Sa=arrivo;
		this.r=rnd;
	
	}
	public void run() {
		
		try {
			m.richiediBici(tipo,Sp,getName());
			sleep(r.nextInt(2000,4000));
			m.restituisciBici(tipo,Sa,getName());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
