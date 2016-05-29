package de.uni_hamburg.informatik.swt.se2.mediathek.materialien;

import java.util.ArrayList;
import java.util.List;

import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;

public class Vormerkkarte {

	// Eigenschaften einer Vormerkkarte
	private final List<Kunde> _vormerkliste;
	private final Medium _medium;
	// TODO auslagern in Fachwert?
	private final int vormerkMaximum = 3;

	/**
	 * Initialisert eine neue Vormerkkarte mit den gegebenen Daten.
	 * 
	 * @param vormerker
	 *            Ein Kunde, der das Medium zuerst vorgemerkt hat.
	 * @param medium
	 *            Das vorgemerkte Medium.
	 * 
	 * @require vormerker != null
	 * @require medium != null
	 * @ensure #getMedium() == medium
	 */
	public Vormerkkarte(Kunde vormerker, Medium medium) {
		assert vormerker != null : "Vorbedingung verletzt: vormerker != null";
		assert medium != null : "Vorbedingung verletzt: medium != null";

		_vormerkliste = new ArrayList<Kunde>();
		_vormerkliste.add(vormerker);
		_medium = medium;

	}

	/**
	 * Gibt eine Liste mit Vormerkern zurück.
	 * 
	 * @return die Líste mit Kunden, die das Medium vorgemerkt haben.
	 * 
	 * @ensure result != null
	 */
	public List<Kunde> getVormerkListe() {
		return _vormerkliste;
	}
	
	/**
	 * Gibt den ersten Vormerker zurück
	 * 
	 * @return Der erste Vormerker der Liste, null falls Liste leer
	 */
	
	public Kunde getErstenVormerker()
	{
		return _vormerkliste.get(0);
	}

	/**
	 * Gibt eine String-Darstellung der Vormerkkarte (enhält Zeilenumbrüche)
	 * zurück.
	 * 
	 * @return Eine formatierte Stringrepäsentation der Vormerkkarte. Enthält
	 *         Zeilenumbrüche.
	 * 
	 * @ensure result != null
	 */
	public String getFormatiertenString() {
		return _medium.getFormatiertenString() + " vorgemerkt für: \n"
				+ getVormerklisteFormatierterString();
	}

	public String getVormerklisteFormatierterString() {
		String result = "";

		for (int i = 0; i < _vormerkliste.size(); i++) {
			// Format: "1. Vormerker: Name,..."
			result += i + 1 + ". Vormerker: "
					+ _vormerkliste.get(i).getFormatiertenString() + "\n";
		}

		return result;
	}

	/**
	 * Gibt das Medium, dessen Vormerkungen gespeichert werden, zurück.
	 * 
	 * @return Das Medium, dessen Vormerkungen gespeichert werden.
	 * 
	 * @ensure result != null
	 */
	public Medium getMedium() {
		return _medium;
	}

	public void merkeVor(Kunde vormerker) {
		if (listeNichtVoll()) {
			if (!istVonKundeVorgemerkt(vormerker)) {
				_vormerkliste.add(vormerker);
			} else {
				// TODO als Fenster. In den Service bzw. Werkzeug für die UI?
				System.out.println("Kunde hat dieses Medium schon vorgemerkt");
			}
		} else {
			// TODO als Fenster. In den Service bzw. Werkzeug für die UI?
			System.out.println("Vormerkliste ist voll");
		}
	}
	
	public void entferneVormerkung(Kunde vormerker)
	{
		if(istVonKundeVorgemerkt(vormerker))
		{
			// TODO klappt das?
			_vormerkliste.remove(vormerker);
		}
	}

	/**
	 * Prüft, ob das Medium vorgemerkt werden kann. Überprüft ob die Liste voll
	 * ist und ob der Kunde das Objekt schon vorgemerkt hat.
	 * 
	 * @return Ist Medium vormerkbar
	 */
	public boolean istVonKundeVorgemerkt(Kunde vormerker) {
		return _vormerkliste.contains(vormerker);
	}

	public boolean listeNichtVoll() {
		return _vormerkliste.size() != vormerkMaximum;
	}

	/// TODO neue equals und hash methode?
	
	/*
	 * @Override public int hashCode() { final int prime = 31; int result = 1;
	 * result = prime * result + ((_ausleihdatum == null) ? 0 :
	 * _ausleihdatum.hashCode()); result = prime * result + ((_entleiher ==
	 * null) ? 0 : _entleiher.hashCode()); result = prime * result + ((_medium
	 * == null) ? 0 : _medium.hashCode()); return result; }
	 * 
	 * @Override public boolean equals(Object obj) { boolean result = false; if
	 * (obj instanceof Verleihkarte) { Verleihkarte other = (Verleihkarte) obj;
	 * 
	 * if (other.getAusleihdatum() .equals(_ausleihdatum) &&
	 * other.getEntleiher() .equals(_entleiher) && other.getMedium()
	 * .equals(_medium))
	 * 
	 * result = true; } return result; }
	 */

	@Override
	public String toString() {
		return getFormatiertenString();
	}
}
