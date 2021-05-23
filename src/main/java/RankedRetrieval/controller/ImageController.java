package RankedRetrieval.controller;

import RankedRetrieval.model.Images;
import RankedRetrieval.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ImageController {
    @Autowired
    ImageService imageService;

    @GetMapping(path = "/images")
    public @ResponseBody
    ModelAndView getImage() {

        ModelAndView model = new ModelAndView();
        ArrayList<Images> images = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        for (int i = 0; i < DocumentController.links.size(); i++) {
            List<Images> image = imageService.getImagesByURL(DocumentController.links.get(i));
            if (image != null)
                for (int j = 0; j < image.size(); j++)
                    if (image.get(j).getBody() != null && !titles.contains(image.get(j).getTitle())) {
                        images.add(image.get(j));
                        titles.add(image.get(j).getTitle());
                    }
        }
        model.addObject("images", images);
        model.setViewName("picture");
        return model;
    }

}
