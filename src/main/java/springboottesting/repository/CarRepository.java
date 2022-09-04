package springboottesting.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import springboottesting.model.Car;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {

    @Query("""
            select c from Car c
            where ((lower(c.model) like lower(concat('%', :search ,'%'))) or (:search is null))
            or (lower(c.brand) like lower(concat('%', :search ,'%'))) or (:search is null)
            """)
    List<Car> findAll(String search, Sort sort);
}