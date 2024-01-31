package Tutorial;

import Brush.BrushParams;
import Brush.BrushService;
import Brush.Brushes;
import Program.CreateResult;
import User.GetUserId;
import User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/tutorials")
public class TutorialController {
    private final TutorialService tutorialService;

    @Autowired
    public TutorialController(TutorialService tutorialService) {
        this.tutorialService = tutorialService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllTutorials(@RequestParam(required = false) String difficulty,
                                             @RequestParam(required = false) String search,
                                             @RequestParam(required = false) int page,
                                             @RequestParam(required = false) int size) {
        if (difficulty != null && search != null) {
            return ResponseEntity.ok(tutorialService.sortByNameAndDifficulty(difficulty, search, page, size));
        }
        if (difficulty != null) {
            return ResponseEntity.ok(tutorialService.sortByDifficulty(difficulty, page, size));
        } else if (search != null) {
            return ResponseEntity.ok(tutorialService.sortByTitle(search, page, size));
        } else {
            return ResponseEntity.ok(tutorialService.getAllTutorials(page, size));
        }
    }

    @GetMapping("/like")
    @GetUserId
    public ResponseEntity<?> getAllLikedTutorials(@RequestParam(required = false) String difficulty,
                                                  @RequestParam(required = false) String search,
                                                  @RequestParam(required = false) int page,
                                                  @RequestParam(required = false) int size,
                                                  HttpServletRequest request) {
        if (difficulty != null && search != null) {
            return ResponseEntity.ok(tutorialService.sortLikedByTitleAndDifficulty(difficulty, search, request, page, size));
        }
        if (difficulty != null) {
            return ResponseEntity.ok(tutorialService.sortLikedByDifficulty(difficulty, request, page, size));
        } else if (search != null) {
            return ResponseEntity.ok(tutorialService.sortLikedByTitle(search, request, page, size));
        } else {
            return ResponseEntity.ok(tutorialService.getAllLikedTutorials(request, page, size));
        }
    }


    @PostMapping("/create")
    public ResponseEntity<CreateResult> createTutorial(@RequestParam String title,
                                                       @RequestParam String link,
                                                       @RequestParam String description,
                                                       @RequestParam String difficulty,
                                                       @RequestParam String duration,
                                                       @RequestParam String author,
                                                       @RequestPart("image") MultipartFile img) {
        return new ResponseEntity<>(tutorialService.createTutorial(new TutorialParams(title, link, description, difficulty, duration, author), img), HttpStatus.CREATED);
    }

    @PostMapping("{tutorialId}/add-favorite")
    @GetUserId
    public ResponseEntity<Users> addAndRemove(@PathVariable("tutorialId") Integer tutorialId, HttpServletRequest request) {
        return new ResponseEntity<>(tutorialService.addAndRemove(tutorialId, request), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<Tutorials>> showTutorial(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(tutorialService.showTutorial(id), HttpStatus.OK);
    }
    @GetMapping("{id}/liked")
    @GetUserId
    public ResponseEntity<?> showLikedTutorial(@PathVariable("id") Integer id, HttpServletRequest request) {
        return new ResponseEntity<>(tutorialService.showLikedTutorialByID(id, request), HttpStatus.OK);
    }
}
