package com.web.rest;

import com.domain.Pet;
import com.repository.PetRepository;
import com.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.domain.Pet}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PetResource {

    private final Logger log = LoggerFactory.getLogger(PetResource.class);

    private static final String ENTITY_NAME = "pet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PetRepository petRepository;

    public PetResource(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    /**
     * {@code POST  /pets} : Create a new pet.
     *
     * @param pet the pet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pet, or with status {@code 400 (Bad Request)} if the pet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pets")
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet) throws URISyntaxException {
        log.debug("REST request to save Pet : {}", pet);
        if (pet.getId() != null) {
            throw new BadRequestAlertException("A new pet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pet result = petRepository.save(pet);
        return ResponseEntity
            .created(new URI("/api/pets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pets/:id} : Updates an existing pet.
     *
     * @param id the id of the pet to save.
     * @param pet the pet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pet,
     * or with status {@code 400 (Bad Request)} if the pet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pets/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable(value = "id", required = false) final Long id, @RequestBody Pet pet)
        throws URISyntaxException {
        log.debug("REST request to update Pet : {}, {}", id, pet);
        if (pet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!petRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pet result = petRepository.save(pet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pets/:id} : Partial updates given fields of an existing pet, field will ignore if it is null
     *
     * @param id the id of the pet to save.
     * @param pet the pet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pet,
     * or with status {@code 400 (Bad Request)} if the pet is not valid,
     * or with status {@code 404 (Not Found)} if the pet is not found,
     * or with status {@code 500 (Internal Server Error)} if the pet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pet> partialUpdatePet(@PathVariable(value = "id", required = false) final Long id, @RequestBody Pet pet)
        throws URISyntaxException {
        log.debug("REST request to partial update Pet partially : {}, {}", id, pet);
        if (pet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!petRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pet> result = petRepository
            .findById(pet.getId())
            .map(existingPet -> {
                if (pet.getForSale() != null) {
                    existingPet.setForSale(pet.getForSale());
                }
                if (pet.getCategory() != null) {
                    existingPet.setCategory(pet.getCategory());
                }
                if (pet.getRace() != null) {
                    existingPet.setRace(pet.getRace());
                }
                if (pet.getName() != null) {
                    existingPet.setName(pet.getName());
                }
                if (pet.getOwner() != null) {
                    existingPet.setOwner(pet.getOwner());
                }
                if (pet.getPicture() != null) {
                    existingPet.setPicture(pet.getPicture());
                }

                return existingPet;
            })
            .map(petRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pet.getId().toString())
        );
    }

    /**
     * {@code GET  /pets} : get all the pets.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pets in body.
     */
    @GetMapping("/pets")
    public ResponseEntity<List<Pet>> getAllPets(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Pets");
        Page<Pet> page = petRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pets/:id} : get the "id" pet.
     *
     * @param id the id of the pet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pets/{id}")
    public ResponseEntity<Pet> getPet(@PathVariable Long id) {
        log.debug("REST request to get Pet : {}", id);
        Optional<Pet> pet = petRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pet);
    }

    /**
     * {@code DELETE  /pets/:id} : delete the "id" pet.
     *
     * @param id the id of the pet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pets/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        log.debug("REST request to delete Pet : {}", id);
        petRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
