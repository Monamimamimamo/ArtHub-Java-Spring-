package Brush;

import Program.CreateResult;
import Program.Programs;
import User.Users;
import User.UsersRepo;
import arthubrtf.ru.arthub.SaveFile;
import arthubrtf.ru.arthub.StaticURLCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BrushService {
    private final BrushRepo brushRepo;
    private final SaveFile saveFile;
    private final StaticURLCreator staticURLCreator;
    private final UsersRepo usersRepo;

    @Autowired
    public BrushService(BrushRepo brushRepo, SaveFile saveFile, StaticURLCreator staticURLCreator, UsersRepo usersRepo) {
        this.brushRepo = brushRepo;
        this.saveFile = saveFile;
        this.staticURLCreator = staticURLCreator;
        this.usersRepo = usersRepo;
    }

    public Optional<Brushes> showBrush(Integer id) {
        return brushRepo.findById(id);
    }

    public CreateResult createBrush(BrushParams params, MultipartFile img) {
        Brushes brush = new Brushes(params.getTitle(), params.getLink(), params.getDescription(), params.getProgram());
        Iterable<Brushes> brushes = brushRepo.findAll();
        boolean hasDuplicate = StreamSupport.stream(brushes.spliterator(), false)
                .anyMatch(br -> br.getTitle().equals(brush.getTitle()) || br.getLink().equals(brush.getLink()));
        if (!hasDuplicate) {
            String imgName = saveFile.createFile(img);
            brush.setImage(staticURLCreator.createURL(imgName));
            brush.setCreatedAt(LocalDateTime.now());
            brush.setUpdatedAt(LocalDateTime.now());
            brushRepo.save(brush);
            return new CreateResult(HttpStatus.CREATED, "Successfully created program");
        } else {
            return new CreateResult(HttpStatus.CONFLICT, "A brush with this title or link already exists");
        }
    }

    public Users addAndRemove(Integer brushId, HttpServletRequest request) {
        Users user = getUser(request);
        List<Integer> brushes = user.getBrushesList();
        if (!brushRepo.existsById(brushId)) {
            throw new IllegalArgumentException("Brush not found");
        }
        if (!brushes.contains(brushId)) {
            brushes.add(brushId);
        } else {
            brushes.remove(brushId);
        }
        user.setBrushesList(brushes);

        usersRepo.save(user);
        return user;
    }

    public Map<String, Object> getAllBrushes(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return Map.of("response", brushRepo.findAll(pageable).getContent(),
                "totalCount", brushRepo.findAll().size());
    }

    public Map<String, Object> sortByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = Arrays.stream(title.split(" "))
                .flatMap(word -> brushRepo.findByTitleIgnoreCase(word).stream())
                .count();
        List<Brushes> brushes = Arrays.stream(title.split(" "))
                .flatMap(word -> brushRepo.findByTitleIgnoreCase(word).stream())
                .skip(pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .toList();
        return Map.of("response", brushes,
                "totalCount", filteredCount);
    }



    public Map<String, Object> sortByProgram(String program, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = brushRepo.findByProgramIgnoreCase(program).stream().count();
        List<Brushes> brushes = brushRepo.findByProgramIgnoreCase(program)
                .stream()
                .skip(pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .toList();
        return Map.of("response", brushes,
                "totalCount", filteredCount);
    }


    public Map<String, Object> sortByNameAndProgram(String program, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = nameAndProgramFilter(name, program).stream().count();
        List<Brushes> brushes = nameAndProgramFilter(name, program);
        return Map.of("response", brushes.stream()
                        .skip(pageable.getPageSize() * pageable.getPageNumber())
                        .limit(pageable.getPageSize())
                        .toList(),
                "totalCount", filteredCount);
    }

    public Map<String, Object> getAllLikedBrushes(HttpServletRequest request, int page, int size) {
        List<Brushes> brushes = brushRepo.findAll().stream().toList();
        Pageable pageable = PageRequest.of(page - 1, size);
        return Map.of("response", outputWithFavorite(brushRepo.findAll(pageable).getContent(), getUser(request)),
                "totalCount", brushRepo.findAll().size());
    }

    public Map<String, Object> sortLikedByTitle(String title, HttpServletRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = Arrays.stream(title.split(" "))
                .flatMap(word -> brushRepo.findByTitleIgnoreCase(word).stream())
                .count();
        List<Brushes> brushes = Arrays.stream(title.split(" "))
                .flatMap(word -> brushRepo.findByTitleIgnoreCase(word).stream())
                .skip(pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .toList();
        return Map.of("response", outputWithFavorite(brushes, getUser(request)),
                "totalCount", filteredCount);
    }

    public Map<String, Object> sortLikedByProgram(String program, HttpServletRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = brushRepo.findByProgramIgnoreCase(program).stream().count();
        List<Brushes> brushes = brushRepo.findByProgramIgnoreCase(program)
                .stream()
                .skip(pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .toList();
        return Map.of("response", outputWithFavorite(brushes, getUser(request)),
                "totalCount", filteredCount);
    }

    public Map<String, Object> sortLikedByNameAndProgram(String program, String name, HttpServletRequest request, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = nameAndProgramFilter(name, program).stream().count();
        List<Brushes> brushes = nameAndProgramFilter(name, program);
        return Map.of("response", outputWithFavorite(brushes.stream()
                        .skip(pageable.getPageSize() * pageable.getPageNumber())
                        .limit(pageable.getPageSize())
                        .toList(), getUser(request)),
                "totalCount", filteredCount);
    }

    private List<Map<String, Object>> outputWithFavorite(List<Brushes> brushes, Users user) {
        return brushes.stream()
                .map(brush -> {
                    boolean isFavorite = user.getBrushesList().contains(brush.getId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", brush.getId());
                    map.put("title", brush.getTitle());
                    map.put("link", brush.getLink());
                    map.put("description", brush.getDescription());
                    map.put("image", brush.getImage());
                    map.put("favorite", isFavorite);
                    return map;
                })
                .collect(Collectors.toList());
    }


    private Users getUser(HttpServletRequest request) {
        return usersRepo.findById((Integer) request.getAttribute("userId"));
    }

    private List<Brushes> nameAndProgramFilter(String name, String program) {
        List<Brushes> brushes = Arrays.stream(name.split(" "))
                .flatMap(word -> brushRepo.findByTitleIgnoreCase(word).stream())
                .toList();
        return brushes.stream()
                .filter(brushRepo.findByProgramIgnoreCase(program)::contains)
                .toList();
    }


}
