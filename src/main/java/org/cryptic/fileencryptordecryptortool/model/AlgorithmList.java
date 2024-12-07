package org.cryptic.fileencryptordecryptortool.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "algorithms")  // Root element for the list of algorithms
public class AlgorithmList {
    private List<Algorithm> algorithms;

    @XmlElement(name = "algorithm")  // Maps each <algorithm> element in the XML
    public List<Algorithm> getAlgorithms() {
        return algorithms;
    }

    public void setAlgorithms(List<Algorithm> algorithms) {
        this.algorithms = algorithms;
    }
}
