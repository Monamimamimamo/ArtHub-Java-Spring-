package arthubrtf.ru.arthub;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class SaveFile {

    public String createFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + ".jpg";
            Path filePath = Paths.get("static");
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath);
            }
            Path targetLocation = Paths.get("static/" + fileName);
            Files.copy(file.getInputStream(), targetLocation);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при записи файлов", e);
        }
    }
}
