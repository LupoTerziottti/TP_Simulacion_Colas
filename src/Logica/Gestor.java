package Logica;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

public class Gestor {
    private int contadorRecalibracion;

    private ArrayList<String> conjuntoEventos;
    private LinkedList<Camion> camionesEnPuerta;

    private static Evento eventoActual;

    private Recepcion ServidorRecepcion;
    private Balanza ServidorBalanza;
    private ConjuntoDarsena ServidoresDarsena;

    public int getContadorRecalibracion() {
        return contadorRecalibracion;
    }

    public void setContadorRecalibracion(int contador) {
        this.contadorRecalibracion = contador;
    }


    public ArrayList<String> getConjuntoEventos() {
        return conjuntoEventos;
    }

    public void setConjuntoEventos(ArrayList<String> conjuntoEventos) {
        this.conjuntoEventos = conjuntoEventos;
    }

    public LinkedList<Camion> getCamionesEnPuerta() {
        return camionesEnPuerta;
    }

    public void setCamionesEnPuerta(LinkedList<Camion> camionesEnPuerta) {
        this.camionesEnPuerta = camionesEnPuerta;
    }

    public Gestor() {
        this.contadorRecalibracion = 0;
        this.conjuntoEventos = new ArrayList<>();
        this.camionesEnPuerta = new LinkedList<>();
        this.ServidorRecepcion = new Recepcion();
        this.ServidorBalanza = new Balanza();
        this.ServidoresDarsena = new ConjuntoDarsena();
    }

    public Evento getEventoActual() {
        return eventoActual;
    }

    public void setEventoActual(Evento eventoActual) {
        this.eventoActual = eventoActual;
    }

    public void inicio() {
        Reloj.getInstancia().setTiempoActual(0);
        LlegadaDeCamion primeraLlegada = new LlegadaDeCamion(this.ServidorRecepcion);
        this.setEventoActual(primeraLlegada);
        this.getConjuntoEventos().add(this.getEventoActual().getNombre());
        primeraLlegada.ejecutar();
        iterar();
    }

    public void addCamionEnPuerta(Camion c) {
        camionesEnPuerta.add(c);
    }


    public void iterar() {
        while (Reloj.getInstancia().getTiempoActual() < 2592000) {

            switch (proxEvento().getClass().toString()){
                case "Recepcion":
                    FinAtencionRecepcion finAtRecepcion = new FinAtencionRecepcion(this.ServidorRecepcion, this.ServidorBalanza);
                    this.setEventoActual(finAtRecepcion);
                    this.getConjuntoEventos().add(this.getEventoActual().getNombre());
                    finAtRecepcion.ejecutar();
                    Reloj.getInstancia().setTiempoActual(this.getServidorRecepcion().getProxFinAtencion());
                    break;
                case "Balanza":
                    FinAtencionBalanza finAtBalanza = new FinAtencionBalanza(this.ServidorBalanza, this.ServidoresDarsena);
                    this.setEventoActual(finAtBalanza);
                    this.getConjuntoEventos().add(this.getEventoActual().getNombre());
                    finAtBalanza.ejecutar();
                    Reloj.getInstancia().setTiempoActual(this.getServidorBalanza().getProxFinAtencion());
                    contadorRecalibracion++;
                    if(contadorRecalibracion == 15){
                        setContadorRecalibracion(0);
                        ServidorBalanza.setEstadoBalanza(EstadoBalanza.En_Recalibracion);
                    }
                    break;
                case "Darsena":
                    FinAtencionDarsena finAtDarsena = new FinAtencionDarsena(this.ServidoresDarsena);
                    this.setEventoActual(finAtDarsena);
                    this.getConjuntoEventos().add(this.getEventoActual().getNombre());
                    finAtDarsena.ejecutar();
                    Reloj.getInstancia().setTiempoActual(this.getServidoresDarsena().getProxFinAtencion());
                    break;


            }
            Reloj.getInstancia().setTiempoActual(eventoActual.getReloj());
            eventoActual.ejecutar();
        }
    }

    public Recepcion getServidorRecepcion() {
        return ServidorRecepcion;
    }

    public void setServidorRecepcion(Recepcion servidorRecepcion) {
        ServidorRecepcion = servidorRecepcion;
    }

    public Balanza getServidorBalanza() {
        return ServidorBalanza;
    }

    public void setServidorBalanza(Balanza servidorBalanza) {
        ServidorBalanza = servidorBalanza;
    }

    public ConjuntoDarsena getServidoresDarsena() {
        return ServidoresDarsena;
    }

    public void setServidoresDarsena(ConjuntoDarsena servidoresDarsena) {
        ServidoresDarsena = servidoresDarsena;
    }

    public double tiempoMinimo() {
        double tiempo;
        tiempo = Math.min(ServidoresDarsena.getDarsenas()[0].getProxFinAtencion(), ServidoresDarsena.getDarsenas()[1].getProxFinAtencion());
        tiempo = Math.min(ServidorBalanza.getProxFinAtencion(), tiempo);
        tiempo = Math.min(ServidorRecepcion.getProxFinAtencion(), tiempo);
        return tiempo;
    }

    public Object proxEvento() {
        double tiempo = tiempoMinimo();
        if (tiempo == ServidoresDarsena.getDarsenas()[0].getProxFinAtencion()) {
            return ServidoresDarsena.getDarsena(0);
        } else if (tiempo == ServidoresDarsena.getDarsenas()[1].getProxFinAtencion()) {
            return ServidoresDarsena.getDarsena(1);
        } else if (tiempo == ServidorBalanza.getProxFinAtencion()) {
            return ServidorBalanza;
        } else {
            return ServidorRecepcion;
        }
    }
}
