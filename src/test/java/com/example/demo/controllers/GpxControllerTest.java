package com.example.demo.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.repositories.GpxRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
//@ContextConfiguration(classes=H2DataBaseTestConfi.class, loader=AnnotationConfigContextLoader.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@SpringBootTest
public class GpxControllerTest {
	private final String url = "/gpx/api/v1";
    private MockMvc mockMvc;
    private Long id;
    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private GpxController gpxController;

    
   
    @Autowired
	private GpxRepository gpxRepository;
   
    @Before
    public void init() {
    	
        mockMvc = MockMvcBuilders.standaloneSetup(gpxController).build();
        try {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "sample.gpx", "multipart/form-data",
                resourceLoader.getResource("classpath:sample.gpx").getInputStream());
        MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.fileUpload(url+"/storegpx").file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA).param("userId", "12")).andExpect(status().is(HttpStatus.OK.value())).andReturn();
        JSONObject json = new JSONObject(andReturn.getResponse().getContentAsString());
        id = json.getLong("id");
        }catch(Exception ioe) {
        	
        }
        
    }
    @After
    public void remove() {
    	gpxRepository.deleteAll();
    }
    @Test
    public void testStoreGpxOK() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "sample.gpx", "multipart/form-data",
                resourceLoader.getResource("classpath:sample.gpx").getInputStream());
        MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.fileUpload(url+"/storegpx").file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA).param("userId", "123")).andExpect(status().is(HttpStatus.OK.value())).andReturn();
        
        
        
    }

    

    @Test
    public void testStoreGpxWrongFormat() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "sample.gpx", "multipart/form-data",
                resourceLoader.getResource("classpath:sample_wrong_format_file.gpx").getInputStream());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload(url+"/storegpx").file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA).param("userId", "123")).andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void testGetLastTracks() throws Exception {
        mockMvc.perform(get(url+"/lasttracks").param("page", "0").param("size", "10")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Bardenas Reales: Piskerra y el Paso de los Ciervos")))
                .andExpect(jsonPath("$.content[0].id", is(id.intValue())))
                .andExpect(jsonPath("$.content[0].fileName", is("sample.gpx")))
                .andExpect(jsonPath("$.content[0].userId", is("12")))
                .andExpect(jsonPath("$.last", is(true))).andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.totalPages", is(1)));

    }

    @Test
    public void testGetLastTrackEmpty() throws Exception {
        mockMvc.perform(get(url+"/lasttracks").param("page", "1").param("size", "10")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(0))).andExpect(jsonPath("$.last", is(true)))
                .andExpect(jsonPath("$.totalElements", is(1))).andExpect(jsonPath("$.totalPages", is(1)));
    }

    @Test
    public void testGetTrackDetail() throws Exception {
        
        MvcResult andReturn = mockMvc.perform(get(url+"/detail/" + id)).andExpect(status().isOk()).andReturn();
        String expected = new String(
                Files.readAllBytes(resourceLoader.getResource("classpath:sample.gpx").getFile().toPath()));

       
        String content = andReturn.getResponse().getContentAsString();
        assertEquals(expected, content);

    }

    @Test
    public void testGetTrackDetail_NotFound() throws Exception {
        Long id = 123l;
        mockMvc.perform(get(url+"/detail/" + id)).andExpect(status().isNotFound());
    }

}
