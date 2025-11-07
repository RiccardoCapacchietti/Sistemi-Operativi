package Es10;

public class BambinoZ extends Thread{
	Fattoria m;
	public BambinoZ(Fattoria f) {
		this.m=f;
	}
	public void run() {
		try {
			System.out.println("Processo (P=" +getName()+") provo a entrare nello zoo");
			m.entraZoo();
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
		m.esceZoo();
	}

}
