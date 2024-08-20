package datasource.transaction.service;

import datasource.transaction.model.entity.EventTransaction;
import datasource.transaction.repository.EventTraceRepository;
import datasource.transaction.repository.EventTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventTransactionService {
    private final EventTransactionRepository transactionRepository;
    private final EventTraceRepository traceRepository;

    public EventTransactionService(EventTransactionRepository transactionRepository, EventTraceRepository traceRepository) {
        this.transactionRepository = transactionRepository;
        this.traceRepository = traceRepository;
    }

    public UUID createTransaction() {
        EventTransaction transaction = new EventTransaction();
        return transactionRepository.save(transaction).getId();
    }
}
