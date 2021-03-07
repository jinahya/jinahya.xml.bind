package com.github.jinahya.xml.bind;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class WrapperTest {

    @XmlRootElement(namespace = "some")
    public static class Some {
        public Some() {
            super();
        }
    }

    @Test
    void test(final @TempDir Path tempDir) throws IOException, JAXBException {
        final List<Some> somes = new ArrayList<>();
        somes.add(new Some());
        somes.add(new Some());
        final File file = Files.createTempFile(tempDir, null, null).toFile();
        Wrapper.marshal(Some.class, somes, file, m -> {
            try {
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            } catch (final PropertyException pe) {
                throw new RuntimeException(pe);
            }
            return m;
        });
        for (final String line : Files.readAllLines(file.toPath())) {
            System.out.println(line);
        }
    }
}