package com.amaurypm.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay
import kotlin.math.pow

/**
 * Creado por Amaury Perea Matsumura el 03/11/23
 */
class MyWorker2(
    private val context: Context,
    private val workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters)  {
    override suspend fun doWork(): Result {
        //val cubo = cubo(3.0)

        val data = inputData

        val num = data.getDouble("num", 0.0)

        val cubo = cubo(num)

        return Result.success(
            workDataOf(
                "cubo" to cubo
            )
        )
    }

    private suspend fun cubo(num: Double): Double{
        delay(3000)
        return num.pow(3.0)
    }
}