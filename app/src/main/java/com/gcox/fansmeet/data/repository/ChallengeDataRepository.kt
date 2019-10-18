package com.gcox.fansmeet.data.repository

import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.data.entity.mapper.ChallengeMapper
import com.gcox.fansmeet.data.repository.datasource.ChallengeDataSource
import com.gcox.fansmeet.domain.repository.ChallengeRepository
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.webservice.request.ChallengeListEntriesRequestModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class ChallengeDataRepository constructor(@Remote private val dataSource: ChallengeDataSource) :
    ChallengeRepository {

    private val mapper = ChallengeMapper()

    override fun deleteSubmission(submissionId: Int): Observable<Boolean> {
        return dataSource.deleteSubmission(submissionId).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(it.data)
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun getChallengeListEntries(
        nextId: Int,
        pageLimited: Int
    ): Observable<BaseDataPagingResponseModel<DisplayableItem>> {
        return dataSource.getChallengeListEntries(ChallengeListEntriesRequestModel(nextId, pageLimited))
            .flatMap {
                when (it.code) {
                    Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(mapper.transform(it.data))
                    else -> Observable.error(GcoxException(it.message, it.code))
                }
            }
    }

    override fun canSubmitChallenge(challengeId: Int): Observable<Boolean> {
        return dataSource.canSubmitChallenge(challengeId)
            .flatMap {
                when (it.code) {
                    Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(it.data)
                    else -> Observable.error(GcoxException(it.message, it.code))
                }
            }
    }


}