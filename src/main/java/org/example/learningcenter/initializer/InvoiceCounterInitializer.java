package org.example.learningcenter.initializer;

import lombok.RequiredArgsConstructor;
import org.example.learningcenter.entity.model.InvoiceCounter;
import org.example.learningcenter.repository.InvoiceCounterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceCounterInitializer implements CommandLineRunner {

    final InvoiceCounterRepository counterRepo;

    @Override
    public void run(String ... args) {
        if (counterRepo.findById("A").isEmpty()) {
            InvoiceCounter counter = new InvoiceCounter();
            counter.setSeries("A");
            counter.setLastNumber(0L);
            counterRepo.save(counter);
        }
    }
}