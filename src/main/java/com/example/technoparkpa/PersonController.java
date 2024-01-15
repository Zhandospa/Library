package com.example.technoparkpa;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
public class PersonController {

    @Autowired
    PersonService people;

    @GetMapping("/people")
    public String people(Model model) {
        model.addAttribute("people", people.getPeople());
        return "people";
    }
    @GetMapping("/people/new")
    public String newPerson(Model model) {
        model.addAttribute("person", new Person());
        return "peopleCreate";
    }

    @PostMapping("/people/new")
    public String addPerson(@Valid @ModelAttribute("person") Person person, BindingResult result) {
        if (result.hasErrors()) {
            return "peopleCreate";
        }

        people.addPerson(person);
        return "redirect:/people";
    }

    @GetMapping("/people/{id}")
    public String person(@PathVariable int id, Model model) {
        var per = people.getPerson(id);
        model.addAttribute("person", per);
        return "person";
    }

    @PostMapping("/people/{id}")
    public String delPerson(@PathVariable int id) {
        var p = people.getPerson(id);
        var l = p.getBooks();
        for (Book b : l) {
            b.delPerson();
        }
        people.deletePerson(id);
        return "redirect:/people";
    }

    @GetMapping("/people/{id}/edit")
    public String editPersonForm(@PathVariable int id, Model model) {
        Person person = people.getPerson(id);
        model.addAttribute("person", person);
        return "edPerson";
    }

    @PostMapping("/people/{id}/edit")
    public String subEdPerson(@PathVariable int id,
                              @Valid @ModelAttribute("person") Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edPerson";
        }

        var p = people.getPerson(id);
        p.setAge(person.getAge());
        p.setFullName(person.getFullName());
        people.upDatePerson(p);
        return "redirect:/people";
    }


}
