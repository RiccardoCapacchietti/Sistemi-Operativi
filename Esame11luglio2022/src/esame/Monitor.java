package esame;

import java.util.concurrent.locks.Condition;
import java.util.Arrays;
import java.util.concurrent.locks.*;
public class Monitor {
	public final static int IN=0;
	public final static int OUT=1;
	public final static int PICCOLA=0;
	public final static int GRANDE=1;
	public final static int PSTANDARD=0;
	public final static int PGRANDE=1;
	private int postiStand;
	private int postiMaxi;
	private int mvNelCanale;
	private int [] barcaGrandeCanale=new int[2];
	private int [] barcaPiccolaCanale=new int[2];

	private Condition [] codaMotoVedette=new Condition[2];
	private Condition [] codaBarcaPiccola=new Condition[2];
	private Condition [] codaBarcaGrande=new Condition[2];

	private Lock lock = new ReentrantLock();
	private int [] sospesiMv=new int[2];
	private int [] sospesiBarcaPiccola=new int[2];
	private int [] sospesiBarcaGrande=new int[2];

	public Monitor(int capStand,int capMaxi) {
		this.postiStand=capStand;
		this.postiMaxi=capMaxi;
		mvNelCanale=0;
		barcaGrandeCanale[IN]=0;
		barcaGrandeCanale[OUT]=0;
		barcaPiccolaCanale[IN]=0;
		barcaPiccolaCanale[OUT]=0;
		for(int i=0;i<2;i++) {
			codaMotoVedette[i]=lock.newCondition();
			codaBarcaPiccola[i]=lock.newCondition();
			codaBarcaGrande[i]=lock.newCondition();
			sospesiMv[i]=0;
			sospesiBarcaPiccola[i]=0;
			sospesiBarcaGrande[i]=0;
			
		}
	}
	
	public void entraMv(String name, int dir) throws InterruptedException {
		lock.lock();
		try {
		if(dir==OUT) {
			while((barcaGrandeCanale[IN] + barcaGrandeCanale[OUT] + barcaPiccolaCanale[IN]
					+ barcaPiccolaCanale[OUT]+ mvNelCanale)>0) {
				sospesiMv[OUT]++;
				codaMotoVedette[OUT].await();
				sospesiMv[OUT]--;

			}
			mvNelCanale++;
			System.out.println("motovedetta" +name+" entra nel canale in direzione out"+state());
		}else if(dir==IN){
			while(sospesiMv[OUT]>0||(barcaGrandeCanale[IN]+barcaGrandeCanale[OUT] 
					+barcaPiccolaCanale[IN]+barcaPiccolaCanale[OUT]+ mvNelCanale)>0) {
				sospesiMv[IN]++;
				codaMotoVedette[IN].await();
				sospesiMv[IN]--;
			}
			mvNelCanale++;
			System.out.println("motovedetta" +name+" entra nel canale in direzione In"+state());

		}
		}finally {
			lock.unlock();
		}
		
	}



	public void esceMv(String name, int dir) {
		lock.lock();
		mvNelCanale--;
		if(dir==OUT)
			System.out.println("motovedetta" +name+" esce dal canale in direzione Out"+state());
		else if(dir==IN)
			System.out.println("motovedetta" +name+" esce dal canale in direzione In"+state());
		
		if(sospesiMv[OUT]>0)
			codaMotoVedette[OUT].signal();
		else if(sospesiMv[IN]>0)
			codaMotoVedette[IN].signal();
		else { 
			
		 if(sospesiBarcaGrande[OUT]>0)
			codaBarcaGrande[OUT].signalAll();
		 if(sospesiBarcaPiccola[OUT]>0)
	    	codaBarcaPiccola[OUT].signalAll();
		 if(sospesiBarcaGrande[IN]>0)
			codaBarcaGrande[IN].signalAll();
		 if(sospesiBarcaPiccola[IN]>0)
	    	codaBarcaPiccola[IN].signalAll();	
		}
		lock.unlock();
	}
	
