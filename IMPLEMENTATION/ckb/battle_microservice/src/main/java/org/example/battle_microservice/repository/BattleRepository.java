package org.example.battle_microservice.repository;
import org.example.battle_microservice.model.BattleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BattleRepository extends JpaRepository<BattleModel, Long> {
    Optional<BattleModel> findByName(String name);
    boolean existsByName(@Param("name") String name);
}
