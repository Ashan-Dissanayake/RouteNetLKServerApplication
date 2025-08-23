package lk.ashan.ntcserverapllication.module.branch.repository;

import lk.ashan.ntcserverapllication.module.branch.model.*;
import lk.ashan.ntcserverapllication.util.factory.BranchTestDataFactory;
import lk.ashan.ntcserverapllication.util.seed.BranchTestDataSeeder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;



import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class BranchRepositoryTest {


    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchTestDataSeeder branchTestDataSeeder;

    @BeforeEach
    void setUp() {

        Province province = branchTestDataSeeder.persistProvince( "Western");

        branchTestDataSeeder.persistDistrict( "Colombo", province);
        branchTestDataSeeder.persistDistrict("Gampaha", province);
        branchTestDataSeeder.persistBranchStatus( "Active");
        branchTestDataSeeder.persistBranchType("Region");
    }


    @Test
    public void shouldSaveBranchWhenValidBranchProvided() {
        Branch branch = BranchTestDataFactory.buildBranchWithBranchcoverages();

        Branch saved = branchRepository.save(branch);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("UNIQUE Branch");
        assertThat(saved.getBranchcoverages()).isNotNull();
        assertThat(saved.getBranchcoverages().size()).isEqualTo(2);
    }

}
