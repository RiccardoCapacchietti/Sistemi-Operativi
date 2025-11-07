package esame;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	public final static int RICHIEDI=0;
	public final static int RESTITUISCI=1;
	public final static int SINGOLO=0;
	public final static int COPPIA=1;

	private int[] casse=new int[3];
	
	

	private Lock lock=new ReentrantLock();

	private Condition[][] codaSingolo=new Condition[2][3];//nel primo definisco se richiedi o restituisco, nel secondo la stazione
	private Condition [][] codaC=new Condition[2][3];
	private int [][] sospesiSingolo=new int [2][3];//matrice di interi dove ad esempio in 0 0 ho gli utenti cbe richiedono, nella stazione a
    private int [][] sospesiCoppie=new int[2][3];
    private int []parcheggiMax=new int [3];
    private int []biciDisp=new int[3];

	char [] stazioni= new char[3];
	
	public Monitor(int NbiciA,int NbiciB,int NbiciC) {
		//inizialmente ho capienza massima e bici disponibili uguale
		this.biciDisp[0]=NbiciA;
		this.biciDisp[1]=NbiciB;
		this.biciDisp[2]=NbiciC;
		this.parcheggiMax[0]=NbiciA;
		this.parcheggiMax[1]=NbiciB;
		this.parcheggiMax[2]=NbiciC;
		for(int i=0;i<2;i++) {
			for(int j=0;j<3;j++) {
				sospesiSingolo[i][j]=0;
				sospesiCoppie[i][j]=0;
				codaSingolo[i][j]=lock.newCondition();
				codaC[i][j]=lock.newCondition();
			}
		}
		stazioni[0]='A';
		stazioni[1]='B';
		stazioni[2]='C';
		
		
		this.casse[0]=0;
		this.casse[1]=0;
		this.casse[2]=0;
		
	}
	public void richiediBici(int tipo, char sPartenza,String name) throws InterruptedException {

		lock.lock();
		try{
			if(tipo==SINGOLO) {
				for(int i=0;i<3;i++) {
					if(sPartenza==stazioni[i]) {
						while(biciDisp[i]==0 ) {
							sospesiSingolo[RICHIEDI][i]++;
							codaSingolo[RICHIEDI][i].await();
							sospesiSingolo[RICHIEDI][i]--;
						}

						casse[i]+=10;
						biciDisp[i]--;
						System.out.println("Cliente singolo " +name+ " ha preso la bici dalla stazione " +stazioni[i]+ ", bici rimaste attualmente " +biciDisp[i]+" cassa: " +casse[i] +state());

						if(sospesiCoppie[RESTITUISCI][i]>0)
							codaC[RESTITUISCI][i].signal();
						else if(sospesiSingolo[RESTITUISCI][i]>0)
						codaSingolo[RESTITUISCI][i].signal();
						
					}
				}
				
			}else if(tipo==COPPIA) {
				for(int i=0;i<3;i++) {
					if(sPartenza==stazioni[i]) {
						while( biciDisp[i]<2 || sospesiSingolo[RICHIEDI][i]>0 ) {
							sospesiCoppie[RICHIEDI][i]++;
							codaC[RICHIEDI][i].await();
							sospesiCoppie[RICHIEDI][i]--;
						}
						casse[i]+=20;
						biciDisp[i]-=2;
						System.out.println("Cliente coppia " +name+ " hanno preso la bici dalla stazione " +stazioni[i]+ ", bici rimaste attualmente " +biciDisp[i]+" cassa: " +casse[i] +state());

						if(sospesiCoppie[RESTITUISCI][i]>0)
							codaC[RESTITUISCI][i].signal();
						else if(sospesiSingolo[RESTITUISCI][i]>0)
						codaSingolo[RESTITUISCI][i].signalAll();	
			       }
				}
			}
			
		}finally {
			lock.unlock();
			
		}
	}

	public void restituisciBici(int tipo, char sArrivo,String name) throws InterruptedException {
		lock.lock();
		try {
			if(tipo==SINGOLO) {
				for(int i=0;i<3;i++) {
					if(sArrivo==stazioni[i]) {
						while(sospesiCoppie[RESTITUISCI][i]>0 || biciDisp[i]==parcheggiMax[i] || casse[i] <10 ) {
							sospesiSingolo[RESTITUISCI][i]++;
							codaSingolo[RESTITUISCI][i].await();
							sospesiSingolo[RESTITUISCI][i]--;
						}
						casse[i]-=10;
						biciDisp[i]++;
						System.out.println("Cliente singolo " +name+ " ha parcheggiato la bici alla stazione " +stazioni[i]+ ", bici rimaste attualmente " +biciDisp[i]+" cassa: " +casse[i] +state());

						if(sospesiSingolo[RICHIEDI][i]>0)
							codaSingolo[RICHIEDI][i].signal();
						else if(sospesiCoppie[RICHIEDI][i]>0)
						codaC[RICHIEDI][i].signal();
						
					}
				}
				
			}else if(tipo==COPPIA) {
				for(int i=0;i<3;i++) {
					if(sArrivo==stazioni[i]) {
						while( biciDisp[i]+2>parcheggiMax[i] || casse[i]<20) {
							sospesiCoppie[RESTITUISCI][i]++;
							codaC[RESTITUISCI][i].await();
							sospesiCoppie[RESTITUISCI][i]--;
						}
						casse[i]-=20;
						biciDisp[i]+=2;
						System.out.println("Cliente coppia " +name+ " ha parcheggiato la bici alla stazione " +stazioni[i]+ ", bici rimaste attualmente " +biciDisp[i]+" cassa: " +casse[i] +state());

						if(sospesiSingolo[RICHIEDI][i]>0)
							codaSingolo[RICHIEDI][i].signalAll();
						else if(sospesiCoppie[RICHIEDI][i]>0)
							codaC[RICHIEDI][i].signal();
							
						
			       }
				}
			}
			
		}finally {
			lock.unlock();
		}
	}
	
	public String state() {
	    return "\nMonitor [\n" +
	           "  casse=" + Arrays.toString(casse) + ",\n" +
	           "  sospesiSingolo=" + matrixToString(sospesiSingolo) + ",\n" +
	           "  sospesiCoppie=" + matrixToString(sospesiCoppie) + ",\n" +
	           "  parcheggiMax=" + Arrays.toString(parcheggiMax) + ",\n" +
	           "  biciDisp=" + Arrays.toString(biciDisp) + ",\n" +
	           "  stazioni=" + Arrays.toString(stazioni) + "\n" +
	           "]";
	}

	private String matrixToString(int[][] matrix) {
	    StringBuilder sb = new StringBuilder();
	    sb.append("[\n");
	    for (int[] row : matrix) {
	        sb.append("    ").append(Arrays.toString(row)).append("\n");
	    }
	    sb.append("  ]");
	    return sb.toString();
	}


}
