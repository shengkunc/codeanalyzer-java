package com.ibm.cldk.entities;

import java.util.List;
import lombok.Data;

@Data
public class EnumConstant {
    private String name;
    private List<String> arguments;
}
