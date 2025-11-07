package Es10;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Fattoria f=new Fattoria(10,30,300,150);
		int numBotanici=21;
		int numZoo=12;
		BambinoB [] bambiniB= new BambinoB[numBotanici];
		BambinoZ[] bambiniZ= new BambinoZ[numZoo];
		
		for(int i=0; i<numBotanici;i++) {
			bambiniB[i]=new BambinoB(f);
		}
		for(int i=0; i<numZoo;i++) {
			bambiniZ[i]=new BambinoZ(f);
		}
		for(int i=0; i<numBotanici;i++) {
			bambiniB[i].start();
		}
		for(int i=0; i<numZoo;i++) {
			bambiniZ[i].start();
		}
		for(int i=0; i<numBotanici;i++) {
	
				bambiniB[i].join();
			
			}
		
		for(int i=0; i<numZoo;i++) {
			bambiniZ[i].join();
		}
		System.out.println(f.toString());
	}

}
