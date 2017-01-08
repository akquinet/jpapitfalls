package de.akquinet.jpapitfalls.experiment.runtime.dto;

import de.akquinet.jpapitfalls.experiment.model.company.Insurance;
import de.akquinet.jpapitfalls.experiment.model.company.TestData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Michael Bouschen on 31.12.16.
 */
@Stateless
public class CopyDTOToEntityOperations {

    private static Logger LOG = Logger.getLogger(CopyDTOToEntityOperations.class);

    @PersistenceContext
    private EntityManager em;

    public CopyDTOToEntityOperations() {}

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createTestData () {
        TestData.createTestData(em);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public InsuranceDTO selectInsurance() {
        return new InsuranceDTO(TestData.getInsurance(em));
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void copyDTOTheWrongWay(InsuranceDTO dto) {
        Insurance ins = new Insurance(dto.getCarrier());
        ins.setId(dto.getId());
        ins = em.merge(ins);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void copyDTOTheRightWay(InsuranceDTO dto) {
        Insurance ins = em.find(Insurance.class, dto.getId());
        ins.setCarrier(dto.getCarrier());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void printInsurance(Long id, StringBuilder result) {
        Insurance ins = em.find(Insurance.class, id);
        result.append(ins).append("<br>");
        result.append("employee:").append("<br>");
        result.append(ins.getEmployee()).append("<br>");
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void clearTestData() {
        TestData.clearTestData(em);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class InsuranceDTO {

        @Getter
        @Setter
        private Long id;

        @Getter
        @Setter
        private String carrier;

        public InsuranceDTO(Insurance ins) {
            this(ins.getId(), ins.getCarrier());
        }
    }
}