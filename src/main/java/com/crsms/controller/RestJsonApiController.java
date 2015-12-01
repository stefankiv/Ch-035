package com.crsms.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crsms.domain.Area;
import com.crsms.domain.Course;
import com.crsms.domain.Group;
import com.crsms.domain.Module;
import com.crsms.domain.Resource;
import com.crsms.domain.Test;
import com.crsms.dto.AreaJsonDto;
import com.crsms.dto.CourseJsonDto;
import com.crsms.dto.GroupNameJsonDto;
import com.crsms.dto.ModuleJsonDto;
import com.crsms.dto.ResourceJsonDto;
import com.crsms.dto.TestJsonDto;
import com.crsms.dto.UserIdAndEmailDto;
import com.crsms.dto.VacancyJsonDto;
import com.crsms.service.AreaService;
import com.crsms.service.CourseService;
import com.crsms.service.DtoService;
import com.crsms.service.GroupService;
import com.crsms.service.ModuleService;
import com.crsms.service.ResourceService;
import com.crsms.service.TestService;
import com.crsms.service.VacancyService;

/**
 * 
 * @author Valerii Motresku
 *
 */

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestJsonApiController {
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private DtoService dtoService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private TestService testService;

	@Autowired
	private VacancyService vacancyService;
	
	@RequestMapping(value = {"/areas"}, method = RequestMethod.GET)
	public List<AreaJsonDto> getAreas(HttpServletResponse response) {
		List<AreaJsonDto> dtos = dtoService.convert(areaService.getAll(),
													AreaJsonDto.class, Area.class);
		response.addIntHeader("X-Total-Count", dtos.size());
		return dtos;
	}
	
	@RequestMapping(value = {"/areas/{areaId}"}, method = RequestMethod.GET)
	public AreaJsonDto getArea(@PathVariable Long areaId) {
		return dtoService.convert(areaService.getById(areaId), AreaJsonDto.class, Area.class);
	}
	
	@RequestMapping(value = {"/courses"}, method = RequestMethod.GET)
	public List<CourseJsonDto> getCourses() {
		return dtoService.convert(courseService.getAll(), CourseJsonDto.class, Course.class);
	}
	
	@RequestMapping(value = {"/courses/{courseId}"}, method = RequestMethod.GET)
	public CourseJsonDto getCourse(@PathVariable Long courseId) {
		return dtoService.convert(courseService.getById(courseId),
									CourseJsonDto.class, Course.class);
	}
	
	@RequestMapping(value = {"/courses/{courseId}/modules"}, method = RequestMethod.GET)
	public List<ModuleJsonDto> getModules(@PathVariable Long courseId) {
		return dtoService.convert(moduleService.getAllByCourseId(courseId),
									ModuleJsonDto.class, Module.class);
	}
	
	@RequestMapping(value = {"/modules/{moduleId}",
							 "/courses/{courseId}/modules/{moduleId}"}, 
					method = RequestMethod.GET)
	public ModuleJsonDto getModule(@PathVariable Long moduleId) {
		return dtoService.convert(moduleService.getById(moduleId),
									ModuleJsonDto.class, Module.class);
	}
	
	@RequestMapping(value = {"/modules/{moduleId}/tests",
							 "/courses/{courseId}/modules/{moduleId}/tests"}, 
					method = RequestMethod.GET)
	public List<TestJsonDto> getTests(@PathVariable Long moduleId) {
		return dtoService.convert(testService.getAllByModuleId(moduleId),
									TestJsonDto.class, Test.class);
	}
	
	@RequestMapping(value = {"/tests/{testId}",
							 "/courses/{courseId}/modules/{moduleId}/tests/{testId}"},
					method = RequestMethod.GET)
	public TestJsonDto getTest(@PathVariable Long testId) {
		return dtoService.convert(testService.getById(testId), TestJsonDto.class, Test.class);
	}
	
	@RequestMapping(value = "/tests/{testId}/delete", method = RequestMethod.GET)
	public Boolean deleteTestById(@PathVariable Long testId) {
		testService.deleteTestById(testId);
		return true;
	}	
	
	@RequestMapping(value = {"/modules/{moduleId}/resources", 
							 "/courses/{courseId}/modules/{moduleId}/resources"}, 
					method = RequestMethod.GET)
	public List<ResourceJsonDto> getResources(@PathVariable Long moduleId) {
		return dtoService.convert(resourceService.getAllByModuleId(moduleId),
									ResourceJsonDto.class, Resource.class);
	}
	
	@RequestMapping(value = {"/resources/{resourceId}", 
							 "/courses/{courseId}/modules/{moduleId}/resources/{resourceId}"}, 
					method = RequestMethod.GET)
	public ResourceJsonDto getResource(@PathVariable Long resourceId) {
		return dtoService.convert(resourceService.getById(resourceId),
									ResourceJsonDto.class, Resource.class);
	}
	
	@RequestMapping(value = {"/vacancies"}, method = RequestMethod.GET)
	public List<VacancyJsonDto> getVacancies() throws IOException {
	  return vacancyService.getAllVacancies();
	}
	
	@RequestMapping(value = {"/groups"}, method = RequestMethod.GET)
	public List<GroupNameJsonDto> getAllGroups() {
		return dtoService.convert(groupService.getAll(), GroupNameJsonDto.class, Group.class);
	}
	
	@RequestMapping(value = {"/courses/{courseId}/groups"}, method = RequestMethod.GET)
	public List<GroupNameJsonDto> getAllGroupsByCourseId(@PathVariable Long courseId) {
		return dtoService.convert(groupService.getAllByCourseId(courseId),
									GroupNameJsonDto.class, Group.class);
	}
	
	@RequestMapping(value = {"/groups/{groupId}/students"}, method = RequestMethod.GET)
	public List<UserIdAndEmailDto> getStudentsFromGroup(@PathVariable Long groupId) {
		return groupService.getStudentsFromGroup(groupId);
	}
}