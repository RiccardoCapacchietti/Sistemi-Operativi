package esame;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	public final static int ENTRA=0;
	public final static int ESCE=1;
	private int max,numVisitatori,numDipendenti;
	private boolean uscierePresente;
	private Condition [] codaDipendente= new Condition [2];
	private Condition [] codaVisitatore= new Condition [2];
	private Condition codaUsciere;//ho un solo usciere, lo addormento o lo sveglio a prescindere da quello che vuole fare
	private int [] sospesiVisitatori=new int [2];
	private int [] sospesiDipendenti=new int [2];
	private int sospesoUsciere;//ho un solo usciere, se è sospeso va a prescindere da entra o esci
	private Lock lock=new ReentrantLock();
	public Monitor(int capMax) {
		this.max=capMax;
		this.numVisitatori=0;
		this.numDipendenti=0;
		this.uscierePresente=false;
		codaUsciere=lock.newCondition();

		for(int i=0;i<2;i++) {
			codaDipendente[i]=lock.newCondition();
			codaVisitatore[i]=lock.newCondition();
			sospesiVisitatori[i]=0;
			sospesiDipendenti[i]=0;
			
		}
		sospesoUsciere=0;
	}





	public void entraUsciere(String name) throws InterruptedException {
		lock.lock();
		try {
			while(numVisitatori>0) {//l usciere entra solo se la sala è vuota, non serve controllare se dipendenti >0 perche tanto possono stare dentro solo se usciere è true
				sospesoUsciere++;
				codaUsciere.await();
				sospesoUsciere--;
			}
			uscierePresente=true;
			System.out.println("Sono l'usciere "+name+ " , sono appena entrato in sala è vuota, num dipendenti: " +numDipendenti+" num visitatori "+numVisitatori );
			if(sospesiDipendenti[ENTRA]>0)
				codaDipendente[ENTRA].signalAll();//risveglio tutti i dipendenti che volevano entrare, ma non cera lusciere
		}finally {
			lock.unlock();
		}
	}

	public void esceUsciere(String name) throws InterruptedException {
		lock.lock();
		try {
			while(numDipendenti>0) {//usciere non può uscire se ci sono dipendendit nella sala
				sospesoUsciere++;
				codaUsciere.await();
				sospesoUsciere--;
			}
			System.out.println("Sono l'usciere "+name +" sto per uscire dalla sala, num dipendenti: " +numDipendenti+" num visitatori "+ numVisitatori);
			uscierePresente=false;
			if(sospesiVisitatori[ENTRA]>0)
				codaVisitatore[ENTRA].signalAll();//usciere uscito, riunione finita, sveglio tutti i visitatori che volevano entrare
		}finally {
			lock.unlock();
		}
	}

	public void entraDipendente(String name) throws InterruptedException {
		lock.lock();
		try {
			while(sospesoUsciere>0 || numDipendenti+1==max || uscierePresente==false) {//il +1 indica l'usciere, se max è 100 e ho 99 dipendenti e un usciere, il dipendente non puo entrare
				sospesiDipendenti[ENTRA]++;
				codaDipendente[ENTRA].await();
				sospesiDipendenti[ENTRA]--;
			}
			numDipendenti++;
			System.out.println("Sono il dipendente " +name+" e sono appena entrato in sala, num dipendenti: "+numDipendenti+ " numVisitatori: "+numVisitatori);
		} finally {
			lock.unlock();
		}
	}

	public void esceDipendente(String name) {
		lock.lock();
		numDipendenti--;
		System.out.println("Sono il dipendente " +name+" e sono appena uscito dalla sala, num dipendenti: "+numDipendenti+ " numVisitatori: "+numVisitatori);
		if(sospesoUsciere>0)
			codaUsciere.signal();
		
		
		lock.unlock();
	}

	public void entraVisitatore(String name) throws InterruptedException {
		lock.lock();
		try {
			while(sospesoUsciere>0 || numVisitatori==max ||( uscierePresente==true && sospesiDipendenti[ENTRA]>0 ) ) {
				sospesiVisitatori[ENTRA]++;
				codaVisitatore[ENTRA].await();
				sospesiVisitatori[ENTRA]--;	
			}
			numVisitatori++;
			System.out.println("Sono il visitatore "+name+" e sono appena entrato in sala, num visitatori: "+numVisitatori+ " numDipendenti:" +numDipendenti);
			
		}finally {
			lock.unlock();
		}
	}

	public void esceVisitatore(String name) {
		lock.lock();
		numVisitatori--;
		System.out.println("Sono il visitatore "+name+" e sono appena uscito dalla sala, num visitatori: "+numVisitatori+ " numDipendenti:" +numDipendenti);
		if(sospesoUsciere>0)
			codaUsciere.signal();
		
		lock.unlock();

	}

}
