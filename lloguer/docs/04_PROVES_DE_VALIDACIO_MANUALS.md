public class Main {

    public static void main(String[] args) {

        // 1. Creem persones i cotxes (de moment no estan relacionats)
        Persona anna = new Persona("Anna", "12345678Z");
        Persona jordi = new Persona("Jordi", "87654321X");

        Cotxe seat = new Cotxe("1234BCD", "Seat", "Ibiza");
        Cotxe renault = new Cotxe("5678FGH", "Renault", "Clio");
        Cotxe toyota = new Cotxe("9012JKL", "Toyota", "Corolla");

        System.out.println("--- Abans de relacionar ---");
        System.out.println(seat.descripcio());

        // 2. Relacionem: l'Anna en té dos i el Jordi un
        anna.afegirCotxe(seat);
        anna.afegirCotxe(renault);
        jordi.afegirCotxe(toyota);

        System.out.println("\n--- Després de relacionar ---");
        anna.mostrarCotxes();
        jordi.mostrarCotxes();
        System.out.println("Propietari del Seat: " + seat.getPropietari().getNom());

        // 3. Un cotxe NOMÉS pot tenir una persona: provem de donar el Seat al Jordi
        System.out.println("\n--- Provem de donar el Seat al Jordi ---");
        try {
            jordi.afegirCotxe(seat);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // 4. L'Anna ven el Renault: primer el treu d'ella i després el té el Jordi
        System.out.println("\n--- L'Anna ven el Renault al Jordi ---");
        anna.treureCotxe(renault);
        System.out.println(renault.descripcio());   // sense propietari
        jordi.afegirCotxe(renault);

        anna.mostrarCotxes();
        jordi.mostrarCotxes();

        System.out.println("\n--- Resum ---");
        System.out.println(anna.descripcio());
        System.out.println(jordi.descripcio());
    }
}
