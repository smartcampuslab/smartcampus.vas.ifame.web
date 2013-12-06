package eu.trentorise.smartcampus.vas.ifame.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.vas.ifame.model.PiattoGiorno;

@Repository
public interface PiattoGiornoRepository extends
		JpaRepository<PiattoGiorno, Long> {

	List<PiattoGiorno> getPiattiDelGiorno(int day);

}
