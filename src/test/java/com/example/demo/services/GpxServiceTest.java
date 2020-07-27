 package com.example.demo.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Optional;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.example.demo.domain.GPS;
import com.example.demo.domain.GpxLatestTrack;
import com.example.demo.gpxdata.GpxType;
import com.example.demo.repositories.GpxRepository;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@SpringBootTest
public class GpxServiceTest {

    @Autowired
    GpxService gpxService;
    Long id;
    @Autowired
    GpxRepository gpxRepository;
    ClassLoader classLoader = getClass().getClassLoader();
    @Before
    public void init () {
    	try {
    	 File file = new File(classLoader.getResource("sample.gpx").getFile());
         byte[] bFile = Files.readAllBytes(file.toPath());
        
         Optional<GPS> optional = gpxService.storeGpxFile(new FileInputStream(file),"sample.gpx",bFile,"123");
         id = optional.get().getId();
    	 }catch(IOException ioe) {
         	assertTrue(false);
         }
    }
    @After
    public void remove() {
    	 gpxRepository.deleteAll();
    }
    @Test
    public void testStoreGpxFile()  {
    	 try {
        File file = new File(classLoader.getResource("sample.gpx").getFile());
        byte[] bFile = Files.readAllBytes(file.toPath());
       
        Optional<GPS> optional = gpxService.storeGpxFile(new FileInputStream(file),"sample.gpx",bFile,"123");
        assertTrue(optional.isPresent());
        //gpxRepository.delete(optional.get().getId());
        
        }catch(IOException ioe) {
        	assertTrue(false);
        }
       
    }
    @Test
    public void testGetDetail() {
    	try {
    	File file = new File(classLoader.getResource("sample.gpx").getFile());
        //byte[] bFile = Files.readAllBytes(file.toPath());
       
        //Optional<GPS> optional = gpxService.storeGpxFile(new FileInputStream(file),"sample.gpx",bFile,"123");
    	String content = gpxService.getGpxContent(id);
    	
    	String expected = new String(
                Files.readAllBytes(file.toPath()));
    	assertEquals(expected, content);
    	 //gpxRepository.delete(optional.get().getId());
    	}catch(IOException ioe) {
    		
    	}
    }
    @Test
    public void testGetLatestTrack() {
    	
        Page<GpxLatestTrack> lstTrack = gpxService.getLstTrack(0, 10);
    	assertTrue(lstTrack.getTotalElements() > 0);
    	
    }
    @Test
    public void testStoreFileWrongFormat() {
    	try {
    	File file = new File(classLoader.getResource("sample_wrong_format_file.gpx").getFile());
        byte[] bFile = Files.readAllBytes(file.toPath());
        
        	  Optional<GPS> optional = gpxService.storeGpxFile(new FileInputStream(file),"sample.gpx",bFile,"123");
              assertTrue(!optional.isPresent());
            }catch(IOException ioe) {
            	assertTrue(true);
            }
    }
    
}
