public class Scrittore implements Utente
{
    private String nome, password; // incapsulation

    public Scrittore(String nome, String password) {
        this.nome = nome;
        this.password = password;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //set/get Storia
    //set/get scenario con possibile oggetto
    //set/get indovinello
}
