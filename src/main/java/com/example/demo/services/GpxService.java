package com.example.demo.services;

import java.io.InputStream;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.GPS;
import com.example.demo.domain.GpxLatestTrack;
import com.example.demo.gpxdata.GpxType;

public interface GpxService {
	public Optional<GPS> storeGpxFile(InputStream file,String fileName,byte[] bytes,String userId); 
	public Page<GpxLatestTrack> getLstTrack(int page, int size);
	public String getGpxContent(Long id);
	
}
