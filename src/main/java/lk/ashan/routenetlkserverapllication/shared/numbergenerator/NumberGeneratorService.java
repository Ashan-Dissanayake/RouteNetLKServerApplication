package lk.ashan.routenetlkserverapllication.shared.numbergenerator;

import lk.ashan.routenetlkserverapllication.shared.numbergenerator.model.CodeType;
import lk.ashan.routenetlkserverapllication.shared.numbergenerator.model.DocSequence;
import lk.ashan.routenetlkserverapllication.shared.numbergenerator.model.Scope;
import lk.ashan.routenetlkserverapllication.shared.numbergenerator.repository.CodeTypeRepository;
import lk.ashan.routenetlkserverapllication.shared.numbergenerator.repository.DocSequenceRepository;
import lk.ashan.routenetlkserverapllication.shared.numbergenerator.repository.ScopeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class NumberGeneratorService {

    private final DocSequenceRepository sequenceRepository;
    private final CodeTypeRepository codeTypeRepository;
    private final ScopeRepository scopeRepository;
    private final DocumentNumberFormatter formatter;


    @Transactional
    public String nextBranchNumber(String branchName) {
        Integer next = nextGlobalSequenceValue("BRANCH", "GLOBAL");
        return formatter.branch(next,branchName);
    }

    @Transactional
    public String nextEmployeeNumber() {
        Integer next = nextGlobalSequenceValue("EMPLOYEE", "GLOBAL");
        return formatter.employee(next);
    }

    @Transactional
    public String nextDriverNumber() {
        Integer next = nextGlobalSequenceValue("DRIVER", "GLOBAL");
        return formatter.driver(next);
    }

    @Transactional
    public String nextConductorNumber() {
        Integer next = nextGlobalSequenceValue("CONDUCTOR", "GLOBAL");
        return formatter.conductor(next);
    }

    private Integer nextGlobalSequenceValue(String codeTypeName, String scopeName) {
        CodeType codeType = codeTypeRepository.findByName(codeTypeName)
                .orElseThrow(() -> new RuntimeException("CodeType not found: " + codeTypeName));
        Scope scope = scopeRepository.findByName(scopeName)
                .orElseThrow(() -> new RuntimeException("Scope not found: " + scopeName));

        DocSequence seq = sequenceRepository.findForUpdate(codeType.getId(), scope.getId(), null)
                .orElseGet(() -> {
                    DocSequence s = new DocSequence();
                    s.setCodetype(codeType);
                    s.setScope(scope);
                    s.setLastvalue(0);
                    return s;
                });

        Integer next = seq.nextValue();
        sequenceRepository.save(seq);
        return next;
    }

    // Similarly for branch-period sequences:
    @Transactional
    public String nextPartRequestNumber(String scopeName, YearMonth ym) {
        Integer next = nextBranchPeriodSequenceValue("PART_REQUEST", scopeName, ym);
        return formatter.partRequest(scopeName, ym, next);
    }

    @Transactional
    public String nextGrnNumber(String scopeName, YearMonth ym) {
        Integer next = nextBranchPeriodSequenceValue("GRN", scopeName, ym);
        return formatter.grn(scopeName, ym, next);
    }

    private Integer nextBranchPeriodSequenceValue(String codeTypeName, String scopeName, YearMonth ym) {
        CodeType codeType = codeTypeRepository.findByName(codeTypeName)
                .orElseThrow(() -> new RuntimeException("CodeType not found: " + codeTypeName));
        Scope scope = scopeRepository.findByName(scopeName)
                .orElseThrow(() -> new RuntimeException("Scope not found: " + scopeName));

        String periodKey = ym.format(DateTimeFormatter.ofPattern("yyyyMM"));

        DocSequence seq = sequenceRepository.findForUpdate(codeType.getId(), scope.getId(), periodKey)
                .orElseGet(() -> {
                    DocSequence s = new DocSequence();
                    s.setCodetype(codeType);
                    s.setScope(scope);
                    s.setPeriodkey(periodKey);
                    s.setLastvalue(0);
                    return s;
                });

        Integer next = seq.nextValue();
        sequenceRepository.save(seq);
        return next;
    }
}
