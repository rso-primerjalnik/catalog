package si.fri.rso.samples.imagecatalog.services.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.samples.imagecatalog.lib.Product;
import si.fri.rso.samples.imagecatalog.models.converters.ConvertProduct;
import si.fri.rso.samples.imagecatalog.models.entities.ProductEntity;

@RequestScoped
public class ProductInfoBean {

    private final Logger log = Logger.getLogger(si.fri.rso.samples.imagecatalog.services.beans.ProductInfoBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Product> getProducts() {
        TypedQuery<ProductEntity> query = em.createNamedQuery("ProductEntity.getAll", ProductEntity.class);
        List<ProductEntity> resultList = query.getResultList();
        return resultList.stream().map(ConvertProduct::makeObject).collect(Collectors.toList());
    }

    public List<Product> getFilteredProducts(UriInfo uriInfo) {
        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, ProductEntity.class, queryParameters).stream()
                .map(ConvertProduct::makeObject).collect(Collectors.toList());
    }


    public Product getProduct(Integer id) {

        ProductEntity productEntity = em.find(ProductEntity.class, id);

        if (productEntity == null) {
            throw new NotFoundException();
        }

        return ConvertProduct.makeObject(productEntity);
    }

    public Product createProduct(Product product) {

        ProductEntity productEntity = ConvertProduct.makeEntity(product);

        try {
            TrBegin();
            em.persist(productEntity);
            TrCommit();
        }
        catch (Exception e) {
            TrRollback();
        }

        if (productEntity.getId() == null) {
            throw new RuntimeException("No entity");
        }

        return ConvertProduct.makeObject(productEntity);
    }

    public Product putProduct(Integer id, Product product) {

        ProductEntity c = em.find(ProductEntity.class, id);

        if (c == null) {
            return null;
        }

        ProductEntity updatedProductEntity = ConvertProduct.makeEntity(product);

        try {
            TrBegin();
            updatedProductEntity.setId(c.getId());
            updatedProductEntity = em.merge(updatedProductEntity);
            TrCommit();
        }
        catch (Exception e) {
            TrRollback();
        }

        return ConvertProduct.makeObject(updatedProductEntity);
    }

    public boolean deleteProduct(Integer id) {

        ProductEntity productEntity = em.find(ProductEntity.class, id);

        if (productEntity != null) {
            try {
                TrBegin();
                em.remove(productEntity);
                TrCommit();
            }
            catch (Exception e) {
                TrRollback();
            }
        }
        else {
            return false;
        }

        return true;
    }

    private void TrBegin() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void TrCommit() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void TrRollback() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}