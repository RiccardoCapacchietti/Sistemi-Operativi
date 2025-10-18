package esame;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		int capacitaMax=15;
		int numdipendenti=17;
		int numvisitatori=30;
		Random r=new Random();
		Monitor m=new Monitor(capacitaMax);
		Visitatore [] visitatori =new Visitatore [numvisitatori];
		Dipendente [] dipendenti= new Dipendente [numdipendenti];
		for(int i=0;i<numdipendenti;i++) {
			dipendenti[i]=new Dipendente(m,r);
		}
		for(int i=0;i<numvisitatori;i++) {
			visitatori[i]=new Visitatore(m,r);
		}
		Usciere u=new Usciere(m,r);
		u.start();
		for(int i=0;i<numdipendenti;i++) {
			dipendenti[i].start();
		}
		for(int i=0;i<numvisitatori;i++) {
			visitatori[i].start();;
		}
		
		
	}

}
