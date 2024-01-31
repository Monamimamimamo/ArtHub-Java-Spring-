package Reference;

import Program.CreateResult;
import Tutorial.TutorialParams;
import Tutorial.TutorialService;
import User.GetUserId;
import User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/references")
public class ReferenceController {
    private final ReferenceService referenceService;

    @Autowired
    public ReferenceController(ReferenceService referenceService) {
        this.referenceService = referenceService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllReferences(@RequestParam(required = false) String tag,
                                             @RequestParam(required = false) String search,
                                             @RequestParam(required = false) int page,
                                             @RequestParam(required = false) int size) {
        if (tag != null && search != null) {
            return ResponseEntity.ok(referenceService.sortByNameAndTag(tag, search, page, size));
        }
        if (tag != null) {
            return ResponseEntity.ok(referenceService.sortByTag(tag, page, size));
        } else if (search != null) {
            return ResponseEntity.ok(referenceService.sortByTitle(search, page, size));
        } else {
            return ResponseEntity.ok(referenceService.getAllReferences(page, size));
        }
    }

    @GetMapping("/like")
    @GetUserId
    public ResponseEntity<?> getAllLikedReferences(@RequestParam(required = false) String tag,
                                                  @RequestParam(required = false) String search,
                                                  @RequestParam(required = false) int page,
                                                  @RequestParam(required = false) int size,
                                                  HttpServletRequest request) {
        if (tag != null && search != null) {
            return ResponseEntity.ok(referenceService.sortLikedByTitleAndTag(tag, search, request, page, size));
        }
        if (tag != null) {
            return ResponseEntity.ok(referenceService.sortLikedByTag(tag, request, page, size));
        } else if (search != null) {
            return ResponseEntity.ok(referenceService.sortLikedByTitle(search, request, page, size));
        } else {
            return ResponseEntity.ok(referenceService.getAllLikedReferences(request, page, size));
        }
    }


    @PostMapping("/create")
    public ResponseEntity<CreateResult> createReference(@RequestParam String title,
                                                       @RequestParam String hashtag,
                                                       @RequestPart("image") MultipartFile img) {
        return new ResponseEntity<>(referenceService.createReference(new ReferenceParams(title,  hashtag), img), HttpStatus.CREATED);
    }

    @PostMapping("{referenceID}/add-favorite")
    @GetUserId
    public ResponseEntity<Users> addAndRemove(@PathVariable("referenceID") Integer referenceID, HttpServletRequest request) {
        return new ResponseEntity<>(referenceService.addAndRemove(referenceID, request), HttpStatus.OK);
    }

    @GetMapping("{userID}")
    public ResponseEntity<List<?>> addAndRemove(@PathVariable("userID") Integer userID) {
        return new ResponseEntity<>(referenceService.showUserReferences(userID), HttpStatus.OK);
    }
}
