public class Inventario {
    private List<Oggetto> oggetti;

    public Inventario() {
        oggetti = new Arraylist<Oggetto>();
    }

    public Optional<Oggetto> cercaOggetto(Oggetto oggetto) {
        // todo, itera su la lista per trovare l'oggetto
    }

    public aggiungiOggetto(Oggetto oggetto) {
        oggetti.add(oggetto);
    }

    public eliminaOggetto(Oggetto oggetto) {
        oggetti.remove(oggetto);
    }
}