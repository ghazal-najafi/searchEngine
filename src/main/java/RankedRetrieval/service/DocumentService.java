package RankedRetrieval.service;

import RankedRetrieval.Index.InvertedIndex;
import RankedRetrieval.model.Resources;
import RankedRetrieval.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {
    @Autowired
    DocumentRepository documentRepository;

    public List<Resources> getAllDocuments() {
        List<Resources> documents = (List<Resources>) documentRepository.findAll();
        if (documents.size() > 0)
            return documents;
        else
            return new ArrayList<Resources>();
    }

    public Resources getDocument(int id) {
        Optional<Resources> resource = documentRepository.findById(id);
        if (resource.isPresent()) {
            return resource.get();
        } else {
            return null;
        }
    }

    public Resources addDocument(Resources resource) {
        if (resource.getId() == null) {
            resource = documentRepository.save(resource);
            System.out.println("resource inserted successfully.");
            return resource;
        } else {
            System.out.println("resource already exists.");
            return null;
        }
    }

//    public Resources searchDocument(String name) {
//        return documentRepository.findByName(name);
//    }

    public ResponseEntity deleteDocument(Integer id) {
        Optional<Resources> resource = documentRepository.findById(id);

        if (resource.isPresent()) {
            documentRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    public int count() {
        return (int) documentRepository.count();
    }

    public Resources updateDocument(Resources resource) throws IOException, SQLException {
        if (resource.getId() == null) {
            resource = documentRepository.save(resource);
            return resource;
        } else {
            Optional<Resources> existingDoc = documentRepository.findById(resource.getId());
            if (existingDoc.isPresent()) {
                Resources newResource = existingDoc.get();
                if (resource.getTitle() == null)
                    newResource.setTitle(existingDoc.get().getTitle());
                else if (resource.getBody() == null)
                    newResource.setTitle(existingDoc.get().getBody());
                else {
                    newResource.setTitle(resource.getTitle());
                    newResource.setBody(resource.getBody());
                }

                newResource = documentRepository.save(newResource);
                return newResource;
            } else {
                resource = documentRepository.save(resource);
                return resource;
            }
        }
    }

    public void index() throws IOException, SQLException {
        InvertedIndex index = new InvertedIndex();
        Resources resources = null;
        int count = count();
        int j = 0;
        for (int i = 0; i < count; j++) {
            resources = getDocument(j);
            if (resources != null) {
                i++;
                index.add(j, resources.getBody());
            }
        }
        index.last();
    }
}
