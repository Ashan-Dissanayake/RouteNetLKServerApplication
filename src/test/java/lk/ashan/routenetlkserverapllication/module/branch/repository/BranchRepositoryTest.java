package lk.ashan.routenetlkserverapllication.module.branch.repository;

import lk.ashan.routenetlkserverapllication.module.branch.model.*;
import lk.ashan.routenetlkserverapllication.util.factory.EntityFactory;
import lk.ashan.routenetlkserverapllication.util.seed.BranchTestDataSeeder;
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
        
        Branchtype branchtype = EntityFactory.branchType(1,"Region");
        Branchstatus branchstatus = EntityFactory.branchStatus(1,"Active");

        Branch branch = EntityFactory.branchWithCoverages("UNIQUE Branch","UNIQUE001-1",EntityFactory.FIXED_DATE,branchtype,branchstatus);

        Branch saved = branchRepository.save(branch);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("UNIQUE Branch");
        assertThat(saved.getBranchcoverages()).isNotNull();
        assertThat(saved.getBranchcoverages().size()).isEqualTo(2);
    }

}
