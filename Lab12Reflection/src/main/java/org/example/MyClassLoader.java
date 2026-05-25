package org.example;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public class MyClassLoader extends URLClassLoader {
    public MyClassLoader(Path folder) throws Exception {
        super(new URL[]{folder.toUri().toURL()});
    }
}