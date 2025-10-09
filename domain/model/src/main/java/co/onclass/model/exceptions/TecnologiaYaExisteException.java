package co.onclass.model.exceptions;

public class TecnologiaYaExisteException extends RuntimeException {
    public TecnologiaYaExisteException(String nombre) {
        super("La tecnología con el nombre '" + nombre + "' ya existe.");
    }
}
