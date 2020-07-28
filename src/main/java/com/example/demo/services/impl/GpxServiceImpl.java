package com.example.demo.services.impl;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.domain.GPS;
import com.example.demo.domain.GpxLatestTrack;
import com.example.demo.gpxdata.GpxType;
import com.example.demo.gpxdata.MetadataType;
import com.example.demo.repositories.GpxRepository;
import com.example.demo.services.GpxService;
import com.example.demo.util.Constants;

@Service
public class GpxServiceImpl implements GpxService {
	@Autowired
	private Unmarshaller unmarshaller;
	@Autowired
	private GpxRepository gpxRepository;
	private static final Logger logger = LoggerFactory.getLogger(GpxServiceImpl.class);
	/**
	 * Validate and save a Gpx file to Database.
	 * @param inputStream : Stream of Gpx file 
	 * @param fileName : Name of Gpx file
	 * @param bytes : An array binary of Gpx file
	 * @param userId : Identity of User.
	 * @return The GPS object
	 */
	public Optional<GPS> storeGpxFile(InputStream inputStream,String fileName,byte[] bytes, String userId)  {
		GPS gps = null;
		try {
			
			JAXBElement<GpxType> rootNode = (JAXBElement<GpxType>) unmarshaller.unmarshal(inputStream);
			GpxType gpxData = rootNode.getValue();
			gps = new GPS();
			gps.setUserId(userId);
			MetadataType metadata = gpxData.getMetadata();
			gps.setName(metadata.getName());
			gps.setDesc(metadata.getDesc());
			gps.setGpxDate(metadata.getTime().toGregorianCalendar().getTime());
			gps.setFileName(fileName);
			gps.setGpxContent(new String(bytes, StandardCharsets.UTF_8) );
			gps = save(gps);
		} catch (JAXBException e) {
            logger.error("Cannot store XML", e.getMessage());
            gps = null;
            
        }
		return Optional.ofNullable(gps);
	}

	/**
	 * Store Gps object to database.
	 * @param gps : GPS object
	 * @return the GPS object contains an Identity generate from database.
	 */
	private GPS save(GPS gps) {
		return gpxRepository.save(gps);

	}
	/**
	 * Get Content of GPx file
	 * @param id : Identity of GPX file in database
	 * @return Content of GPX file.
	 */
	public String getGpxContent(Long id) {
		Optional<GPS> optional = gpxRepository.findById(id);
		String content = "";
		if (optional.isPresent()) {
			content = optional.get().getGpxContent();
		}
		return content;

	}
	/**
	 * Get a list Gpx files with latest tracking from database. 
	 * @param page : number of pages
	 * @param size : number elements in a page.
	 * @return a list elements in above number of pages.
	 */
	@Override
	public Page<GpxLatestTrack> getLstTrack(int page, int size) {

		Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, Constants.GPX_DATE);
		return gpxRepository.findBy(pageable);
	}
}
