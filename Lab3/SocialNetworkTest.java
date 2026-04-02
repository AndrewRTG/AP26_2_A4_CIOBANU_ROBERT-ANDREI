import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class SocialNetworkTest {

    private SocialNetwork network;
    private Person p1, p2, p3, p4, p5;


    @BeforeEach
    void setUp() {
        network = new SocialNetwork();

        // Creăm 5 persoane cu ID-uri unice
        p1 = new Person(1, "Person A", LocalDate.of(1990, 1, 1), "Test");
        p2 = new Person(2, "Person B", LocalDate.of(1990, 1, 1), "Test");
        p3 = new Person(3, "Person C (Nod Critic)", LocalDate.of(1990, 1, 1), "Test");
        p4 = new Person(4, "Person D", LocalDate.of(1990, 1, 1), "Test");
        p5 = new Person(5, "Person E", LocalDate.of(1990, 1, 1), "Test");

       //facem 2 triunghiuri unite prin p3
        p1.addRelationships(p2, "Friend");
        p2.addRelationships(p3, "Friend");
        p3.addRelationships(p1, "Friend");


        p3.addRelationships(p4, "Friend");
        p4.addRelationships(p5, "Friend");
        p5.addRelationships(p3, "Friend");


        network.addProfile(p1);
        network.addProfile(p2);
        network.addProfile(p3);
        network.addProfile(p4);
        network.addProfile(p5);
    }

    @Test
    void testFindArticulationPoints() {
        List<Profile> cutPoints = network.findArticulationPoints();
        assertEquals(1, cutPoints.size(), "Ar trebui să găsească exact 1 punct de articulație.");
        assertTrue(cutPoints.contains(p3), "Person C trebuie să fie punctul de articulație identificat.");
    }

    @Test
    void testFindBiconnectedComponents() {
        List<Set<Profile>> bcc = network.findBiconnectedComponents();
        assertEquals(2, bcc.size(), "Ar trebui să existe exact 2 componente biconexe.");
        boolean foundGroup1 = false;
        boolean foundGroup2 = false;
        for (Set<Profile> component : bcc) {
            if (component.contains(p1) && component.contains(p2) && component.contains(p3)) {
                foundGroup1 = true;
                assertEquals(3, component.size(), "Grupul 1 trebuie să aibă exact 3 persoane.");
            } else if (component.contains(p3) && component.contains(p4) && component.contains(p5)) {
                foundGroup2 = true;
                assertEquals(3, component.size(), "Grupul 2 trebuie să aibă exact 3 persoane.");
            }
        }

        assertTrue(foundGroup1, "Grupul 1 (A, B, C) nu a fost găsit corect.");
        assertTrue(foundGroup2, "Grupul 2 (C, D, E) nu a fost găsit corect.");
    }

    @Test
    void testNoArticulationPointsInSimpleCycle() {

        SocialNetwork smallNetwork = new SocialNetwork();
        smallNetwork.addProfile(p1);
        smallNetwork.addProfile(p2);
        smallNetwork.addProfile(p3);
        List<Profile> cutPoints = smallNetwork.findArticulationPoints();
        List<Set<Profile>> bcc = smallNetwork.findBiconnectedComponents();
        assertTrue(cutPoints.isEmpty(), "Un graf ciclu simplu nu are puncte de articulație.");
        assertEquals(1, bcc.size(), "Un graf ciclu simplu formează exact 1 componentă biconexă.");
    }
}