package com.cst438.controllers;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentDTO;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

@RestController
@CrossOrigin
public class AssignmentController {

	@Autowired
	AssignmentRepository assignmentRepository;

	@Autowired
	CourseRepository courseRepository;

	@GetMapping("/assignment")
	public AssignmentDTO[] getAllAssignmentsForInstructor() {
		// get all assignments for this instructor
		String instructorEmail = "dwisneski@csumb.edu";  // user name (should be instructor's email)
		List<Assignment> assignments = assignmentRepository.findByEmail(instructorEmail);
		AssignmentDTO[] result = new AssignmentDTO[assignments.size()];
		for (int i=0; i<assignments.size(); i++) {
			Assignment as = assignments.get(i);
			AssignmentDTO dto = new AssignmentDTO(
					as.getId(),
					as.getName(),
					as.getDueDate().toString(),
					as.getCourse().getTitle(),
					as.getCourse().getCourse_id());
			result[i]=dto;
		}
		return result;
	}

	// TODO create CRUD methods for Assignment

//	Create
	@PostMapping("/assignment")
	public int insertNewAssignment(@RequestBody AssignmentDTO userAssignment){
		String instructorEmail = "dwisneski@csumb.edu";
		Course c = courseRepository.findById(userAssignment.courseId()).orElse(null);

		if (c==null || ! c.getInstructor().equals(instructorEmail)) {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "course id not found or not authorized "+ userAssignment.courseId());
		}


			Assignment newAssignment = new Assignment();
			newAssignment.setName(userAssignment.assignmentName());
			newAssignment.setDueDate(Date.valueOf(userAssignment.dueDate()));
			newAssignment.setCourse(c);


			assignmentRepository.save(newAssignment);
			return newAssignment.getId();


	}


//	Retrieve
//	@GetMapping("/result/{alias}")
//	public MultiplyResult[] getLastNresults(
//			@PathVariable("alias") String alias,
//			@RequestParam("lastN") Optional<Integer> lastN) {
//		int n = lastN.orElse(5);
//		return history.getHistory(alias, n);
//	}
	@GetMapping("/assignment/{alias}")
	public AssignmentDTO getAssignmentByID(@PathVariable("alias") int alias){
		Optional<Assignment> newAssignment = assignmentRepository.findById(alias);
//		System.out.println(assignmentRepository.findById(alias));
//		System.out.println(newAssignment);
		Assignment returned = newAssignment.get();
//		System.out.println(returned);
		AssignmentDTO yo = new AssignmentDTO(returned.getId(), returned.getName(), returned.getDueDate().toString(), returned.getCourse().getTitle(), returned.getCourse().getCourse_id());

		return yo;
	}


//	Update
@PutMapping("/assignment/{id}")
public void updateAssignment(@PathVariable("id") int id, @RequestBody AssignmentDTO DTO) {
		String instructorEmail = "dwisneski@csumb.edu";  // user name (should be instructor's email)
		Assignment a = assignmentRepository.findById(id).orElse(null);
		if (a==null || ! a.getCourse().getInstructor().equals(instructorEmail)) {
			throw  new ResponseStatusException( HttpStatus.NOT_FOUND, "assignment not found or not authorized "+id);
		}
		Assignment temp = assignmentRepository.findById(DTO.id()).get();
		temp.setName(DTO.assignmentName());
		temp.setCourse(courseRepository.findById(DTO.courseId()).get());
		temp.setDueDate(Date.valueOf(DTO.dueDate()));

		assignmentRepository.save(temp);
	}

//	Delete
@DeleteMapping("/assignment/{id}")
	public void deleteAssignment(@PathVariable("id") int id, @RequestParam("force") Optional<String> force) {
	String instructorEmail = "dwisneski@csumb.edu";
	Assignment tempA = assignmentRepository.findById(id).orElse(null);

	if (tempA == null) {
		return;
	}
	if (!tempA.getCourse().getInstructor().equals(instructorEmail)) {
		System.out.println(tempA.getCourse().getInstructor());
	    	throw  new ResponseStatusException( HttpStatus.FORBIDDEN, "not authorized "+id);
	    }
	if (tempA.getAssignmentGrades().size()==0 || force.isPresent()) {
	    	assignmentRepository.deleteById(id);
	    } else {
	    	throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "assignment has grades ");
	    }



	}

}
