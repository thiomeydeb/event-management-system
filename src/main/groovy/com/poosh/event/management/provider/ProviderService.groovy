package com.poosh.event.management.provider

import com.poosh.event.management.apiresponse.BaseApiResponse
import com.poosh.event.management.exceptions.BadRequestException
import com.poosh.event.management.exceptions.InternalServerErrorException
import com.poosh.event.management.provider.dto.ProviderCreateDto
import com.poosh.event.management.provider.dto.ProviderUpdateDto
import com.poosh.event.management.providercategory.ProviderCategoryRepository
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class ProviderService {

    private final ProviderRepository providerRepository
    private final ProviderCategoryRepository providerCategoryRepository
    private final ModelMapper modelMapper

    @Autowired
    ProviderService(ProviderRepository providerRepository, ModelMapper modelMapper, ProviderCategoryRepository providerCategoryRepository) {
        this.providerRepository = providerRepository
        this.modelMapper = modelMapper
        this.providerCategoryRepository = providerCategoryRepository
    }

    BaseApiResponse addProvider (ProviderCreateDto body){
        //initialize response object
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "successfully added provider", [])

        //check provider category
        def optionalProviderCategory = providerCategoryRepository.findById(body.categoryId)

        if(!optionalProviderCategory.isPresent()){
            throw new InternalServerErrorException(String.format("Provider category with id %s does not exist", body.categoryId), [])
        }

        def providerCategory = optionalProviderCategory.get()
        body.providerCategory = providerCategory
        //map data from ProviderDto object to Provider entity in order to save using ProviderRepository
        def provider = modelMapper.map(body, Provider.class)

        /*set default value for isActive field.
         *this is to prevent an error since mapping will insert a null value which is against the table's not null constraint
        */
        provider.setActive(true)

        //save new provider using ProviderRepository
        def savedProvider = providerRepository.save(provider)

        //assign saved record to response data field
        res.data = savedProvider
        return res
    }

    BaseApiResponse updateProvider (Long providerId, ProviderUpdateDto body){
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "successfully updated provider category", [])
        //fetch provider category from db
        def optionalProvider = providerRepository.findById(providerId)

        /*
         *check if a record was returned.
         *if a record is not present throw an exception
         *if a record is present, update the provider category details
        */
        if (optionalProvider.isPresent()) {
            //retrieve provider from optionalProvider
            def provider = optionalProvider.get()

            if(body.categoryId){
                //check provider category
                def optionalProviderCategory = providerCategoryRepository.findById(body.categoryId)

                if(!optionalProviderCategory.isPresent()){
                    throw new InternalServerErrorException(String.format("Provider category with id %s does not exist", body.categoryId), [])
                }
                body.providerCategory = optionalProviderCategory.get()
            }

            //map request body (ProviderUpdateDto) to provider (Provider entity)
            modelMapper.map(body, provider)

            //perform update by saving using providerRepository
            def updatedProvider = providerRepository.save(provider)

            //assign updated provider details to response data field
            res.data = updatedProvider
        } else {
            //throw
            throw new BadRequestException(String.format("record with id %s not found", providerId), [])
        }
        return res
    }

    BaseApiResponse updateStatus(Long providerId, boolean status){
        //initialize response object
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "update successful", [])

        //fetch provider by id from db using ProviderRepository
        def optionalProvider = providerRepository.findById(providerId)

        /*
         *check if a record was returned.
         *if a record is not present throw an exception
         *if a record is present, update the status
        */
        if (optionalProvider.isPresent()) {
            //retrieve provider from optionalProvider
            def provider = optionalProvider.get()

            //set new status
            provider.setActive(status)

            //perform update by saving using providerRepository
            def updatedProvider = providerRepository.save(provider)

            //assign updated provider details to response data field
            res.data = updatedProvider
        } else {
            //throw
            throw new BadRequestException(String.format("record with id %s not found", providerId), [])
        }
        return res
    }

    BaseApiResponse getAllProviders(){
        //initialize response object. this is to create a common structure for all api responses
        BaseApiResponse res = new BaseApiResponse(null, HttpStatus.OK.value(), "success", null)

        //fetch all providers from db using providerRepository
        List<Provider> allProviders = providerRepository.findAll()

        //check if any record is returned. if no record is returned, update status field to Http Status code 204 (meaning no content)
        if (allProviders.size() == 0) {
            res.status = HttpStatus.NO_CONTENT.value()
        }
        //if records were returned, assign fetched records to BaseApiResponse (res) data field
        res.data = allProviders

        //return instance of BaseApiResponse
        return res
    }


    BaseApiResponse getProviderById(long providerId) {
        //initialize response object. this is to create a common structure for all api responses
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "success", [])

        //fetch provider by id from db using ProviderRepository
        Optional<Provider> providerOptional = providerRepository.findById(providerId)

        /*
         *check if a record was returned.
         *if a record is not present update status field to Http Status code 204 (meaning no content)
         *if a record is present update status field to Http Status code 200 (meaning no ok or the request is successful and as data)
         *update response data field with the fetched item
        */
        if (!providerOptional.isPresent()) {
            res.status = HttpStatus.NO_CONTENT.value()
            res.message = String.format("record with id %s not found", providerId)
        } else {
            res.status = HttpStatus.OK.value()

            //retrieving provider record from providerOptional
            res.data = providerOptional.get()
        }
        return res
    }
}
