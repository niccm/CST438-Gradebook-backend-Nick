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
		int courseId = userAssignment.courseId();
		Course c = courseRepository.findById(userAssignment.courseId()).orElse(null);
		if( c.getInstructor() == "jgross@csumb.edu" || c.getInstructor() == "dwisneski@csumb.edu" || c.getInstructor() =="sislam@csumb.edu"){
			Assignment newAssignment = new Assignment();
			newAssignment.setName(userAssignment.assignmentName());
			newAssignment.setDueDate(Date.valueOf(userAssignment.dueDate()));
			newAssignment.setCourse(courseRepository.findById(courseId).get());

			assignmentRepository.save(newAssignment);
			return newAssignment.getId();
		}
		return 0;
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
	@PutMapping("/update")
	public void update(AssignmentDTO DTO){
		Assignment tempUpdate = new Assignment();
		tempUpdate.setName(DTO.assignmentName());
		tempUpdate.setId(DTO.id());
		tempUpdate.setDueDate(Date.valueOf(DTO.dueDate()));
		int id = DTO.courseId();
		Course c = courseRepository.findById(DTO.courseId()).orElse(null);
		tempUpdate.setCourse(courseRepository.findById(id).get());

		assignmentRepository.save(tempUpdate);


	}

//	Delete
	@DeleteMapping("/delete")
	public void delete(AssignmentDTO DTO){
		Assignment tempDelete = new Assignment();
		tempDelete.setName(DTO.assignmentName());
		tempDelete.setId(DTO.id());
		tempDelete.setDueDate(Date.valueOf(DTO.dueDate()));
		int id = DTO.courseId();
		Course c = courseRepository.findById(DTO.courseId()).orElse(null);
		tempDelete.setCourse(courseRepository.findById(id).get());
		assignmentRepository.delete(tempDelete);
	}


}
