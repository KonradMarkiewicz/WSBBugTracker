package com.wsb.WSBBugTracker.projects;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProjectRepository projectRepository;

    @Test
    @WithMockUser(username = "admin", password = "aA123456", authorities = "ROLE_PROJECTS_TAB")
    public void testAccessToProjectsIndex() throws Exception {
        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "aA123456", authorities = "ROLE_PROJECTS_TAB")
    public void testProjectsContainsText() throws Exception {
        assertTrue(mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Projekty"));
    }

    @Test
    @WithMockUser(username = "admin", password = "aA123456", authorities = {"ROLE_PROJECTS_TAB","ROLE_CREATE_PROJECT"})
    public void testProjectsCreateContainsText() throws Exception {
        assertTrue(mockMvc.perform(get("/projects/create/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Dodaj projekt"));
    }

    @Test
    public void testSaveProject(){
        Project project = new Project();
        projectRepository.save(project);
        verify(projectRepository, times(1)).save(any());
    }

    @Test
    public void testFindProjectById() {
        Project p = new Project();
        p.setId(1L);
        when(projectRepository.getOne(1L)).thenReturn(p);
        Project project = projectRepository.getOne(1L);
        verify(projectRepository).getOne(1L);
        assertEquals(1L, project.getId().longValue());
    }

}

