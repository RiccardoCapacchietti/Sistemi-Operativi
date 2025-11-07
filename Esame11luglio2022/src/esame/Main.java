package esame;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		int pStandard=10;
		int pMaxi=15;
		int nMv=2;
		Random r=new Random();
		int nBarche=100;
		Monitor m=new Monitor(pStandard,pMaxi);
		MotoVedetta [] MotoVedette= new MotoVedetta[nMv];
		Barca [] Barche = new Barca [nBarche];
		for(int i=0; i<nMv;i++) {
			MotoVedette[i]=new MotoVedetta(m,r);
		}
		for(int i=0;i<nBarche;i++) {
			Barche[i]=new Barca(m,r,r.nextInt(0,2));
		}
		for(int i=0; i<nBarche;i++) {
			Barche[i].start();
		}
		for(int i=0; i<nMv;i++) {
			MotoVedette[i].start();
		}
		
	}

}
