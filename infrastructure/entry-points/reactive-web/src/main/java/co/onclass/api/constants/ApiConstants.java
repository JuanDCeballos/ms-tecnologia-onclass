package co.onclass.api.constants;

public final class ApiConstants {

    private ApiConstants() {
    }

    private static final String API_VERSION_BASE = "/api/v1";
    private static final String TECNOLOGIA_BASE = API_VERSION_BASE + "/tecnologia";

    public static final String GUARDAR_TECNOLOGIA = TECNOLOGIA_BASE;
    public static final String ASIGNAR_TECNOLOGIA = TECNOLOGIA_BASE + "/asignar-tecnologia";
}
