package com.poosh.event.management.providercategory

import com.poosh.event.management.apiresponse.BaseApiResponse
import com.poosh.event.management.exceptions.BadRequestException
import com.poosh.event.management.providercategory.dto.ProviderCategoryCreateDto
import com.poosh.event.management.providercategory.dto.ProviderCategoryUpdateDto
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class ProviderCategoryService {

    private final ProviderCategoryRepository providerCategoryRepository
    private final ModelMapper modelMapper

    @Autowired //inject the object dependency implicitly
    ProviderCategoryService(ProviderCategoryRepository providerCategoryRepository, ModelMapper modelMapper) {
        this.providerCategoryRepository = providerCategoryRepository
        this.modelMapper = modelMapper
    }

    BaseApiResponse addProviderCategory (ProviderCategoryCreateDto body){
        //initialize response object
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "successfully added provider category", [])

        //map data from ProviderCategoryCreateDto object to ProviderCategory entity in order to save using ProviderCategoryRepository
        def providerCategory = modelMapper.map(body, ProviderCategory.class)

        /*set default value for isActive field.
         *this is to prevent an error since mapping will insert a null value which is against the table's not null constraint
        */
        providerCategory.setActive(true)

        //save new provider category type using ProviderCategoryRepository
        def savedProviderCategory = providerCategoryRepository.save(providerCategory)

        //assign saved record to response data field
        res.data = savedProviderCategory
        return res
    }

    BaseApiResponse updateProviderCategory(Long providerCategoryId, ProviderCategoryUpdateDto body){
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "successfully updated provider category", [])
        //fetch provider category from db
        def optionalProviderCategory = providerCategoryRepository.findById(providerCategoryId)

        /*
         *check if a record was returned.
         *if a record is not present throw an exception
         *if a record is present, update the provider category details
        */
        if (optionalProviderCategory.isPresent()) {
            //retrieve provider category from optionalProviderCategory
            def providerCategory = optionalProviderCategory.get()

            //map request body (ProviderCategoryUpdateDto) to providerCategory (ProviderCategory entity)
            modelMapper.map(body, providerCategory)

            //perform update by saving using providerCategoryRepository
            def updatedProviderCategory = providerCategoryRepository.save(providerCategory)

            //assign updated provider category details to response data field
            res.data = updatedProviderCategory
        } else {
            //throw
            throw new BadRequestException(String.format("record with id %s not found", providerCategoryId), [])
        }
        return res
    }

    BaseApiResponse updateStatus(Long providerCategoryId, boolean status){
        //initialize response object
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "update successful", [])

        //fetch provider category by id from db using ProviderCategoryRepository
        def optionalProviderCategory = providerCategoryRepository.findById(providerCategoryId)

        /*
         *check if a record was returned.
         *if a record is not present throw an exception
         *if a record is present, update the status
        */
        if (optionalProviderCategory.isPresent()) {
            //retrieve provider category from optionalProviderCategory
            def providerCategory = optionalProviderCategory.get()

            //set new status
            providerCategory.setActive(status)

            //perform update by saving using providerCategoryRepository
            def updatedProviderCategory = providerCategoryRepository.save(providerCategory)

            //assign updated provider category details to response data field
            res.data = updatedProviderCategory
        } else {
            //throw
            throw new BadRequestException(String.format("record with id %s not found", providerCategoryId), [])
        }
        return res
    }

    BaseApiResponse getAllProviderCategory(){
        //initialize response object. this is to create a common structure for all api responses
        BaseApiResponse res = new BaseApiResponse(null, HttpStatus.OK.value(), "success", null)

        //fetch all provider categories from db using providerCategoryRepository
        List<ProviderCategory> allProviderCategories = providerCategoryRepository.findAll()

        //check if any record is returned. if no record is returned, update status field to Http Status code 204 (meaning no content)
        if (allProviderCategories.size() == 0) {
            res.status = HttpStatus.NO_CONTENT.value()
        }
        //if records were returned, assign fetched records to BaseApiResponse (res) data field
        res.data = allProviderCategories

        //return instance of BaseApiResponse
        return res
    }

    BaseApiResponse getProviderCategoryById(Long providerCategoryId){
        //initialize response object. this is to create a common structure for all api responses
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "success", [])

        //fetch provider category by id from db using ProviderCategoryRepository
        Optional<ProviderCategory> providerCategoryOptional = providerCategoryRepository.findById(providerCategoryId)

        /*
         *check if a record was returned.
         *if a record is not present update status field to Http Status code 204 (meaning no content)
         *if a record is present update status field to Http Status code 200 (meaning no ok or the request is successful and as data)
         *update response data field with the fetched item
        */
        if (!providerCategoryOptional.isPresent()) {
            res.status = HttpStatus.NO_CONTENT.value()
            res.message = String.format("record with id %s not found", providerCategoryId)
        } else {
            res.status = HttpStatus.OK.value()

            //retrieving provider category record from providerCategoryOptional
            res.data = providerCategoryOptional.get()
        }
        return res
    }
}
