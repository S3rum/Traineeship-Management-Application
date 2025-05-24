package com.hustle.Traineeship.Management.Application.controllers;

import com.hustle.Traineeship.Management.Application.model.Student;
import com.hustle.Traineeship.Management.Application.model.TraineeshipLogBook;
import com.hustle.Traineeship.Management.Application.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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

    @GetMapping("/create_profile")
    public String showProfileForm(Model model, Principal principal) {
        Student student = studentsService.findByUsername(principal.getName());
        model.addAttribute("student", student);
        return "student-profile-creation";
    }


    @PostMapping("/create_profile")
    public String updateProfile(@ModelAttribute("student") Student student, Principal principal) {
        studentsService.updateStudentProfile(student, principal.getName());
        return "redirect:/student/profile";
    }


    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        Student student = studentsService.findByUsername(principal.getName());
        model.addAttribute("student", student);
        return "student-profile";
    }

    @GetMapping("/{studentId}/apply")
    public String showTraineeshipSelection(@PathVariable Long studentId, Principal principal, RedirectAttributes redirectAttributes, Model model) {
        Student authenticatedStudent = studentsService.findByUsername(principal.getName());
        if (!authenticatedStudent.getId().equals(studentId)) {
            throw new AccessDeniedException("You are not authorized to perform this action for the given student ID.");
        }
        studentsService.setTraineeshipId(studentId);
        redirectAttributes.addFlashAttribute("successMessage", "Your traineeship application has been submitted.");
        return "redirect:/student/profile";
    }

    @PostMapping("/apply")
    public String applyForTraineeship(@RequestParam Long positionId, Principal principal, RedirectAttributes redirectAttributes) {
        Student student = studentsService.findByUsername(principal.getName());
        String resultMessage = studentsService.applyForTraineeship(student.getId(), positionId);
        redirectAttributes.addFlashAttribute("successMessage", resultMessage);
        return "redirect:/student/profile";
    }



    @GetMapping("/{studentId}/traineeship_logbook")
    public String showTraineeshipLogbook(@PathVariable("studentId") Long studentId, Model model, Principal principal) {
        Student authenticatedStudent = studentsService.findByUsername(principal.getName());
        if (!authenticatedStudent.getId().equals(studentId)) {
            throw new AccessDeniedException("You are not authorized to view this logbook.");
        }
        Student student = studentsService.findStudentById(studentId);
        model.addAttribute("student", student);

        List<TraineeshipLogBook> logbookEntries = studentsService.getTraineeshipLogbook(studentId);
        model.addAttribute("logbook", logbookEntries);

        model.addAttribute("logEntry", new TraineeshipLogBook());

        return "traineeship-logbook";
    }


    @PostMapping("/{studentId}/traineeship_logbook")
    public String addLogEntry(@PathVariable("studentId") Long studentId,
                              @ModelAttribute("logEntry") TraineeshipLogBook logEntry,
                              Principal principal) {
        Student authenticatedStudent = studentsService.findByUsername(principal.getName());
        if (!authenticatedStudent.getId().equals(studentId)) {
            throw new AccessDeniedException("You are not authorized to add a log entry for this student.");
        }
        studentsService.addLogEntry(studentId, logEntry);
        return "redirect:/student/" + studentId + "/traineeship_logbook";
    }


    @GetMapping("/{studentId}/traineeship_logbook/edit/{entryId}")
    public String editLogEntryForm(@PathVariable Long studentId,
                                   @PathVariable Long entryId,
                                   Model model,
                                   Principal principal) {
        Student authenticatedStudent = studentsService.findByUsername(principal.getName());
        if (!authenticatedStudent.getId().equals(studentId)) {
            throw new AccessDeniedException("You are not authorized to edit this log entry.");
        }
        Student student = studentsService.findStudentById(studentId);
        TraineeshipLogBook logEntry = studentsService.findLogEntryById(entryId);
        model.addAttribute("student", student);
        model.addAttribute("logEntry", logEntry);
        return "edit-traineeship-logbook";
    }


    @PostMapping("/{studentId}/traineeship_logbook/edit/{entryId}")
    public String updateLogEntry(@PathVariable Long studentId,
                                 @PathVariable Long entryId,
                                 @ModelAttribute("logEntry") TraineeshipLogBook updatedEntry,
                                 Principal principal) {
        Student authenticatedStudent = studentsService.findByUsername(principal.getName());
        if (!authenticatedStudent.getId().equals(studentId)) {
            throw new AccessDeniedException("You are not authorized to update this log entry.");
        }

        TraineeshipLogBook existingEntry = studentsService.findLogEntryById(entryId);
        existingEntry.setStartDate(updatedEntry.getStartDate());
        existingEntry.setEndDate(updatedEntry.getEndDate());
        existingEntry.setDescription(updatedEntry.getDescription());

        studentsService.updateLogEntry(studentId, existingEntry);
        return "redirect:/student/" + studentId + "/traineeship_logbook";
    }


    @PostMapping("/{studentId}/traineeship_logbook/delete/{entryId}")
    public String deleteLogEntry(@PathVariable Long studentId,
                                 @PathVariable Long entryId,
                                 Principal principal) {
        Student authenticatedStudent = studentsService.findByUsername(principal.getName());
        if (!authenticatedStudent.getId().equals(studentId)) {
            throw new AccessDeniedException("You are not authorized to delete this log entry.");
        }
        studentsService.deleteLogEntry(entryId);
        return "redirect:/student/" + studentId + "/traineeship_logbook";
    }
}