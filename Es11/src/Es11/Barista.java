package Es11;

public class Barista extends Thread{
	private Monitor m;
	private int UP;
	private int DOWN;
	public Barista(Monitor M) {
		this.m=M;
		this.UP=Monitor.UP;
		this.DOWN=Monitor.DOWN;
	}
	public void run() {
		
			try {
				while(true)
				{		
	
			m.entraBarista(UP,getName());
			sleep(1000);
			m.esceBarista(UP,getName());
			sleep(1000);
			m.entraBarista(DOWN,getName());
			sleep(1000);
			m.esceBarista(DOWN,getName());
				}
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
