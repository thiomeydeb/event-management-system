package com.poosh.event.management.venue

import com.poosh.event.management.apiresponse.BaseApiResponse
import com.poosh.event.management.exceptions.BadRequestException
import com.poosh.event.management.venue.dto.VenueCreateDto
import com.poosh.event.management.venue.dto.VenueUpdateDto
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class VenueService {

    private final VenueRepository venueRepository
    private final ModelMapper modelMapper

    @Autowired //inject the object dependency implicitly
    VenueService(VenueRepository venueRepository, ModelMapper modelMapper) {
        this.venueRepository = venueRepository
        this.modelMapper = modelMapper
    }

    BaseApiResponse getAllVenues() {
        //initialize response object. this is to create a common structure for all api responses
        BaseApiResponse res = new BaseApiResponse(null, HttpStatus.OK.value(), "success", null)

        //fetch all venues from db using VenueRepository
        List<Venue> allVenues = venueRepository.findAll()

        //check if any record is returned. if no record is returned, update status field to Http Status code 204 (meaning no content)
        if (allVenues.size() == 0) {
            res.status = HttpStatus.NO_CONTENT.value()
        }
        //if records were returned, assign fetched records to BaseApiResponse (res) data field
        res.data = allVenues

        //return instance of BaseApiResponse
        return res
    }

    BaseApiResponse getVenueById(Long venueId) {
        //initialize response object. this is to create a common structure for all api responses
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "success", [])

        //fetch venue by id from db using Venue Repository
        Optional<Venue> venueOptional = venueRepository.findById(venueId)

        /*
         *check if a record was returned.
         *if a record is not present update status field to Http Status code 204 (meaning no content)
         *if a record is present update status field to Http Status code 200 (meaning no ok or the request is successful and as data)
         *update response data field with the fetched item
        */
        if (!venueOptional.isPresent()) {
            res.status = HttpStatus.NO_CONTENT.value()
            res.message = String.format("record with id %s not found", venueId)
        } else {
            res.status = HttpStatus.OK.value()

            //retrieving the venue record from the venue optional
            res.data = venueOptional.get()
        }
        return res
    }

    BaseApiResponse addVenue(VenueCreateDto body){
        //initialize response object
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "successfully added venue", [])

        //map data from VenueCreateDto object to Venue entity in order to save using VenueRepository
        def venue = modelMapper.map(body, Venue.class)

        /*set default value for isActive field.
         *this is to prevent an error since mapping will insert a null value which is against the table's not null constraint
        */
        venue.setActive(true)

        //save new venue using venueRepository
        def savedVenue = venueRepository.save(venue)

        //assign saved record to response data field
        res.data = savedVenue
        return res
    }

    BaseApiResponse updateVenue(Long venueId, VenueUpdateDto body){
        //initialize response object
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "successfully updated venue", [])
        //fetch venue from db
        def optionalVenue = venueRepository.findById(venueId)

        /*
         *check if a record was returned.
         *if a record is not present throw an exception
         *if a record is present, update the venue details
        */
        if (optionalVenue.isPresent()) {
            //retrieve venue from optionalVenue
            def venue = optionalVenue.get()

            //map request body (VenueUpdateDto) to venue (Venue entity)
            modelMapper.map(body, venue)

            //perform update by saving using venueRepository
            def updatedVenue = venueRepository.save(venue)

            //assign updated venue details to response data field
            res.data = updatedVenue
        } else {
            //throw
            throw new BadRequestException(String.format("record with id %s not found", venueId), [])
        }
        return res
    }

    BaseApiResponse updateVenueStatus(long venueId, boolean status) {
        //initialize response object
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "update successful", [])

        //fetch venue by id from db using Venue Repository
        def optionalVenue = venueRepository.findById(venueId)

        /*
         *check if a record was returned.
         *if a record is not present throw an exception
         *if a record is present, update the status
        */
        if (optionalVenue.isPresent()) {
            //retrieve venue from optionalEventType
            def venue = optionalVenue.get()

            //set new status
            venue.setActive(status)

            //perform update by saving using venueRepository
            def updatedVenue = venueRepository.save(venue)

            //assign updated venue details to response data field
            res.data = updatedVenue
        } else {
            //throw
            throw new BadRequestException(String.format("record with id %s not found", venueId), [])
        }
        return res
    }
}
