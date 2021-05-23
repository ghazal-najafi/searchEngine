package RankedRetrieval.controller;

import RankedRetrieval.cache.Redis;
import RankedRetrieval.killList.KillList;
import RankedRetrieval.repository.ImageRepository;
import RankedRetrieval.scraping.WebScraping;
import RankedRetrieval.model.Resources;
import RankedRetrieval.ranking.COSINESCORE;
import RankedRetrieval.search.LogFile;
import RankedRetrieval.search.SimpleSearch;
import RankedRetrieval.service.DocumentService;
import RankedRetrieval.service.ImageService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

@Controller
public class DocumentController {
    Redis cache = new Redis();
    SimpleSearch simpleSearch = new SimpleSearch();
    public int editDocumentid;
    @Autowired
    DocumentService documentService;
    ImageService imageService;
    KillList killList = new KillList();
    ImageRepository imageRepository;
    static List<String> links = new ArrayList<>();

    @GetMapping(value = "/index")
    public String index(Model m) {
        ArrayList<String> list = LogFile.getLogFile();
        m.addAttribute("list", list);

        return "index";
    }

    @GetMapping(path = "/docs/{id}")
    public @ResponseBody
    String getResource(@PathVariable("id") int id) throws IOException, SQLException {
        Resources s = documentService.getDocument(id);
        return s.getBody();
    }

