package uk.co.dajohnston.accounts.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uk.co.dajohnston.accounts.model.Category;

@RestController
public class CategoryController {

    private Map<String, Category> categories = new HashMap<>();

    @RequestMapping(value = "/categories", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        if (category.getName().trim().isEmpty() || categories.containsKey(category.getName())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        categories.put(category.getName(), category);
        return new ResponseEntity<Category>(category, HttpStatus.OK);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public Collection<Category> getAllCategories() {
        return categories.values();
    }

    @RequestMapping(value = "/categories/{name}", method = RequestMethod.GET)
    public ResponseEntity<Category> getCategoryByName(@PathVariable String name) {
        if (!categories.containsKey(name)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Category>(categories.get(name), HttpStatus.OK);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.DELETE)
    public void deleteAllCategories() {
        categories.clear();
    }

    @RequestMapping(value = "/categories/{name}", method = RequestMethod.DELETE)
    public Collection<Category> deleteCategory(@PathVariable String name) {
        categories.remove(name);

        return getAllCategories();
    }

    @RequestMapping(value = "/categories/{name}", method = RequestMethod.PUT)
    public List<String> setCategoryMatchers(@PathVariable String name, @RequestBody List<String> matchers) {
        Category category = categories.get(name);

        category.setMatchers(matchers);

        return category.getMatchers();
    }
}
