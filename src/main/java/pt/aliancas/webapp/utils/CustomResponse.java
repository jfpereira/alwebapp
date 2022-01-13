package pt.aliancas.webapp.utils;

import lombok.Data;

import java.util.List;

@Data
public class CustomResponse<T> {

    private T data;
    private List<String> errors;
}
