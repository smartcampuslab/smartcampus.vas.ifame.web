package eu.trentorise.smartcampus.vas.ifame.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.vas.ifame.model.Giudizio;

@Repository
public interface GiudizioRepository extends JpaRepository<Giudizio, Long> {

	List<Giudizio> getGiudiziApproved(String mensa_id, Long piatto_id);

	Giudizio getUserGiudizioApproved(String mensa_id, Long piatto_id,
			Long user_id);

	List<Giudizio> getGiudiziAll(String mensa_id, Long piatto_id);

	Giudizio getUserGiudizioAll(String mensa_id, Long piatto_id, Long user_id);

}
