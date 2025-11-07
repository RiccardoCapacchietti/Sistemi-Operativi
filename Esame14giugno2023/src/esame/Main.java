package esame;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		int pMax=100;
		int nCitt=100;
		int nForn=80;
		Monitor m=new Monitor(pMax);
		Cittadino [] cittadini=new Cittadino[nCitt];
		Fornitore [] fornitori=new Fornitore[nForn];
		Random r=new Random();
		Addetto a=new Addetto(m,r);
		for(int i=0;i<nCitt;i++) {
			cittadini[i]=new Cittadino(m,r);
		}
		for(int i=0;i<nForn;i++) {
			fornitori[i]=new Fornitore(m,r);
		}
		for(int i=0;i<nForn;i++) {
			fornitori[i].start();
		}
		for(int i=0;i<nCitt;i++) {
			cittadini[i].start();
		}
		
		a.start();
		
	}

}
