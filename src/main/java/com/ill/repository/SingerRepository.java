package com.ill.repository;

import com.ill.model.Singer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "singer", path = "singer")
public interface SingerRepository extends JpaRepository<Singer, Long> {
}
