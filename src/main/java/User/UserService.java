package User;

import Brush.BrushRepo;
import Brush.Brushes;
import Program.FullProdDTO;
import Program.Programs;
import Program.ProgramsRepo;
import Reference.References;
import Reference.ReferencesRepo;
import Tutorial.Tutorials;
import Tutorial.TutorialsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UsersRepo usersRepo;
    private final BrushRepo brushRepo;
    private final ProgramsRepo programsRepo;
    private final ReferencesRepo referencesRepo;
    private final TutorialsRepo tutorialsRepo;

    @Autowired

    public UserService(UsersRepo usersRepo, BrushRepo brushRepo, ProgramsRepo programsRepo, ReferencesRepo referencesRepo, TutorialsRepo tutorialsRepo) {
        this.usersRepo = usersRepo;
        this.brushRepo = brushRepo;
        this.programsRepo = programsRepo;
        this.referencesRepo = referencesRepo;
        this.tutorialsRepo = tutorialsRepo;
    }


    public List<Brushes> getUsersBrushes(HttpServletRequest request) {
        Users user = getUser(request);
        List<Integer> brushesIDS = user.getBrushesList();
        List<Brushes> brushes = new ArrayList<>();
        for (Integer id : brushesIDS) {
            brushes.add(brushRepo.findById(id).get());
        }

        return brushes;
    }

    public List<?> getUsersPrograms(HttpServletRequest request) {
        Users user = getUser(request);
        List<Integer> programsIDS = user.getProgramsList();
        List<Programs> programs = new ArrayList<>();
        for (Integer id : programsIDS) {
            programs.add(programsRepo.findById(id).get());
        }
        return programs.stream().map(program -> {
            String[] minuses = program.getMinuses().split("\\R");
            String[] pluses = program.getPluses().split("\\R");
            String[] systems = program.getSystems().split(" ");
            String[] examples = program.getExamples().split(" ");
            return new FullProdDTO(program.getName(), program.getLink(), program.getDescription(), systems, minuses, pluses, program.getSite(), program.getLogo(), examples);
        }).collect(Collectors.toList());
    }

    public List<Tutorials> getUsersTutorials(HttpServletRequest request) {
        Users user = getUser(request);
        List<Integer> tutIDS = user.getTutorialsList();
        List<Tutorials> tuts = new ArrayList<>();
        for (Integer id : tutIDS) {
            tuts.add(tutorialsRepo.findById(id).get());
        }
        return tuts;
    }
    public List<References> getUsersReferences(HttpServletRequest request) {
        Users user = getUser(request);
        List<Integer> refsIDS = user.getReferencesList();
        List<References> refs = new ArrayList<>();
        for (Integer id : refsIDS) {
            refs.add(referencesRepo.findById(id).get());
        }
        return refs;
    }

    private Users getUser(HttpServletRequest request) {
        return usersRepo.findById((Integer) request.getAttribute("userId"));
    }
}
