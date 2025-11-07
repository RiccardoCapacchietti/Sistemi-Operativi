package Es11;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
//PER CAPIRLO BENE GUARDARE ES11 IPAD
public class Monitor {
	public final static int UP = 1;
	public final static int DOWN = 0;
	private int maxBaita;
	private int maxSentiero;
	private int numBaita;
	private int numSentiero;
	private int numBaristaInBaita;
	private Lock lock=new ReentrantLock();
	private int [] SospesiS= new int [2];
	private int [] SospesiB=  new int [2];
	private int [] SospesiC= new int [2];
	private Condition [] codaS = new Condition[2];
	private Condition [] codaB = new Condition[2];
	private Condition [] codaC = new Condition[2];
	private int [] numS= new int [2];//questi 3 array di num mi indicano in quanti sono nel sentiero, in up e in down
	private int [] numB= new int [2];
	private int [] numC= new int [2];//in questo caso il numero di comitive, di cui per ognuna ho n perosne
	public Monitor(int maxB,int maxS) {
		if(maxB<0 || maxS<0 || maxB<maxS) throw new IllegalArgumentException("parametri errati");
		this.maxBaita=maxB;
		this.maxSentiero=maxS;
		this.numBaita=0;
		this.numSentiero=0;
		this.numBaristaInBaita=0;
		for(int i =0; i<2; i++){
			this.numS[i]=0;
			this.numB[i]=0;
			this.numC[i]=0;
			this.codaS[i]=lock.newCondition();
			this.codaB[i]=lock.newCondition();
			this.codaC[i]=lock.newCondition();
			this.SospesiS[i]=0;
			this.SospesiB[i]=0;
			this.SospesiC[i]=0;
			
			}
	}
	
	
	
