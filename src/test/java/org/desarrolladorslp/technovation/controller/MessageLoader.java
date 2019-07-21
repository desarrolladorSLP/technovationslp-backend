package org.desarrolladorslp.technovation.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

class MessageLoader {
    static String loadExampleRequest(String path) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(path)) {

            if (inputStream == null) {
                throw new IOException("File <" + path + "> does not exist");
            }

            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }
}
