package com.ibm.cldk.javaee;

import com.ibm.cldk.javaee.camel.CamelEntrypointFinder;
import com.ibm.cldk.javaee.jakarta.JakartaEntrypointFinder;
import com.ibm.cldk.javaee.jax.JaxRsEntrypointFinder;
import com.ibm.cldk.javaee.spring.SpringEntrypointFinder;
import com.ibm.cldk.javaee.struts.StrutsEntrypointFinder;
import com.ibm.cldk.javaee.utils.interfaces.AbstractEntrypointFinder;
import java.util.stream.Stream;
import org.apache.commons.lang3.NotImplementedException;

public class EntrypointsFinderFactory {
    public static AbstractEntrypointFinder getEntrypointFinder(String framework) {
        switch (framework.toLowerCase()) {
            case "jakarta":
                return new JakartaEntrypointFinder();
            case "spring":
                return new SpringEntrypointFinder();
            case "camel":
                throw new NotImplementedException("Camel CRUD finder not implemented yet");
            case "struts":
                throw new NotImplementedException("Struts CRUD finder not implemented yet");
            default:
                throw new IllegalArgumentException("Unknown framework: " + framework);
        }
    }

    public static Stream<AbstractEntrypointFinder> getEntrypointFinders() {
        return Stream.of(new JakartaEntrypointFinder(), new StrutsEntrypointFinder(), new SpringEntrypointFinder(), new CamelEntrypointFinder(), new JaxRsEntrypointFinder());
    }
}
