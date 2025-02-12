package br.com.thallyta.saletickets.batch.importationjob.tasklet;

import br.com.thallyta.saletickets.common.FileMoveException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;

@Component
@Slf4j
public class MoveFilesTasklet {

    public Tasklet moveFilesTasklet() {
        return (contribution, chunkContext) -> {
            Path sourceFolder = Paths.get("files");
            Path destinationFolder = Paths.get("imported-files");

            try {
                if (!Files.exists(sourceFolder) || !Files.isDirectory(sourceFolder)) {
                    log.warn("Source folder '{}' does not exist or is not a directory. No files to move.", sourceFolder);
                    return RepeatStatus.FINISHED;
                }

                Files.createDirectories(destinationFolder);

                try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceFolder, "*.csv")) {
                    for (Path file : stream) {
                        Path destinationFile = destinationFolder.resolve(file.getFileName());
                        try {
                            Files.move(file, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                            log.info("File moved: {}", file.getFileName());
                        } catch (IOException e) {
                            log.error("Failed to move file: {}", file.getFileName(), e);
                            throw new FileMoveException("Failed to move file: " + file.getFileName(), e);
                        }
                    }
                }
            } catch (IOException e) {
                log.error("Error handling file movement", e);
                throw new FileMoveException("Error handling file movement", e);
            }

            return RepeatStatus.FINISHED;
        };
    }
}
