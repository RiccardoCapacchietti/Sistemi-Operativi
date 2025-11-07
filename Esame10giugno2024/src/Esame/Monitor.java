package Esame;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	private int TotAree;
	private int PresentiInAree;
	private Lock lock=new ReentrantLock();
	private Condition codaA;
	private Condition codaM;
	private Condition codaF;
	private int sospesoAddetto;
	private int addettoIN;
	private int [] sospesiMono = new int [2];
	private int numTunnel;
	private int [] sospesiFull = new int[2];//in sospesiFull[0] indico i sospesi per entrare nel tunnel, in sospesiFull[1] quelli per entrare nel area

	public Monitor(int MaxAree) {
		this.TotAree=MaxAree;
		this.PresentiInAree=0;
		
		this.sospesoAddetto=0;
		this.sospesiFull[0]=0;
		this.sospesiFull[1]=0;
		this.sospesiMono[0]=0;
		this.sospesiMono[1]=0;

		this.numTunnel=0;
		this.codaA=lock.newCondition();
		this.codaF=lock.newCondition();
		this.codaM=lock.newCondition();
	}
	public void entraMono(int tipoServizio,String id) throws InterruptedException {
		lock.lock();
		try {
		if(tipoServizio==0){//voglio entrare nel tunnel
			while(sospesiFull[0]>0 || numTunnel>0 || addettoIN !=1) {
				sospesiMono[0]++;
				codaM.await();
				sospesiMono[0]--;
			}
			numTunnel++;
			System.out.println("mono entrato nell tunnel "+id+"\n"+state());

		}else if(tipoServizio==1){//voglio entrare nell area
			while(sospesiFull[1]>0 || PresentiInAree==TotAree) {
				sospesiMono[1]++;
				codaM.await();
				sospesiMono[1]--;
				
			}
			PresentiInAree++;
			System.out.println("mono entrato nell area "+id+"\n"+state());

			
			
		}
		
	} finally {
		lock.unlock();
	}
	}

	public void esceMono(int tipoServizio,String id) {
		lock.lock();
		if(tipoServizio==0) {//esco dal tunnel
			numTunnel--;
			System.out.println("mono uscito dal tunnel "+id+"\n"+state());
			if(sospesoAddetto>0)
				codaA.signal();
			if(sospesiFull[0]>0)
				codaF.signal();
			else if(sospesiMono[0]>0)
				codaM.signal();

			
		}else if(tipoServizio==1) {
			
			PresentiInAree--;
			System.out.println("mono uscito dal area "+id+"\n"+state());

			if(sospesiFull[1]>0) 
				codaF.signal();
			else if(sospesiMono[1]>0)
				codaM.signal();
		}
		lock.unlock();
	}

	public void entraFullTunnel(String id) throws InterruptedException {
		
		lock.lock();
		try {
			while(numTunnel>0) {
				sospesiFull[0]++;
				codaF.await();
				sospesiFull[0]--;
			}
			numTunnel++;
			System.out.println("Full entrato nel tunnel "+id+"\n"+state());
			
		}finally {
			lock.unlock();
		}
	}
	public void entraFullArea(String id) throws InterruptedException {
		
		lock.lock();
		try {
	
			while(PresentiInAree==TotAree) {
				sospesiFull[1]++;
				codaF.await();
				sospesiFull[1]--;
				
			}
			PresentiInAree++;
			System.out.println("Full entrato nel area"+id+"\n"+state());
			
		}finally {
			lock.unlock();
		}
	}

	public void esceFullTunnel(String id) {
		lock.lock();
		numTunnel--;
		System.out.println("full uscito dall tunnel "+id+"\n"+state());

		if(sospesoAddetto>0)
			codaA.signal();
		if(sospesiFull[0]>0)
			codaF.signal();
		else if(sospesiMono[0]>0)
			codaM.signal();

		
		
		lock.unlock();
	}
	public void esceFullArea(String id) {
		lock.lock();
		PresentiInAree--;
		System.out.println("full uscito dall'area "+id+"\n"+state());
		if(sospesiFull[1]>0)
			codaF.signal();
		else if(sospesiMono[1]>0)
			codaM.signal();

		
		
		lock.unlock();
	}

	public void entraAddetto(String id) {
		lock.lock();
		addettoIN=1;
		System.out.println("addetto entrato nel tunnel "+id+"\n"+state());
		if(sospesiFull[0]>0)
			codaF.signal();
		else if(sospesiMono[0]>0)
			codaM.signal();
		
		lock.unlock();

	}

	public void esceAddetto(String id) throws InterruptedException {
		lock.lock();
		try {
		while(numTunnel>0) {
			sospesoAddetto++;
			codaA.await();
			sospesoAddetto--;
		}
		addettoIN=0;
		System.out.println("addetto uscito dal tunnel "+id +"\n"+state());
		}finally {
			lock.unlock();
		}

	}

	public String state() {
		return "Monitor [TotAree=" + TotAree + ", PresentiInAree=" + PresentiInAree + ", sospesoAddetto=" + sospesoAddetto
				+ ", addettoIN=" + addettoIN + ", sospesiMono=" + Arrays.toString(sospesiMono) + ", numTunnel="
				+ numTunnel + ", sospesiFull=" + Arrays.toString(sospesiFull) + "]";
	}

}
