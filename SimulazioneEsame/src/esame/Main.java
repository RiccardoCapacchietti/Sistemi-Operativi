package esame;

import java.util.Random;


public class Main {

	public static void main(String[] args) {
		Random r=new Random();
		int maxBiciA=2;
		int maxBiciB=1;
		int maxBiciC=2;
		Monitor m=new Monitor(maxBiciA,maxBiciB,maxBiciC);
		int numUtenti=10;
		Utente [] utenti=new Utente[numUtenti];
		for(int i=0; i<2;i++) {
			int j=r.nextInt(0,2);
			if(j==0) utenti[i]=new Utente(m,Monitor.SINGOLO,'A','C',r);
			else utenti[i]=new Utente(m,Monitor.COPPIA,'A','C',r);
		}
		for(int i=2; i<4;i++) {
			int j=r.nextInt(0,2);
			if(j==0) utenti[i]=new Utente(m,Monitor.SINGOLO,'A','B',r);
			else utenti[i]=new Utente(m,Monitor.COPPIA,'A','B',r);
		}
		for(int i=4; i<6;i++) {
			int j=r.nextInt(0,2);
			if(j==0) utenti[i]=new Utente(m,Monitor.SINGOLO,'A','A',r);
			else utenti[i]=new Utente(m,Monitor.COPPIA,'A','A',r);
		}
		for(int i=6; i<8;i++) {
			int j=r.nextInt(0,2);
			if(j==0) utenti[i]=new Utente(m,Monitor.SINGOLO,'B','C',r);
			else utenti[i]=new Utente(m,Monitor.COPPIA,'B','C',r);
		}
		for(int i=8; i<10;i++) {
			int j=r.nextInt(0,2);
			if(j==0) utenti[i]=new Utente(m,Monitor.SINGOLO,'C','A',r);
			else utenti[i]=new Utente(m,Monitor.COPPIA,'C','A',r);
		}
		for(int i=0;i<numUtenti;i++) {
			utenti[i].start();
		}
	}

}
