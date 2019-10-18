package com.gcox.fansmeet.domain.repository

import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface ChallengeRepository {


    fun getChallengeListEntries(nextId: Int, pageLimited: Int): Observable<BaseDataPagingResponseModel<DisplayableItem>>

    fun canSubmitChallenge(challengeId: Int): Observable<Boolean>

    fun deleteSubmission(submissionId:Int): Observable<Boolean>
}