package Es10;


public class BambinoB extends Thread{
	Fattoria m;
	public BambinoB(Fattoria f) {
		this.m=f;
	}
 public void run() {
	 try {
		 System.out.println("Processo (P=" +getName()+") provo a entrare nella botanica");
		m.entraBot();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 try {
		sleep(5000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 m.esceBot();
 
 }
 
}


