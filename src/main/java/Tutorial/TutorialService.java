package Tutorial;

import Brush.Brushes;
import Program.CreateResult;
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

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TutorialService {
    private final TutorialsRepo tutorialsRepo;
    private final SaveFile saveFile;
    private final StaticURLCreator staticURLCreator;
    private final UsersRepo usersRepo;

    @Autowired
    public TutorialService(TutorialsRepo tutorialsRepo, SaveFile saveFile, StaticURLCreator staticURLCreator, UsersRepo usersRepo) {
        this.tutorialsRepo = tutorialsRepo;
        this.saveFile = saveFile;
        this.staticURLCreator = staticURLCreator;
        this.usersRepo = usersRepo;
    }

    public CreateResult createTutorial(TutorialParams params, MultipartFile img) {
        Tutorials tutorial = new Tutorials(params.getTitle(), params.getLink(), params.getDescription(), params.getDifficulty(), params.getDuration(), params.getAuthor());
        Iterable<Tutorials> tutorials = tutorialsRepo.findAll();
        boolean hasDuplicate = StreamSupport.stream(tutorials.spliterator(), false)
                .anyMatch(tut -> tut.getTitle().equals(tutorial.getTitle()) || tut.getLink().equals(tutorial.getLink()));
        if (!hasDuplicate) {
            String imgName = saveFile.createFile(img);
            tutorial.setImage(staticURLCreator.createURL(imgName));
            tutorial.setCreatedAt(LocalDateTime.now());
            tutorial.setUpdatedAt(LocalDateTime.now());
            tutorialsRepo.save(tutorial);
            return new CreateResult(HttpStatus.CREATED, "Successfully created program");
        } else {
            return new CreateResult(HttpStatus.CONFLICT, "A title with this title or link already exists");
        }


    }

    public Users addAndRemove(Integer tutorialId, HttpServletRequest request) {
        Users user = getUser(request);
        List<Integer> tutorials = user.getTutorialsList();
        if (!tutorialsRepo.existsById(tutorialId)) {
            throw new IllegalArgumentException("Tutorial not found");
        }
        if (!tutorials.contains(tutorialId)) {
            tutorials.add(tutorialId);
        } else {
            tutorials.remove(tutorialId);
        }
        user.setTutorialsList(tutorials);

        usersRepo.save(user);
        return user;
    }

    public Map<String, Object> getAllTutorials(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return Map.of("response", tutorialsRepo.findAll(pageable).getContent(),
                "totalCount", tutorialsRepo.findAll().size());
    }

    public Map<String, Object> showLikedTutorialByID(int tutorialId, HttpServletRequest request) {
        Users user = getUser(request);
        Optional<Tutorials> tutoriaal = tutorialsRepo.findById(tutorialId);
        if(tutoriaal != null){
            Tutorials tutorial = tutoriaal.get();
            boolean isFavorite = user.getTutorialsList().contains(tutorial.getId());
            Map<String, Object> map = new HashMap<>();
            map.put("id", tutorial.getId());
            map.put("title", tutorial.getTitle());
            map.put("link", tutorial.getLink());
            map.put("description", tutorial.getDescription());
            map.put("difficulty", tutorial.getDifficulty());
            map.put("duration", tutorial.getDuration());
            map.put("author", tutorial.getDuration());
            map.put("image", tutorial.getImage());
            map.put("favorite", isFavorite);
            return map;
        }else {
            throw new IllegalArgumentException("Tutorial not found");
        }
    }

    public Map<String, Object> sortByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = Arrays.stream(title.split(" "))
                .flatMap(word -> tutorialsRepo.findByTitleIgnoreCase(word).stream())
                .count();
        List<Tutorials> tutorials = Arrays.stream(title.split(" "))
                .flatMap(word -> tutorialsRepo.findByTitleIgnoreCase(word).stream())
                .skip(pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .toList();
        return Map.of("response", tutorials,
                "totalCount", filteredCount);
    }

    public Map<String, Object> sortByDifficulty(String difficulty, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = tutorialsRepo.findByDifficultyIgnoreCase(difficulty).stream().count();
        List<Tutorials> tutorials = tutorialsRepo.findByDifficultyIgnoreCase(difficulty)
                .stream()
                .skip(pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .toList();
        return Map.of("response", tutorials,
                "totalCount", filteredCount);
    }

    public Map<String, Object> sortByNameAndDifficulty(String difficulty, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = nameAndDifficultyFilter(name, difficulty).stream().count();
        List<Tutorials> tutorials = nameAndDifficultyFilter(name, difficulty);
        return Map.of("response", tutorials.stream()
                        .skip(pageable.getPageSize() * pageable.getPageNumber())
                        .limit(pageable.getPageSize())
                        .toList(),
                "totalCount", filteredCount);
    }

    public Map<String, Object> getAllLikedTutorials(HttpServletRequest request, int page, int size) {
        List<Tutorials> tutorials = tutorialsRepo.findAll().stream().toList();
        Pageable pageable = PageRequest.of(page - 1, size);
        return Map.of("response", outputWithFavorite(tutorialsRepo.findAll(pageable).getContent(), getUser(request)),
                "totalCount", tutorialsRepo.findAll().size());
    }

    public Map<String, Object> sortLikedByTitle(String title, HttpServletRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = Arrays.stream(title.split(" "))
                .flatMap(word -> tutorialsRepo.findByTitleIgnoreCase(word).stream())
                .count();
        List<Tutorials> tutorials = Arrays.stream(title.split(" "))
                .flatMap(word -> tutorialsRepo.findByTitleIgnoreCase(word).stream())
                .skip(pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .toList();
        return Map.of("response", outputWithFavorite(tutorials, getUser(request)),
                "totalCount", filteredCount);
    }

    public Map<String, Object> sortLikedByDifficulty(String difficulty, HttpServletRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = tutorialsRepo.findByDifficultyIgnoreCase(difficulty).stream().count();
        List<Tutorials> tutorials = tutorialsRepo.findByDifficultyIgnoreCase(difficulty)
                .stream()
                .skip(pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .toList();
        return Map.of("response", outputWithFavorite(tutorials, getUser(request)),
                "totalCount", filteredCount);
    }

    public Map<String, Object> sortLikedByTitleAndDifficulty(String difficulty, String title, HttpServletRequest request, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = nameAndDifficultyFilter(title, difficulty).stream().count();
        List<Tutorials> tutorials = nameAndDifficultyFilter(title, difficulty);
        return Map.of("response", outputWithFavorite(tutorials.stream()
                        .skip(pageable.getPageSize() * pageable.getPageNumber())
                        .limit(pageable.getPageSize())
                        .toList(), getUser(request)),
                "totalCount", filteredCount);
    }

    private List<Map<String, Object>> outputWithFavorite(List<Tutorials> tutorials, Users user) {
        return tutorials.stream()
                .map(tutorial -> {
                    boolean isFavorite = user.getTutorialsList().contains(tutorial.getId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", tutorial.getId());
                    map.put("title", tutorial.getTitle());
                    map.put("link", tutorial.getLink());
                    map.put("description", tutorial.getDescription());
                    map.put("difficulty", tutorial.getDifficulty());
                    map.put("duration", tutorial.getDuration());
                    map.put("author", tutorial.getDuration());
                    map.put("image", tutorial.getImage());
                    map.put("favorite", isFavorite);
                    return map;
                })
                .collect(Collectors.toList());
    }

    private List<Tutorials> nameAndDifficultyFilter(String title, String difficulty) {
        List<Tutorials> brushes = Arrays.stream(title.split(" "))
                .flatMap(word -> tutorialsRepo.findByTitleIgnoreCase(word).stream())
                .toList();
        return brushes.stream()
                .filter(tutorialsRepo.findByDifficultyIgnoreCase(difficulty)::contains)
                .toList();
    }

    private Users getUser(HttpServletRequest request) {
        return usersRepo.findById((Integer) request.getAttribute("userId"));
    }

    public Optional<Tutorials> showTutorial(Integer id) {
        return tutorialsRepo.findById(id);
    }
}

