package org.example.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonResultObject <T>{
    private String code;
    private String message;
    private String errorMessage;
    private String errorCode;
    private T data;
}
