package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.TraineeshipLogBook;
import com.hustle.Traineeship.Management.Application.model.TraineeshipPosition;
import com.hustle.Traineeship.Management.Application.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentsService studentsService;

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

    @GetMapping("/{studentId}/apply")
    public String showTraineeshipSelection(@PathVariable Long studentId, Principal principal, RedirectAttributes redirectAttributes, Model model) {
        studentsService.setTraineeshipId(studentId);
        redirectAttributes.addFlashAttribute("successMessage", "Your traineeship application has been submitted.");
        return "redirect:/student/profile";
    }

    @PostMapping("/apply")
    public String applyForTraineeship(@RequestParam Long positionId, Principal principal, RedirectAttributes redirectAttributes) {
        Student student = studentsService.findByUsername(principal.getName());
        String resultMessage = studentsService.applyForTraineeship(student.getId(), positionId);
        // Add the message to RedirectAttributes
        redirectAttributes.addFlashAttribute("successMessage", resultMessage);
        return "redirect:/student/profile";
    }


    // GET endpoint for displaying the traineeship logbook.
    @GetMapping("/{studentId}/traineeship_logbook")
    public String showTraineeshipLogbook(@PathVariable("studentId") Long studentId, Model model, Principal principal) {
        Student student = studentsService.findStudentById(studentId);
        model.addAttribute("student", student);

        List<TraineeshipLogBook> logbookEntries = studentsService.getTraineeshipLogbook(studentId);
        model.addAttribute("logbook", logbookEntries);

        // Provide an empty log entry object for adding a new entry.
        model.addAttribute("logEntry", new TraineeshipLogBook());

        return "traineeship-logbook";
    }

    // POST endpoint for adding a log entry.
    @PostMapping("/{studentId}/traineeship_logbook")
    public String addLogEntry(@PathVariable("studentId") Long studentId,
                              @ModelAttribute("logEntry") TraineeshipLogBook logEntry,
                              Principal principal) {
        // Optionally, verify that principal corresponds to studentId.
        studentsService.addLogEntry(studentId, logEntry);
        return "redirect:/student/" + studentId + "/traineeship_logbook";
    }

    // GET endpoint to display the edit form for a log entry.
    @GetMapping("/{studentId}/traineeship_logbook/edit/{entryId}")
    public String editLogEntryForm(@PathVariable Long studentId,
                                   @PathVariable Long entryId,
                                   Model model,
                                   Principal principal) {
        // Optionally verify that the current user is allowed to edit this entry.
        Student student = studentsService.findStudentById(studentId);
        TraineeshipLogBook logEntry = studentsService.findLogEntryById(entryId);
        model.addAttribute("student", student);
        model.addAttribute("logEntry", logEntry);
        return "edit-traineeship-logbook";
    }

    // POST endpoint for updating a log entry.
    @PostMapping("/{studentId}/traineeship_logbook/edit/{entryId}")
    public String updateLogEntry(@PathVariable Long studentId,
                                 @PathVariable Long entryId,
                                 @ModelAttribute("logEntry") TraineeshipLogBook updatedEntry,
                                 Principal principal) {
        // Optionally verify that the current user is allowed to update this entry.
        TraineeshipLogBook existingEntry = studentsService.findLogEntryById(entryId);
        existingEntry.setStartDate(updatedEntry.getStartDate());
        existingEntry.setEndDate(updatedEntry.getEndDate());
        existingEntry.setDescription(updatedEntry.getDescription());
        // If you have a dedicated update method in the service, call it here.
        studentsService.updateLogEntry(studentId, existingEntry);
        return "redirect:/student/" + studentId + "/traineeship_logbook";
    }

    // POST endpoint for deleting a log entry.
    @PostMapping("/{studentId}/traineeship_logbook/delete/{entryId}")
    public String deleteLogEntry(@PathVariable Long studentId,
                                 @PathVariable Long entryId,
                                 Principal principal) {
        // Optionally verify that the current user is allowed to delete this entry.
        studentsService.deleteLogEntry(entryId);
        return "redirect:/student/" + studentId + "/traineeship_logbook";
    }
}