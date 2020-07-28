package com.example.demo.controllers;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.GPS;
import com.example.demo.domain.GpxLatestTrack;
import com.example.demo.domain.GpxResponse;
import com.example.demo.services.GpxService;
import com.example.demo.services.impl.GpxServiceImpl;

@Controller
@CrossOrigin
@RequestMapping(value = "/gpx/api/v1")
public class GpxController {
	private final String STORE_GPX_SUCCESS = "Save GPX successfully.";
	private final String STORE_GPX_FAILED = "The Gpx file is invalid.";
	private static final Logger logger = LoggerFactory.getLogger(GpxServiceImpl.class);
	@Autowired
	private GpxService gpxService;

	/**
	 * API for upload a Gpx file.
	 * 
	 * @param file   : Gpx file
	 * @param userId : identity of User
	 * @return ResponseEntity is include the identity of GpxFile.
	 */
	@RequestMapping(path = "/storegpx", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<GpxResponse> storeGpx(@RequestPart("file") MultipartFile file,
			@RequestParam("userId") String userId) {
		GpxResponse gpxRes = new GpxResponse();
		gpxRes.setUserId(userId);
		try {
			Optional<GPS> optional = gpxService.storeGpxFile(file.getInputStream(), file.getOriginalFilename(),
					file.getBytes(), userId);

			if (optional.isPresent()) {

				GPS gps = optional.get();

				gpxRes.setFileName(gps.getFileName());
				gpxRes.setId(gps.getId());
				gpxRes.setName(gps.getName());
				gpxRes.setMessage(STORE_GPX_SUCCESS);
				return new ResponseEntity(gpxRes, HttpStatus.OK);
			}
		} catch (IOException ioe) {
			logger.error(ioe.getMessage());
			
		}
		gpxRes.setMessage(STORE_GPX_FAILED);
		return new ResponseEntity(gpxRes, HttpStatus.BAD_REQUEST);

	}
    /**
     * API for get detail a Gpx file from Identity
     * @param id: Identity of GpxFile
     * @return Gpx file content.
     */
	@GetMapping(value = "/detail/{id}", produces = "application/xml")
	public ResponseEntity<String> detailGpx(@PathVariable Long id) {
		String content = gpxService.getGpxContent(id);
		if (!content.isEmpty()) {
			return new ResponseEntity(content, HttpStatus.OK);

		} else {
			return new ResponseEntity(null, HttpStatus.NOT_FOUND);
		}

	}
	/**
	 * API for get a list Gpx files with latest tracking.
	 * @param page: number of pages  
	 * @param size: number of elements in a page
	 * @return a list element in above number of pages.
	 */
	@GetMapping(value = "/lasttracks", params = { "page", "size" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Page<GpxLatestTrack>> lastTrack(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {

		Page<GpxLatestTrack> lstLatestTrack = gpxService.getLstTrack(page, size);
		return new ResponseEntity(lstLatestTrack, HttpStatus.OK);
	}
}
