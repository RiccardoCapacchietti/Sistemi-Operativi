package Es11;

import java.util.Random;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Monitor m=new Monitor(35,25);
		int numBaristi=5;
		int numSingolo=10;
		int numComitiva=4;
		Random r=new Random();
		Singolo [] escursionisti=new Singolo[numSingolo];
		Comitiva [] comitive = new Comitiva[numComitiva];
		Barista [] baristi=new Barista[numBaristi];
		for(int i=0; i<numSingolo; i++) {
			escursionisti[i]=new Singolo(m);
		}
		for(int i=0;i<numComitiva;i++) {
			comitive[i]=new Comitiva(m,r.nextInt(1, 10));
		}
		for(int i=0;i<numBaristi;i++) {
			baristi[i]=new Barista(m);
		}
		for(int i=0; i<numSingolo; i++) {
			escursionisti[i].start();
		}
		for(int i=0;i<numComitiva;i++) {
			comitive[i].start();
		}
		for(int i=0;i<numBaristi;i++) {
			baristi[i].start();
		}
		for(int i=0; i<numSingolo; i++) {
			escursionisti[i].join();
		}
		for(int i=0;i<numComitiva;i++) {
			comitive[i].join();
		}
		for(int i=0;i<numBaristi;i++) {
			baristi[i].join();
		}
		
	}

}