	//controllo prima di entrare nel canale se ce posto, altrimenti entrerei nel canale senza uscirne mai finche non esce una barca, rishio deadlock
	public void entraBarca(String name, int tipo, int dir,int parcheggio) throws InterruptedException {
		lock.lock();
		try {
		if(tipo==PICCOLA) {
			if(dir==IN) {
				while(sospesiMv[OUT]>0 || sospesiMv[IN]>0|| sospesiBarcaGrande[OUT]>0||
						sospesiBarcaGrande[IN]>0||sospesiBarcaPiccola[OUT]>0||
						(barcaGrandeCanale[OUT]+ mvNelCanale)>0 ||
						( parcheggio==PSTANDARD && postiStand<1)||
						( parcheggio==PGRANDE && postiMaxi<1)) {
					sospesiBarcaPiccola[IN]++;
					codaBarcaPiccola[IN].await();
					sospesiBarcaPiccola[IN]--;
					
				}
				if(parcheggio==PSTANDARD) postiStand--;
				else postiMaxi--;
				barcaPiccolaCanale[IN]++;
				System.out.println("Barca piccola" +name+" entra nel canale in direzione IN"+state());
				
			}else if(dir==OUT) {
				while(sospesiMv[OUT]>0 || sospesiMv[IN]>0|| sospesiBarcaGrande[OUT]>0||
						 (barcaGrandeCanale[IN]+
						mvNelCanale)>0) {
					sospesiBarcaPiccola[OUT]++;
					codaBarcaPiccola[OUT].await();
					sospesiBarcaPiccola[OUT]--;
				}
				barcaPiccolaCanale[OUT]++;
				System.out.println("Barca piccola" +name+" entra nel canale in direzione OUT e libera un posto"+state());
				if(parcheggio==PSTANDARD) {
					postiStand++;
					if(sospesiBarcaPiccola[IN]>0)
						codaBarcaPiccola[IN].signal();
				} else if(parcheggio==PGRANDE) {
					postiMaxi++;
					if(sospesiBarcaGrande[IN]>0)
						codaBarcaGrande[IN].signal();
					if(sospesiBarcaPiccola[IN]>0)
						codaBarcaPiccola[IN].signal();

				}
				

				

			}
		}else if(tipo==GRANDE) {
			if(dir==IN) {
				while(sospesiMv[OUT]>0 || sospesiMv[IN]>0|| sospesiBarcaGrande[OUT]>0||
						sospesiBarcaPiccola[OUT]>0|| (barcaGrandeCanale[OUT]+barcaPiccolaCanale[OUT]
						+ mvNelCanale)>0|| postiMaxi<1) {
					sospesiBarcaGrande[IN]++;
					codaBarcaGrande[IN].await();
					sospesiBarcaGrande[IN]--;
				}
				postiMaxi--;
				barcaGrandeCanale[IN]++;
				System.out.println("Barca Grande" +name+" entra nel canale in direzione IN"+state());

			}else if(dir==OUT) {
				while(sospesiMv[OUT]>0 || sospesiMv[IN]>0|| sospesiBarcaGrande[OUT]>0||
						sospesiBarcaPiccola[OUT]>0|| (barcaGrandeCanale[OUT]+ barcaPiccolaCanale[OUT]
						+ mvNelCanale)>0  ) {
					sospesiBarcaGrande[OUT]++;
					codaBarcaGrande[OUT].await();
					sospesiBarcaGrande[OUT]--;
				}
				postiMaxi++;
				barcaGrandeCanale[OUT]++;
				System.out.println("Barca Grande" +name+" entra nel canale in direzione out e libero un posto"+state());
				if(sospesiBarcaGrande[IN]>0)
					codaBarcaGrande[IN].signal();
				if(sospesiBarcaPiccola[IN]>0)
					codaBarcaPiccola[IN].signal();

			}
		}
		}finally{
			lock.unlock();
		}
	}
	public void esceBarca(String name, int tipo, int dir, int parcheggio) {		
		lock.lock();
		if(tipo==PICCOLA) {
			if(dir==IN) {
				barcaPiccolaCanale[IN]--;
				System.out.println("Barca Piccola" +name+" esce dal canale in direzione IN"+state());

				

			}else if(dir==OUT) {
				barcaPiccolaCanale[OUT]--;
				System.out.println("Barca Piccola" +name+" esce dal canale in direzione OUT"+state());
				
			}
			if(sospesiMv[OUT]>0)
				codaMotoVedette[OUT].signal();
			else if(sospesiMv[IN]>0)
				codaMotoVedette[IN].signal();
			else if(sospesiBarcaGrande[OUT]>0)
				codaBarcaGrande[OUT].signal();
			else if(sospesiBarcaPiccola[OUT]>0)
				codaBarcaPiccola[OUT].signal();
			else if(sospesiBarcaGrande[IN]>0)
				codaBarcaGrande[IN].signal();
			else if(sospesiBarcaPiccola[IN]>0)
				codaBarcaPiccola[IN].signal();
			
			
		}else if(tipo==GRANDE) {
			if(dir==IN) {
				barcaGrandeCanale[IN]--;
				System.out.println("Barca Grande" +name+" esce dal canale in direzione IN "+state());

			}else if(dir==OUT) {
				barcaGrandeCanale[OUT]--;
				System.out.println("Barca Grande" +name+" esce dal canale in direzione OUT"+state());

			}
			if(sospesiMv[OUT]>0)
				codaMotoVedette[OUT].signal();
			else if(sospesiMv[IN]>0)
				codaMotoVedette[IN].signal();
			else if(sospesiBarcaGrande[OUT]>0)
				codaBarcaGrande[OUT].signal();
			else if(sospesiBarcaPiccola[OUT]>0)
				codaBarcaPiccola[OUT].signal();
			else if(sospesiBarcaGrande[IN]>0)
				codaBarcaGrande[IN].signal();
			else if(sospesiBarcaPiccola[IN]>0)
				codaBarcaPiccola[IN].signal();
		}
		lock.unlock();
	}
	public String state() {
		return "\n Monitor [postiStand=" + postiStand + ", postiMaxi=" + postiMaxi + ", mvNelCanale=" + mvNelCanale
				+ ", barcaGrandeCanale=" + Arrays.toString(barcaGrandeCanale) + ", barcaPiccolaCanale="
				+ Arrays.toString(barcaPiccolaCanale) + ", sospesiMv=" + Arrays.toString(sospesiMv)
				+ ", sospesiBarcaPiccola=" + Arrays.toString(sospesiBarcaPiccola) + ", sospesiBarcaGrande="
				+ Arrays.toString(sospesiBarcaGrande) + "]";
	}

}
