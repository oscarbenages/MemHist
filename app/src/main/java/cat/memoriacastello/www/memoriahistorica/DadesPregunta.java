package cat.memoriacastello.www.memoriahistorica;

/**
 * Created by coet on 16/08/2017.
 */

public class DadesPregunta {
    //Atributs
    private int id;
    private String enunciat;
    private String resposta1;
    private String resposta2;
    private String resposta3;
    private int respCorrecta;
    private String imatge;
    private int estat;

    //Constructors
    public DadesPregunta(int id, String e, String r1, String r2, String r3, int rC){
        this.id = id;
        enunciat = escape(e);
        resposta1 = r1;
        resposta2 = r2;
        resposta3 = r3;
        respCorrecta = rC;
        imatge = null;
        estat = 0;
    }

    public DadesPregunta(int id, String e, String r1, String r2, String r3, int rC, String i){
        this.id = id;
        enunciat = escape(e);
        resposta1 = r1;
        resposta2 = r2;
        resposta3 = r3;
        respCorrecta = rC;
        imatge = i;
        estat = 0;
    }

    private String escape(String s){
        return s.replaceAll("\\\\t","\t").replaceAll("\\\\n","\n");
    }

    //Mètodes
    public int getId() {
        return id;
    }

    public String getEnunciat() {
        return enunciat;
    }

    public String getResposta1() {
        return resposta1;
    }

    public String getResposta2() {
        return resposta2;
    }

    public String getResposta3() {
        return resposta3;
    }

    public int getRespCorrecta() {
        return respCorrecta;
    }

    public void setEstat(int e) {
        estat = e;
    }

    public int getEstat() {
        return estat;
    }
}
