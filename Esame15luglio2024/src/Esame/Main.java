package Esame;

import java.util.Random;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		double litriMax=3.5;
		Monitor m=new Monitor(litriMax);
	    Random r=new Random();
	    int numClienti=3;
	    Cliente [] clienti= new Cliente [30];
	    Addetto a=new Addetto(m,r);
		for(int i=0;i<numClienti;i++) {
			int j=r.nextInt(0,2);
			if(j==0)
				clienti[i]=new Cliente(m,r,Monitor.PICCOLO);
			else clienti[i]=new Cliente(m,r,Monitor.GRANDE);
		}
		for(int i=0;i<numClienti;i++) {
			clienti[i].start();
		}
		a.start();
		for(int i=0;i<numClienti;i++) {
			clienti[i].join();
		}
		a.join();
	}

}
