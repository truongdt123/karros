package com.example.demo.repositories;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.domain.GPS;
import com.example.demo.domain.GpxLatestTrack;
import com.example.demo.util.Constants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.*;


@RunWith(SpringRunner.class)
@DataJpaTest
public class GpxRepositoryTest {
	@Autowired
	private GpxRepository gpxRepository;
	private final String content ="<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" version=\"1.1\" creator=\"OruxMaps v.7.1.6 Donate\">\r\n" + 
			"	<metadata>\r\n" + 
			"		<name>Bardenas Reales: Piskerra y el Paso de los Ciervos</name>\r\n" + 
			"		<desc>Este espectacular Parque Natural semidesértico de belleza salvaje fue declarado Reserva de la Biosfera por la UNESCO. Un espectáculo insólito al sureste de Navarra próximo a Tudela, que a pesar de contar con una apariencia desnuda e inhóspita, esconde grandes valores naturales. La erosión de sus suelos arcillosos, yesos y areniscas ha esculpido caprichosas formas creando un mundo de apariencia casi lunar poblado de barrancos, mesetas planas y cerros solitarios. Es por ello por lo que ha servido como fuente de inspiración a pintores y escritores, además de ser escenario de anuncios televisivos, videoclips musicales y películas. Las Bárdenas carecen de núcleos urbanos, su vegetación es muy escasa y las múltiples corrientes de agua que surcan el territorio tienen un caudal marcadamente irregular, permaneciendo secos la mayor parte del año. &#xD;&#xA;El paisaje está marcado por la erosión, la cual crea un paisaje que es uno de sus principales atractivos </desc>\r\n" + 
			"		<author></author>\r\n" + 
			"		<link href=\"http://www.oruxmaps.com\">\r\n" + 
			"			<text>OruxMaps</text>\r\n" + 
			"		</link>\r\n" + 
			"		<time>2017-10-22T09:41:33Z</time>\r\n" + 
			"	</metadata>\r\n" + 
			"";
	private Long id;
	private GPS gps;
	@Before
	public void init() {
		 gps = new GPS();
		gps.setFileName("sample.gpx");
		gps.setName("Bardenas");
		gps.setUserId("testing");
		gps.setGpxContent(content);
		gps.setGpxDate(new Date());
		gps.setAuthor("author");
		gps.setDesc("desc");
		GPS gpsResult = gpxRepository.save(gps);
		id = gpsResult.getId();
	}
	@After
	public void remove() {
		gpxRepository.deleteAll();
	}
	@Test
	public void testFindById() {
	 Optional<GPS> optional = gpxRepository.findById(id);
	 assertTrue(optional.isPresent());
	 assertEquals(gps.getName(), optional.get().getName());
	 assertEquals(gps.getUserId(), optional.get().getUserId());
	 assertEquals(gps.getGpxContent(), optional.get().getGpxContent());
	 assertEquals(gps.getFileName(), optional.get().getFileName());
	 assertEquals(gps.getAuthor(), optional.get().getAuthor());
	 assertEquals(gps.getDesc(), optional.get().getDesc());
	}
	@Test
	public void testFindBy() {
		Pageable pageable = new PageRequest(0, 10, Sort.Direction.DESC, Constants.GPX_DATE);
		Page<GpxLatestTrack> lstTrack = gpxRepository.findBy(pageable);
		assertTrue(lstTrack.getContent().size() > 0);
		GpxLatestTrack gpxLatest = lstTrack.getContent().get(0);
		assertEquals("sample.gpx",gpxLatest.getFileName());
		assertEquals("testing",gpxLatest.getUserId());
		pageable = new PageRequest(1, 10, Sort.Direction.DESC, Constants.GPX_DATE);
		lstTrack = gpxRepository.findBy(pageable);
		assertTrue(lstTrack.getContent().size() == 0);
		
		
		
	}
}
