package com.gcox.fansmeet.domain.interactors.celebrity

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.CelebrityRepository
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityGridResponse
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityResponse
import com.gcox.fansmeet.webservice.request.CelebrityRequestModel
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by ngoc on 5/18/18.
 */
class GetCelebrityGridUseCase constructor(uiThread: Scheduler, executorThread: Scheduler, private val dataRepository: CelebrityRepository)
    : UseCase<CelebrityGridResponse, CelebrityRequestModel>(uiThread, executorThread){

    override fun buildObservable(params: CelebrityRequestModel): Observable<CelebrityGridResponse> {
        return dataRepository.getCelebrityGrid(params)
    }
}