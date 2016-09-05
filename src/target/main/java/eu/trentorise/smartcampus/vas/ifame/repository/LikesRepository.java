package eu.trentorise.smartcampus.vas.ifame.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.vas.ifame.model.Likes;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

	List<Likes> getGiudizioLikes(Long giudizio_id);

	Likes alreadyLiked(Long giudizio_id, Long user_id);

}
