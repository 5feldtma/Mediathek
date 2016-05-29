package de.uni_hamburg.informatik.swt.se2.mediathek.materialien;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni_hamburg.informatik.swt.se2.mediathek.fachwerte.Kundennummer;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.CD;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;

public class VormerkkarteTest {
	

    private Vormerkkarte _karte;
    private Kunde _kunde;
    private Kunde _kunde2;
    private Kunde _kunde3;
    private Kunde _kunde4;
    
    private Medium _medium;

    public VormerkkarteTest()
    {
        _kunde = new Kunde(new Kundennummer(123456), "Herr", "Eins");
        _kunde2 = new Kunde(new Kundennummer(123457), "Frau", "Zwei");
        _kunde3 = new Kunde(new Kundennummer(123458), "Kunde", "Drei");
        _kunde4 = new Kunde(new Kundennummer(123459), "Mister", "Vier");
        		
        _medium = new CD("bar", "baz", "foo", 123);
        
        _karte = new Vormerkkarte(_kunde, _medium);
    }

    @Test
    public void testegetFormatiertenString() throws Exception
    {
        assertNotNull(_karte.getFormatiertenString());
    }

    @Test
    public void testeKonstruktor() throws Exception
    {
        assertEquals(_kunde, _karte.getErstenVormerker());
        assertEquals(_medium, _karte.getMedium());
    }
    
    @Test
    public void testeVormerken() throws Exception
    {
    	_karte.merkeVor(_kunde2);
        assertEquals(_kunde2, _karte.getBestimmtenVormerker(2));
        _karte.merkeVor(_kunde2);
        assertNull(_karte.getBestimmtenVormerker(3));
    }
    
    @Test
    public void testeVormerkenMitVollerListe() throws Exception
    {
    	_karte.merkeVor(_kunde2);
    	_karte.merkeVor(_kunde3);
    	_karte.merkeVor(_kunde4);
    	assertEquals(_kunde3, _karte.getBestimmtenVormerker(3));
    	assertNull(_karte.getBestimmtenVormerker(4));
    }
    
    @Test
    public void testeVormerkungEntfernen() throws Exception
    {
    	_karte.entferneVormerkung(_kunde);
    	assertNull(_karte.getErstenVormerker());
    }

}
