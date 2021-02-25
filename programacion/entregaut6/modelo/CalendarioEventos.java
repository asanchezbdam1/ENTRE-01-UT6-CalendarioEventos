package programacion.entregaut6.modelo;
import programacion.entregaut6.io.CalendarioIO;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
/**
 * Esta clase modela un sencillo calendario de eventos.
 * 
 * Por simplicidad consideraremos que los eventos no se solapan
 * y no se repiten
 * 
 * El calendario guarda en un map los eventos de una serie de meses
 * Cada mes (la clave en el map, un enumerado Mes) tiene asociados 
 * en una colección ArrayList los eventos de ese mes
 * 
 * Solo aparecen los meses que incluyen algún evento
 * 
 * Las claves se recuperan en orden alfabético
 * 
 */
public class CalendarioEventos {
    private TreeMap<Mes, ArrayList<Evento>> calendario;

    /**
     * el constructor
     */
    public CalendarioEventos() {
        this.calendario = new TreeMap<>();
    }

    /**
     * añade un nuevo evento al calendario
     * Si la clave (el mes del nuevo evento) no existe en el calendario
     * se creará una nueva entrada con dicha clave y la colección formada
     * por ese único evento
     * Si la clave (el mes) ya existe se añade el nuevo evento insertándolo de forma
     * que quede ordenado por fecha y hora de inicio
     * 
     * Pista! Observa que en la clase Evento hay un método antesDe() que vendrá
     * muy bien usar aquí
     */
    public void addEvento(Evento nuevo) {
        Mes mes = nuevo.getMes();
        if (calendario.containsKey(mes)) {
            ArrayList aux = calendario.get(mes);
            aux.add(posicionEvento(aux, nuevo), nuevo);
            calendario.put(mes, aux);
        }
        else {
            calendario.put(mes, new ArrayList<>());
            calendario.get(mes).add(nuevo);
        }
    }

    /**
     * 
     */
    private int posicionEvento(ArrayList<Evento> eventos, Evento nuevo) {
        int n = 0;
        while (n < eventos.size() && eventos.get(n).antesDe(nuevo)) {
            n++;
        }
        return n;
    }

    /**
     * Representación textual del calendario
     * Hacer de forma eficiente 
     * Usar el conjunto de entradas  
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Mes, ArrayList<Evento>> ent : calendario.entrySet()) {
            sb.append(ent.getKey() + "\n\n");
            for (Evento ev : ent.getValue()) {
                sb.append(ev + "\n");
            }
        }
        return sb.toString();
    }

    /**
     * Dado un mes devolver la cantidad de eventos que hay en ese mes
     * Si el mes no existe se devuelve 0
     */
    public int totalEventosEnMes(Mes mes) {
        if(calendario.get(mes) == null) {
            return 0;
        }
        return calendario.get(mes).size();
    }

    /**
     * Devuelve un conjunto (importa el orden) 
     * con los meses que tienen mayor nº de eventos
     * Hacer un solo recorrido del map con el conjunto de claves
     *  
     */
    public TreeSet<Mes> mesesConMasEventos() {
        TreeSet<Mes> meses = new TreeSet<>();
        int max = 0;
        for (Mes mes : calendario.keySet()) {
            int total = totalEventosEnMes(mes);
            if (total > max) {
                max = total;
                meses.clear();
                meses.add(mes);
            }
            else if (total == max){
                meses.add(mes);
            }
        }
        return meses;
    }

    /**
     * Devuelve el nombre del evento de mayor duración en todo el calendario
     * Se devuelve uno solo (el primero encontrado) aunque haya varios
     */
    public String eventoMasLargo() {
        String str = "";
        Set<Map.Entry<Mes, ArrayList<Evento>>> entradas = calendario.entrySet();
        int max = Integer.MIN_VALUE;
        for (Map.Entry<Mes, ArrayList<Evento>> ent : entradas) {
            for (Evento ev : ent.getValue()) {
                if (ev.getDuracion() > max) {
                    max = ev.getDuracion();
                    str = ev.getNombre();
                }
            }
        }
        return str;
    }

    /**
     * Borrar del calendario todos los eventos de los meses indicados en el array
     * y que tengan lugar el día de la semana proporcionado (se entiende día de la
     * semana como 1 - Lunes, 2 - Martes ..  6 - Sábado, 7 - Domingo)
     * 
     * Si alguno de los meses del array no existe el el calendario no se hace nada
     * Si al borrar de un mes los eventos el mes queda con 0 eventos se borra la entrada
     * completa del map
     */
    public int cancelarEventos(Mes[] meses, int dia) {
        int n = 0;
        Dia d = Dia.values()[dia - 1];
        for (Mes mes : meses) {
            ArrayList<Evento> eventos = calendario.get(mes);
            if (eventos != null) {
                Iterator<Evento> it = eventos.iterator();
                while (it.hasNext()) {
                    if (it.next().getDia() == d) {
                        it.remove();
                        n++;
                    }
                }
            }
        }
        return n;
    }

    /**
     * Código para testear la clase CalendarioEventos
     */
    public static void main(String[] args) {
        CalendarioEventos calendario = new CalendarioEventos();
        CalendarioIO.cargarEventos(calendario);
        System.out.println(calendario);

        System.out.println();

        Mes mes = Mes.FEBRERO;
        System.out.println("Eventos en " + mes + " = "
            + calendario.totalEventosEnMes(mes));
        mes = Mes.MARZO;
        System.out.println("Eventos en " + mes + " = "
            + calendario.totalEventosEnMes(mes));
        System.out.println("Mes/es con más eventos "
            + calendario.mesesConMasEventos());

        System.out.println();
        System.out.println("Evento de mayor duración: "
            + calendario.eventoMasLargo());

        System.out.println();
        Mes[] meses = {Mes.FEBRERO, Mes.MARZO, Mes.MAYO, Mes.JUNIO};
        int dia = 6;
        System.out.println("Cancelar eventos de " + Arrays.toString(meses));
        int cancelados = calendario.cancelarEventos(meses, dia);
        System.out.println("Se han cancelado " + cancelados +
            " eventos");
        System.out.println();
        System.out.println("Después de cancelar eventos ...");
        System.out.println(calendario);
    }

}
