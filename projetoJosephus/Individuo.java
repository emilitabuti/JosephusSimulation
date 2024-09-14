
/**
 * Escreva uma descrição da classe Individuo aqui.
 * 
 * @author (seu nome) 
 * @version (um número da versão ou uma data)
 */
public class Individuo
{
    int identificador;
    String status;
    
    Individuo(int identificador, String status){
        this.identificador = identificador;
        this.status = status;
    }
    
    public void setStatus(String status){
        this.status = status;
    }

    public Object getStatus(){
        return(this.status);
    }
    
    public int getIdentificador(){
        return(this.identificador);
    }
}
