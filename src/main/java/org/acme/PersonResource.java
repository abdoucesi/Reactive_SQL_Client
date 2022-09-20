package org.acme;




import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;
import java.io.FileWriter;
import java.io.IOException;



import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;




import org.hibernate.jdbc.Expectation;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;






@Path("/Person")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {
    private static final Logger LOGGER = Logger.getLogger(PersonResource.class);

    @GET
    public List<Person> getAll() throws IOException {
        
        LOGGER.info("Le getAll marche");

        List<Person> allPersons = Person.listAll();

        

        List<String> names = allPersons.stream().map(p -> p.firstName)
            .filter( s-> s.contains("i"))
            .collect(Collectors.toList());
        
        

        System.out.println(names);
        
        return allPersons;
        
           
    }

  
    @POST
    @Transactional
    public Response create(Person p, Expectation e){

       

        if (p == null || p.id != null)
            LOGGER.error(" error POST : " + e );
            p.persist();
            return Response.ok(p).status(200).build();
        

       

    }

    @PUT
    @Transactional
    @Path("{id}")
    public Person update(@PathParam Long id, Person p){
        Person entity = Person.findById(id);
        if(entity == null){
            throw  new WebApplicationException("la personne avec cet id : "+ id +" n'existe pas");
        }
            if (p.salutation != null) entity.salutation = p.salutation;
            if (p.firstName != null) entity.firstName = p.firstName;
            if (p.lastName != null) entity.lastName = p.lastName;
            
            return entity;
        }
         
    

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam Long id) {
       
    
   
         Person entity = Person.findById(id);
        if (entity == null) {
            LOGGER.error("error");
        }
        entity.delete();
        LOGGER.info("DELETE OK !");
        return Response.status(204).build();
    
        
  


}
}