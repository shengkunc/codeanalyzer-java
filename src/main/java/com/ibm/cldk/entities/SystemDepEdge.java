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

import com.ibm.wala.ipa.slicer.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;

/**
 * The type System dep edge.
 */
public class SystemDepEdge extends AbstractGraphEdge {
    /**
     * The constant serialVersionUID.
     */
    public static final long serialVersionUID = -8284030936836318929L;
    /**
     * The Source pos.
     */
    public final Integer sourcePos;
    /**
     * The Destination pos.
     */
    public final Integer destinationPos;
    /**
     * The Type.
     */
    public final String type;
    public final String sourceKind;
    public final String destinationKind;

    /**
     * Instantiates a new System dep edge.
     *
     * @param sourceStatement      the source statement
     * @param destinationStatement the destination statement
     * @param type                 the type
     */
    public SystemDepEdge(Statement sourceStatement, Statement destinationStatement, String type) {
        super();
        this.sourceKind = sourceStatement.getKind().toString();
        this.destinationKind = destinationStatement.getKind().toString();
        this.type = type;
        this.sourcePos = getStatementPosition(sourceStatement);
        this.destinationPos = getStatementPosition(destinationStatement);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(sourcePos).append(destinationPos).append(context).append(type)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof SystemDepEdge) && (this.toString().equals(o.toString()))
                && Integer.valueOf(this.hashCode()).equals(o.hashCode())
                && this.type.equals(((SystemDepEdge) o).getType());
    }


    public String getSourceKind() {
        return sourceKind;
    }

    public String getDestinationKind() {
        return destinationKind;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets source pos.
     *
     * @return the source pos
     */
    public Integer getSourcePos() {
        return sourcePos;
    }

    /**
     * Gets destination pos.
     *
     * @return the destination pos
     */
    public Integer getDestinationPos() {
        return destinationPos;
    }

    public Map<String, Attribute> getAttributes() {
        Map<String, Attribute> map = new LinkedHashMap<>();
        map.put("source_kind", DefaultAttribute.createAttribute(getSourceKind()));
        map.put("type", DefaultAttribute.createAttribute(getType()));
        map.put("destination_kind", DefaultAttribute.createAttribute(getDestinationKind()));
        map.put("weight", DefaultAttribute.createAttribute(String.valueOf(getWeight())));
        return map;
    }

    public Map<String, String> getAttributesMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("source_kind", getSourceKind());
        map.put("type", getType());
        map.put("destination_kind", getDestinationKind());
        map.put("weight", String.valueOf(getWeight()));
        return map;
    }
}
