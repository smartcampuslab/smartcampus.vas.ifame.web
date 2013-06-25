package eu.trentorise.smartcampus.vas.ifame.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.vas.ifame.model.GiudizioNew;

@Repository
public interface GiudizioNewRepository extends JpaRepository<GiudizioNew, Long> {

	List<GiudizioNew> getGiudizi(Long mensa_id, Long piatto_id);

	GiudizioNew getUserGiudizio(Long mensa_id, Long piatto_id, Long user_id);

}
