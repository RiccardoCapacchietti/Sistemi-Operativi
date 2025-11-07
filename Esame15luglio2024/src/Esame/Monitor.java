package Esame;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	public final static double GRANDE = 0.5;
	public final static double PICCOLO = 0.33;
	private double litriDisponibili;
	boolean erogatoreLibero;//se libero=true, altrimenti false
	private int[] sospesiC = new int[2];//con sospesiC[0] indico chi ha il bicchiere piccolo, con sospesiC[1] chi ha il grande
	private int sospesiA;
	private Lock lock=new ReentrantLock();
	private Condition codaA;
	private Condition [] codaC = new Condition[2];
	private double litriMax;

	public Monitor(double litri) {
		if(litri<0) throw new IllegalArgumentException("litri devono essere un intero positivo");
		this.litriDisponibili=litri;
		this.litriMax=litri;
		this.erogatoreLibero=true;
		this.sospesiA=0;
		this.sospesiC[0]=0;
		this.sospesiC[1]=0;
		codaA=lock.newCondition();
		codaC[0]=lock.newCondition();
		codaC[1]=lock.newCondition();
	}
	public void iniziaSostituzione(String name) throws InterruptedException {
		lock.lock();
		try {
			while(litriDisponibili>GRANDE || erogatoreLibero==false) {
				sospesiA++;
				codaA.await();
				sospesiA--;
			}
			System.out.println("Addetto " +name+ " sta iniziando la sostituzione, litri disponibili attualmente " +litriDisponibili + state());
			erogatoreLibero=false;
		}finally {
			lock.unlock();
		}
	}

	public void terminaSostituzione(String name) {
		lock.lock();
		litriDisponibili=litriMax;
		erogatoreLibero=true;
		System.out.println("Addetto "+name+" ha terminato la sostituzione, litri disponibili attualmente "+ litriDisponibili + state());
		if(sospesiC[0]>0)
			codaC[0].signal();
		else if(sospesiC[1]>0)
		codaC[1].signal();
		lock.unlock();
	}

	public void iniziaErogazione(double t, String name) throws InterruptedException {
		// TODO Auto-generated method stub
		lock.lock();
		try {
			if(t==GRANDE) {
				while(litriDisponibili<GRANDE || erogatoreLibero==false || sospesiC[0]>0) {
					if(litriDisponibili<GRANDE) codaA.signal(); //in dubbio, l'idea è che se la causa è la mancanza di litri, risveglio l'addetto
					sospesiC[1]++;
					codaC[1].await();
					sospesiC[1]--;
					
				}
				erogatoreLibero=false;//occupo l'erogatore
				}else if(t==PICCOLO) {
					while(litriDisponibili<PICCOLO || erogatoreLibero==false) {
						if(litriDisponibili<PICCOLO) codaA.signal(); //in dubbio, l'idea è che se la causa è la mancanza di litri, risveglio l'addetto
						sospesiC[0]++;
						codaC[0].await();
						sospesiC[0]--;
					}
					erogatoreLibero=false;
			}
			System.out.println("Il cliente "+name+ " sta per prendere della birra in quantità: "+t+ state());

			
		}finally {
			lock.unlock();
		}
		
	}

	public void terminaErogazione(double t, String name) {
		// TODO Auto-generated method stub
		lock.lock();
		if(t==GRANDE) litriDisponibili-=GRANDE;//tolgo i litri quando ho finito l'erogazione
		else if(t==PICCOLO) litriDisponibili-=PICCOLO;
		erogatoreLibero=true;//lascio l'erogatore
		System.out.println("il cliente" +name + "ha preso "+t+" litri di birra"+state());
		if(sospesiC[0]>0)
			codaC[0].signal();
		else if(sospesiC[1]>0)
			codaC[1].signal();
		
		lock.unlock();
		
	}
	public String state() {
		return "\n Monitor [litriDisponibili=" + litriDisponibili + ", erogatoreLibero=" + erogatoreLibero + ", sospesiC="
				+ Arrays.toString(sospesiC) + ", sospesiA=" + sospesiA + ", litriMax=" + litriMax + "]"+"\n";
	}

}
