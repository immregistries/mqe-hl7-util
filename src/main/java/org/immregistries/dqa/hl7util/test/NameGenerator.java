package org.immregistries.dqa.hl7util.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NameGenerator {

    private static final Logger logger = LoggerFactory.getLogger(NameGenerator.class);
    private static final String nameFile = "/nameList.txt";

    public String getRandomName() {
        String name = "Aysla";
        int randomNumber = (int) (Math.random() * 14006);/*thats how many lines are in the file. */
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("nameList.txt");

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null && lineNum++ < randomNumber) {
                name = line;
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
        return name;
    }
}
