package Esame;

import java.util.Random;

public class MonoService extends Thread{
	private Monitor m;
	private int tipoServizio;
	Random r;
	
	//se tipo==0 uso il tunnel, se ==1 entro nell area
	public MonoService(Monitor M,int tipo,Random rnd) {
		this.m=M;
		this.tipoServizio=tipo;
		this.r=rnd;
	}
	public void run() {
		try {
				m.entraMono(tipoServizio,getName());
			sleep(r.nextInt(1,3000));
			m.esceMono(tipoServizio,getName());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
