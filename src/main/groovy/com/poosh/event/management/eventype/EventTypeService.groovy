package com.poosh.event.management.eventype

import com.poosh.event.management.apiresponse.BaseApiResponse
import com.poosh.event.management.eventype.dto.EventTypeCreateDto
import com.poosh.event.management.eventype.dto.EventTypeUpdateDto
import com.poosh.event.management.exceptions.BadRequestException
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
class EventTypeService {

    private final EventTypeRepository eventTypeRepository
    private final ModelMapper modelMapper

    @Autowired
    EventTypeService(EventTypeRepository eventTypeRepository, ModelMapper modelMapper) {
        this.eventTypeRepository = eventTypeRepository
        this.modelMapper = modelMapper
    }

    BaseApiResponse getAllEventTypes() {
        //initialize response object. this is to create a common structure for all api responses
        BaseApiResponse res = new BaseApiResponse(null, HttpStatus.OK.value(), "success", null)

        //fetch all event types from db using Event Type Repository
        List<EventType> allEventTypes = eventTypeRepository.findAll()

        //check if any record is returned. if no record is returned, update status field to Http Status code 204 (meaning no content)
        if (allEventTypes.size() == 0) {
            res.status = HttpStatus.NO_CONTENT.value()
        }
        //if records were returned, assign fetched records to BaseApiResponse (res) data field
        res.data = allEventTypes

        //return instance of BaseApiResponse
        return res
    }

    BaseApiResponse getEventTypeById(Long eventId) {
        //initialize response object. this is to create a common structure for all api responses
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "success", [])

        //fetch event type by id from db using Event Type Repository
        Optional<EventType> eventTypeOptional = eventTypeRepository.findById(eventId)

        /*
         *check if a record was returned.
         *if a record is not present update status field to Http Status code 204 (meaning no content)
         *if a record is present update status field to Http Status code 200 (meaning no ok or the request is successful and as data)
         *update response data field with the fetched item
        */
        if (!eventTypeOptional.isPresent()) {
            res.status = HttpStatus.NO_CONTENT.value()
            res.message = String.format("record with id %s not found", eventId)
        } else {
            res.status = HttpStatus.OK.value()

            //retrieving the event type record from the event type optional
            res.data = eventTypeOptional.get()
        }
        return res
    }

    BaseApiResponse addEventType(EventTypeCreateDto body){
        //initialize response object
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "successfully added event type", [])

        //map data from EventTypeCreateDto object to EventType entity in order to save using EventTypeRepository
        def eventType = modelMapper.map(body, EventType.class)

        /*set default value for isActive field.
         *this is to prevent an error since mapping will insert a null value which is against the table's not null constraint
        */
        eventType.setActive(true)

        //save new event type using EventTypeRepository
        def savedEventType = eventTypeRepository.save(eventType)

        //assign saved record to response data field
        res.data = savedEventType
        return res
    }

    BaseApiResponse updateEventType(Long eventId, EventTypeUpdateDto body){
        //initialize response object
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "successfully updated event type", [])
        //fetch event type from db
        def optionalEventType = eventTypeRepository.findById(eventId)

        /*
         *check if a record was returned.
         *if a record is not present throw an exception
         *if a record is present, update the event type details
        */
        if (optionalEventType.isPresent()) {
            //retrieve event type from optionalEventType
            def eventType = optionalEventType.get()

            //map request body (EventTypeUpdateDto) to eventType (EventType entity)
            modelMapper.map(body, eventType)

            //perform update by saving using eventTypeRepository
            def updatedEventType = eventTypeRepository.save(eventType)

            //assign updated event details to response data field
            res.data = updatedEventType
        } else {
            //throw
            throw new BadRequestException(String.format("record with id %s not found", eventId), [])
        }
        return res
    }

    BaseApiResponse updateEventTypeStatus(long eventId, boolean status) {
        //initialize response object
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "update successful", [])

        //fetch event type by id from db using Event Type Repository
        def optionalEventType = eventTypeRepository.findById(eventId)

        /*
         *check if a record was returned.
         *if a record is not present throw an exception
         *if a record is present, update the status
        */
        if (optionalEventType.isPresent()) {
            //retrieve event type from optionalEventType
            def eventType = optionalEventType.get()

            //set new status
            eventType.setActive(status)

            //perform update by saving using eventTypeRepository
            def updatedEventType = eventTypeRepository.save(eventType)

            //assign updated event details to response data field
            res.data = updatedEventType
        } else {
            //throw
            throw new BadRequestException(String.format("record with id %s not found", eventId), [])
        }
        return res
    }
}
