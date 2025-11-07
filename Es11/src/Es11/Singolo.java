package Es11;

public class Singolo extends Thread{
private Monitor m;
private int UP;
private int DOWN;
public Singolo(Monitor M){
	this.m=M;
	this.UP=Monitor.UP;
	this.DOWN=Monitor.DOWN; 
	
}
public void run() {
	
	try {
	m.entraSingolo(UP,getName());//entra e percorre il sentiero
	sleep(2000);

	m.esceSingolo(UP,getName());//esce dal sentiero e sono alla baita
	sleep(2000);
	m.entraSingolo(DOWN,getName());//esco dalla baita e vado sentiero in uscita
	sleep(2000);
	m.esceSingolo(DOWN,getName());//sono fuori dal sentiero e sono free
	
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
