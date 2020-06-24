package info.andersw.dataprocessing;

import lombok.ToString;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;

@Service
@CommonsLog
@ToString
public class WorkdirMonitor {

    @Autowired
    private DataprocessingConfig conf;

    private Path workdir;

    public WorkdirMonitor() {}

    @PostConstruct
    public void init() throws IOException {
        this.workdir = Paths.get(conf.getWorkdir());
        WatchService watcher = FileSystems.getDefault().newWatchService();

    }
}
