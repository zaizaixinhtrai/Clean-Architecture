package com.gcox.fansmeet.data.repository

import com.gcox.fansmeet.data.repository.datasource.CelebrityDataSource
import com.gcox.fansmeet.domain.repository.CelebrityRepository

/**
 * Created by ngoc on 5/18/18.
 */
class CelebrityDataRepository constructor(@Remote private val homeDataSource: CelebrityDataSource) :
    CelebrityRepository {


}