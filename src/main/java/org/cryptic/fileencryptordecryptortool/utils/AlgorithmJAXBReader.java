package org.cryptic.fileencryptordecryptortool.utils;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.cryptic.fileencryptordecryptortool.model.Algorithm;
import org.cryptic.fileencryptordecryptortool.model.AlgorithmList;

import java.io.InputStream;
import java.util.List;

public class AlgorithmJAXBReader {
    public List<Algorithm> readAlgorithms(InputStream inputStream) {
        try {
            // Create JAXB context for the AlgorithmList class
            JAXBContext context = JAXBContext.newInstance(AlgorithmList.class);

            // Create an unmarshaller to convert the XML file to Java objects
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Read the XML file and convert it to an AlgorithmList object
            AlgorithmList algorithmList = (AlgorithmList) unmarshaller.unmarshal(inputStream);

            // Return the list of algorithms
            return algorithmList.getAlgorithms();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
