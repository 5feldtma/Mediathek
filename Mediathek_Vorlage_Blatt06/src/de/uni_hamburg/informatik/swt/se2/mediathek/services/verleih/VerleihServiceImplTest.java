package de.uni_hamburg.informatik.swt.se2.mediathek.services.verleih;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uni_hamburg.informatik.swt.se2.mediathek.fachwerte.Datum;
import de.uni_hamburg.informatik.swt.se2.mediathek.fachwerte.Kundennummer;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Verleihkarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Vormerkkarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.CD;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.ServiceObserver;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.kundenstamm.KundenstammService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.kundenstamm.KundenstammServiceImpl;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.medienbestand.MedienbestandService;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.medienbestand.MedienbestandServiceImpl;

/**
 * @author SE2-Team
 */
public class VerleihServiceImplTest
{
    private Datum _datum;
    private Kunde _kunde;
    private VerleihService _service;
    private List<Medium> _medienListe;
    private Kunde _vormerkkunde;
    private List<Kunde> kunden = new ArrayList<>();
    
    @Before
    public void setup(){
    }

    public VerleihServiceImplTest()
    {
        _datum = new Datum(3, 4, 2009);
        KundenstammService kundenstamm = new KundenstammServiceImpl(
                new ArrayList<Kunde>());
        _kunde = new Kunde(new Kundennummer(123456), "ich", "du");
        
        for (int i=0; i<10; i++){
            kunden.add(new Kunde(new Kundennummer(i+100000), "Kunde"+i, "Kunde"+i));
            kundenstamm.fuegeKundenEin(kunden.get(i));
        }

        _vormerkkunde = new Kunde(new Kundennummer(666999), "paul", "panter");

        kundenstamm.fuegeKundenEin(_kunde);
        kundenstamm.fuegeKundenEin(_vormerkkunde);
        MedienbestandService medienbestand = new MedienbestandServiceImpl(
                new ArrayList<Medium>());
        Medium medium = new CD("CD1", "baz", "foo", 123);
        medienbestand.fuegeMediumEin(medium);
        medium = new CD("CD2", "baz", "foo", 123);
        medienbestand.fuegeMediumEin(medium);
        medium = new CD("CD3", "baz", "foo", 123);
        medienbestand.fuegeMediumEin(medium);
        medium = new CD("CD4", "baz", "foo", 123);
        medienbestand.fuegeMediumEin(medium);
        _medienListe = medienbestand.getMedien();
        _service = new VerleihServiceImpl(kundenstamm, medienbestand,
                new ArrayList<Verleihkarte>());
    }

    @Test
    public void testeNachInitialisierungIstNichtsVerliehen() throws Exception
    {
        assertTrue(_service.getVerleihkarten()
            .isEmpty());
        assertFalse(_service.istVerliehen(_medienListe.get(0)));
        assertFalse(_service.sindAlleVerliehen(_medienListe));
        assertTrue(_service.sindAlleNichtVerliehen(_medienListe));
    }

    @Test
    public void testeVerleihUndRueckgabeVonMedien() throws Exception
    {
        // Lege eine Liste mit nur verliehenen und eine Liste mit ausschließlich
        // nicht verliehenen Medien an
        List<Medium> verlieheneMedien = _medienListe.subList(0, 2);
        List<Medium> nichtVerlieheneMedien = _medienListe.subList(2, 4);
        _service.verleiheAn(_kunde, verlieheneMedien, _datum);

        // Prüfe, ob alle sondierenden Operationen für das Vertragsmodell
        // funktionieren
        assertTrue(_service.istVerliehen(verlieheneMedien.get(0)));
        assertTrue(_service.istVerliehen(verlieheneMedien.get(1)));
        assertFalse(_service.istVerliehen(nichtVerlieheneMedien.get(0)));
        assertFalse(_service.istVerliehen(nichtVerlieheneMedien.get(1)));
        assertTrue(_service.sindAlleVerliehen(verlieheneMedien));
        assertTrue(_service.sindAlleNichtVerliehen(nichtVerlieheneMedien));
        assertFalse(_service.sindAlleNichtVerliehen(verlieheneMedien));
        assertFalse(_service.sindAlleVerliehen(nichtVerlieheneMedien));
        assertFalse(_service.sindAlleVerliehen(_medienListe));
        assertFalse(_service.sindAlleNichtVerliehen(_medienListe));
        assertTrue(_service.istVerliehenAn(_kunde, verlieheneMedien.get(0)));
        assertTrue(_service.istVerliehenAn(_kunde, verlieheneMedien.get(1)));
        assertFalse(
                _service.istVerliehenAn(_kunde, nichtVerlieheneMedien.get(0)));
        assertFalse(
                _service.istVerliehenAn(_kunde, nichtVerlieheneMedien.get(1)));
        assertTrue(_service.sindAlleVerliehenAn(_kunde, verlieheneMedien));
        assertFalse(
                _service.sindAlleVerliehenAn(_kunde, nichtVerlieheneMedien));

        // Prüfe alle sonstigen sondierenden Methoden
        assertEquals(2, _service.getVerleihkarten()
            .size());

        _service.nimmZurueck(verlieheneMedien, _datum);
        // Prüfe, ob alle sondierenden Operationen für das Vertragsmodell
        // funktionieren
        assertFalse(_service.istVerliehen(verlieheneMedien.get(0)));
        assertFalse(_service.istVerliehen(verlieheneMedien.get(1)));
        assertFalse(_service.istVerliehen(nichtVerlieheneMedien.get(0)));
        assertFalse(_service.istVerliehen(nichtVerlieheneMedien.get(1)));
        assertFalse(_service.sindAlleVerliehen(verlieheneMedien));
        assertTrue(_service.sindAlleNichtVerliehen(nichtVerlieheneMedien));
        assertTrue(_service.sindAlleNichtVerliehen(verlieheneMedien));
        assertFalse(_service.sindAlleVerliehen(nichtVerlieheneMedien));
        assertFalse(_service.sindAlleVerliehen(_medienListe));
        assertTrue(_service.sindAlleNichtVerliehen(_medienListe));
        assertTrue(_service.getVerleihkarten()
            .isEmpty());
    }

