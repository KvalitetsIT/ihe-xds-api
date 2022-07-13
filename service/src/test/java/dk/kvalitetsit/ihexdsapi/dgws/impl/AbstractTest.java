package dk.kvalitetsit.ihexdsapi.dgws.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public abstract class AbstractTest {

    protected String getFileString(String f) {
        InputStream i = CredentialServiceImplTest.class.getResourceAsStream(f);
        String text = new BufferedReader(new InputStreamReader(i)).lines().collect(Collectors.joining("\n"));
        return text;
    }
}
