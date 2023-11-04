package com.amaurypm.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay

/**
 * Creado por Amaury Perea Matsumura el 03/11/23
 */
class MyWorker1(
    private val context: Context,
    private val workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters)  {
    override suspend fun doWork(): Result {

        val data = inputData

        val num1 = data.getDouble("num1", 0.0)
        val num2 = data.getDouble("num2", 0.0)
        val suma = suma(num1, num2)

        //if(suma>10000) return Result.failure()

        return Result.success(
            workDataOf(
                "suma" to suma
            )
        )
    }

    private suspend fun suma(num1: Double, num2: Double): Double{
        delay(5000)
        return num1+num2
    }
}