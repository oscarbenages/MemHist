package cat.memoriacastello.www.memoriahistorica;

/**
 * Created by coet on 05/09/2017.
 */

public class Cadenes {
    protected String concatena(String vector[]) {
        /*
         *  Este mètode permet concatenar els elements d'una llista amb la coma
         *  de separador i la conjunció entre el darrer i el penúltim element si s'escau.
         */
        StringBuilder sb = new StringBuilder();
        int i = 0, e = 0; //'i' es l'index dels vectors i 'e' és el nombre d'elements no nuls.

        for (String s: vector) {
            if (s == null) {
                i++;
                continue;
            }
            e++;

            if (i + 1 == vector.length && e > 1)
                sb.append(" i ");

            sb.append(s);
            i++;
            if (i < vector.length-1)
                sb.append(", ");
            else if (i == vector.length)
                sb.append(".");
        }

        return sb.toString();
    }

    protected String obtéDifTemps(long t1, long t2){
        long diff = t1 - t2;
        long vector[] = {
                diff / (60 * 60 * 1000) % 24,
                diff / (60 * 1000) % 60,
                diff / 1000 % 60
        };
        String unitats_sg[] = {"hora", "minut", "segon"};
        String unitats_pl[] = {"hores", "minuts", "segons"};
        String resultat[] = {null, null, null};
        for (int i = 0; i < vector.length; i++)
            if (vector[i]>0) resultat[i] = String.format("%d %s", vector[i], vector[i] != 1 ? unitats_pl[i] : unitats_sg[i]);
        return concatena(resultat);
    }

    protected String obtéDifTemps(long t1, long t2, boolean abreujat){
        long diff = t1 - t2;
        long vector[] = {
                diff / (60 * 60 * 1000) % 24,
                diff / (60 * 1000) % 60,
                diff / 1000 % 60
        };
        String unitats_sg[] = {"hora", "minut", "segon"};
        String unitats_pl[] = {"hores", "minuts", "segons"};
        String unitats_abr[] = {"h", "m", "s"};
        String resultat[] = {null, null, null};
        for (int i = 0; i < vector.length; i++)
            if (vector[i]>0) resultat[i] = String.format("%d %s", vector[i], !abreujat ? vector[i] != 1 ? unitats_pl[i] : unitats_sg[i] : unitats_abr[i]);
        return concatena(resultat);
    }

}
