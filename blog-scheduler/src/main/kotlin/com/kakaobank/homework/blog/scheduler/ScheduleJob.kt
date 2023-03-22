package com.kakaobank.homework.blog.scheduler

import com.kakaobank.homework.blog.core.service.LockService
import com.kakaobank.homework.blog.core.service.PopularSearchWordSyncService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduleJob(
  private val popularSearchWordSyncService: PopularSearchWordSyncService,
  private val lockService: LockService
) {
  private val lockKey = "popularSearchWordLock"

  @Scheduled(cron = "0 */5 * * * *")
  fun syncPopularSearchWordToDatabase() {
    if (!lockService.lock(lockKey, 5000)) {
      return
    }
    popularSearchWordSyncService.sync()
    lockService.unlock(lockKey)
  }
}
