package RankedRetrieval.controller;

import RankedRetrieval.model.Resources;
import RankedRetrieval.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
public class databaseController {
    @Autowired
    private DocumentRepository documentRepository;

    @PostMapping(path = "/add") // Map ONLY POST Requests
    public @ResponseBody
    String addNewUser(@RequestParam String link
            , @RequestParam String title
            , @RequestParam String body) throws SQLException {

        Resources resources = new Resources();
        resources.setLink(link);
        resources.setTitle(title);
        resources.setBody(body);
        documentRepository.save(resources);
        return "Saved";
    }
}