    @Test
    public void testVerleihEreignisBeobachter() throws ProtokollierException
    {
        final boolean ereignisse[] = new boolean[1];
        ereignisse[0] = false;
        ServiceObserver beobachter = new ServiceObserver()
        {
            @Override
            public void reagiereAufAenderung()
            {
                ereignisse[0] = true;
            }
        };
        _service.verleiheAn(_kunde,
                Collections.singletonList(_medienListe.get(0)), _datum);
        assertFalse(ereignisse[0]);

        _service.registriereBeobachter(beobachter);
        _service.verleiheAn(_kunde,
                Collections.singletonList(_medienListe.get(1)), _datum);
        assertTrue(ereignisse[0]);

        _service.entferneBeobachter(beobachter);
        ereignisse[0] = false;
        _service.verleiheAn(_kunde,
                Collections.singletonList(_medienListe.get(2)), _datum);
        assertFalse(ereignisse[0]);
    }
    
    @Test
    public void testMerkeVor() {
        _service.merkeVor(kunden.get(0), _medienListe);
        for (Medium m : _medienListe){
            Vormerkkarte karte = _service.getVormerkkarte(m);
            assertTrue(_service.getVormerkkarten().contains(karte));
        }
    }
    
    @Test
    public void testEntferneVormerkung() {
        //nicht existierende Vormerkungen
        _service.entferneVormerkungen(kunden.get(0), _medienListe);
        
        //Alle Vormerkungen entfernen
        _service.merkeVor(kunden.get(0), _medienListe);
        _service.entferneVormerkungen(kunden.get(0), _medienListe);
        assertTrue(_service.getVormerkkarten().size() == 0);
        
        //Eine von vielen Vormerkungen entfernen
        _service.merkeVor(kunden.get(1), _medienListe);
        List<Medium> testMedien = new ArrayList<>();
        testMedien.add(_medienListe.get(0));
        _service.entferneVormerkungen(kunden.get(1), testMedien);
        assertTrue(_service.getVormerkkarten().size() == 0);
        
    }
    
    @Test
    public void testVormerkenMöglich() throws ProtokollierException {
        
        //Warteliste leer, nicht an Kunde verliehen, nicht vom Kunden vorgemerkt
        boolean result = _service.istVormerkenMöglich(_vormerkkunde, _medienListe);
        assertTrue(result);
        
        //Warteliste leer, bereits an Kunde verliehen, nicht vom Kunden vorgemerkt
        _service.verleiheAn(_kunde, _medienListe, Datum.heute());
        result = _service.istVormerkenMöglich(_kunde, _medienListe);
        assertFalse(result);
        
        //Warteliste.size = 1, nicht an Kunde verliehen, vom Kunden bereits vorgemerkt
        _service.merkeVor(_vormerkkunde, _medienListe);
        result = _service.istVormerkenMöglich(_vormerkkunde, _medienListe);
        assertFalse(result);
        
        //Warteliste.size = 1, nicht an Kunde verliehen, nicht vom Kunden vorgemerkt
        result = _service.istVormerkenMöglich(kunden.get(0), _medienListe);
        assertTrue(result);
        
        //Warteliste.size = 2, nicht an Kunde verliehen, nicht vom Kunden vorgemerkt
        _service.merkeVor(kunden.get(0), _medienListe);        
        result = _service.istVormerkenMöglich(kunden.get(1), _medienListe);
        assertTrue(result);
        
        //Warteliste.size = 3, nicht an Kunde verliehen, nicht vom Kunden vorgemerkt
        _service.merkeVor(kunden.get(1), _medienListe);        
        result = _service.istVormerkenMöglich(kunden.get(2), _medienListe);
        assertFalse(result);
        
        //Warteliste.size = 3, nicht an Kunde verliehen, bereits vom Kunden vorgemerkt
        _service.merkeVor(kunden.get(2), _medienListe);        
        result = _service.istVormerkenMöglich(kunden.get(2), _medienListe);
        assertFalse(result);
        
        
    }
    
    @Test
    public void istVomKundenVorgemerkt() {
        assertFalse(_service.istVomKundenVorgemerkt(kunden.get(0), _medienListe.get(0)));
        
        _service.merkeVor(kunden.get(0), _medienListe);
        assertTrue(_service.istVomKundenVorgemerkt(kunden.get(0), _medienListe.get(0)));
    }
    
    @Test
    public void getVormerkkarten(){
        
    }
}