    @PostMapping("/curlResult")
    public ResponseEntity curlResult(@RequestParam("lname") String lname) throws IOException, SQLException, JSONException {
        links.clear();
        Map<String, ArrayList<String>> correctWordsMap = simpleSearch.getQuery(lname);
        ArrayList correctWords = new ArrayList();
        ArrayList<String> correctSentences = correctWordsMap.get("cs");
        correctWordsMap.remove("cs");
        String correctSentence = correctSentences.get(0);

        for (Map.Entry<String, ArrayList<String>> entry : correctWordsMap.entrySet()) {
            correctWords.add(entry.getValue());
        }
        List<Integer> rankedDocIds = new ArrayList<>();
        ArrayList<Resources> resources = new ArrayList<>();
        ArrayList<Resources> cachedResources = cache.get(correctSentence.trim());
        if (cachedResources == null) {
            Map<Integer, String> ranking = new HashMap<>();
            ArrayList<Integer> docIds = simpleSearch.returnDocId(correctSentence);
            for (int i = 0; i < docIds.size(); i++) {
                int id = docIds.get(i);
                if (documentService.getDocument(id) != null)
                    ranking.put(id, documentService.getDocument(id).getBody());
            }
            COSINESCORE cosinescore = new COSINESCORE();
            rankedDocIds = cosinescore.score(ranking, correctSentence);

            resources = new ArrayList<>();
            for (int i = 0; i < rankedDocIds.size(); i++) {
                int id = rankedDocIds.get(i);
                Resources res = documentService.getDocument(id);
                String title = res.getTitle();
                String body = res.getBody();
                Resources resource = new Resources();
                resource.setId(id);
                resource.setTitle(title);
                resource.setBody(body);
                resource.setLink(res.getLink());
                resources.add(resource);
            }
            if (docIds.size() == 0)
                System.out.println("Id Not Found Exception");
            else
                cache.store(correctSentence.trim(), resources);
        } else {
            for (int i = 0; i < cachedResources.size(); i++) {
                Resources resource = cachedResources.get(i);
                int id = resource.getId();
                rankedDocIds.add(id);
                resources.add(resource);
            }
        }
        for (int i = 0; i < rankedDocIds.size(); i++) {
            System.out.println(rankedDocIds.get(i));
            System.out.println(resources.get(i).getLink());
        }

        for (int i = 0; i < resources.size(); i++) {
            if (!links.contains(resources.get(i).getLink()))
                links.add(resources.get(i).getLink());
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(resources);
    }

    @PostMapping("/result")
    public ModelAndView result(@RequestParam("lname") String lname, ModelAndView model) throws IOException, SQLException {
        links.clear();
        Map<String, ArrayList<String>> correctWordsMap = simpleSearch.getQuery(lname);
        ArrayList correctWords = new ArrayList();
        ArrayList<String> correctSentences = correctWordsMap.get("cs");
        correctWordsMap.remove("cs");
        String correctSentence = correctSentences.get(0);

        for (Map.Entry<String, ArrayList<String>> entry : correctWordsMap.entrySet()) {
            correctWords.add(entry.getValue());
        }
        model.addObject("lname", lname);
        model.addObject("correctWords", correctWords);
        model.addObject("correctSentences", correctSentences);

        List<Integer> rankedDocIds = new ArrayList<>();
        ArrayList<Resources> resources = new ArrayList<>();
        ArrayList<Resources> cachedResources = cache.get(correctSentence.trim());
        if (cachedResources == null) {
            Map<Integer, String> ranking = new HashMap<>();
            ArrayList<Integer> docIds = simpleSearch.returnDocId(correctSentence);
            for (int i = 0; i < docIds.size(); i++) {
                int id = docIds.get(i);
                if (documentService.getDocument(id) != null)
                    ranking.put(id, documentService.getDocument(id).getBody());
            }
            COSINESCORE cosinescore = new COSINESCORE();
            rankedDocIds = cosinescore.score(ranking, correctSentence);

            resources = new ArrayList<>();
            for (int i = 0; i < rankedDocIds.size(); i++) {
                int id = rankedDocIds.get(i);
                Resources res = documentService.getDocument(id);
                String title = res.getTitle();
                String body = res.getBody();
                Resources resource = new Resources();
                resource.setId(id);
                resource.setTitle(title);
                resource.setBody(body);
                resource.setLink(res.getLink());
                resources.add(resource);
            }
            if (docIds.size() == 0)
                System.out.println("Id Not Found Exception");
            else
                cache.store(correctSentence.trim(), resources);
        } else {
            for (int i = 0; i < cachedResources.size(); i++) {
                Resources resource = cachedResources.get(i);
                int id = resource.getId();
                rankedDocIds.add(id);
                resources.add(resource);
            }
        }
        for (int i = 0; i < rankedDocIds.size(); i++) {
            System.out.println(rankedDocIds.get(i));
            System.out.println(resources.get(i).getLink());
        }

        for (int i = 0; i < resources.size(); i++) {
            if (!links.contains(resources.get(i).getLink()))
                links.add(resources.get(i).getLink());
        }

        model.addObject("resources", resources);
        model.setViewName("secondPage");
        return model;
    }

    @PostMapping("/editDocument")
    public ModelAndView editِDocument(@RequestParam("url") String url,
                                      @RequestParam("title") String title,
                                      @RequestParam("body") String body, ModelAndView m) throws IOException, SQLException {
        Resources editedDoucment = new Resources();
        editedDoucment.setId(editDocumentid);
        editedDoucment.setLink(url);
        editedDoucment.setTitle(title);
        editedDoucment.setBody(body);
        documentService.updateDocument(editedDoucment);
        killList.update(body, editDocumentid);
        killList.closeDB();
        System.out.println("url " + url);
        System.out.println("title " + title);
        System.out.println("body" + body);
        m.setViewName("edit");
        return m;
    }

    @PutMapping("/editDocument")
    public ResponseEntity<Resources> curlEditِDocument(@RequestParam("url") String url,
                                                       @RequestParam("title") String title,
                                                       @RequestParam("body") String body) throws IOException, SQLException {
        Resources editedDoucment = new Resources();
        editedDoucment.setId(editDocumentid);
        editedDoucment.setLink(url);
        editedDoucment.setTitle(title);
        editedDoucment.setBody(body);
        Resources doucment = documentService.updateDocument(editedDoucment);
        killList.update(body, editDocumentid);
        killList.closeDB();
        System.out.println("url " + url);
        System.out.println("title " + title);
        System.out.println("body" + body);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(doucment);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView edit(@RequestParam("lname") int lname, ModelAndView m) {
        System.out.println("id " + lname);
        editDocumentid = lname;
        m.setViewName("edit");
        return m;
    }

    @PostMapping(value = "/editDoc")
    public ResponseEntity curlEdit(@RequestParam("lname") int lname) {
        editDocumentid = lname;
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{lname}")
    public ResponseEntity curlDelete(@RequestParam("lname") int lname) throws IOException, SQLException {
        ResponseEntity response = documentService.deleteDocument(lname);
        if (response.getStatusCode() == HttpStatus.OK) {
            cache.deleteResource(lname);
            editDocumentid = lname;
            boolean bool = killList.delete(editDocumentid);
            if (bool == false)
                documentService.index();
            killList.closeDB();
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/delete/{lname}")
    public ModelAndView delete(@RequestParam("lname") int lname) throws IOException, SQLException {
        ModelAndView m = new ModelAndView();
        ResponseEntity response = documentService.deleteDocument(lname);
        if (response.getStatusCode() == HttpStatus.OK) {
            cache.deleteResource(lname);
            editDocumentid = lname;
            boolean bool = killList.delete(editDocumentid);
            if (bool == false)
                documentService.index();
            killList.closeDB();
            m.setViewName("success");
            return m;
        }
        m.setViewName("error");
        return m;
    }

    @GetMapping(path = "/indexingFile")
    public @ResponseBody
    String indexingFile() throws IOException, SQLException {
        documentService.index();
        return "indexing File.";
    }

    @GetMapping(value = "/insert")
    public String insert() {
        return "secondPage";
    }

    @PostMapping("/insert")
    public ModelAndView getURL(@RequestParam("url") String url, ModelAndView model) throws IOException, SQLException {
        model.addObject("url", url);
        model.setViewName("secondPage");
        Resources resource = WebScraping.webScrap(url);
        documentService.addDocument(resource);
        boolean bool = killList.add(resource.getBody(), resource.getId());
        if (bool == false)
            documentService.index();
        killList.closeDB();

        return model;
    }

    @PostMapping("/curlInsert")
    public ResponseEntity curlInsert(@RequestParam("url") String url) throws IOException, SQLException {
        Resources resource = WebScraping.webScrap(url);
        documentService.addDocument(resource);
        boolean bool = killList.add(resource.getBody(), resource.getId());
        if (bool == false)
            documentService.index();
        killList.closeDB();

        return new ResponseEntity(HttpStatus.OK);
    }
}