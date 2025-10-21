package co.onclass.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionMessages {

    TECNOLOGIA_YA_EXISTE(409, "La tecnología ya existe en nuestro sistema.");

    private final int code;
    private final String message;
}
