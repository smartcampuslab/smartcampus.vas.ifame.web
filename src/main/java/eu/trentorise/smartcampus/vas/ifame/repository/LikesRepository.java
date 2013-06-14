package eu.trentorise.smartcampus.vas.ifame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.vas.ifame.model.table.mapping.Likes;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

}
