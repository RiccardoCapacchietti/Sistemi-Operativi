package esame;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	public final static int SINGOLO=0;
	public final static int FAMIGLIA=1;
	private int Pmax;
	private int numPacchi;
	private boolean allertaMeteo;
	private Condition [] codaCittadini=new Condition[2];
	private Condition [] codaFornitori=new Condition[2];


	private int [] sospesiFornitori=new int[2];
	private int [] sospesiCittadini=new int[2];
	private Lock lock=new ReentrantLock();
	public Monitor(int capMax) {
		this.Pmax=capMax;
		for(int i=0;i<2;i++) {
		codaCittadini[i]=lock.newCondition();
		codaFornitori[i]=lock.newCondition();
		this.sospesiCittadini[i]=0;
		this.sospesiFornitori[i]=0;
		}
	

		
		this.numPacchi=0;
	}
	

	public void prelevaPacco(String name, int formato) throws InterruptedException {
		lock.lock();
		try {
			if(formato==SINGOLO) {
				while(allertaMeteo==true || sospesiCittadini[FAMIGLIA]>0 || numPacchi==0 ) {
					sospesiCittadini[SINGOLO]++;
					codaCittadini[SINGOLO].await();
					sospesiCittadini[SINGOLO]--;
				}
				numPacchi--;
				System.out.println("Cittadino "+name+" ha appena prelevato un pacco singolo, numPacchi: "+numPacchi +state() );

				if(sospesiFornitori[SINGOLO]>0)
					codaFornitori[SINGOLO].signal();
				else if(sospesiFornitori[FAMIGLIA]>0)
					codaFornitori[FAMIGLIA].signal();
			}else if(formato==FAMIGLIA) {
				while(allertaMeteo==true || numPacchi<3) {
					sospesiCittadini[FAMIGLIA]++;
					codaCittadini[FAMIGLIA].await();
					sospesiCittadini[FAMIGLIA]--;
				}
				numPacchi-=3;
				System.out.println("Cittadino "+name+" ha appena prelevato un pacco famiglia, numPacchi: "+numPacchi +state() );

				if(sospesiFornitori[SINGOLO]>0)
					codaFornitori[SINGOLO].signalAll();//ho tolto 3 pacchi
				if(sospesiFornitori[FAMIGLIA]>0)//non metto lelse if perche magari questi 3 pacchi eprmettono a un singolo di prendere e anche a una famiglia
					codaFornitori[FAMIGLIA].signal();
			}
		}finally {
			lock.unlock();
		}
		
	}

	public void depositaPacco(String name, int formato) throws InterruptedException {
		lock.lock();
		try {
			if(formato==SINGOLO) {
				while(allertaMeteo==true||numPacchi==Pmax ) {
					sospesiFornitori[SINGOLO]++;
					codaFornitori[SINGOLO].await();
					sospesiFornitori[SINGOLO]--;
			}
				numPacchi++;
				System.out.println("Fornitore "+name+" di pacco singolo ha appena depositato un pacco, numPacchi: "+numPacchi +state() );
				if(sospesiCittadini[FAMIGLIA]>0)
					codaCittadini[FAMIGLIA].signal();
				else if(sospesiCittadini[SINGOLO]>0)
					codaCittadini[SINGOLO].signal();
			}else if(formato==FAMIGLIA) {
				while(allertaMeteo==true|| numPacchi+3>Pmax|| sospesiFornitori[SINGOLO]>0) {
					sospesiFornitori[FAMIGLIA]++;
					codaFornitori[FAMIGLIA].await();
					sospesiFornitori[FAMIGLIA]--;
				}
				numPacchi+=3;
				System.out.println("Fornitore "+name+" di pacco famiglia ha appena depositato un pacco triplo, numPacchi: "+numPacchi +state() );
				if(sospesiCittadini[FAMIGLIA]>0)
					codaCittadini[FAMIGLIA].signal();
				if(sospesiCittadini[SINGOLO]>0)
					codaCittadini[SINGOLO].signalAll();//esempio ce un sospeso pacco famiglia a cui manca un pacco, e 2 sospesi singolo
			}
			
		}finally {
			lock.unlock();
		}
	}

	


	public void chiudiPalazzo(String name) {
		lock.lock();
		allertaMeteo=true;
		System.out.println("Addetto "+name+" ha appeba chiuso il palazzo" +state());
		lock.unlock();
	}

	public void apriPalazzo(String name) {
		lock.lock();
		allertaMeteo=false;
		System.out.println("Addetto "+name+" ha appeba aperto il palazzo" +state());
		if(sospesiCittadini[FAMIGLIA]>0)
			codaCittadini[FAMIGLIA].signalAll();
		if(sospesiCittadini[SINGOLO]>0)
			codaCittadini[SINGOLO].signalAll();
		if(sospesiFornitori[SINGOLO]>0)
			codaFornitori[SINGOLO].signalAll();

		if(sospesiFornitori[FAMIGLIA]>0)
			codaFornitori[FAMIGLIA].signalAll();

		lock.unlock();


	}
	

	public String state() {
		return "\n Monitor [Pmax=" + Pmax + ", numPacchi=" + numPacchi + ", allertaMeteo=" + allertaMeteo
				+ ", sospesiFornitori=" + Arrays.toString(sospesiFornitori) + ", sospesiCittadini="
				+ Arrays.toString(sospesiCittadini) + "]";
	}

}
