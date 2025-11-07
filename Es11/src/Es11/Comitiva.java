package Es11;

public class Comitiva extends Thread{
private Monitor m;
private int UP;
private int DOWN;
private int num;
public Comitiva(Monitor M,int n) {
		this.m=M;
		this.UP=Monitor.UP;
		this.DOWN=Monitor.DOWN; 
		this.num=n;
	}
public void run() {
	try {
	m.entraComitiva(UP,num,getName());
	sleep(1000);
	m.esceComitiva(UP,num,getName());
	sleep(1000);
	m.entraComitiva(DOWN, num,getName());
	sleep(1000);
	m.esceComitiva(DOWN, num,getName());
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

}
