package Es10;


import java.util.concurrent.locks.*;

public class Fattoria {
	private int Completi;
	private int max;
	private int numB;
	private int numZ;
	private int prezzoB;
	private int prezzoZ;
	private int sospesiB;
	private int sospesiZ;
	private int incassi;
	private int bigliettiVend;
	private Lock lock= new ReentrantLock();
	private Condition conditionB;
	private Condition conditionZ;
	public Fattoria(int maxCompleti, int capMax,int pBot,int pZ) {
		if(maxCompleti>=capMax) throw new IllegalArgumentException("numero completi non può essere maggiore della capacità");
		this.Completi=maxCompleti;
		this.max=capMax;
		this.prezzoB=pBot;
		this.prezzoZ=pZ;
		this.sospesiB=0;
		this.sospesiZ=0;
		this.conditionB= lock.newCondition();
		this.conditionZ= lock.newCondition();
		this.incassi=0;
		this.bigliettiVend=0;
		this.numB=0;
		this.numZ=0;
		
	}
	public void entraBot() throws InterruptedException{
		lock.lock();
		try {
			while(numB+numZ==max) {
				System.out.println("capacita massima raggiunta, aspetto");
				sospesiB++;
				conditionB.await();
				sospesiB--;
			}
			
			bigliettiVend++;
			incassi+=prezzoB;
			numB++;
			System.out.println("Bambino entrato nella botanica, numero bambini nella botanica: "+numB);
			
		}finally{lock.unlock();}
	}

	public void esceBot() {
		// TODO Auto-generated method stub
		lock.lock();
		numB--;
		if(sospesiB>0) {
			conditionB.signal();
		}else if(sospesiZ>0) {
			conditionZ.signal();
		}
		lock.unlock();
		
	}

	public void entraZoo() throws InterruptedException {
		lock.lock();
		try {
			while(numB+numZ==max || numZ>Completi) {
				System.out.println("capacita massima raggiunta o completo mancante, aspetto");
				sospesiZ++;
				conditionZ.await();
				sospesiZ--;
			}
			Completi--;
			bigliettiVend++;
			numZ++;
			System.out.println("Bambino entrato nello zoo, numero bambini nello zoo: "+numZ);
			incassi+=prezzoZ;
			
		} finally {lock.unlock();
		}
		
	}

	public void esceZoo() {
		lock.lock();
		numZ--;
		Completi++;
		if(sospesiZ>0) {
			conditionZ.signal();
		}else if(sospesiB>0) {
			conditionB.signal();
		}
		lock.unlock();
		
		
	}
	@Override
	public String toString() {
		return "Il numero di bambini totali è " + bigliettiVend+" e l'incasso totale è "+incassi+ "€";  
	}

}
