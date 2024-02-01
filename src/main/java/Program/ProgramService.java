package Program;


import User.Users;
import User.UsersRepo;
import arthubrtf.ru.arthub.SaveFile;
import arthubrtf.ru.arthub.StaticURLCreator;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ProgramService {
    private final ProgramsRepo programsRepo;
    private final SaveFile saveFile;
    private final StaticURLCreator staticURLCreator;
    private final UsersRepo usersRepo;


    @Autowired
    public ProgramService(ProgramsRepo programsRepo, SaveFile saveFile, StaticURLCreator staticURLCreator, UsersRepo usersRepo) {
        this.programsRepo = programsRepo;
        this.saveFile = saveFile;
        this.staticURLCreator = staticURLCreator;
        this.usersRepo = usersRepo;
    }

    public Iterable<?> getAllPrograms() {
        return StreamSupport.stream(programsRepo.findAll().spliterator(), false)
                .map(this::normalizeOutput)
                .toList();
    }

    public List<Map<String, Object>> getAllLikedPrograms(HttpServletRequest request) {
        List<Programs> programs = StreamSupport.stream(programsRepo.findAll().spliterator(), false).toList();
        return outputWithFavorite(programs, getUser(request));
    }


    public Optional<?> showProgram(String name) {
        String decodedName = UriUtils.decode(name, StandardCharsets.UTF_8);
        return StreamSupport.stream(programsRepo.findAll().spliterator(), false)
                .map(program -> {
                    String[] minuses = program.getMinuses().split("\\R");
                    String[] pluses = program.getPluses().split("\\R");
                    return new FullProdDTO(program.getName(), program.getLink(), program.getDescription(), program.getSystems(), minuses, pluses, program.getSite());
                })
                .filter(program -> program.getName().toLowerCase().contains(decodedName.toLowerCase()))
                .findFirst();
    }



    public CreateResult createProgram(ProgramParams params, MultipartFile[] files) {
        Programs program = new Programs(params.getName(), params.getLink(), params.getDescription(), params.getSystems(), params.getPluses(), params.getMinuses(), params.getSite());
        Iterable<Programs> programs = programsRepo.findAll();
        boolean hasDuplicate = StreamSupport.stream(programs.spliterator(), false)
                .anyMatch(prog -> prog.getName().equals(program.getName()) || prog.getLink().equals(program.getLink()));
        if (!hasDuplicate) {
            String logoName = saveFile.createFile(files[0]);
            String example1Name = saveFile.createFile(files[1]);
            String example2Name = saveFile.createFile(files[2]);
            String example3Name = saveFile.createFile(files[3]);
            program.setLogo(staticURLCreator.createURL(logoName));
            program.setExamples(staticURLCreator.createURL(example1Name) + " " + staticURLCreator.createURL(example2Name) + " " + staticURLCreator.createURL(example3Name));

            program.setCreatedAt(LocalDateTime.now());
            program.setUpdatedAt(LocalDateTime.now());
            programsRepo.save(program);
            return new CreateResult(HttpStatus.CREATED, "Successfully created program");
        } else {
            return new CreateResult(HttpStatus.CONFLICT, "A program with this name or link already exists");
        }
    }

    public Users addAndRemove(Integer programId, HttpServletRequest request) {
        Users user = getUser(request);
        List<Integer> programs = user.getProgramsList();
        if (!programsRepo.existsById(programId)) {
            throw new IllegalArgumentException("Program not found");
        }
        if (!programs.contains(programId)) {
            programs.add(programId);
        } else {
            programs.remove(programId);
        }
        user.setProgramsList(programs);

        usersRepo.save(user);
        return user;
    }


    public List<Map<String, Object>> sortByName(String name) {
        return Arrays.stream(name.split(" "))
                .flatMap(word -> programsRepo.findByNameIgnoreCase(word).stream())
                .map(this::normalizeOutput)
                .toList();
    }

    public List<Map<String, Object>> sortLikedByName(String name, HttpServletRequest request) {
        List<Programs> programs = Arrays.stream(name.split(" "))
                .flatMap(word -> programsRepo.findByNameIgnoreCase(word).stream())
                .toList();
        return outputWithFavorite(programs, getUser(request));
    }

    public List<Map<String, Object>> sortBySystem(String system) {
        return programsRepo.findBySystemsIgnoreCase(system)
                .stream()
                .map(this::normalizeOutput)
                .toList();
    }

    public List<Map<String, Object>> sortLikedBySystem(String system, HttpServletRequest request) {
        return outputWithFavorite(
                programsRepo.findBySystemsIgnoreCase(system),
                getUser(request)
        );
    }

    public List<Map<String, Object>> sortByNameAndSystem(String system, String name) {
        List<Programs> programs = nameAndSystemFilter(name, system);
        return programs.stream().map(this::normalizeOutput).toList();
    }

    public List<Map<String, Object>> sortLikedByNameAndSystem(String system, String name, HttpServletRequest request) {
        return outputWithFavorite(
                nameAndSystemFilter(name, system),
                getUser(request)
        );
    }

    private List<Programs> nameAndSystemFilter(String name, String system) {
        List<Programs> programs = Arrays.stream(name.split(" "))
                .flatMap(word -> programsRepo.findByNameIgnoreCase(word).stream())
                .toList();
        return programs.stream()
                .filter(programsRepo.findBySystemsIgnoreCase(system)::contains)
                .toList();
    }


    private Map<String, Object> normalizeOutput(Programs program) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", program.getId());
        map.put("name", program.getName());
        map.put("systems", Arrays.asList(program.getSystems().split(" ")));
        map.put("description", program.getDescription());
        map.put("logo", program.getLogo());
        return map;
    }

    private List<Map<String, Object>> outputWithFavorite(List<Programs> programs, Users user) {
        return programs.stream()
                .map(program -> {
                    boolean isFavorite = user.getProgramsList().contains(program.getId());
                    Map<String, Object> map = normalizeOutput(program);
                    map.put("favorite", isFavorite);
                    return map;
                })
                .collect(Collectors.toList());
    }

    private Users getUser(HttpServletRequest request) {
        return usersRepo.findById((Integer) request.getAttribute("userId"));
    }
}