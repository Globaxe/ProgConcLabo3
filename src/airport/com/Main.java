package airport.com;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Main {

	static String[] codePlane = { "3B147", "B3291", "6B239", "B1086", "780B4", "32A64", "17A69", "2A431", "647B8", "349A8",
			"536B8", "9103A", "9B210", "139A4", "96B01", "207B9", "830B6", "8435A", "7301B", "1076B", "5281B", "8A521",
			"3B806", "B6842", "B6238", "7B816", "A9437", "849A3", "60B18", "094B6", "4709B", "36A84", "085A3", "0718B",
			"80B21", "0A369", "5290A", "370B4", "021A3", "84A02", "052A6", "B6350", "630B5", "8B903", "1398B", "2693A",
			"902A6", "51A20", "971A5", "A7891" };
	
	static public Semaphore semaphore = new Semaphore(0);
	static boolean paused = false;
	static long time; 
	
	public static void main(String[] args) {
		int nbAvion = 20; //nombre d'avion 
		int nbPisteArr = 2;//pistes d'atterrisage
		int nbPisteDep = 2;//"" de depart
		int nbPlace = 4; //parking 
		boolean useBlockingQueu = false;//utilise les blocking queu

		AirportFrame airportFrame = new AirportFrame(nbPisteArr, nbPisteDep, nbPlace, nbAvion);

		BlockingQueue<Avion> airArr = new ArrayBlockingQueue<Avion>(nbAvion);
		BlockingQueue<Avion> tarmacLand = new ArrayBlockingQueue<Avion>(nbPisteArr);
		BlockingQueue<Avion> tarmacTakeOff = new ArrayBlockingQueue<Avion>(nbPisteDep);
		BlockingQueue<Avion> terminal = new ArrayBlockingQueue<Avion>(nbPlace);
		BlockingQueue<Avion> airDep = new ArrayBlockingQueue<Avion>(nbAvion);
		
		List<Avion> listAirArr = new ArrayList<>(nbAvion);
		List<Avion> listTarmacLand = new ArrayList<>(nbPisteArr);
		List<Avion> listTarmacTakeOff = new ArrayList<>(nbPisteDep);
		List<Avion> listTerminal = new ArrayList<>(nbPlace);
		List<Avion> listAirDep = new ArrayList<>(nbAvion);
		
		ArrayList<Avion> Threads = new ArrayList<>();

		if(useBlockingQueu){
			
		    for (int i = 0; i < nbAvion; i++) {
	            Avion avion = new Avion(airportFrame, codePlane[i], airArr, tarmacLand, tarmacTakeOff, terminal, airDep, nbAvion,
	                    nbPisteArr, nbPisteDep, nbPlace,semaphore);
	            
	            Threads.add(avion);
	        }
		}
		else
		{
			
		    for (int i = 0; i < nbAvion; i++) {
	            Avion avion = new Avion(airportFrame, codePlane[i], listAirArr, listTarmacLand, listTarmacTakeOff, listTerminal, listAirDep, nbAvion,
	                    nbPisteArr, nbPisteDep, nbPlace, semaphore);
	            
	            Threads.add(avion);
	        }
		}
		
		
		for (Avion avion : Threads)
		{
		    new Thread(avion).start();
		}
		
		airportFrame.getButtonStart().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	time =  System.nanoTime();    			
            	paused = false;
				semaphore.release(1);
            }
        });
		airportFrame.getButtonStop().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	if(paused == false){
	            	paused = true;
					try {
						semaphore.acquire(1);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					System.out.println("Works!");   
            	}
            }
        });
		airportFrame.setVisible(true);
		airportFrame.pack();
	}
	public static void returnTime(){
		System.out.println("Process time: "+ (System.nanoTime() - time));
	}

}
