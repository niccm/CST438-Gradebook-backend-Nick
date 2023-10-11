package com.cst438;

import com.cst438.domain.AssignmentDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.GradeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

/* 
 * Example of using Junit 
 * Mockmvc is used to test a simulated REST call to the RestController
 * This test assumes that students test4@csumb.edu, test@csumb.edu are enrolled in course 
 * with assignment with id=1
 */
@SpringBootTest
@AutoConfigureMockMvc
public class JunitTestGradebook {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private AssignmentGradeRepository assignmentGradeRepository;

	/*
	 * Enter a new grade for student test4@csumb.edu for assignment id=1
	 */
	@Test
	public void gradeAssignment() throws Exception {

		MockHttpServletResponse response;

		// do an http get request for assignment 1 and test4
		response = mvc.perform(MockMvcRequestBuilders.get("/gradebook/1").accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// verify return data with entry for one student without no score
		assertEquals(200, response.getStatus());

		// verify that returned data has non zero primary key
		GradeDTO[] result = fromJsonString(response.getContentAsString(), GradeDTO[].class);
		 
		for (int i=0; i<result.length; i++) {
			GradeDTO g = result[i];
			if (g.email().equals("test4@csumb.edu")) {
				// change grade from null to 80.
				assertNull(g.grade());
				result[i] = new GradeDTO(g.assignmentGradeId(), g.name(), g.email(), 80);
				
			}
		}

		// send updates to server
		response = mvc
				.perform(MockMvcRequestBuilders.put("/gradebook/1").accept(MediaType.APPLICATION_JSON)
						.content(asJsonString(result)).contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// verify that return status = OK (value 200)
		assertEquals(200, response.getStatus());
		// verify that database assignmentGrade table was correctly updated
		AssignmentGrade ag = assignmentGradeRepository.findByAssignmentIdAndStudentEmail(1,  "test4@csumb.edu");
		assertEquals(80, ag.getScore());
		
	}
//	CREATE TESTS ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Test
	public void insertNewAssignment() throws Exception {
		AssignmentDTO adto = new AssignmentDTO(0, "test name", "2024-01-01", null, 31045);
		MockHttpServletResponse response;
		response = mvc.perform(
						MockMvcRequestBuilders
								.post("/assignment")
								.contentType(MediaType.APPLICATION_JSON)
								.content(asJsonString(adto))
								.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals(200, response.getStatus());
		int new_id = Integer.parseInt(response.getContentAsString());
		assertTrue(new_id > 0);

		// now get the assignment
		response = mvc.perform(
						MockMvcRequestBuilders
								.get("/assignment/"+new_id)
								.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals(200, response.getStatus());
		AssignmentDTO adtor = fromJsonString(response.getContentAsString(), AssignmentDTO.class);
		assertEquals(adto.assignmentName(), adtor.assignmentName());
		assertEquals(adto.courseId(), adtor.courseId());
		assertEquals(adto.dueDate(), adtor.dueDate());

		// now delete the assignment
		response = mvc.perform(
						MockMvcRequestBuilders
								.delete("/assignment/"+new_id))
				.andReturn().getResponse();
		assertEquals(200, response.getStatus());

		// delete the assignment again.  Should be silently ignored.
		response = mvc.perform(
						MockMvcRequestBuilders
								.delete("/assignment/"+new_id))
				.andReturn().getResponse();
		assertEquals(200, response.getStatus());

	}

		@Test
		public void createAssignmentInvalidCourse() throws Exception {
			AssignmentDTO adto = new AssignmentDTO(0, "test name", "2024-01-01", null, 31000);
			MockHttpServletResponse response;
			response = mvc.perform(
							MockMvcRequestBuilders
									.post("/assignment")
									.contentType(MediaType.APPLICATION_JSON)
									.content(asJsonString(adto))
									.accept(MediaType.APPLICATION_JSON))
					.andReturn().getResponse();
			assertEquals(400, response.getStatus());
			assertTrue(response.getErrorMessage().contains("course id not found "));

		}

		@Test
		public void createAssignmentNotInstructor() throws Exception {
			AssignmentDTO adto = new AssignmentDTO(0, "test name", "2024-01-01", null, 30291);
			MockHttpServletResponse response;
			response = mvc.perform(
							MockMvcRequestBuilders
									.post("/assignment")
									.contentType(MediaType.APPLICATION_JSON)
									.content(asJsonString(adto))
									.accept(MediaType.APPLICATION_JSON))
					.andReturn().getResponse();
			assertEquals(400, response.getStatus());
			assertTrue(response.getErrorMessage().contains("not authorized"));
		}

//	UPDATE TESTS //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* 
	 * Update existing grade of test@csumb.edu for assignment id=1 from 90 to 88.
	 */
	@Test
	public void updateAssignmentGrade() throws Exception {

		MockHttpServletResponse response;

		// do an http get request for assignment 1
		response = mvc.perform(MockMvcRequestBuilders.get("/gradebook/1").accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// verify return data with entry for one student without no score
		assertEquals(200, response.getStatus());

		// verify that returned data has non zero primary key
		GradeDTO[] result = fromJsonString(response.getContentAsString(), GradeDTO[].class);
		// change grade of student test@csumb.edu from 90 to 88
		for (int i=0; i<result.length; i++) {
			GradeDTO g = result[i];
			if (g.email().equals("test@csumb.edu")) {
				assertEquals(90, g.grade());
				result[i] = new GradeDTO(g.assignmentGradeId(), g.name(), g.email(), 88);
				
			}
		}

		// send updates to server
		response = mvc
				.perform(MockMvcRequestBuilders.put("/gradebook/1").accept(MediaType.APPLICATION_JSON)
						.content(asJsonString(result)).contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// verify that return status = OK (value 200)
		assertEquals(200, response.getStatus());
		
		AssignmentGrade ag = assignmentGradeRepository.findByAssignmentIdAndStudentEmail(1,  "test@csumb.edu");
		assertEquals(88, ag.getScore());


	}

	@Test
	public void updateAssignmentNotFound() throws Exception {
		MockHttpServletResponse response;
		// try to update assignment that does not exist
		AssignmentDTO adto2 = new AssignmentDTO(9999, "test name updated", "2024-02-02", null, 0);
		response = mvc.perform(
						MockMvcRequestBuilders
								.put("/assignment/9999")
								.contentType(MediaType.APPLICATION_JSON)
								.content(asJsonString(adto2))
								.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals(404, response.getStatus());

	}


	@Test
	public void updateAssignmentNotInstructor() throws Exception {
		MockHttpServletResponse response;
		// try to update assignment that belongs to another instructor
		AssignmentDTO adto2 = new AssignmentDTO(3, "test name updated", "2024-02-02", null, 0);
		response = mvc.perform(
						MockMvcRequestBuilders
								.put("/assignment/3")
								.contentType(MediaType.APPLICATION_JSON)
								.content(asJsonString(adto2))
								.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals(404, response.getStatus());
	}

//	DELETE TESTS //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	public void deleteAssignmentWithGrades() throws Exception {
		MockHttpServletResponse response;
		// try to delete assignment that has grades.  should fail.
		response = mvc.perform(
						MockMvcRequestBuilders
								.delete("/assignment/1"))
				.andReturn().getResponse();
		assertEquals(400, response.getStatus());
		assertTrue(response.getErrorMessage().contains("has grades"));

		// now delete using force=yes.  should be good.
		response = mvc.perform(
						MockMvcRequestBuilders
								.delete("/assignment/1?force=yes"))
				.andReturn().getResponse();
		assertEquals(200, response.getStatus());
	}

	@Test
	public void deleteAssignmentNotInstructor() throws Exception {
		MockHttpServletResponse response;
		// try to delete assignment that belongs to another instructor
		response = mvc.perform(
						MockMvcRequestBuilders
								.delete("/assignment/3"))
				.andReturn().getResponse();
		assertEquals(403, response.getStatus());

	}


	////////////////////////////////////////////////////////
	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T fromJsonString(String str, Class<T> valueType) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
