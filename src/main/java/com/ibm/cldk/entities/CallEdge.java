/*
Copyright IBM Corporation 2023, 2024

Licensed under the Apache Public License 2.0, Version 2.0 (the "License");
you may not use this file except in compliance with the License.

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.ibm.cldk.entities;

import java.util.LinkedHashMap;
import java.util.Map;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;

/**
 * The type Call edge.
 */
public class CallEdge extends AbstractGraphEdge {
    /**
     * The constant serialVersionUID.
     */
    public static final long serialVersionUID = -8284030936836318929L;
    /**
     * The Type.
     */
    public final String type;

    /**
     * Instantiates a new Call edge.
     */
    public CallEdge() {
        super();
        this.type = toString();
    }

    /**
     * Instantiates a new Call edge.
     *
     * @param context the context
     */
    public CallEdge(String context) {
        super(context);
        this.type = toString();
    }

    @Override
    public String toString() {
        return "CALL_DEP";
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof CallEdge) && (toString().equals(o.toString()));
    }

    public Map<String, Attribute> getAttributes() {
        Map<String, Attribute> map = new LinkedHashMap<>();
        map.put("type", DefaultAttribute.createAttribute(toString()));
        map.put("weight", DefaultAttribute.createAttribute(String.valueOf(getWeight())));
        return map;
    }

    public Map<String, String> getAttributesMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("type", toString());
        map.put("weight", String.valueOf(getWeight()));
        return map;
    }
}
