package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.TraineeshipLogBook;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.repos.TraineeshipPositionRepository;
import com.hustle.Traineeship.Management.Application.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentsService studentsService;

    @Autowired
    private TraineeshipPositionRepository traineeshipPositionRepository;

    // Display the student's profile creation form.
    @GetMapping("/create_profile")
    public String showProfileForm(Model model, Principal principal) {
        Student student = studentsService.findByUsername(principal.getName());
        model.addAttribute("student", student);
        return "student-profile-creation";  // Corresponding template: student-profile-creation.html
    }

    // Process profile updates.
    @PostMapping("/create_profile")
    public String updateProfile(@ModelAttribute("student") Student student, Principal principal) {
        studentsService.updateStudentProfile(student, principal.getName());
        return "redirect:/student/profile";
    }

    // Display the student's profile.
    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        Student student = studentsService.findByUsername(principal.getName());
        model.addAttribute("student", student);
        return "student-profile"; // Corresponding template: student-profile.html
    }

    // GET endpoint for displaying the traineeship selection page.
    @GetMapping("/{studentId}/apply")
    public String showTraineeshipSelection(@PathVariable Long studentId, Model model) {
        Student student = studentsService.findStudentById(studentId);
        model.addAttribute("student", student);

        List<TraineeshipPosition> availablePositions = studentsService.getAvailableTraineeshipPositions();
        model.addAttribute("positions", availablePositions);

        return "traineeship-selection"; // Create traineeship-selection.html accordingly.
    }

    // POST endpoint for applying for a traineeship.
    // Remove the studentId from the path and use Principal
    @PostMapping("/apply")
    public String applyForTraineeship(@RequestParam Long positionId, Principal principal) {
        // Retrieve the student by username from Principal
        Student student = studentsService.findByUsername(principal.getName());
        // Apply using the student's id
        studentsService.applyForTraineeship(student.getId(), positionId);
        return "redirect:/student/profile";
    }


    // GET endpoint for displaying the traineeship logbook.
    @GetMapping("/{studentId}/traineeship_logbook")
    public String showTraineeshipLogbook(@PathVariable("studentId") Long studentId, Model model) {
        Student student = studentsService.findStudentById(studentId);
        model.addAttribute("student", student);

        // Retrieve the logbook entries for the student.
        List<TraineeshipLogBook> logbookEntries = studentsService.getTraineeshipLogbook(studentId);
        model.addAttribute("logbook", logbookEntries);

        // Provide an empty log entry object for the form.
        model.addAttribute("logEntry", new TraineeshipLogBook());

        return "traineeship-logbook";  // This corresponds to traineeship-logbook.html
    }

    // POST endpoint for adding a log entry.
    @PostMapping("/{studentId}/traineeship_logbook")
    public String addLogEntry(@PathVariable("studentId") Long studentId,
                              @ModelAttribute("logEntry") TraineeshipLogBook logEntry) {
        studentsService.addLogEntry(studentId, logEntry);
        return "redirect:/student/" + studentId + "/traineeship_logbook";
    }

    @GetMapping("/{studentId}/traineeship_logbook/edit/{entryId}")
    public String editLogEntry(@PathVariable Long studentId, @PathVariable Long entryId, Model model) {
        // Retrieve the log entry by entryId, add it to the model, and return an edit view.
        TraineeshipLogBook entry = studentsService.findLogEntryById(entryId);
        model.addAttribute("logEntry", entry);
        model.addAttribute("studentId", studentId);
        return "edit-traineeship-logbook"; // Create this template accordingly.
    }

    @GetMapping("/{studentId}/traineeship_logbook/delete/{entryId}")
    public String deleteLogEntry(@PathVariable Long studentId, @PathVariable Long entryId) {
        studentsService.deleteLogEntry(entryId);
        return "redirect:/student/" + studentId + "/traineeship_logbook";
    }
}
