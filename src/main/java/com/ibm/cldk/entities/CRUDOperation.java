package com.ibm.cldk.entities;

import com.ibm.cldk.javaee.utils.enums.CRUDOperationType;
import com.ibm.cldk.utils.annotations.NotImplemented;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CRUDOperation {
    private int lineNumber = -1;
    private CRUDOperationType operationType;

    @NotImplemented
    private String targetTable = null;
    @NotImplemented
    private List<String> involvedColumns;
    @NotImplemented
    private String condition;
    @NotImplemented
    private List<String> joinedTables;
}
