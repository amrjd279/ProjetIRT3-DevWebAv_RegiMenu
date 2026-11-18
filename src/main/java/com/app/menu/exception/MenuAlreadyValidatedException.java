package com.app.menu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception métier levée lorsqu'une action de modification (ex: régénération)
 * est tentée sur un menu du jour déjà validé par l'utilisateur.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class MenuAlreadyValidatedException extends RuntimeException {

    public MenuAlreadyValidatedException(String message) {
        super(message);
    }
}