	public void entraSingolo(int dir,String id) throws InterruptedException {
		lock.lock();
		try {
			if(dir==UP) {//sta entrando nel sentiero, tutti prioritari a eccezione del sospesiC[Up] che ha meno priorita di noi
			while(SospesiB[UP]+SospesiB[DOWN]+SospesiC[DOWN] + SospesiS[DOWN] >0 || numSentiero==maxSentiero || numBaita==maxBaita || numBaristaInBaita==0 ) {
				SospesiS[UP]++;
				codaS[UP].await();;
				SospesiS[UP]--;
				
			}
			
			numS[UP]++;//sono entrato nel sentiero come singolo, in direzione up
			numSentiero++;//aumento num sentiero
			System.out.println("Singolo "+id+" entrato nel sentiero dir="+dir+".\n"+stato());

			
			}else if(dir==DOWN) {//sta uscendo dalla baita
				while(numSentiero==maxSentiero||SospesiC[DOWN]>0) {
					SospesiS[DOWN]++;
					codaS[DOWN].await();
					SospesiS[DOWN]--;
				}
				numBaita--;
				numSentiero++;
				numS[DOWN]++;//sto uscendo dalla baita per tornare nel sentiero, risveglio solo i processi che vogliono entrare nella baita,ovvero direzione up
				System.out.println("Singolo "+id+" entrato nel sentiero dir="+dir+".\n"+stato());

				if(SospesiB[UP]>0) codaB[UP].signal();
				else if(SospesiS[UP]>0) codaS[UP].signal();
				else if(SospesiC[UP]>0) codaC[UP].signal();
				
				
				
			}
			
		}finally { lock.unlock();
			
		}
		
	}
	public void esceSingolo(int dir,String id) {
		lock.lock();
		if(dir==UP) {//sta entrando nella baita
			numBaita++;
			numSentiero--;
			numS[UP]--;
			System.out.println("Singolo "+id+" uscito dal sentiero dir="+dir+".\n"+stato());

		}else if(dir==DOWN) {
			numSentiero--;
			numS[DOWN]--;
			System.out.println("Singolo "+id+" uscito dal sentiero dir="+dir+".\n"+stato());

		}
		if(SospesiC[DOWN]>0) codaC[DOWN].signal();
		else if(SospesiS[DOWN]>0) codaS[DOWN].signal();
		else if(SospesiB[DOWN]>0) codaB[DOWN].signal();
		else if(SospesiB[UP]>0) codaB[UP].signal();
		else if(SospesiS[UP]>0) codaS[UP].signal();
		else if(SospesiC[UP]>0) codaC[UP].signal();
		
		lock.unlock();
	}
	public void entraComitiva(int dir, int num,String id) throws InterruptedException {
		lock.lock();
		try {
			if(dir==UP) {
				while(maxSentiero-numSentiero<num || numC[DOWN]>0 || 
						SospesiS[UP]+SospesiB[UP]+ SospesiB[DOWN]+ SospesiS[DOWN]+SospesiC[DOWN]>0 ||
						maxBaita-numBaita<num || numBaristaInBaita == 0) {
					SospesiC[UP]++;
					codaC[UP].await();
					SospesiC[UP]--;
					
				}

				numSentiero+=num;
				numC[UP]++;
				System.out.println("Comitiva "+id+" ("+num+" persone) entrata nel sentiero dir="+dir+".\n"+ stato());	

			}else if(dir==DOWN) {
				while(maxSentiero-numSentiero<num || numC[UP]>0 ) {
					SospesiC[DOWN]++;
					codaC[DOWN].await();
					SospesiC[DOWN]--;
				}

				numBaita-=num;
				numSentiero+=num;
				numC[DOWN]++;
				System.out.println("Comitiva "+id+" ("+num+" persone) entrata nel sentiero dir="+dir+".\n"+ stato());	

				//stanno uscendo dalla baita n persone, faccio una signalAll cosi risveglio piu processi
				if(SospesiB[UP]>0) codaB[UP].signalAll();
				else if(SospesiS[UP]>0) codaS[UP].signalAll();
				else if(SospesiC[UP]>0) codaC[UP].signalAll();
				
				
			}
			
		}finally {
			lock.unlock();
		}
	}
	public void esceComitiva(int dir, int num,String id) {
		lock.lock();
		if(dir==UP) {//sta entrando nella baita
			numBaita+=num;
			numSentiero-=num;
			numC[UP]--;
			System.out.println("Comitiva "+id+" uscita dal sentiero dir="+dir+".\n"+stato());

		}else if(dir==DOWN) {
			numSentiero-=num;
			numS[DOWN]--;
			System.out.println("Comitiva "+id+" uscita dal sentiero dir="+dir+".\n"+stato());

		}
		if(SospesiC[DOWN]>0) codaC[DOWN].signalAll();
		else if(SospesiS[DOWN]>0) codaS[DOWN].signalAll();
		else if(SospesiB[DOWN]>0) codaB[DOWN].signalAll();
		else if(SospesiB[UP]>0) codaB[UP].signalAll();
		else if(SospesiS[UP]>0) codaS[UP].signalAll();
		else if(SospesiC[UP]>0) codaC[UP].signalAll();
		
		lock.unlock();
	}
	public void entraBarista(int dir,String id) throws InterruptedException {
		lock.lock();
		try {
			if(dir==UP) {
			while(numSentiero==maxSentiero|| SospesiC[DOWN]+SospesiB[DOWN]+SospesiS[DOWN]>0 || numBaita==maxBaita) {
			
				SospesiB[UP]++;
				codaB[UP].await();;
				SospesiB[UP]--;
				
				
			}
			numB[UP]++;//sono entrato nel sentiero come barista, in direzione up
			numSentiero++;//aumento num sentiero
			System.out.println("Barista "+id+" entrato nel sentiero dir="+dir+".\n"+stato());
			
			}else if(dir==DOWN) {//sta uscendo dalla baita
				while(numSentiero==maxSentiero||SospesiC[DOWN]>0 || numBaristaInBaita==1) {
					SospesiB[DOWN]++;
					codaB[DOWN].await();
					SospesiB[DOWN]--;
				}
				numBaita--;
				numSentiero++;
				numB[DOWN]++;//sto uscendo dalla baita per tornare nel sentiero, risveglio solo i processi che vogliono entrare nella baita,ovvero direzione up
				numBaristaInBaita--;
				System.out.println("Barista "+id+" entrato nel sentiero dir="+dir+".\n"+stato());

				if(SospesiB[UP]>0) codaB[UP].signal();
				else if(SospesiS[UP]>0) codaS[UP].signal();
				else if(SospesiC[UP]>0) codaC[UP].signal();
				
			}
			
		}finally { lock.unlock();
			
		}
	}
	public void esceBarista(int dir,String id) {
		// TODO Auto-generated method stub
		//ricorda se barisca esce in direzione up devo incrementae numBaristaInBaita
		lock.lock();
		if(dir==UP) {//sta entrando nella baita
			numBaita++;
			numBaristaInBaita++;
			numSentiero--;
			numB[UP]--;
			System.out.println("Barista "+id+" uscito dal sentiero dir="+dir+".\n"+stato());
		}else if(dir==DOWN) {
			numSentiero--;
			numB[DOWN]--;
			System.out.println("Barista "+id+" uscito dal sentiero dir="+dir+".\n"+stato());
		}
		if(SospesiC[DOWN]>0) codaC[DOWN].signal();
		else if(SospesiS[DOWN]>0) codaS[DOWN].signal();
		else if(SospesiB[DOWN]>0) codaB[DOWN].signal();
		else if(SospesiB[UP]>0) codaB[UP].signal();
		else if(SospesiS[UP]>0) codaS[UP].signal();
		else if(SospesiC[UP]>0) codaC[UP].signal();
		
		lock.unlock();
	}
	
	public String stato() { //per DEBUG
		return "[totInBaita="+numBaita+" baristiInBaita="+numBaristaInBaita+" totInSentiero="+numSentiero
				+ " singoli[UP]="+numS[UP]+" comitive[UP]="+numC[UP]+" baristi[UP]="+numB[UP]
						+ " singoli[DOWN]="+numS[DOWN]+" comitive[DOWN]="+numC[DOWN]+" baristi[DOWN]="+numB[DOWN]
								+ " sospS[UP]="+SospesiS[UP]+" sospC[UP]="+SospesiC[UP]+" sospB[UP]="+SospesiB[UP]
										+ " sospS[DOWN]="+SospesiS[DOWN]+" sospC[DOWN]="+SospesiC[DOWN]+" sospB[DOWN]="+SospesiB[DOWN]+"   ]";
	}

}
