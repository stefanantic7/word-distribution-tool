package rs.raf.word_distribution;

import rs.raf.word_distribution.file_input.Disk;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Config {

    private static Properties prop;

    static {
        prop = new Properties();
        try {
            InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("app.properties");
            if (inputStream != null) {
                prop.load(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final long FILE_INPUT_SLEEP_TIME = Long.parseLong(prop.getProperty("file_input_sleep_time"));
    public static final List<Disk> DISKS = Arrays.stream(prop.getProperty("disks").split(";")).map(Disk::new).collect(Collectors.toUnmodifiableList());
    public static final int COUNTER_DATA_LIMIT = Integer.parseInt(prop.getProperty("counter_data_limit"));
    public static final long SORT_PROGRESS_LIMIT = Long.parseLong(prop.getProperty("sort_progress_limit"));
}
