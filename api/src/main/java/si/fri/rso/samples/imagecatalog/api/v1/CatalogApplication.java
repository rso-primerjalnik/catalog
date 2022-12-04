package si.fri.rso.samples.imagecatalog.api.v1;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Catalog", version = "v1",
        contact = @Contact(email = "ad1323@fri.uni-lj.si"),
        license = @License(name = "dev"), description = "Our first project API for product info."),
        servers = @Server(url = "http://localhost:8080/"))
@ApplicationPath("/v1")
public class CatalogApplication extends Application{
}
