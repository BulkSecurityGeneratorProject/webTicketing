package ec.com.se.web.rest;

import com.codahale.metrics.annotation.Timed;
import ec.com.se.domain.Category;
import ec.com.se.domain.Subcategory;
import ec.com.se.service.SubcategoryService;
import ec.com.se.web.rest.util.HeaderUtil;
import ec.com.se.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Subcategory.
 */
@RestController
@RequestMapping("/api")
public class SubcategoryResource {

    private final Logger log = LoggerFactory.getLogger(SubcategoryResource.class);

    @Inject
    private SubcategoryService subcategoryService;

    /**
     * POST  /subcategories : Create a new subcategory.
     *
     * @param subcategory the subcategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subcategory, or with status 400 (Bad Request) if the subcategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/subcategories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Subcategory> createSubcategory(@Valid @RequestBody Subcategory subcategory) throws URISyntaxException {
        log.debug("REST request to save Subcategory : {}", subcategory);
        if (subcategory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("subcategory", "idexists", "A new subcategory cannot already have an ID")).body(null);
        }
        Subcategory result = subcategoryService.save(subcategory);
        return ResponseEntity.created(new URI("/api/subcategories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("subcategory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /subcategories : Updates an existing subcategory.
     *
     * @param subcategory the subcategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subcategory,
     * or with status 400 (Bad Request) if the subcategory is not valid,
     * or with status 500 (Internal Server Error) if the subcategory couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/subcategories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Subcategory> updateSubcategory(@Valid @RequestBody Subcategory subcategory) throws URISyntaxException {
        log.debug("REST request to update Subcategory : {}", subcategory);
        if (subcategory.getId() == null) {
            return createSubcategory(subcategory);
        }
        Subcategory result = subcategoryService.save(subcategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("subcategory", subcategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /subcategories : get all the subcategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of subcategories in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/subcategories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Subcategory>> getAllSubcategories(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Subcategories");
        Page<Subcategory> page = subcategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/subcategories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /** Get List SUb-Categories Enabled */
    @RequestMapping(value = "/subcategories/enabled",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Subcategory>> getAllSubcategoriesEnabled(@RequestParam(value = "category", required = true) String category , Pageable pageable)
        throws URISyntaxException {
          Category cat= new Category();
          cat.setId(Long.valueOf(category));
          Page<Subcategory> page = subcategoryService.findAllEnabled(cat, true, pageable);
          HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/subcategories-enabled");
          return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);          
    }

    /**
     * GET  /subcategories/:id : get the "id" subcategory.
     *
     * @param id the id of the subcategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subcategory, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/subcategories/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Subcategory> getSubcategory(@PathVariable Long id) {
        log.debug("REST request to get Subcategory : {}", id);
        Subcategory subcategory = subcategoryService.findOne(id);
        return Optional.ofNullable(subcategory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /subcategories/:id : delete the "id" subcategory.
     *
     * @param id the id of the subcategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/subcategories/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSubcategory(@PathVariable Long id) {
        log.debug("REST request to delete Subcategory : {}", id);
        subcategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("subcategory", id.toString())).build();
    }

}
