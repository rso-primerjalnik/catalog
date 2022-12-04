package si.fri.rso.samples.imagecatalog.models.converters;

import si.fri.rso.samples.imagecatalog.lib.Product;
import si.fri.rso.samples.imagecatalog.models.entities.ProductEntity;

public class ConvertProduct {

    public static Product makeObject(ProductEntity entity) {

        Product obj = new Product();
        obj.setId(entity.getId());
        obj.setName(entity.getName());
        obj.setDescription(entity.getDescription());
        obj.setWeight(entity.getWeight());
        return obj;

    }

    public static ProductEntity makeEntity(Product obj) {

        ProductEntity entity = new ProductEntity();
        entity.setName(obj.getName());
        entity.setDescription(obj.getDescription());
        entity.setWeight(obj.getWeight());

        return entity;

    }
}
