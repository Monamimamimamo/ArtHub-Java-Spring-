package arthubrtf.ru.arthub;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class StaticURLCreator {
    public String createURL(String name){
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath(null)
                .path("/" + name)
                .build()
                .toUriString();
    }
}
