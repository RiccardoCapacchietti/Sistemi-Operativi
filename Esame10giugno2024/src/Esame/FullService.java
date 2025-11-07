package Esame;

import java.util.Random;

public class FullService extends Thread {
	private Monitor m;
	Random r;
	public FullService(Monitor M,Random rnd) {
		this.m=M;
		this.r=rnd;
	}
	public void run() {
		try {
				m.entraFullTunnel(getName());
			sleep(r.nextInt(1,3000));
			m.esceFullTunnel(getName());
			sleep(r.nextInt(1,3000));
			m.entraFullTunnel(getName());
			sleep(r.nextInt(1,3000));
			m.esceFullTunnel(getName());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
