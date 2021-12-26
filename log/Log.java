package log;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Log {
    public void log(String name, String msg) throws IOException;
}
