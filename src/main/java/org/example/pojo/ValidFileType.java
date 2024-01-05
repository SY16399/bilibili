package org.example.pojo;

import java.util.ArrayList;
import java.util.List;

public class ValidFileType {
    private final List<String> validType = new ArrayList<>();

    public ValidFileType() {
        validType.add("image/png");
        validType.add("image/jpg");
        validType.add("image/jepg");
    }

    public List<String> getValidType() {
        return validType;
    }
}
