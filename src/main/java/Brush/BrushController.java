package Brush;

import Program.CreateResult;
import Program.Programs;
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
@RequestMapping("/brushes")
public class BrushController {

    private final BrushService brushService;

    @Autowired
    public BrushController(BrushService brushService) {
        this.brushService = brushService;
    }


    @GetMapping()
    public ResponseEntity<?> getAllBrushes(@RequestParam(required = false) String program,
                                           @RequestParam(required = false) String search,
                                           @RequestParam(required = false) int page,
                                           @RequestParam(required = false) int size) {
        if (program != null && search != null) {
            return ResponseEntity.ok(brushService.sortByNameAndProgram(program, search, page, size));
        }
        if (program != null) {
            return ResponseEntity.ok(brushService.sortByProgram(program, page, size));
        } else if (search != null) {
            return ResponseEntity.ok(brushService.sortByTitle(search, page, size));
        } else {
            return ResponseEntity.ok(brushService.getAllBrushes(page, size));
        }
    }

    @GetMapping("/like")
    @GetUserId
    public ResponseEntity<?> getAllLikedBrushes(@RequestParam(required = false) String program,
                                                @RequestParam(required = false) String search,
                                                @RequestParam(required = false) int page,
                                                @RequestParam(required = false) int size,
                                                HttpServletRequest request) {
        if (program != null && search != null) {
            return ResponseEntity.ok(brushService.sortLikedByNameAndProgram(program, search, request, page, size));
        }
        if (program != null) {
            return ResponseEntity.ok(brushService.sortLikedByProgram(program, request, page, size));
        } else if (search != null) {
            return ResponseEntity.ok(brushService.sortLikedByTitle(search, request, page, size));
        } else {
            return ResponseEntity.ok(brushService.getAllLikedBrushes(request, page, size));
        }
    }


    @PostMapping("/create")
    public ResponseEntity<CreateResult> createBrush(@RequestParam String title,
                                                    @RequestParam String link,
                                                    @RequestParam String description,
                                                    @RequestParam String program,
                                                    @RequestPart("image") MultipartFile img) {
        return new ResponseEntity<>(brushService.createBrush(new BrushParams(title, link, description, program), img), HttpStatus.CREATED);
    }

    @PostMapping("{brushId}/add-favorite")
    @GetUserId
    public ResponseEntity<Users> addAndRemove(@PathVariable("brushId") Integer brushId, HttpServletRequest request) {
        return new ResponseEntity<>(brushService.addAndRemove(brushId, request), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<Brushes>> showProgram(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(brushService.showBrush(id), HttpStatus.OK);
    }
}

