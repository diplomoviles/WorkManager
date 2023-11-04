package com.amaurypm.workmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.amaurypm.workmanager.databinding.ActivityMainBinding
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val decimalFormat = DecimalFormat("###,###,###.00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = workDataOf(
            "num1" to 30.25,
            "num2" to 45.18
        )

        //Para que los textviews se puedan ver con la barra de desplazamiento
        binding.tvWork1Status.movementMethod = ScrollingMovementMethod()
        binding.tvWork2Status.movementMethod = ScrollingMovementMethod()


        val workRequest1 = OneTimeWorkRequestBuilder<MyWorker1>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(
                        NetworkType.CONNECTED
                    )
                    .setRequiresBatteryNotLow(
                        true
                    )
                    .build()
            )
            .setInputData(data)
            .build()



        val workManager = WorkManager.getInstance(this)

        binding.ivStartWork.setOnClickListener {
            workManager.beginUniqueWork("work1", ExistingWorkPolicy.KEEP, workRequest1)
                //.then(workRequest2)
                .enqueue()
            /*workManager.beginUniqueWork("work2", ExistingWorkPolicy.KEEP, workRequest2)
                .enqueue()*/
        }

        workManager.getWorkInfoByIdLiveData(workRequest1.id)
            .observe(this){ work1info ->
                if(work1info!=null){
                    val workStatus = work1info.state.name
                    binding.tvWork1Status.append("${workStatus}\n")
                    if(work1info.state.isFinished){ //Terminó el workrequest1
                        //binding.tvWork1Status.append("\n")
                        val result1 = work1info.outputData.getDouble("suma", 0.0)
                        binding.tvWork1Status.append("Suma=\$${decimalFormat.format(result1)}\n")

                        val workRequest2 = OneTimeWorkRequestBuilder<MyWorker2>()
                            .setConstraints(
                                Constraints.Builder()
                                    .setRequiredNetworkType(
                                        NetworkType.CONNECTED
                                    )
                                    .setRequiresBatteryNotLow(
                                        true
                                    )
                                    .build()
                            )
                            .setInputData(
                                workDataOf(
                                    "num" to result1
                                )
                            )
                            .build()

                        workManager.beginUniqueWork("work2", ExistingWorkPolicy.KEEP, workRequest2)
                            .enqueue()

                        workManager.getWorkInfoByIdLiveData(workRequest2.id)
                            .observe(this){ work2info ->
                                if(work2info!=null){
                                    val workStatus = work2info.state.name
                                    binding.tvWork2Status.append("${workStatus}\n")
                                    if(work2info.state.isFinished){
                                        //binding.tvWork2Status.append("\n")

                                        val result2 = work2info.outputData.getDouble("cubo", 0.0)

                                        binding.tvWork2Status.append("Cubo=\$${decimalFormat.format(result2)}\n")

                                    }
                                }
                            }
                    }
                }
            }



    }
}