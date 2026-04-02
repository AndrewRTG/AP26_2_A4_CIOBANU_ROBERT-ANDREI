import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        SocialNetwork myNetwork = new SocialNetwork();

        Person john = new Person(1,"John Snow", LocalDate.of(1985,8,24), "Targaryen");
        Person ned = new Person(2,"Ned Stark", LocalDate.of(1963, 4, 17), "Winterfell");
        Programmer marcello = new Programmer(4,"Marcello Muratori", LocalDate.of(1990, 5, 10), "Italian", "Java");
        Designer ygritte = new Designer(5,"Ygritte", LocalDate.of(1987, 2, 9), "Wildling", "Photoshop");
        Person robb = new Person(3,"Robb Stark", LocalDate.of(1983, 12, 26), "Northman");
        Company nightWatch = new Company(6,"Night's Watch");

        robb.addRelationships(ned, "Father");
        john.addRelationships(nightWatch, "Lord Commander");
        robb.addRelationships(nightWatch, "Ally");
        marcello.addRelationships(ned, "Friend");
        robb.addRelationships(john, "Brother");
        ned.addRelationships(robb, "Child");

        myNetwork.addProfile(ned);
        myNetwork.addProfile(marcello);
        myNetwork.addProfile(ygritte);
        myNetwork.addProfile(robb);
        myNetwork.addProfile(nightWatch);
        myNetwork.addProfile(john);

        System.out.println("--- Inainte de sortare ---");
        System.out.println(myNetwork);

        myNetwork.sortNetwork();

        System.out.println("\n--- Dupa sortare ---");
        System.out.println(myNetwork);

        myNetwork.sortNetworkImportance();
        System.out.println("\n--- Dupa sortare dupa importanta ---");
        System.out.println(myNetwork);

        System.out.println();
    }
}