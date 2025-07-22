package com.ibm.cldk.entities;

import java.io.Serializable;
import java.util.Map;
import org.jgrapht.nio.Attribute;


public abstract class AbstractGraphVertex implements Serializable {

        public abstract Map<String, Attribute> getAttributes();

        @Override
        public boolean equals(Object obj) {
                return super.equals(obj);
        }

        @Override
        public int hashCode() {
                return super.hashCode();
        }
}
