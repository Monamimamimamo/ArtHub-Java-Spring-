package Reference;

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
public class ReferenceService {
    private final ReferencesRepo referencesRepo;
    private final SaveFile saveFile;
    private final StaticURLCreator staticURLCreator;
    private final UsersRepo usersRepo;

    @Autowired
    public ReferenceService(ReferencesRepo referencesRepo, SaveFile saveFile, StaticURLCreator staticURLCreator, UsersRepo usersRepo) {
        this.referencesRepo = referencesRepo;
        this.saveFile = saveFile;
        this.staticURLCreator = staticURLCreator;
        this.usersRepo = usersRepo;
    }

    public CreateResult createReference(ReferenceParams params, MultipartFile img) {
        References reference = new References(params.getTitle(), params.getHashtag());
        Iterable<References> references = referencesRepo.findAll();
        boolean hasDuplicate = StreamSupport.stream(references.spliterator(), false)
                .anyMatch(ref -> ref.getTitle().equals(reference.getTitle()));
        if (!hasDuplicate) {
            String imgName = saveFile.createFile(img);
            reference.setImage(staticURLCreator.createURL(imgName));
            reference.setCreatedAt(LocalDateTime.now());
            reference.setUpdatedAt(LocalDateTime.now());
            referencesRepo.save(reference);
            return new CreateResult(HttpStatus.CREATED, "Successfully created program");
        } else {
            return new CreateResult(HttpStatus.CONFLICT, "A reference with this titlealready exists");
        }
    }

    public Users addAndRemove(Integer referenceID, HttpServletRequest request) {
        Users user = getUser(request);
        List<Integer> references = user.getReferencesList();
        if (!referencesRepo.existsById(referenceID)) {
            throw new IllegalArgumentException("Reference not found");
        }
        if (!references.contains(referenceID)) {
            references.add(referenceID);
        } else {
            references.remove(referenceID);
        }
        user.setReferencesList(references);

        usersRepo.save(user);
        return user;
    }

    public List<?> showUserReferences(Integer userID){
        Users user = usersRepo.findById(userID);
        List<Integer> usersReferences = user.getReferencesList();
        return referencesRepo.findAllById(usersReferences);
    }

    public Map<String, Object> getAllReferences(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return Map.of("response", referencesRepo.findAll(pageable).getContent(),
                "totalCount", referencesRepo.findAll().size());
    }

    public Map<String, Object> sortByTitle(String title, int page, int size) {
        System.out.println(title);
        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = Arrays.stream(title.split(" "))
                .flatMap(word -> referencesRepo.findByTitleIgnoreCase(word).stream())
                .count();
        List<References> references = Arrays.stream(title.split(" "))
                .flatMap(word -> referencesRepo.findByTitleIgnoreCase(word).stream())
                .skip(pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .toList();
        return Map.of("response", references,
                "totalCount", filteredCount);
    }

    public Map<String, Object> sortByTag(String tag, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = referencesRepo.findByTagIgnoreCase(tag).stream().count();
        List<References> references = referencesRepo.findByTagIgnoreCase(tag)
                .stream()
                .skip(pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .toList();
        return Map.of("response", references,
                "totalCount", filteredCount);
    }

    public Map<String, Object> sortByNameAndTag(String tag, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = nameAndDifficultyFilter(name, tag).stream().count();
        List<References> references = nameAndDifficultyFilter(name, tag);
        return Map.of("response", references.stream()
                        .skip(pageable.getPageSize() * pageable.getPageNumber())
                        .limit(pageable.getPageSize())
                        .toList(),
                "totalCount", filteredCount);
    }

    public Map<String, Object> getAllLikedReferences(HttpServletRequest request, int page, int size) {
        List<References> references = referencesRepo.findAll().stream().toList();
        Pageable pageable = PageRequest.of(page - 1, size);
        return Map.of("response", outputWithFavorite(referencesRepo.findAll(pageable).getContent(), getUser(request)),
                "totalCount", referencesRepo.findAll().size());
    }

    public Map<String, Object> sortLikedByTitle(String title, HttpServletRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = Arrays.stream(title.split(" "))
                .flatMap(word -> referencesRepo.findByTitleIgnoreCase(word).stream())
                .count();
        List<References> references = Arrays.stream(title.split(" "))
                .flatMap(word -> referencesRepo.findByTitleIgnoreCase(word).stream())
                .skip(pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .toList();
        return Map.of("response", outputWithFavorite(references, getUser(request)),
                "totalCount", filteredCount);
    }

    public Map<String, Object> sortLikedByTag(String tag, HttpServletRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = referencesRepo.findByTagIgnoreCase(tag).stream().count();
        List<References> references = referencesRepo.findByTagIgnoreCase(tag)
                .stream()
                .skip(pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .toList();
        return Map.of("response", outputWithFavorite(references, getUser(request)),
                "totalCount", filteredCount);
    }

    public Map<String, Object> sortLikedByTitleAndTag(String tag, String title, HttpServletRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        long filteredCount = nameAndDifficultyFilter(title, tag).stream().count();
        List<References> references = nameAndDifficultyFilter(title, tag);
        return Map.of("response", outputWithFavorite(references.stream()
                        .skip(pageable.getPageSize() * pageable.getPageNumber())
                        .limit(pageable.getPageSize())
                        .toList(), getUser(request)),
                "totalCount", filteredCount);
    }

    private List<Map<String, Object>> outputWithFavorite(List<References> references, Users user) {
        return references.stream()
                .map(reference -> {
                    boolean isFavorite = user.getReferencesList().contains(reference.getId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", reference.getId());
                    map.put("title", reference.getTitle());
                    map.put("description", reference.getHashtag());
                    map.put("image", reference.getImage());
                    map.put("favorite", isFavorite);
                    return map;
                })
                .collect(Collectors.toList());
    }

    private Users getUser(HttpServletRequest request) {
        return usersRepo.findById((Integer) request.getAttribute("userId"));
    }

    private List<References> nameAndDifficultyFilter(String title, String tag) {
        List<References> brushes = Arrays.stream(title.split(" "))
                .flatMap(word -> referencesRepo.findByTitleIgnoreCase(word).stream())
                .toList();
        return brushes.stream()
                .filter(referencesRepo.findByTagIgnoreCase(tag)::contains)
                .toList();
    }
}
