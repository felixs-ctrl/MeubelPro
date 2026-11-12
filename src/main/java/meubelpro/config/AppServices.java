package meubelpro.config;

import meubelpro.dao.AdminDAO;
import meubelpro.dao.ProdukDAO;
import meubelpro.dao.BahanBakuDAO;
import meubelpro.dao.ProduksiDAO;

public record AppServices(
        AdminDAO adminDAO,
        ProdukDAO produkDAO,
        BahanBakuDAO bahanBakuDAO,
        ProduksiDAO produksiDAO
) {
}
