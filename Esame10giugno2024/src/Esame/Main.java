package Esame;

import java.util.Random;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		int numAree=10;
		Random rnd=new Random();
		Monitor m=new Monitor(numAree);
		Addetto a=new Addetto(m,rnd);
		int numFs=5;
		FullService [] fs=new FullService [numFs];
		int numMs=15;
		MonoService [] ms= new MonoService[numMs];
		for(int i=0;i<numFs;i++) {
			fs[i]=new FullService(m,rnd);
		}
		for(int i=0;i<numMs;i++) {
			ms[i]=new MonoService(m,rnd.nextInt(0,2),rnd);//il tipo è 0 o 1, lo randomizzo
		}
		a.start();
		for(int i=0;i<numFs;i++) {
			fs[i].start();
		}
		
		for(int i=0;i<numMs;i++) {
			ms[i].start();
		}
		a.join();
		for(int i=0;i<numFs;i++) {
			fs[i].join();
		}
		for(int i=0;i<numMs;i++) {
			ms[i].join();
		}
	}

}
