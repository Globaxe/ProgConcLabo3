package airport.com;
import java.util.concurrent.BlockingQueue;
//represente l'avion

public class Avion implements Runnable {

	AirportFrame airportFrame;
	String codePlane;
	BlockingQueue<Avion> airArr;
	BlockingQueue<Avion> tarmacLand;
	BlockingQueue<Avion> tarmacTakeOff;
	BlockingQueue<Avion> terminal;
	BlockingQueue<Avion> airDep;
	int nbAvion;
	int nbPisteArr;
	int nbPisteDep;
	int nbPlace;

	int position;
	
	public Avion(AirportFrame _airportFrame, String _codePlane, BlockingQueue<Avion> _airArr, BlockingQueue<Avion> _tarmacLand,
			BlockingQueue<Avion> _tarmacTakeOff, BlockingQueue<Avion> _terminal, BlockingQueue<Avion> _airDep,
			int _nbAvion, int _nbPisteArr, int _nbPisteDep, int _nbPlace) {
		airportFrame = _airportFrame;
		codePlane = _codePlane;

		airArr = _airArr;
		tarmacLand = _tarmacLand;
		tarmacTakeOff = _tarmacTakeOff;
		terminal = _terminal;
		airDep = _airDep;

		nbAvion = _nbAvion;
		nbPisteArr = _nbPisteArr;
		nbPisteDep = _nbPisteDep;
		nbPlace = _nbPlace;
	}

	public void run() {
		
	}

	public String getCode() {
		return codePlane;
	}

}
