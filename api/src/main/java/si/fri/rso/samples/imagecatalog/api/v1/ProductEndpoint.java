package si.fri.rso.samples.imagecatalog.api.v1;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.glassfish.jersey.logging.LoggingFeature;
import si.fri.rso.samples.imagecatalog.lib.Product;
import si.fri.rso.samples.imagecatalog.services.beans.ProductInfoBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, HEAD, OPTIONS, PUT")
public class ProductEndpoint {

    private final Logger log = Logger.getLogger(ProductEndpoint.class.getName());

    @Inject
    private ProductInfoBean productBean;

    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Get all products.", summary = "Get all products")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "List of products",
                    content = @Content(schema = @Schema(implementation = Product.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of products in list")}
            )
    })
    @GET
    public Response getAllProducts() {
        List<Product> products = productBean.getProducts();
        return Response.ok(products).header("X-Total-Count", products.size()).build();
    }

    @Operation(description = "Get filtered products.", summary = "Get filtered products")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "List of filtered products",
                    content = @Content(schema = @Schema(implementation = Product.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of products in list")}
            )
    })
    @GET
    @Path("/filter")
    public Response getFilteredProducts() {
        List<Product> products = productBean.getFilteredProducts(uriInfo);
        return Response.ok(products).header("X-Total-Count", products.size()).build();
    }

    @Operation(description = "Get data for a single product.", summary = "Get data for a single product")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Single product information.",
                    content = @Content(schema = @Schema(implementation = Product.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Product could not be found."
            ),
    })
    @GET
    @Path("/{productId}")
    public Response getSingleProduct(@Parameter(description = "Product ID.", required = true)
                                     @PathParam("productId") Integer productId) {

        Product product = productBean.getProduct(productId);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(product).build();
    }

    @Operation(description = "Insert new product.", summary = "Insert new product")
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Product successfully added.",
                    content = @Content(schema = @Schema(implementation = Product.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "The product could not be inserted. Incorrect or missing parameters."
            ),
    })
    @POST
    public Response createProduct(@RequestBody(
            description = "Object with product information.",
            required = true, content = @Content(
            schema = @Schema(implementation = Product.class))) Product product) {

        if ((product.getName() == null || product.getDescription() == null || product.getWeight() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            product = productBean.createProduct(product);
        }

        return Response.status(Response.Status.CREATED).entity(product).build();

    }


    @Operation(description = "Update single product.", summary = "Update product")
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Product successfully updated.",
                    content = @Content(schema = @Schema(implementation = Product.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Product could not be found."
            )
    })
    @PUT
    @Path("{productId}")
    public Response putProduct(@Parameter(description = "Product ID.", required = true)
                               @PathParam("productId") Integer productId,
                               @RequestBody(
                                       description = "DTO object with product information.",
                                       required = true, content = @Content(
                                       schema = @Schema(implementation = Product.class)))
                               Product product) {

        product = productBean.putProduct(productId, product);

        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(product).build();
    }

    @Operation(description = "Delete product.", summary = "Delete product")
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Product successfully deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Product could not be found."
            )
    })
    @DELETE
    @Path("{productId}")
    public Response deleteProduct(@Parameter(description = "Product ID.", required = true)
                                  @PathParam("productId") Integer productId) {

        boolean deleted = productBean.deleteProduct(productId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
