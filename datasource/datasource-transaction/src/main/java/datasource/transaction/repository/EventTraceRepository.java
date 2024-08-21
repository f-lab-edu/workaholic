package datasource.transaction.repository;

import datasource.transaction.model.entity.EventTrace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTraceRepository extends JpaRepository<EventTrace, Long> {
}
