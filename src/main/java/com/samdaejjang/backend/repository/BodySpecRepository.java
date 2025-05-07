package com.samdaejjang.backend.repository;

import com.samdaejjang.backend.entity.BodySpec;
import com.samdaejjang.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BodySpecRepository extends JpaRepository<BodySpec, Long> {

    BodySpec save(BodySpec bodySpec);
}
