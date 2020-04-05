package rs.raf.word_distribution.file_input;

import rs.raf.word_distribution.InputDataFrame;
import rs.raf.word_distribution.events.EventManager;
import rs.raf.word_distribution.events.EventType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class FileReader implements Callable<InputDataFrame> {

    private File file;

    public FileReader(File file) {
        this.file = file;
    }

    @Override
    public InputDataFrame call() throws Exception {
        return this.readFile();
    }

    private InputDataFrame readFile() {
        try {
            System.out.println("Reading file:" + file.getCanonicalPath());

            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String content = new String(data, StandardCharsets.US_ASCII);

            System.out.println("Reading finish:" + file.getCanonicalPath());

            return new InputDataFrame(file.getName(), content);
        } catch (OutOfMemoryError outOfMemoryError) {
            EventManager.getInstance().notify(EventType.OUT_OF_MEMORY);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
