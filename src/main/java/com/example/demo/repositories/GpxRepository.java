package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import com.example.demo.domain.GPS;
import com.example.demo.domain.GpxLatestTrack;

public interface GpxRepository extends JpaRepository<GPS, Long>,JpaSpecificationExecutor<GPS>  {
	Optional<GPS> findById(Long id);
	public Page<GpxLatestTrack> findBy(Pageable page);
}
