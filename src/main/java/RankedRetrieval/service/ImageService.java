package RankedRetrieval.service;

import RankedRetrieval.model.Images;
import RankedRetrieval.repository.ImageRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class ImageService {
    @Autowired
    ImageRepository imageRepository;

    public List<Images> getAllImages() {
        List<Images> documents = (List<Images>) imageRepository.findAll();
        if (documents.size() > 0)
            return documents;
        else
            return new ArrayList<Images>();
    }

    public Images getImages(int id) {
        Optional<Images> image = imageRepository.findById(id);
        if (image.isPresent()) {
            return image.get();
        } else {
            return null;
        }
    }

    public List<Images> getImagesByURL(String link) {
        List<Images> image = imageRepository.findByLink(link);
        if (image.size() > 0) {
            return image;
        } else {
            return null;
        }
    }

    public Images addImage(Images images) {
        if (images.getImageId() == null) {
            images = imageRepository.save(images);
            return images;
        } else {
            System.out.println("resource already exists.");
            return null;
        }
    }

    public String deleteImage(Integer id) throws NotFoundException {
        Optional<Images> image = imageRepository.findById(id);

        if (image.isPresent()) {
            imageRepository.deleteById(id);
        } else {
            throw new NotFoundException("No resource record exist for given id");
        }
        return "no such resource.";
    }

    public int count() {
        return (int) imageRepository.count();
    }
}