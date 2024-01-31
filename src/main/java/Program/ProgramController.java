package Program;


import User.GetUserId;
import User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/programs")
public class ProgramController {

    private final ProgramService programService;

    @Autowired
    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }


    @GetMapping()
    public ResponseEntity<?> getAllPrograms(@RequestParam(required = false) String system,
                                            @RequestParam(required = false) String search) {
        if (system != null && search != null) {
            return ResponseEntity.ok(programService.sortByNameAndSystem(system, search));
        }
        if (system != null) {
            return ResponseEntity.ok(programService.sortBySystem(system));
        } else if (search != null) {
            return ResponseEntity.ok(programService.sortByName(search));
        } else {
            return ResponseEntity.ok(programService.getAllPrograms());
        }
    }

    @GetMapping("/like")
    @GetUserId
    public ResponseEntity<?> getAllLikedPrograms(@RequestParam(required = false) String system,
                                                 @RequestParam(required = false) String search,
                                                 HttpServletRequest request) {
        if (system != null && search != null) {
            return ResponseEntity.ok(programService.sortLikedByNameAndSystem(system, search, request));
        }
        if (system != null) {
            return ResponseEntity.ok(programService.sortLikedBySystem(system, request));
        } else if (search != null) {
            return ResponseEntity.ok(programService.sortLikedByName(search, request));
        } else {
            return ResponseEntity.ok(programService.getAllLikedPrograms(request));
        }
    }


    @PostMapping("/create")
    public ResponseEntity<CreateResult> createProgram(@RequestParam String name,
                                                             @RequestParam String link,
                                                             @RequestParam String description,
                                                             @RequestParam String systems,
                                                             @RequestParam String pluses,
                                                             @RequestParam String minuses,
                                                             @RequestParam String site,
                                                             @RequestPart("logo") MultipartFile logo,
                                                             @RequestPart("example1") MultipartFile example1,
                                                             @RequestPart("example2") MultipartFile example2,
                                                             @RequestPart("example3") MultipartFile example3) {
        MultipartFile[] files = {logo, example1, example2, example3};
        return new ResponseEntity<>(programService.createProgram(new ProgramParams(name, link, description, systems, pluses, minuses, site), files), HttpStatus.CREATED);
    }

    @PostMapping("{progId}/add-favorite")
    @GetUserId
    public ResponseEntity<Users> addAndRemove(@PathVariable("progId") Integer progId, HttpServletRequest request) {
        return new ResponseEntity<>(programService.addAndRemove(progId, request), HttpStatus.OK);
    }

    @GetMapping("{program}")
    public ResponseEntity<Optional<Programs>> showProgram(@PathVariable("program") String program) {
        return new ResponseEntity<>(programService.showProgram(program), HttpStatus.OK);
    }
}

