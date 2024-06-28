package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DipendenteNotFound extends RuntimeException{
    public DipendenteNotFound(String message){
        super(message);
    }
}
