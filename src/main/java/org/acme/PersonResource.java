package org.acme;


import java.util.List;
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

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import io.quarkus.panache.common.Sort;




@Path("/Person")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    @GET
    public List<Person> getAL() throws Exception{
        return Person.findAll(Sort.ascending("last_name")).list();
    }


    @POST
    @Transactional
    public Response create(Person p){

        if (p == null || p.id != null)
            throw new WebApplicationException("id != null");
        p.persist();
        return Response.ok(p).status(200).build();

    }

    @PUT
    @Transactional
    @Path("{id}")
    public Person update(@PathParam Long id, Person p){
        Person entity = Person.findById(id);
        if(entity == null){
            throw  new WebApplicationException("la personne avec cet id : "+id+" n'existe pas");
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
            throw new WebApplicationException("La personne avec cet " + id + " n'existe pas.", 404);
        }
        entity.delete();
        return Response.status(204).build();
    }


}